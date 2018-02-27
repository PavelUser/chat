package com.company.client;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

public class ClientChat{
    static final Logger log=Logger.getLogger(ClientChat.class);

    public static void main(String args[]) {
        final int port = 2000;

        log.info("Клиент запущен\n");

        Scanner scanner = new Scanner(System.in);
        log.info("Введите ip сервера: ");
        String ip=scanner.nextLine();                                           //Переменная для чтения строки, ввденной с клавиатуры

        try (Socket socket = new Socket(ip, port)){

            log.info("Соединение с сервером установлено"
                    +"\nВведите сообщение\n");

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            Thread thread=new Thread(){                                         //Поток обработки входящего сообщения
                private String line;
                private Calendar calendar;

                @Override
                public void run() {
                    while (true){

                        try {
                            line = dataInputStream.readUTF();                   //прием сообщения от сервера
                        }catch (IOException ex){
                            log.info("Сообщение не может быть прочитано\n");
                        }

                        calendar=Calendar.getInstance();                        //Получаем время доставки

                        System.out.printf("\n\t\t%tT \n%s", calendar,           //Печать времени и сообщения
                                "Ответ сервера: " + line+"\n");
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();

            String line;
            Calendar calendar;

            while (true) {
                line = scanner.nextLine();
                calendar=Calendar.getInstance();                                //Получаем время отправки
                dataOutputStream.writeUTF(line);                                //передача сообщения серверу

                System.out.printf("\t\t%tT \n%s", calendar,
                        "Отправлена строка: " + line);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.info("Ошибка I/O: " + e.getMessage()+"\n");
        }

    }

}
