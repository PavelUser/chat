package com.company.ui;

import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {
    public ChatFrame() throws HeadlessException {
        super();
        setTitle("Chat");
        setSize(new Dimension(600, 400));
        ChatView chatView  = new ChatView();
        setContentPane(chatView);
    }

    public static void main(String[] args) {
        ChatFrame frame = new ChatFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIUtils.centerScreen(frame);
        frame.setVisible(true);
    }
}
