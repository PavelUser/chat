package com.company.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class MyFrame extends JFrame {
   MyFrame(int width,int height){
      setTitle("Chat");
      setSize(width,height);
      setLocationRelativeTo(null);
      setBackground(Color.GRAY);
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

      add(new MyPanel(5,5));

      setVisible(true);
   }

   public static void main(String[] a){
      new MyFrame(500,660);
   }

   private class MyPanel extends JPanel{               //не нравятся углы панели. Почему они острые и выходят за Border?
      MyPanel(int x,int y){
         setLayout(null);
         setBackground(Color.PINK);
         setBounds(x,y,437,569);
         setBorder(BorderFactory.createCompoundBorder(            //увеличить радиус по углам
                 new LineBorder(Color.GRAY, 1,true),
                 BorderFactory.createEmptyBorder(200,200,200,200)));

         add(new MyTable(1,1,305,495));       //306, 499
                                                                  //(306,1,130,499));
         add(new MyTextArea(1,496,305,72));   //306, 568
         add(new MyButton(306,496,130,72));   //436, 568
      }

      private class MyTextArea extends JTextArea{
         MyTextArea(int x,int y,int width,int height){
            setBounds(x,y,width,height);
            setLineWrap(true);
            setAutoscrolls(true);

            addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  if (e.getKeyCode()==KeyEvent.VK_ENTER)
                  {
                     System.out.println("Сделать функцию для отправки");
                     setText(null);                                  //Переделать
                  }
               }
            });
         }

      }            //доделать

      private class MyButton extends JButton{
         MyButton(int x,int y,int width, int height){
            setBounds(x,y,width,height);
            setText("Отправить");

            addActionListener((ActionEvent ae)->{
               System.out.println("Сделать функцию для отправки");
            });
         }

      }                //не нравится внешний вид. Возможно уберу или переделаю

      private class MyTable extends JTable{
         MyTable(int x,int y,int width, int height) {
            setBounds(x, y, width, height);
            setAutoscrolls(true);
            setBackground(Color.DARK_GRAY);
         }

      }                  //разобраться как использовать

   }

}