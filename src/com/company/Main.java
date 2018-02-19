package com.company;

import com.company.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static boolean bool=true;

    public static void main(String[] args) throws IOException{
        new frame();
        int port=2000;
        String str=null;
        InetAddress ipadress=InetAddress.getLocalHost();
        System.out.println("ip adress(getLocalHost): "+ipadress);

        ServerSocket serverSocket=new ServerSocket(port);

       // Socket socket=serverSocket.accept();
        System.out.println("Ждём подключения");

       // DataInputStream instream=new DataInputStream(socket.getInputStream());
       // DataOutputStream outstream=new DataOutputStream(socket.getOutputStream());
        new Thread() {
            private boolean running=true;

            @Override
            public void run() {
                Scanner scanner=new Scanner(System.in);
                System.out.println("поток запущен");

                do{
                    if (scanner.nextInt()==1)
                    running=false;
                }while (running);

                bool = false;
                System.out.println("Выключение сервера");
            }
        }.start();

        while (bool){

            //temp--------------------------------------------
          // writer.write(str);
            //temp--------------------------------------------
           /* System.out.println("ждем сообщение от клиента"
                                +"\nПрислали строку: "+str);
            //str=instream.readUTF();
            System.out.println("\nОтпровляем обратно");
           // outstream.writeUTF(str);
           // outstream.flush();
            System.out.println("Отправлена строка: "+str+"\n");     */
        }
        // socket.close();
	serverSocket.close();
    }
}