package com.kassa.support;

import javax.swing.*;
import java.awt.*;

public class DoubleInputTextVerifier extends InputVerifier {
    private final JTextField textField;
    private final JLabel label;
    private String labelText;

    public DoubleInputTextVerifier(JTextField textField, JLabel label) {
        this.textField = textField;
        this.label = label;
    }

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        try {
            Double.valueOf(text);
            if (labelText != null) {
                label.setText(labelText);
            }
            return true;
        } catch (NumberFormatException e) {
            textField.setSelectionStart(0);
            textField.setSelectionEnd(textField.getText().length());
            textField.setSelectedTextColor(Color.RED);
            labelText = label.getText();
            label.setText("<html><font color='red'>Только цифры</font></html>");
            return false;
        }
    }
}
