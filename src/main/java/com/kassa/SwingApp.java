package com.kassa;

import com.kassa.client.CheckClient;
import com.kassa.client.ProductClient;
import com.kassa.entity.Check;
import com.kassa.entity.Product;
import com.kassa.service.CheckService;
import com.kassa.service.ProductService;
import com.kassa.support.DoubleInputTextVerifier;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class SwingApp extends JFrame {
    CheckClient checkClient = new CheckClient();
    CheckService checkService = new CheckService(checkClient);

    ProductClient productClient = new ProductClient();
    ProductService productService = new ProductService(productClient);

    private JPanel mainPanel;
    private JPanel addCheckPanel;
    private JPanel checkAddedSuccessfulPanel;
    private JPanel addProductPanel;
    private JPanel getAllChecksPanel;
    private DefaultTableModel productsModel;

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
        setSize(800, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        add(mainPanel);
        JButton addButton = new JButton("Новый чек");
        addButton.addActionListener((ActionEvent event) -> {
            mainPanel.setVisible(false);
            addNewCheck();
        });
        mainPanel.add(addButton);

        JButton getAllChecksButton = new JButton("Смотреть чеки");
        getAllChecksButton.addActionListener((ActionEvent event) -> {
            mainPanel.setVisible(false);
            getAllChecks();
        });
        mainPanel.add(getAllChecksButton);
    }

    private void getAllChecks() {
        getAllChecksPanel = new JPanel(new BorderLayout(0, 1));
        JPanel panel = new JPanel(new GridLayout(1, 0));
        getAllChecksPanel.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> {
            saveChanges();
            getAllChecksPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
        buttonPanel.add(okButton);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(actionEvent -> {
            saveChanges();
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e-> {
            getAllChecksPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
        buttonPanel.add(cancelButton);

        getAllChecksPanel.add(buttonPanel, BorderLayout.SOUTH);

        //check table
        JTable checksTable = createChecksTable();
        checksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane checksPane = new JScrollPane(checksTable);
        checksPane.setMinimumSize(new Dimension(400, 300));
        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new GridLayout());
        checkPanel.add(checksPane);

        String[] columns = new String[]{
                "Id", "Название", "Стоимость", "Количество", "Вес", "Комментарий"
        };
        JTable productsTable = new JTable();
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsModel = new DefaultTableModel(new Object[][]{}, columns);
        productsTable.setModel(productsModel);
        JScrollPane productsPane = new JScrollPane(productsTable);

        JPanel verticalButtonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(verticalButtonPanel,BoxLayout.Y_AXIS);
        verticalButtonPanel.setLayout(layout);

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener( e-> productsModel.addRow(new Object[][]{
            null, null,null,null,null,null
        }));
        verticalButtonPanel.add(addButton);

        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(e-> {
            int i;
                i = JOptionPane.showConfirmDialog(getParent(), "Удалить этот продукт?","Удаление продукта",
                        JOptionPane.YES_NO_OPTION);
                if(i == 0){
                    productsModel.removeRow(productsTable.getSelectedRow());
            }});
        verticalButtonPanel.add(removeButton);

        JButton copyButton = new JButton("Копировать");
        copyButton.addActionListener(e->{
            productsModel.addRow(new Object[]{productsTable.getSelectedRow()});
        });
        verticalButtonPanel.add(copyButton);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(productsPane, BorderLayout.CENTER);
        rightPanel.add(verticalButtonPanel, BorderLayout.WEST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, checkPanel, rightPanel);
        panel.add(splitPane);
        add(getAllChecksPanel);

        //selection listener
        ListSelectionModel checkSelectionModel = checksTable.getSelectionModel();
        checkSelectionModel.addListSelectionListener(listSelectionEvent ->
                repaintProductTable(checksTable, columns, productsTable));

    }

    private void saveChanges() {
        for (int i = 0; i < productsModel.getRowCount(); i++) {
            Long productId = null;
            if(!productsModel.getValueAt(i, 0).toString().isEmpty()){
                productId = Long.valueOf(productsModel.getValueAt(i, 0).toString());
            }
            String name = productsModel.getValueAt(i, 1).toString();
            Integer sum = 0;
            if(!productsModel.getValueAt(i, 2).toString().isEmpty()){
                sum = Integer.valueOf(productsModel.getValueAt(i, 2).toString()); //добавить валидацию в общ таблицу
            }
            Integer count = 0;
            if(!productsModel.getValueAt(i, 3).toString().isEmpty()){
                count = Integer.valueOf(productsModel.getValueAt(i, 3).toString());
            }
            Long checkId = productService.getProductById(productId).getCheckId();
            BigDecimal weight = new BigDecimal(0.0);
            if(!productsModel.getValueAt(i, 4).toString().isEmpty()){
                weight = new BigDecimal(productsModel.getValueAt(i, 4).toString());
            }
            String comment = productsModel.getValueAt(i, 5).toString();
            Product product = new Product(productId, name, sum, count, weight, checkId, comment);
            if(productService.deleteProduct(productId)){
                productService.addNewProduct(product);
            }
        }
    }

    private void repaintProductTable(JTable checkTable, String[] columns, JTable productsTable) {
        productsModel.setRowCount(0);
        Long valueAt = Long.valueOf(checkTable.getModel().getValueAt(checkTable.getSelectedRow(), 0).toString());

        //product table
        List<Product> allProducts = productService.getAllProductsByCheckId(valueAt);
        Object[][] data = new Object[allProducts.size()][columns.length];
        for (int i = 0; i < data.length; i++) {
            Product product = allProducts.get(i);
            data[i] = new Object[]{
                    product.getId(),
                    product.getProductName(),
                    product.getAmount(),
                    product.getAccount(),
                    product.getWeight(),
                    product.getComment()
            };
            productsModel.addRow(data[i]);
        }
        productsTable.repaint();
    }

    private JTable createChecksTable() {
        String[] columns = new String[]{
                "Id", "Дата", "Магазин", "Сумма", "Комментарий"
        };
        List<Check> allChecks = checkService.getAllChecks();
        Object[][] data = new Object[allChecks.size()][columns.length];
        for (int i = 0; i < data.length; i++) {
            Check check = allChecks.get(i);
            data[i] = new Object[]{
                    check.getId(),
                    check.getDate(),
                    check.getShopName(),
                    check.getSumAmount(),
                    check.getComment()};
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        return new JTable(tableModel);
    }

    private void addNewCheck() {
        addCheckPanel = new JPanel();
        add(addCheckPanel);

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
        sumField.setInputVerifier(new DoubleInputTextVerifier(sumField, sumLabel));

        JTextField commentField = new JTextField();
        addCheckPanel.add(commentField);
        JLabel commentLabel = new JLabel("Комментарий");
        addCheckPanel.add(commentLabel);

        JButton okButton = new JButton("OK");

        okButton.addActionListener(e -> {
            Long checkId = checkService.addNewCheck(new Check(null,
                    Double.valueOf(sumField.getText()),
                    dlf.stringToLocalDate(dtp.getJFormattedTextField().getText()),
                    shopField.getText(),
                    commentField.getText())).getId();
            addCheckPanel.setVisible(false);
            checkAddedSuccessful(checkId);
        });
        addCheckPanel.add(okButton);
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> {
            addCheckPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
        addCheckPanel.add(cancelButton);
    }

    private void checkAddedSuccessful(Long checkId) {
        checkAddedSuccessfulPanel = new JPanel();
        add(checkAddedSuccessfulPanel);

        JButton addProductButton = new JButton("Добавить товары");
        addProductButton.addActionListener(e -> {
            addNewProduct(checkId);
            checkAddedSuccessfulPanel.setVisible(false);
        });
        checkAddedSuccessfulPanel.add(addProductButton);

        JButton addCheckButton = new JButton("Добавить еще чек");
        addCheckButton.addActionListener(e -> {
            addNewCheck();
            checkAddedSuccessfulPanel.setVisible(false);
        });
        checkAddedSuccessfulPanel.add(addCheckButton);

        JButton toMainPanel = new JButton("В главное меню");
        toMainPanel.addActionListener(e -> {
            mainPanel.setVisible(true);
            checkAddedSuccessfulPanel.setVisible(false);
        });
        checkAddedSuccessfulPanel.add(toMainPanel);
        checkAddedSuccessfulPanel.setVisible(true);
    }

    private void addNewProduct(Long checkId) {

        addProductPanel = new JPanel();
        add(addProductPanel);
        addCheckPanel.setVisible(false);

        GridLayout layout = new GridLayout(7, 2, 5, 12);
        addProductPanel.setLayout(layout);

        JTextField productNameField = new JTextField();
        addProductPanel.add(productNameField);
        JLabel productNameLabel = new JLabel("Название товара");
        addProductPanel.add(productNameLabel);

        JTextField sumField = new JTextField();
        addProductPanel.add(sumField);
        JLabel sumLabel = new JLabel("Стоимость");
        addProductPanel.add(sumLabel);
        sumField.setInputVerifier(new DoubleInputTextVerifier(sumField, sumLabel));

        JTextField countField = new JTextField();
        addProductPanel.add(countField);
        JLabel countLabel = new JLabel("Количество");
        addProductPanel.add(countLabel);
        countField.setInputVerifier(new DoubleInputTextVerifier(countField, countLabel));

        JTextField weightField = new JTextField();
        addProductPanel.add(weightField);
        JLabel weightLabel = new JLabel("Вес");
        addProductPanel.add(weightLabel);
        weightField.setInputVerifier(new DoubleInputTextVerifier(weightField, weightLabel));

        JTextField commentField = new JTextField();
        addProductPanel.add(commentField);
        JLabel commentLabel = new JLabel("Комментарий");
        addProductPanel.add(commentLabel);

        JLabel resultLabel = new JLabel("");
        addProductPanel.add(resultLabel);
        resultLabel.setForeground(new Color(28, 168, 103));
        JLabel label = new JLabel("");
        addProductPanel.add(label);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            productService.addNewProduct(
                    new Product(null,
                            productNameField.getText(),
                            Integer.valueOf(sumField.getText()),
                            Integer.valueOf(countField.getText()),
                            new BigDecimal(weightField.getText()),
                            checkId,
                            commentField.getText()));
            resultLabel.setText("Товар " + productNameField.getText() + " добавлен.");
            productNameField.setText("");
            sumField.setText("");
            countField.setText("");
            weightField.setText("");
            commentField.setText("");
        });
        addProductPanel.add(okButton);
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> {
            addProductPanel.setVisible(false);
            checkAddedSuccessfulPanel.setVisible(true);
        });
        addProductPanel.add(cancelButton);
    }

    static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

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

        public LocalDate stringToLocalDate(String text) {
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
            return LocalDate.parse(text, dtf);
        }
    }
}
