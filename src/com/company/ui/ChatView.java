package com.company.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatView extends JPanel {

    public ChatView() {
        /*
            здесь почитай про GridBagLayout http://www.frolov-lib.ru/programming/javasamples/vol5/vol5_5/index.html
            по английски можно здесь https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        */
        setLayout(new GridBagLayout());

        ControlPanel controlPanel = new ControlPanel();

        GridBagConstraints c1 = new GridBagConstraints();
        //положение компонента в сетке по x
        c1.gridx = 0;
        //положение компонента в сетке по y
        c1.gridy = 0;

        //указание на то как растягивать компонет BOTH означает по вертикали и горизонтали
        c1.fill = GridBagConstraints.BOTH;
        //пропорция занимаемого места по горизонтали
        c1.weightx = 1.0;
        //пропорция занимаемого места по вертикали
        c1.weighty = 1.0;
        //отсупы по краям
        c1.insets = new Insets(20, 50, 20, 0);
        add(controlPanel, c1);

        ChatListPanel chatListPanel = new ChatListPanel();
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 1;
        c2.gridy = 0;
        c2.fill = GridBagConstraints.BOTH;
        c2.insets = new Insets(20, 3, 100, 20);
        chatListPanel.setPreferredSize(new Dimension(100, 10));
        add(chatListPanel, c2);
    }

    class ControlPanel extends JPanel {
        JButton sendButton;
        JTextArea sendField;
        ControlPanel() {
            //http://www.frolov-lib.ru/programming/javasamples/vol5/vol5_3/index.html
            super(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("Основаня панель"));

            JPanel sendPanel  =new JPanel(new BorderLayout());
            sendPanel.setPreferredSize(new Dimension(0, 80));

            sendButton = new JButton("Send");
            sendButton.setPreferredSize(new Dimension(80, 0));
            sendButton.addActionListener((ActionEvent ae)->{
				System.out.println("Сделать функцию для отправки");
				sendField.setText(null);
			});

            sendField = new JTextArea();
            sendField.setLineWrap(true);
            sendField.setWrapStyleWord(true);
            sendField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"none");
            sendField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						System.out.println("Сделать функцию для отправки");
						sendField.setText(null);
					}
				}
			});

            sendPanel.add(sendButton, BorderLayout.EAST);
            sendPanel.add(new JScrollPane(sendField), BorderLayout.CENTER);
            add(BorderLayout.SOUTH, sendPanel);

        }
    }

    class ChatListPanel extends JPanel{
         ChatListPanel() {
             setBorder(BorderFactory.createTitledBorder("Список чатов"));
        }
    }
}
