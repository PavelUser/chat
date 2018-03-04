package com.company.server;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerChat {
    private final Logger log = Logger.getLogger(ServerChat.class);

    final static private int PORT=2000;
    private boolean working=true;
    private String msg;

    private ArrayList<Socket> clientlist=new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private ConnectionHandler connectionHandler;
    private DataInputStream dataInputStream;

    public static void main(String[] args){
        ServerChat serverChat=new ServerChat();
        serverChat.startWork();
    }

    private void showIP(){
        try {
            log.info("ip address: "+InetAddress.getLocalHost().getHostAddress());
        }catch (UnknownHostException host){
            log.error("IP сервера не получен", host);
        }
    }

    private void showCommands(){
        System.out.print("Список доступных команд:\n"+"exit\n"+"list\n");
    }

    private boolean isWorking(){
        return working;
    }

    private void setWorking(boolean working){
        this.working=working;
    }

    private void startWork() {
        try {
            serverSocket=new ServerSocket(PORT);
            showIP();
            connectionHandler=new ConnectionHandler();
            showCommands();
            commandHandler();

            while (isWorking()) {
                Thread.sleep(1000);
            }

            connectionHandler.setRunning(false);
        }catch (IOException io){
            log.error("I/O exception",io);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connectionHandler.closeAllSocket();
        log.info("Сервер выключен\n");
    }

    private void commandHandler(){

        new Thread() {
            private boolean running = true;
            private String command;

            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                do {
                    command=scanner.nextLine().toLowerCase();
                    switch (command){
                        case "exit": {
                            running = false;
                            break;
                        }
                        case "list": {
                            System.out.println("\tСписок клиентов");
                            for (Socket client:clientlist)
                                System.out.println(client.getInetAddress());
                            break;
                        }
                        default:{
                            System.out.println("Список доступных команд:\nexit\nlist");
                            break;
                        }
                    }
                } while (running);

                setWorking(false);
            }
        }.start();
    }

    private class ConnectionHandler extends Thread {
        final private Logger logger=Logger.getLogger(ConnectionHandler.class);

        private boolean running=true;

        ConnectionHandler(){
            super("ConnectionHandler");
            setDaemon(true);
            start();
        }

        @Override
        public void run(){
            log.info("Поток "+getName()+ " Запущен");
            while (isRunning()) {                                                           //не нравится выход из цикла нужно ждать подключения
                try {
                    socket = serverSocket.accept();                                         //Обрабатываем порт на наличие подключений
                    logger.info("Соединение с клиентом установлено\n");
                    clientlist.add(socket);                                                 //Добавляем клиента в список подключенных
                    InputStreamHandler inputStreamHandler=new InputStreamHandler();         //Создаем по потоку на клиента
                    OutputHandler outputStreamHandler=new OutputHandler();
                    inputStreamHandler.addPropertyChangeListener(outputStreamHandler);
                } catch (IOException ex) {
                    logger.error(getName(), ex);
                }
            }
            closeAllSocket();
            logger.info("Остановлен поток: " + getName());
        }

        void closeAllSocket(){
            for (Socket s:clientlist) {
                try {
                    s.close();
                }catch (IOException io){
                    logger.warn(s.getInetAddress().getHostAddress()+": сокет не закрыт",io);
                }
            }
            logger.info("Все сокеты закрыты");
        }

        private boolean isRunning(){
            return running;
        }

        void setRunning(boolean running){
            this.running=running;
        }

    }

    private class InputStreamHandler extends Thread{
        private Logger logger=Logger.getLogger(InputStreamHandler.class);

        private String ip;
        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        InputStreamHandler(){
            ip=socket.getInetAddress().getHostAddress();
            setName("inputStreamHandler: "+ip);
            try {
                dataInputStream=new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                logger.error(getName(), e);
            }
            setDaemon(false);
            start();
        }

        void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        @Override
        public void run() {
            logger.info("Поток "+getName()+" запущен");
            try {
                while (true) {                                                   //Подумать как организовать цикл
                    logger.info("Принимаем сообщение");
                    msg = dataInputStream.readUTF();                            //Принимаем сообщение
                    logger.info("Принято сообщение: " + msg);
                    propertyChangeSupport.firePropertyChange("msg", null, msg);
                }
            } catch (IOException e) {
                logger.warn(getName()+" сообщение не принято",e);
                try {
                    dataInputStream.close();
                    logger.info("Входный поток закрыт");
                } catch (IOException e1) {
                    logger.error("Входной поток "+ip+" не закрыт");
                }
            }
        }
    }

    private class OutputHandler implements PropertyChangeListener{
        private final Logger logger=Logger.getLogger(OutputHandler.class);

        private String ip;

        public OutputHandler() {
            ip=socket.getInetAddress().getHostAddress();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for (Socket client:clientlist) {
                try {
                    dataOutputStream=new DataOutputStream(client.getOutputStream());
                    dataOutputStream.writeUTF(msg);
                } catch (IOException io) {
                    logger.error("Сообщение: " + msg + " не отправлено");
                    try {
                        dataOutputStream.close();
                        logger.info("Выходной поток закрыт");
                    } catch (IOException e1) {
                        logger.error("Выходной поток " + ip + " не закрыт");
                    }
                }
            }
            logger.info("Отправлено: " + msg);
        }
    }

}