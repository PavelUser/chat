package com.company.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
        chatListPanel.setPreferredSize(new Dimension(100, 20));
        add(chatListPanel, c2);
    }

    class ControlPanel extends JPanel {
		JTable textTable;
        JButton sendButton;
        JTextArea sendField;
        ArrayList<String> listMessage = new ArrayList<>(20);
		MyTableModel myTableModel;

        ControlPanel() {
            //http://www.frolov-lib.ru/programming/javasamples/vol5/vol5_3/index.html
            super(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("Основная панель"));
            //****************************Источник ошибки************************************Думаю проблема с scrollPane
            JPanel textPanel = new JPanel(new BorderLayout());

            myTableModel = new MyTableModel(listMessage);
			textTable = new JTable(myTableModel);
			textTable.setBackground(Color.PINK);
			textTable.setPreferredSize(new Dimension(10,70));

			JScrollPane scrollPane = new JScrollPane(textTable);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			textPanel.add(scrollPane,BorderLayout.WEST);
            add(textPanel,BorderLayout.CENTER);
            //*******************************************************************************

            JPanel sendPanel = new JPanel(new BorderLayout());
            sendPanel.setPreferredSize(new Dimension(0, 80));

            sendButton = new JButton("Send");
            sendButton.setPreferredSize(new Dimension(80, 0));
            sendButton.addActionListener((ActionEvent ae)->{
				sendMessage();
			});

            sendField = new JTextArea();
            sendField.setLineWrap(true);
            sendField.setWrapStyleWord(true);
            sendField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"none");
            sendField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER)
						sendMessage();
				}
			});

            scrollPane = new JScrollPane(sendField);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

            sendPanel.add(sendButton, BorderLayout.EAST);
            sendPanel.add(scrollPane, BorderLayout.CENTER);
            add(BorderLayout.SOUTH, sendPanel);

        }

		private void sendMessage(){
			System.out.println("Привязвть к серверу");
			if(sendField.getText().equals(""))
				return;
			listMessage.add(sendField.getText());
			myTableModel.fireTableDataChanged();
			sendField.setText(null);
		}

		private class MyTableModel extends AbstractTableModel{
			private ArrayList<String> listMessage;

			MyTableModel(ArrayList<String> listMessage){
				this.listMessage = listMessage;
			}

			@Override
			public int getRowCount() {
				return listMessage.size();
			}

			@Override
			public int getColumnCount() {
				return 1;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return listMessage.get(rowIndex);
			}
		}
    }

    class ChatListPanel extends JPanel{
        JList userList;
         ChatListPanel() {
             setBorder(BorderFactory.createTitledBorder("Список чатов"));
             setLayout(new BorderLayout());

             userList=new JList();
             userList.setBackground(Color.PINK);

             add(userList,BorderLayout.CENTER);
        }
    }
}
