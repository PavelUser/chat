package com.company.client;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

public class ClientChat{
    static final Logger log=Logger.getLogger(ClientChat.class);

    private final int port = 2000;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Scanner scanner=new Scanner(System.in);
    private String line=null;
    private Calendar calendar=null;

    public static void main(String args[]) {

        new ClientChat().startWork();
    }

    private void startWork(){

        log.info("Клиент запущен\n");
        log.info("Введите ip сервера: ");

        try (Socket socket = new Socket(enterIP(), port)){
            log.info("Соединение с сервером установлено"
                    +"\nВведите сообщение\n");

            initDataStream(socket);
            inputMsgHandler(dataInputStream);

            while (true) {
                sendMsg(line,calendar);
            }

        } catch (UnknownHostException e) {
            log.error("IP не установлен: ", e);
        } catch (IOException e) {
            log.error("Ошибка I/O: ", e);
        }
    }                                                   //отредактировать под новый код

    private String enterIP(){

        return scanner.nextLine();                                           //Переменная для чтения строки, ввденной с клавиатуры
    }

    private void initDataStream(Socket socket) throws IOException {

        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
    }             //отредактировать под новый код

    private void inputMsgHandler(DataInputStream dataInputStream){

        Thread thread=new Thread(){                                         //Поток обработки входящего сообщения
            private String line;
            private Calendar calendar;

            @Override
            public void run() {

                try {

                    while (true){
                        line = dataInputStream.readUTF();                   //прием сообщения от сервера

                        calendar=Calendar.getInstance();                        //Получаем время доставки

                        System.out.printf("\n\t\t%tT \n%s", calendar,           //Печать времени и сообщения
                                "Ответ сервера: " + line+"\n");
                    }

                } catch (EOFException ex){
                    log.error("Соединение с сервером потеряно\n", ex);
                    System.exit(1);
                } catch (IOException ex){
                    log.error("Сообщение не может быть прочитано\n", ex);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }              //отредактировать под новый код

    private void sendMsg(String line, Calendar calendar) throws IOException {

        line = scanner.nextLine();
        calendar=Calendar.getInstance();                                //Получаем время отправки
        dataOutputStream.writeUTF(line);                                //передача сообщения серверу

        System.out.printf("\t\t%tT \n%s", calendar,
                "Отправлена строка: " + line);
    }   //отредактировать под новый код

}
