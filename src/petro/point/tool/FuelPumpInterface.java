
package petro.point.tool;
import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import petro.point.tool.FetchFuelPrices;
import javax.swing.*;
import java.util.*;

public class FuelPumpInterface extends javax.swing.JFrame {
    
     private PetroPointInterface petroPointInterface;

    private static class con {

        public con() {
        }
    }
     
    // Node for Singly Linked List
    class TransactionNode {
        
        String fuelType;
        double amount;
        String dateTime;
        TransactionNode next;

        public TransactionNode(String fuelType, int amount, String dateTime) {
            this.fuelType = fuelType;
            this.amount = amount;
            this.dateTime = dateTime;
            this.next = null;
        }
    }

    // Singly Linked List for Transactions
    class FuelTransactionList {
        TransactionNode head;
        TransactionNode tail;
        

        // Add a transaction to the linked list
        public void addTransaction(String fuelType, int amount) {
    String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    TransactionNode newNode = new TransactionNode(fuelType, amount, dateTime);

    if (head == null) {
        head = tail = newNode;
    } else {
        tail.next = newNode;
        tail = newNode;
    }

    // Perform database operations (insert and update) in a transactional way
    performDatabaseOperations(fuelType, amount, dateTime);
    calculatePumpStats(fuelType); 
    }

        // Display all transactions
        public void displayTransactions() {
            TransactionNode current = head;
            while (current != null) {
                System.out.println("Fuel Type: " + current.fuelType + ", Amount: " + current.amount + ", Date-Time: " + current.dateTime);
                current = current.next;
            }
        }

        // Save transaction to database
       private void performDatabaseOperations(String fuelType, int amount, String dateTime) {
        String pumpTable;
            String stockTable;

   if (fuelType.equals("Petrol")) {
                pumpTable = "petrolpump";
                stockTable = "petrolstock";
            } else {
                pumpTable = "dieselpump";
                stockTable = "dieselstock";
            }

            try (Connection con = DBConnection.getdbconnection()) {
                // Start a transaction
                con.setAutoCommit(false);

                // Insert into pump table
                try (PreparedStatement insertStmt = con.prepareStatement(
                        "INSERT INTO " + pumpTable + " (amount, datetime) VALUES (?, ?)")) {
                    insertStmt.setInt(1, amount);
                    insertStmt.setString(2, dateTime);
                    insertStmt.executeUpdate();
                }

        // Deduct stock row by row
        deductStock(con, stockTable, amount);

        // Commit the transaction if both operations succeed
        con.commit();
        System.out.println("Transaction successfully inserted and stock updated.");
    } catch (Exception e) {
        e.printStackTrace();
        try {
            // Roll back if there is an issue
            //con.rollback();
            JOptionPane.showMessageDialog(null, "Error occurred, transaction rolled back: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception rollbackEx) {
            rollbackEx.printStackTrace();
        }
    }
}

// Deduct stock row by row
private void deductStock(Connection con, String stockTable, int amountToDeduct) throws Exception {
    // Query to get rows without any specific ordering
    String fetchStockQuery;
            String idColumn;
            
if (stockTable.equals("petrolstock")) {
                fetchStockQuery = "SELECT psid, amount FROM " + stockTable + " ORDER BY psid DESC";
                idColumn = "psid";
            } else {
                fetchStockQuery = "SELECT dsid, amount FROM " + stockTable + " ORDER BY dsid DESC";
                idColumn = "dsid";
            }

   try (PreparedStatement fetchStmt = con.prepareStatement(fetchStockQuery);
                 ResultSet rs = fetchStmt.executeQuery()) {

                while (rs.next() && amountToDeduct > 0) {
                    int id = rs.getInt(idColumn);
                    int currentAmount = rs.getInt("amount");

                    // Determine the deduction amount for this row
                    int deduction = Math.min(amountToDeduct, currentAmount);

                    // Update the current row's stock
                    try (PreparedStatement updateStmt = con.prepareStatement(
                            "UPDATE " + stockTable + " SET amount = amount - ? WHERE " + idColumn + " = ?")) {
                        updateStmt.setInt(1, deduction);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                    }

            // Reduce the remaining amount to deduct
            amountToDeduct -= deduction;
        }

        if (amountToDeduct > 0) {
            throw new Exception("Not enough stock available to fulfill the request.");
        }
    }
}

    }

    private FuelTransactionList transactionList = new FuelTransactionList();

    public FuelPumpInterface() {
        initComponents();
        calculatePumpStats("Petrol");
        
        //******** Fetch the fuel prices*************************
        Map<String, String> fuelPrices = FetchFuelPrices.fetchFuelPrices();

        // Set the prices in the text fields
        Pprice_txt.setText(fuelPrices.getOrDefault("Petrol", "Price not available"));
        Dprice_txt.setText(fuelPrices.getOrDefault("Diesel", "Price not available"));


    petroPointInterface = new PetroPointInterface();
        
        petroPointInterface = new PetroPointInterface();
    }
    
