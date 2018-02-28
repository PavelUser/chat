package com.company.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ConnectionHandler extends Thread {
    final private Logger logger=Logger.getLogger(ConnectionHandler.class);

    private ServerSocket serverSocket;
    private ArrayList<Socket> clientlist=new ArrayList<>(1);

    ConnectionHandler(ServerSocket serverSocket){
        super("ConnectionHandler");
        setDaemon(true);
        this.serverSocket=serverSocket;
        start();
    }

    @Override
    public void run(){
        logger.info("Поток "+this.getName()+ " Запущен");

        try {

            while (true) {
                clientlist.add(serverSocket.accept());
            }

        }catch (IOException ex){
            logger.error(this.getName(),ex);
        }

    }

    ArrayList getClientList(){
        return clientlist;
    }

    void closeAllSocket()throws IOException{
        for (Socket s:clientlist)
            s.close();
    }
}
