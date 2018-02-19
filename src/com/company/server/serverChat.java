package com.company.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class serverChat {
    static boolean bool=true;

    public static void main(String[] args){
        InetAddress ipadress=null;
        int port=2000;
        String str=null;
        try {
            ipadress = InetAddress.getLocalHost();
        }catch (UnknownHostException host){
            host.printStackTrace();
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("ip adress(getLocalHost): " + ipadress);

            Socket socket = serverSocket.accept();
            System.out.println("Ждём подключения");

            DataInputStream instream = new DataInputStream(socket.getInputStream());
            DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());

            new Thread() {
                private boolean running = true;

                @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("поток запущен");

                    do {
                        if (scanner.nextInt() == 1)
                            running = false;
                    } while (running);

                    bool = false;
                    System.out.println("Выключение сервера");
                }
            }.start();

            while (bool) {

                System.out.println("ждем сообщение от клиента"
                        + "\nПрислали строку: " + str);
                str = instream.readUTF();
                System.out.println("\nОтпровляем обратно");
                outstream.writeUTF(str);
                outstream.flush();
                System.out.println("Отправлена строка: " + str + "\n");
            }

            socket.close();
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}