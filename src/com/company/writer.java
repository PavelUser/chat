package com.company;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

abstract class writer {

    static void write(String str){
        String s="ждем сообщение от клиента"+"\nПрислали строку: "+str+"\nОтпровляем обратно"+"\nОтправлена строка: "+str+"\n\n";
        try (BufferedWriter writer=new BufferedWriter(new FileWriter("E:\\Android\\TCP@IP\\test.txt"))){
            writer.write(s.toCharArray(),0,s.length());
        }catch (FileNotFoundException f){
            System.out.println("Файл не найден");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
