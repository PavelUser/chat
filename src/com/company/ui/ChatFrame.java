package com.company.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatFrame extends JFrame {
    public ChatFrame() throws HeadlessException {
        super();
        setTitle("Chat");
        setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Android\\TCP@IP\\src\\com\\company\\ui\\mail2.png"));
        setSize(new Dimension(600, 400));
        ChatView chatView  = new ChatView();
        setContentPane(chatView);
    }

    public static void main(String[] args) {
        ChatFrame frame = new ChatFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Реализовать отключение от сервера");
            }
        });
        UIUtils.centerScreen(frame);
        frame.setVisible(true);
    }
}
