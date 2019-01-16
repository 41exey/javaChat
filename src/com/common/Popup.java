package com.common;

import javax.swing.*;
import java.awt.*;

public class Popup {
    public static JPanel createPopupPanel(JScrollPane scroll) {
        JPanel popupPanel = new JPanel(new BorderLayout());
        popupPanel.setOpaque(false);
        popupPanel.setMaximumSize(new Dimension(70, 70));

        popupPanel.setVisible(false);
        popupPanel.setBackground( Color.BLACK );

        JButton popupCloseButton = new JButton("Down");
//        RoundButton popupCloseButton = new RoundButton(">");
        popupPanel.add(wrapInPanel(popupCloseButton), BorderLayout.SOUTH);

        popupCloseButton.addActionListener(e -> {
            JScrollBar verticalScroll = scroll.getVerticalScrollBar();
            System.out.println("Value: " + verticalScroll.getValue() + " Maximum: " + verticalScroll.getMaximum());

            verticalScroll.setValue(verticalScroll.getMaximum());

        });

        return popupPanel;
    }

    private static JPanel wrapInPanel(JComponent component) {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(50, 210, 250, 100));
        jPanel.add(component);
        return jPanel;
    }
}