    private void calculatePumpStats(String fuelType) {
        String pumpTable = fuelType.equals("Petrol") ? "petrolpump" : "dieselpump";
        ArrayList<Integer> pumpAmounts = new ArrayList<>();

        // Step 1: Fetch Data from Database
        try (Connection con = DBConnection.getdbconnection();
             PreparedStatement stmt = con.prepareStatement("SELECT amount FROM " + pumpTable);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pumpAmounts.add(rs.getInt("amount"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching pump data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pumpAmounts.isEmpty()) {
            minpump_txt.setText("0");
            maxpump_txt.setText("0");
            avaragepump_txt.setText("0.00");
            return;
        }

        // Step 2: Sort the Data
        quickSort(pumpAmounts, 0, pumpAmounts.size() - 1);

        // Step 3: Calculate Min, Max, and Average
        int min = pumpAmounts.get(0);
        int max = pumpAmounts.get(pumpAmounts.size() - 1);
        double average = pumpAmounts.stream().mapToDouble(Integer::doubleValue).average().orElse(0);

        // Step 4: Display in JTextFields
        minpump_txt.setText(String.valueOf(min));
        maxpump_txt.setText(String.valueOf(max));
        avaragepump_txt.setText(String.format("%.2f", average));
    }

    // QuickSort Implementation
    private void quickSort(ArrayList<Integer> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private int partition(ArrayList<Integer> list, int low, int high) {
        int pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j) <= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
 


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Pprice_txt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Dprice_txt = new javax.swing.JTextField();
        FuelType_Combo = new javax.swing.JComboBox<>();
        pump_text = new javax.swing.JTextField();
        btn_pump = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        btn_clr = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        maxpump_txt = new javax.swing.JTextField();
        minpump_txt = new javax.swing.JTextField();
        avaragepump_txt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(178, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PETRO POINT");
        jPanel2.add(jLabel1);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 40));

        jPanel3.setBackground(new java.awt.Color(178, 0, 0));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 660, 50));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Today petrol Price : ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 140, 30));

        Pprice_txt.setEditable(false);
        Pprice_txt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Pprice_txt.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(Pprice_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 120, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Today diesel Price :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 140, 30));

        Dprice_txt.setEditable(false);
        Dprice_txt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Dprice_txt.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(Dprice_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 120, 30));

        FuelType_Combo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FuelType_Combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Petrol", "Diesel" }));
        FuelType_Combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FuelType_ComboActionPerformed(evt);
            }
        });
        jPanel1.add(FuelType_Combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 140, 30));

        pump_text.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pump_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pump_textActionPerformed(evt);
            }
        });
        jPanel1.add(pump_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 140, 30));

        btn_pump.setBackground(new java.awt.Color(178, 0, 0));
        btn_pump.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_pump.setForeground(new java.awt.Color(255, 255, 255));
        btn_pump.setText("Pump");
        btn_pump.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pumpActionPerformed(evt);
            }
        });
        jPanel1.add(btn_pump, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 420, 160, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Enter Pump Amount");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 140, 20));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Select Fuel Type");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 80, 20));

        btn_back.setBackground(new java.awt.Color(178, 0, 0));
        btn_back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_back.setForeground(new java.awt.Color(255, 255, 255));
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        jPanel1.add(btn_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 160, 40));

        btn_clr.setBackground(new java.awt.Color(178, 0, 0));
        btn_clr.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_clr.setForeground(new java.awt.Color(255, 255, 255));
        btn_clr.setText("Clear");
        btn_clr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clrActionPerformed(evt);
            }
        });
        jPanel1.add(btn_clr, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 160, 40));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 640, 10));

        jLabel6.setText("Maximum Pump Value");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 130, 30));

        jLabel7.setText("Minimum Pump Value");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, 30));

        jLabel8.setText("Average Pump Value");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, 30));

        maxpump_txt.setEditable(false);
        jPanel1.add(maxpump_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 110, -1));

        minpump_txt.setEditable(false);
        jPanel1.add(minpump_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 110, -1));

        avaragepump_txt.setEditable(false);
        jPanel1.add(avaragepump_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 660, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FuelType_ComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FuelType_ComboActionPerformed
        String fuelType = FuelType_Combo.getSelectedItem().toString();
        calculatePumpStats(fuelType);
    }//GEN-LAST:event_FuelType_ComboActionPerformed

    private void btn_pumpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pumpActionPerformed
         try {
            String fuelType = FuelType_Combo.getSelectedItem().toString();
            int amount = Integer.parseInt(pump_text.getText());

            transactionList.addTransaction(fuelType, amount);
            JOptionPane.showMessageDialog(this, "Fuel pumped successfully!");

            transactionList.displayTransactions();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_pumpActionPerformed

    private void pump_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pump_textActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pump_textActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_clrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_clrActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
     
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FuelPumpInterface().setVisible(true);
            }
        });
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FuelPumpInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Dprice_txt;
    private javax.swing.JComboBox<String> FuelType_Combo;
    private javax.swing.JTextField Pprice_txt;
    private javax.swing.JTextField avaragepump_txt;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_clr;
    private javax.swing.JButton btn_pump;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField maxpump_txt;
    private javax.swing.JTextField minpump_txt;
    private javax.swing.JTextField pump_text;
    // End of variables declaration//GEN-END:variables
}
