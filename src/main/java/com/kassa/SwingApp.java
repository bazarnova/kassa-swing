package com.kassa;

import com.kassa.client.CheckClient;
import com.kassa.client.PhotoClient;
import com.kassa.client.ProductClient;
import com.kassa.entity.Check;
import com.kassa.entity.Photo;
import com.kassa.entity.Product;
import com.kassa.service.CheckService;
import com.kassa.service.PhotoService;
import com.kassa.service.ProductService;
import com.kassa.support.DoubleInputTextVerifier;
import com.kassa.image.ImagePanel;
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

    PhotoClient photoClient = new PhotoClient();
    PhotoService photoService = new PhotoService(photoClient);

    private JPanel mainPanel;
    private JPanel addCheckPanel;
    private JPanel checkAddedSuccessfulPanel;
    private JPanel addProductPanel;
    private JPanel getAllChecksPanel;
    private DefaultTableModel productsModel;
    private JTable checksTable;
    private List<Photo> notProcessedPhotos;

    public SwingApp() {
        notProcessedPhotos = photoService.getNotProcessedPhotos();
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
        setSize(1000, 600);
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

        JButton getAllChecksButton = new JButton("Смотреть/редактировать чеки");
        getAllChecksButton.addActionListener((ActionEvent event) -> {
            mainPanel.setVisible(false);
            getAllChecks();
        });
        mainPanel.add(getAllChecksButton);

        if(!notProcessedPhotos.isEmpty()){
            JButton processChecksButton = new JButton("Обработать чеки");
            processChecksButton.addActionListener((ActionEvent event) -> {
                ImagePanel imagePanel = new ImagePanel(notProcessedPhotos);
                mainPanel.add(imagePanel);
                mainPanel.setVisible(false);
                imagePanel.setVisible(true);
            });
        }
        checkNewPhotosFromTelegram();
    }

    private void checkNewPhotosFromTelegram() {
        if(!notProcessedPhotos.isEmpty()){
            int i;
            i = JOptionPane.showConfirmDialog(getParent(), "Поступили новые чеки, желаете их обработать?", "Телеграм-бот",
                    JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                ImagePanel imagePanel = new ImagePanel(notProcessedPhotos);
                add(imagePanel);
                mainPanel.setVisible(false);
                imagePanel.setVisible(true);
            }
        }
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
        cancelButton.addActionListener(e -> {
            getAllChecksPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
        buttonPanel.add(cancelButton);

        getAllChecksPanel.add(buttonPanel, BorderLayout.SOUTH);

        //check table
        checksTable = createChecksTable();
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
        BoxLayout layout = new BoxLayout(verticalButtonPanel, BoxLayout.Y_AXIS);
        verticalButtonPanel.setLayout(layout);

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> {
            if (checksTable.getSelectedRow() > -1) {
                productsModel.addRow(new Object[][]{null, null, null, null, null, null});
            } else {
                JOptionPane.showMessageDialog(getParent(),
                        "Выберите в левом окне чек", "Внимание", JOptionPane.WARNING_MESSAGE);
            }
        });
        verticalButtonPanel.add(addButton);

        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(e -> {
            if (productsTable.getSelectedRow() > -1){
                int i;
                i = JOptionPane.showConfirmDialog(getParent(), "Удалить этот продукт?", "Удаление продукта",
                        JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    productsModel.removeRow(productsTable.getSelectedRow());
                }
            }
        });
        verticalButtonPanel.add(removeButton);

        JButton copyButton = new JButton("Копировать");
        copyButton.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if(selectedRow>-1){
                Object name = productsModel.getValueAt(selectedRow, 1);
                Object sum = productsModel.getValueAt(selectedRow, 2);
                Object count = productsModel.getValueAt(selectedRow, 3);
                Object weight = productsModel.getValueAt(selectedRow, 4);
                Object comment = productsModel.getValueAt(selectedRow, 5);
                productsModel.addRow(new Object[]{null, name, sum, count, weight, comment});
            }
            else {
                JOptionPane.showMessageDialog(getParent(),
                        "Выберите в правом окне продукт для копирования", "Внимание", JOptionPane.WARNING_MESSAGE);
            }
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
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productsModel.getRowCount(); i++) {
            Long productId = null;
            if (productsModel.getValueAt(i, 0) != null) {
                productId = Long.valueOf(productsModel.getValueAt(i, 0).toString());
            }
            String name = "";
            if (productsModel.getValueAt(i, 1) != null) {
                name = productsModel.getValueAt(i, 1).toString();
            }
            Integer sum = 0;
            if (productsModel.getValueAt(i, 2) != null) {
                sum = Integer.valueOf(productsModel.getValueAt(i, 2).toString());
            }
            Integer count = 0;
            if (productsModel.getValueAt(i, 3) != null) {
                count = Integer.valueOf(productsModel.getValueAt(i, 3).toString());
            }
            Long checkId = Long.valueOf(checksTable.getModel().getValueAt(checksTable.getSelectedRow(), 0).toString());

            BigDecimal weight = new BigDecimal(0.0);
            if (productsModel.getValueAt(i, 4) != null) {
                weight = new BigDecimal(productsModel.getValueAt(i, 4).toString());
            }
            String comment = "";
            if (productsModel.getValueAt(i, 5) != null) {
                comment = productsModel.getValueAt(i, 5).toString();
            }
            Product product = new Product(productId, name, sum, count, weight, checkId, comment);
            products.add(product);
        }
        for (Product product : products) {
            if (product.getId() != null) {
                productService.deleteProduct(product.getId());
            }
            product.setId(null);
        }
        for (Product product : products) {
            productService.addNewProduct(product);
        }
    }

    private void repaintProductTable(JTable checkTable, String[] columns, JTable productsTable) {
        productsModel.setRowCount(0);
        Long checkId = Long.valueOf(checkTable.getModel().getValueAt(checkTable.getSelectedRow(), 0).toString());

        //product table
        List<Product> allProducts = productService.getAllProductsByCheckId(checkId);
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
