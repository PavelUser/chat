package com.company.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends JFrame {

   MyFrame(int width,int height){
      setTitle("Chat");
      setSize(width,height);
      setLocationRelativeTo(null);
      setLayout(null);
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Android\\TCP@IP\\src\\com\\company\\ui\\mail2.png"));

      //*******************TEMP**********************
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            System.out.println("Завершение ..." +
                    "\nРеализовать отключение от сервера");
         }
      });
      //*********************************************
      MyPanel panel=new MyPanel(5,5);
      add(panel);
      setContentPane(panel);

      setVisible(true);
   }

   public static void main(String[] a){
      new MyFrame(500,660);
   }

}