import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseTracker extends JFrame {
    private JTextField descriptionField;
    private JTextField amountField;
    private JButton addButton;
    private JList<String> expenseList;
    private JLabel totalLabel;

    private List<String> expenses;
    private double total;

    private static final String DATA_FILE = "expenses.txt";

    public ExpenseTracker() {
        setTitle("Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        descriptionField = new JTextField(20);
        amountField = new JTextField(10);
        addButton = new JButton("Add Expense");
        expenseList = new JList<>();
        totalLabel = new JLabel("Total Expenses: $0.00");

       
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(expenseList), BorderLayout.CENTER);
        add(totalLabel, BorderLayout.SOUTH);


        expenses = new ArrayList<>();
        total = 0.0;

        
        loadExpenseData();

       
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
    }

    private void addExpense() {
        String description = descriptionField.getText();
        String amountText = amountField.getText();

        if (!description.isEmpty() && !amountText.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountText);
                expenses.add(description + " - $" + amount);
                total += amount;
                updateExpenseList();
                updateTotalLabel();
                descriptionField.setText("");
                amountField.setText("");

              
                saveExpenseData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.");
            }
        }
    }

    private void updateExpenseList() {
        expenseList.setListData(expenses.toArray(new String[0]));
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total Expenses: $" + String.format("%.2f", total));
    }

    private void loadExpenseData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenses.add(line);
                total += Double.parseDouble(line.split(" - ")[1].substring(1));
            }
            updateExpenseList();
            updateTotalLabel();
        } catch (IOException | NumberFormatException ex) {
            
        }
    }

    private void saveExpenseData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (String expense : expenses) {
                writer.write(expense);
                writer.newLine();
            }
        } catch (IOException ex) {
       
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExpenseTracker tracker = new ExpenseTracker();
                tracker.setVisible(true);
            }
        });
    }
}