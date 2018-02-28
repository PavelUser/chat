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
    private final Logger log = Logger.getLogger(ServerChat.class);

    DataInputStream inStream;
    DataOutputStream outstream;
    final int port=2000;

    boolean work=true;

    public static void main(String[] args){

        ServerChat serverChat=new ServerChat();
        serverChat.getIP();
        serverChat.startWork();

    }

    private void getIP(){

        try {
            log.info("ip address: "+InetAddress.getLocalHost().getHostAddress());
        }catch (UnknownHostException host){
            log.error("IP сервера не получен", host);
        }
    }

    private void handleMsg(DataInputStream inStream, DataOutputStream outstream, String str) throws IOException{

        log.info("ждем сообщение от клиента\n");
        str = inStream.readUTF();
        log.info("Прислали строку: " + str+"\n");
        log.info("Отпровляем обратно\n");
        outstream.writeUTF(str);
        outstream.flush();
        log.info("Отправлена строка: " + str + "\n\n");
    }

    private void initDataStream(Socket socket) throws IOException{

        inStream = new DataInputStream(socket.getInputStream());
        outstream = new DataOutputStream(socket.getOutputStream());
    }

    private void startWork() {

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {

            log.info("Соединение с клиентом установлено\n");

            initDataStream(socket);
            keyboardHendler();

            log.info("Для выключения сервера введите 1\n\n");
            String str=null;

            while (work) {
                handleMsg(inStream,outstream,str);
            }

        }catch (IOException io){
            log.error("I/O exception",io);
        }

            log.info("Сервер выключен\n");
    }

    private void keyboardHendler(){

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
    }

}