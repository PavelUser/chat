package com.company.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ConnectionHandler extends Thread {                                         //работает как задумывал
    final private Logger logger=Logger.getLogger(ConnectionHandler.class);

    private boolean running = true;

    private StreamHandler streamHandler;
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientlist=new ArrayList<>(2);        //В будущем поменять емкость

    ConnectionHandler(ServerSocket serverSocket){
        super("ConnectionHandler");
        this.serverSocket=serverSocket;
        setDaemon(true);
        start();
    }

    @Override
    public void run(){
        logger.info("Поток "+getName()+ " Запущен");

        try {

            while (running) {                                       //не нравится выход из цикла нужно ждать подключения
                socket=serverSocket.accept();                       //Обрабатываем порт на наличие подключений
                logger.info("Соединение с клиентом установлено\n");
                clientlist.add(socket);                             //Добавляем клиента в список подключенных

                streamHandler=new StreamHandler(socket);            //Создаем по потоку на клиента
                                                                    //для обработки входящих сообщений

            }
            logger.info("Остановлен поток: "+getName());

        }catch (IOException ex){
            logger.error(getName(),ex);
        }

    }

    ArrayList<Socket> getClientList(){
        return clientlist;
    }

    void closeAllSocket(){

        try {
            for (Socket s:clientlist)
                s.close();
        }catch (IOException io){
            logger.error(getName()+" : сокет не закрыт",io);
        }
        logger.info("Все сокеты закрыты");
    }

    void mySuspend(){
        running=false;
    }                             //пока пойдет

}
