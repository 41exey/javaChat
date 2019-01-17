package com.common;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CellRenderer extends JTextArea implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public CellRenderer() {

        super();
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object arg1, boolean isSelected, boolean hasFocus, int row, int column) {

        String data = (String) arg1.toString();
//        System.out.println(data);
        int lineWidth = this.getFontMetrics(this.getFont()).stringWidth(data);
        int lineHeight = this.getFontMetrics(this.getFont()).getHeight();
        int rowWidth = table.getCellRect(row, column, true).width;

        int newRowHeight = (int) ((lineWidth / rowWidth) * (lineHeight)) + lineHeight * 2;
        if (table.getRowHeight(row) != newRowHeight) {
            table.setRowHeight(row, newRowHeight);
        }
        this.setText(data);
        return this;
    }
}
