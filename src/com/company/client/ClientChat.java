package com.company.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

public class ClientChat{

    public static void main(String args[]) {
        int port = 2000;

        System.out.println("Клиент запущен");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ip сервера: ");
        String ip=scanner.nextLine();                                           //Переменная для чтения строки, ввденной с клавиатуры

        try (Socket socket = new Socket(ip, port)){

            System.out.println("Соединение с сервером установлено"
                    +"\nВведите сообщение");

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            Thread thread=new Thread(){                                         //Поток обработки входящего сообщения
                private String line;
                private Calendar calendar;

                @Override
                public void run() {
                    while (true){

                        try {
                            line = dataInputStream.readUTF();                   //прием сообщения от сервера
                        }catch (IOException ex){
                            System.out.println("Сообщение не может быть прочитано");
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
            System.out.println("Ошибка I/O: " + e.getMessage());
        }

    }

}
