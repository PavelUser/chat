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
    private ConnectionHandler connectionHandler;
    private OutputHandler outputHandler;

    public static void main(String[] args){
        ServerChat serverChat=new ServerChat();
        serverChat.startWork();
    }

    private void showIP(){
        try {
            log.info("ip address: "+InetAddress.getLocalHost().getHostAddress());
        }catch (UnknownHostException host){
            log.error("IP не получен", host);
        }
    }

    private void showCommands(){
        System.out.print("Список доступных команд:\n"+"exit\n"+"list\n"+"count\n");
    }

    private boolean isWorking(){
        return working;
    }

    private void inverseWorking(){
        working=!working;
    }

    private void startWork() {
        try {
            serverSocket=new ServerSocket(PORT,20);
            showIP();
            outputHandler=new OutputHandler();
            connectionHandler=new ConnectionHandler();
            showCommands();
            commandHandler();

            while (isWorking()) {
                Thread.sleep(1000);
            }

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
                        case "exit":
                            running = false;
                            break;
                        case "count":
                            System.out.println(getCountClient());
                            break;
                        case "list":
                            System.out.println("\tСписок клиентов");
                            for (Socket client:clientlist)
                                System.out.println(client.getInetAddress());
                            break;
                        default:
                            System.out.println("Список доступных команд:\nexit\nlist\ncount");
                            break;
                    }
                } while (running);

                inverseWorking();
            }
        }.start();
    }

    private class ConnectionHandler extends Thread {
        final private Logger logger=Logger.getLogger(ConnectionHandler.class);

        ConnectionHandler(){
            super("ConnectionHandler");
            setDaemon(true);
            start();
        }

        @Override
        public void run(){
            log.info("Поток "+getName()+ " Запущен");
            while (true) {                                                           //не нравится выход из цикла нужно ждать подключения
                try {
                    Socket socket = serverSocket.accept();                                         //Обрабатываем порт на наличие подключений
                    logger.info("Соединение с клиентом установлено\n");
                    clientlist.add(socket);                                                 //Добавляем клиента в список подключенных
                    new InputStreamHandler(socket);         //Создаем по потоку на клиента
                } catch (IOException ex) {
                    logger.error(getName(), ex);
                }
            }
        }

        void closeAllSocket(){
            for (Socket s:clientlist) {
                try {
                    s.close();
                }catch (IOException io){
                    logger.warn(s.getInetAddress().getHostAddress()+": сокет не закрыт",io);
                }
            }
            clientlist.clear();
            logger.info("Все сокеты закрыты");
        }

    }

    private class InputStreamHandler extends Thread{
        private Logger logger=Logger.getLogger(InputStreamHandler.class);

        private boolean working=true;
        private Socket socket;
        private DataInputStream dataInputStream;
        private String ip;
        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        InputStreamHandler(Socket socket){
            addPropertyChangeListener(outputHandler);
            this.socket=socket;
            ip=this.socket.getInetAddress().getHostAddress();
            setName("inputStreamHandler: "+ip);
            try {
                dataInputStream=new DataInputStream(this.socket.getInputStream());
            } catch (IOException e) {
                logger.error(getName(), e);
            }
            setDaemon(true);
            start();
        }

        void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        @Override
        public void run() {
            logger.info("Поток "+getName()+" запущен");
            try {
                while (isWorking()) {                                                   //Подумать как организовать цикл
                    logger.info("Принимаем сообщение");
                    msg = dataInputStream.readUTF();

                    switch(msg.toLowerCase()){
                        case "exit":
                            clientlist.remove(socket);
                            logger.info("Клиент отключился "+ip);
                            try {
                                socket.close();
                                dataInputStream.close();
                            }catch (IOException io) {
                                logger.warn("Входной поток " + ip + " не закрыт");
                            }
                            setWorking(false);
                            break;
                        default:
                            logger.info("Принято сообщение: " + msg);
                            propertyChangeSupport.firePropertyChange("msg", null, msg);
                            break;
                    }
                }
            } catch (IOException e) {
                logger.warn(getName()+" сообщение не принято",e);
                try {
                    dataInputStream.close();
                    logger.info("Входный поток закрыт");
                } catch (IOException e1) {
                    logger.warn("Входной поток " + ip + " не закрыт");
                }
            }
        }

        private boolean isWorking(){
            return working;
        }
        private void setWorking(boolean bool){
            working=bool;
        }
    }

    private class OutputHandler implements PropertyChangeListener{
        private final Logger logger=Logger.getLogger(OutputHandler.class);

        private DataOutputStream dataOutputStream;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for (Socket client:clientlist) {
                try {
                    dataOutputStream=new DataOutputStream(client.getOutputStream());
                    dataOutputStream.writeUTF(msg);
                    //dataOutputStream.close();                                         //почему бросает ошибку????
                } catch (IOException io) {
                    logger.error("Сообщение: " + msg + " не отправлено");
                    try {
                        dataOutputStream.close();
                        logger.info("Выходной поток закрыт");
                    } catch (IOException e1) {
                        logger.error("Выходной поток " + " не закрыт");
                    }
                }
            }
            logger.info("Отправлено: " + msg);
        }
    }

    private int getCountClient(){
        return clientlist.size();
    }
}