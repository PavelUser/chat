package com.company.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientChat{

    public static void main(String args[]) {
        int port = 2000;
        //String ipadress = "192.168.1.103";

        System.out.println("Клиент запущен");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ip сервера: ");
        String ip=scanner.nextLine();

        try (Socket socket = new Socket(ip, port)){

            System.out.println("соединение с сервером установлено");

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String line;

            while (true) {

                System.out.println("Введите сообщение: ");
                line = scanner.nextLine();
                dataOutputStream.writeUTF(line);
                System.out.println("Отправлена строка: " + line
                        + "\nОжидаем ответа от сервера");
                line = dataInputStream.readUTF();
                System.out.println("Ответ сервера: " + line+"\n");

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ошибка I/O: " + e.getMessage());
        }

    }

}
