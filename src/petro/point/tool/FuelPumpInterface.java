
package petro.point.tool;
import java.sql.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

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
    String pumpTable = fuelType.equals("Petrol") ? "petrolpump" : "dieselpump";
    String stockTable = fuelType.equals("Petrol") ? "petrolstock" : "dieselstock";

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
    String fetchStockQuery = "SELECT psid, amount FROM " + stockTable + " ORDER BY psid DESC";

    try (PreparedStatement fetchStmt = con.prepareStatement(fetchStockQuery);
         ResultSet rs = fetchStmt.executeQuery()) {

        while (rs.next() && amountToDeduct > 0) {
            int id = rs.getInt("psid");
            int currentAmount = rs.getInt("amount");

            // Determine the deduction amount for this row
            int deduction = Math.min(amountToDeduct, currentAmount);

            // Update the current row's stock
            try (PreparedStatement updateStmt = con.prepareStatement(
                    "UPDATE " + stockTable + " SET amount = amount - ? WHERE psid = ?")) {
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
        
        petroPointInterface = new PetroPointInterface();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PETRO POINT");
        jPanel2.add(jLabel1);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 40));

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 660, 50));

        jLabel3.setText("Today petrol price");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 110, 20));

        Pprice_txt.setEditable(false);
        jPanel1.add(Pprice_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 120, 30));

        jLabel2.setText("Today diesel price");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 110, 20));

        Dprice_txt.setEditable(false);
        jPanel1.add(Dprice_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 130, 30));

        FuelType_Combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Petrol", "Diesel" }));
        FuelType_Combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FuelType_ComboActionPerformed(evt);
            }
        });
        jPanel1.add(FuelType_Combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 140, 40));

        pump_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pump_textActionPerformed(evt);
            }
        });
        jPanel1.add(pump_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 140, 40));

        btn_pump.setText("Pump");
        btn_pump.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pumpActionPerformed(evt);
            }
        });
        jPanel1.add(btn_pump, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, 160, 40));

        jLabel4.setText("Enter Pump Amount");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 110, 20));

        jLabel5.setText("Fuel Type");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 60, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 660, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FuelType_ComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FuelType_ComboActionPerformed

    }//GEN-LAST:event_FuelType_ComboActionPerformed

    private void btn_pumpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pumpActionPerformed
        try {
            String fuelType = FuelType_Combo.getSelectedItem().toString();
            int amount = Integer.parseInt(pump_text.getText());

            transactionList.addTransaction(fuelType, amount);
            JOptionPane.showMessageDialog(this, "Fuel pumped successfully!");

            // Display transactions in the console for verification
            transactionList.displayTransactions();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_pumpActionPerformed

    private void pump_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pump_textActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pump_textActionPerformed

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
    private javax.swing.JButton btn_pump;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField pump_text;
    // End of variables declaration//GEN-END:variables
}
