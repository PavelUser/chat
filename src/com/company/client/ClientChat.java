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

            System.out.println("соединение с сервером установлено");

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

                        System.out.println(calendar.get(Calendar.HOUR)+":"      //Печать времени и сообщения (не нравится печать секунд и минут)
                                +calendar.get(Calendar.MINUTE)+":"
                                +calendar.get(Calendar.SECOND)
                                +"\tОтвет сервера: " + line+"\n");
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();

            String line;
            Calendar calendar;

            while (true) {
                System.out.println("Введите сообщение: ");
                line = scanner.nextLine();
                dataOutputStream.writeUTF(line);                                //передача сообщения серверу

                calendar=Calendar.getInstance();                                //Получаем время отправки

                System.out.println(calendar.get(Calendar.HOUR)+":"              //Печать времени и сообщения (не нравится печать секунд и минут)
                        +calendar.get(Calendar.MINUTE)+":"
                        +calendar.get(Calendar.SECOND)
                        +"\tОтправлена строка: " + line
                        + "\nОжидаем ответа от сервера");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ошибка I/O: " + e.getMessage());
        }

    }

}
