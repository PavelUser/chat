package com.company.server;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerChat {
    private static final Logger log = Logger.getLogger(ServerChat.class);

    static boolean work=true;

    public static void main(String[] args){

        final int port=2000;
        InetAddress inetAddress=null;

        try {
            inetAddress = InetAddress.getLocalHost();
        }catch (UnknownHostException host){
            host.printStackTrace();
        }

        log.info("ip address(getLocalHost): " + inetAddress
                +"\nЖдём подключения клиента\n");

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {

            log.info("Соединение с клиентом установлено\n");

            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());

            new Thread() {
                private boolean running = true;

                @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);

                    do {
                        if (scanner.nextInt() == 1)
                            running = false;
                    } while (running);

                    work = false;
                }
            }.start();
            String str;

            log.info("Для выключения сервера введите 1\n\n");

            while (work) {

                log.info("ждем сообщение от клиента\n");
                str = inStream.readUTF();
                log.info("Прислали строку: " + str+"\n");
                log.info("Отпровляем обратно\n");
                outstream.writeUTF(str);
                outstream.flush();
                log.info("Отправлена строка: " + str + "\n\n");
            }

            socket.close();
        }catch (IOException io){
            log.error("I/O exception",io);
        }

        log.info("Сервер выключен\n");
    }
}