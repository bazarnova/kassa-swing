package com.kassa;

import com.kassa.entity.Check;
import com.kassa.service.CheckService;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class SwingApp extends JFrame {

    CheckService checkService = new CheckService();
    private JPanel mainPanel;
    private JPanel addCheckPanel;
    private JPanel addProductPanel;

    public SwingApp() {
        initUI();
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SwingApp.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            SwingApp ex = ctx.getBean(SwingApp.class);
            ex.setVisible(true);
        });
    }

    private void initUI() {
        setTitle("kassa-swing");
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        add(mainPanel);
        JButton addButton = new JButton("Новый чек");
        addButton.addActionListener((ActionEvent event) -> addNewCheck());
        mainPanel.add(addButton);
    }

    private void addNewCheck() {

        addCheckPanel = new JPanel();
        add(addCheckPanel);
        mainPanel.setVisible(false);

        GridLayout layout = new GridLayout(5, 2, 5, 12);
        addCheckPanel.setLayout(layout);

        UtilDateModel uiModel = new UtilDateModel();
        JDatePanelImpl dtpPanel = new JDatePanelImpl(uiModel, new Properties());
        DateLabelFormatter dlf = new DateLabelFormatter();

        JDatePickerImpl dtp = new JDatePickerImpl(dtpPanel, dlf);

        addCheckPanel.add(dtp);
        JLabel label = new JLabel("Дата чека");
        addCheckPanel.add(label);

        JTextField shopField = new JTextField();
        addCheckPanel.add(shopField);
        JLabel shopLabel = new JLabel("Магазин");
        addCheckPanel.add(shopLabel);

        JTextField sumField = new JTextField();
        addCheckPanel.add(sumField);
        JLabel sumLabel = new JLabel("Сумма");
        addCheckPanel.add(sumLabel);

        JTextField commentField = new JTextField();
        addCheckPanel.add(commentField);
        JLabel commentLabel = new JLabel("Комментарий");
        addCheckPanel.add(commentLabel);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
//            saveCheck();
//            mainPanel.remove(addCheckPanel);
            addNewProduct();
        });
        addCheckPanel.add(okButton);
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener( e-> {
            List<Check> allChecks = checkService.getAllChecks();
            int o = 5;
        });
        addCheckPanel.add(cancelButton);

    }
    private void addNewProduct() {

        addProductPanel = new JPanel();
        add(addProductPanel);
        addCheckPanel.setVisible(false);

        GridLayout layout = new GridLayout(6, 2, 5, 12);
        addProductPanel.setLayout(layout);

        JTextField productNameField = new JTextField();
        addProductPanel.add(productNameField);
        JLabel productNameLabel = new JLabel("Название продукта");
        addProductPanel.add(productNameLabel);

        JTextField sumField = new JTextField();
        addProductPanel.add(sumField);
        JLabel sumLabel = new JLabel("Стоимость");
        addProductPanel.add(sumLabel);

        JTextField weightField = new JTextField();
        addProductPanel.add(weightField);
        JLabel weightLabel = new JLabel("Вес");
        addProductPanel.add(weightLabel);

        JTextField countField = new JTextField();
        addProductPanel.add(countField);
        JLabel countLabel = new JLabel("Количество");
        addProductPanel.add(countLabel);

        JTextField commentField = new JTextField();
        addProductPanel.add(commentField);
        JLabel commentLabel = new JLabel("Комментарий");
        addProductPanel.add(commentLabel);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> addNewProduct());
        addProductPanel.add(okButton);
        JButton cancelButton = new JButton("Отмена");
        addProductPanel.add(cancelButton);

    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "dd.MM.yyyy";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
