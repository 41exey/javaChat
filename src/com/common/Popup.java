package com.common;

import javax.swing.*;
import javax.swing.border.Border;
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
        popupCloseButton.setBorder(new RoundedBorder(7));
        popupPanel.add(wrapInPanel(popupCloseButton), BorderLayout.SOUTH);

        popupCloseButton.addActionListener(e -> {
            JScrollBar verticalScroll = scroll.getVerticalScrollBar();
            System.out.println("Value: " + verticalScroll.getValue() + " Maximum: " + verticalScroll.getMaximum());

            verticalScroll.setValue(verticalScroll.getMaximum());

        });
        popupPanel.setAlignmentX(0.5f);
        popupPanel.setAlignmentY(0.5f);

        return popupPanel;
    }

    private static JPanel wrapInPanel(JComponent component) {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(50, 210, 250, 0));
        jPanel.add(component);
        return jPanel;
    }
}

class RoundedBorder implements Border {
    int radius;
    RoundedBorder(int radius) {
        this.radius = radius;
    }
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }
    public boolean isBorderOpaque() {
        return true;
    }
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x,y,width-1,height-1,radius,radius);
    }
}