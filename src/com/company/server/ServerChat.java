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

        int port=2000;
        InetAddress inetAddress=null;

        try {
            inetAddress = InetAddress.getLocalHost();
        }catch (UnknownHostException host){
            host.printStackTrace();
        }
                /*
        System.out.println("ip adress(getLocalHost): " + inetAddress
                            +"\nЖдём подключения клиента");*/
        log.info("ip adress(getLocalHost): " + inetAddress
                +"\nЖдём подключения клиента");

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {

            System.out.println("Соединение с клиентом установлено");

            DataInputStream instream = new DataInputStream(socket.getInputStream());
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

            System.out.println("Для выключения сервера введите 1");

            while (work) {

                System.out.println("ждем сообщение от клиента");
                str = instream.readUTF();
                System.out.println("Прислали строку: " + str);
                System.out.println("Отпровляем обратно");
                outstream.writeUTF(str);
                outstream.flush();
                System.out.println("Отправлена строка: " + str + "\n");
            }

            socket.close();
        }catch (IOException io){
            io.printStackTrace();
        }

        System.out.println("Сервер выключен");
    }
}