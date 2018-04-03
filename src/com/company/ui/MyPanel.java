package com.company.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_ENTER;

class MyPanel extends JPanel {

	MyPanel(int x,int y){
		setSettings(x,y);
		//*********************************************************************************
				//Сделать отдельную панель
		addMyComponents();
	}

	private void setSettings(int x,int y){
		setLayout(null);
		setBackground(Color.GRAY);
		setBounds(x,y,437,569);
	}

	private void addMyComponents(){
		//*************************************TEMP****************************************
		String columns[]={"ID","Name"}, rows[][]={{"1","aaaaaaaaa"},
				{"2","bbbbbbbbb"},
				{"3","ccccccccc"},
				{"4","ddddddddd"}};
		add(new MyTable(1,1,305,495,columns,rows));       //306, 499
		//*************************************TEMP****************************************                                                         //(306,1,130,499));
		MyTextArea textArea=new MyTextArea(0,0,305,72);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setLocation(1,496);

		//add(new MyTextArea(1,496,305,72));   //306, 568
		add(scrollPane);
		add(new MyButton(306,496,130,72));   //436, 568
	}

	private class MyTextArea extends JTextArea{
		KeyStroke ENTER_KEY = KeyStroke.getKeyStroke(VK_ENTER, 0);

		MyTextArea(int x,int y,int width,int height){
			setSettings(x,y,width,height);

			getInputMap(JComponent.WHEN_FOCUSED).put(ENTER_KEY, "none");

			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==VK_ENTER)
					{
						System.out.println("Сделать функцию для отправки");
						setText("");
					}
				}
			});
		}

		private void setSettings(int x,int y,int width,int height){
			setBounds(x,y,width,height);
			setLineWrap(true);
			setWrapStyleWord(true);
			setAutoscrolls(true);
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
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createBevelBorder(BevelBorder.LOWERED),
					BorderFactory.createEmptyBorder(100, 100, 100, 100)));		//что за цифры?
		}

	}                  //разобраться как использовать
}
