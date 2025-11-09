import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class FinanceTrackerApp {
    private JFrame frame;
    private JTextField descriptionField, amountField, dateField;
    private JComboBox<String> categoryComboBox, typeComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private ArrayList<Transaction> transactionList;
    private JLabel balanceLabel;
    private double balance;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanceTrackerApp().initialize());
    }

    private void initialize() {
        transactionList = new ArrayList<>();
        balance = 0.0;

        frame = new JFrame("Personal Finance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        frame.add(mainPanel);

        // Top Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Transaction"));

        descriptionField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();
        categoryComboBox = new JComboBox<>(new String[] { "Food", "Rent", "Utilities", "Entertainment", "Other" });
        typeComboBox = new JComboBox<>(new String[] { "Income", "Expense" });
        JButton addButton = new JButton("Add Transaction");
        JButton deleteButton = new JButton("Delete Selected");
        JButton sortButton = new JButton("Sort by Date");

        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table Panel (Center)
        tableModel = new DefaultTableModel(new String[] { "Date", "Description", "Amount", "Category", "Type" }, 0);
        transactionTable = new JTable(tableModel);
        transactionTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(transactionTable);
        mainPanel.add(tableScroll, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        balanceLabel = new JLabel("Current Balance: $0.00");
        buttonPanel.add(sortButton);
        bottomPanel.add(balanceLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());
        sortButton.addActionListener(e -> sortTransactionsByDate());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addTransaction() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryComboBox.getSelectedItem().toString();
            String type = typeComboBox.getSelectedItem().toString();
            String date = dateField.getText();

            Transaction transaction = new Transaction(description, amount, category, type, date);
            transactionList.add(transaction);

            if (type.equals("Income")) {
                balance += amount;
            } else {
                balance -= amount;
            }

            balanceLabel.setText("Current Balance: $" + String.format("%.2f", balance));

            tableModel.addRow(new Object[] { date, description, amount, category, type });

            descriptionField.setText("");
            amountField.setText("");
            dateField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid amount.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow >= 0) {
            double amount = (double) tableModel.getValueAt(selectedRow, 2);
            String type = (String) tableModel.getValueAt(selectedRow, 4);

            if (type.equals("Income")) {
                balance -= amount;
            } else {
                balance += amount;
            }
            balanceLabel.setText("Current Balance: $" + String.format("%.2f", balance));

            transactionList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sortTransactionsByDate() {
        Collections.sort(transactionList, (t1, t2) -> t1.getDate().compareTo(t2.getDate()));
        tableModel.setRowCount(0);
        for (Transaction t : transactionList) {
            tableModel.addRow(
                    new Object[] { t.getDate(), t.getDescription(), t.getAmount(), t.getCategory(), t.getType() });
        }
    }
}
