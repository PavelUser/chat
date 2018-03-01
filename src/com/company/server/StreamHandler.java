package com.company.server;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StreamHandler extends Thread {                                 //работает как задумывал
                                                                            //только одна проблема с message

    private Logger logger=Logger.getLogger(StreamHandler.class);

    private DataInputStream dataInputStream;

    String message;

    StreamHandler(Socket socket){

        super("StreamHandler: "+socket.getInetAddress().getHostName());

        try {
            dataInputStream=new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.error(getName(), e);
        }

        setDaemon(true);
        start();

    }

    @Override
    public void run() {
        logger.info("Поток "+getName()+" запущен");

        try {
            while (true) {                                                   //Подумать как организовать цикл
                logger.info("Принимаем сообщение");
                message = dataInputStream.readUTF();                         //Принимаем сообщение
                logger.info("Принято сообщение: " + message);
            }
            /*
                Как отдать message в ServerChat? Для дальнейшей отправки всем клиентам
            */

        } catch (IOException e) {
            logger.error(getName(),e);
        }

    }

}
