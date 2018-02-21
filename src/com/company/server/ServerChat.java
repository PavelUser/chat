package com.company.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerChat {
    static boolean work=true;

    public static void main(String[] args){
        InetAddress inetAddress=null;
        int port=2000;
        String str=null;

        try {
            inetAddress = InetAddress.getLocalHost();
        }catch (UnknownHostException host){
            host.printStackTrace();
        }

        Socket socket=null;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("ip adress(getLocalHost): " + inetAddress);

            System.out.println("Для выключения сервера введите 1"+"\nЖдём подключения клиента");
            socket = serverSocket.accept();

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
        }finally {
            if (socket!=null){
                try {
                    socket.close();
                    System.out.println("Сервер выключен");
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}