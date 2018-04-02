package com.company.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class MyPanel extends JPanel {               //не нравятся углы панели. Почему они острые и выходят за Border?

	MyPanel(int x,int y){
		setLayout(null);
		setBackground(Color.PINK);
		setBounds(x,y,437,569);
		setBorder(BorderFactory.createCompoundBorder(            //увеличить радиус по углам
				new LineBorder(Color.GRAY, 1,true),
				BorderFactory.createEmptyBorder(200,200,200,200)));

		//*************************************TEMP****************************************
		String columns[]={"ID","Name"}, rows[][]={{"1","aaaaaaaaa"},
				{"2","bbbbbbbbb"},
				{"3","ccccccccc"},
				{"4","ddddddddd"}};
		add(new MyTable(1,1,305,495,columns,rows));       //306, 499
		//*************************************TEMP****************************************                                                         //(306,1,130,499));

		add(new MyTextArea(1,496,305,72));   //306, 568
		add(new MyButton(306,496,130,72));   //436, 568
	}

	private class MyTextArea extends JTextArea{
		MyTextArea(int x,int y,int width,int height){
			setBounds(x,y,width,height);
			setLineWrap(true);
			setWrapStyleWord(true);
			setAutoscrolls(true);

			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_ENTER)
					{
						System.out.println("Сделать функцию для отправки");
						setText("");
						setRows(0);
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
		MyTable(int x,int y,int width, int height,Object[] columns,Object[][] rows) {    //переделать
			super(rows,columns);                                                          //Удалить?
			setBounds(x, y, width, height);
			setBackground(Color.DARK_GRAY);
			setGridColor(Color.WHITE);
			setFont(new Font("Times New Roman",Font.PLAIN,14));
		}

	}                  //разобраться как использовать
}
