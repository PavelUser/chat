package com.company.ui;

import javax.swing.*;
import java.awt.*;

public final class UIUtils {
    private UIUtils() {

    }

    public static void centerScreen(JFrame frame) {
        Dimension dim = frame.getToolkit().getScreenSize();
        Rectangle abounds = frame.getBounds();
        frame.setLocation((dim.width - abounds.width) / 2,
                (dim.height - abounds.height) / 2);
    }
}
