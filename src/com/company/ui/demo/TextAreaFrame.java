package com.company.ui.demo;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;

import static java.awt.event.KeyEvent.VK_ENTER;

public class TextAreaFrame extends JFrame {
    KeyStroke ENTER_KEY = KeyStroke.getKeyStroke(VK_ENTER, 0);

    public static void replaceOnEmptyAction(JComponent component, KeyStroke keyStroke) {
        component.getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke, "none");
    }

    public TextAreaFrame(){
        super();
        setTitle("TextArea");
        setSize(new Dimension(400, 200));

        JPanel contentPanel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        //добавление скрола  - создаешь просто обертку из элемента  JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(contentPanel);

        /*
            так можно убрать обработчик на любую клавишу (в нашем случае на enter )
        */
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(ENTER_KEY, "none");
    }

    public static void main(String[] args){
        TextAreaFrame frame = new TextAreaFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
