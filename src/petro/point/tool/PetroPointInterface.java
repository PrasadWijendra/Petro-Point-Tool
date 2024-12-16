
package petro.point.tool;
import java.sql.*;
import javax.swing.JOptionPane;

public class PetroPointInterface extends javax.swing.JFrame {

  
    private int[] petrolStock; // Array for petrol stock
    private int[] dieselStock; // Array for diesel stock (added for fuel type selection)
    
    ResultSet rst;
  
    public PetroPointInterface() {
        
           petrolStock = new int[1]; // Fixed size for petrol queue
           dieselStock = new int[1]; // Fixed size for diesel queue
        initComponents();
        loadFuelStocksFromDatabase(); // Load both petrol and diesel stocks from the database
        updateStockDisplay("Petrol");
    }
    
    private void loadFuelStocksFromDatabase() {
        try {
            Statement st = DBConnection.getdbconnection().createStatement();

            // Get the total petrol stock
            rst = st.executeQuery("SELECT SUM(amount) AS TotalAmount FROM petrolstock");
            if (rst.next()) {
                petrolStock[0] = rst.getInt("TotalAmount");
            } else {
                petrolStock[0] = 0; // Default stock if no rows found
            }

            // Get the total diesel stock
            rst = st.executeQuery("SELECT SUM(amount) AS TotalAmount FROM dieselstock");
            if (rst.next()) {
                dieselStock[0] = rst.getInt("TotalAmount");
            } else {
                dieselStock[0] = 0; // Default stock if no rows found
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading fuel stocks from database!");
        }
    }
    
   // Update available stock and free stock
    private void updateStockDisplay(String fuelType) {
        try {
            int availableStock = 0;
            String tableName = fuelType.equals("Petrol") ? "petrolstock" : "dieselstock";

            Statement st = DBConnection.getdbconnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM(amount) AS TotalAmount FROM " + tableName);

            if (rs.next()) {
                availableStock = rs.getInt("TotalAmount");
            }

            int totalCapacity = 10000; // Maximum capacity
            int freeStock = totalCapacity - availableStock;

            // Update text fields
            availablestock_txt.setText(String.valueOf(availableStock));
            spacestock_txt.setText(String.valueOf(freeStock));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating stock display!");
        }
    }

    
    // Refill fuel
    public void RefillFuel(String fuelType, int fuelAmount) {
        int currentStock = fuelType.equals("Petrol") ? petrolStock[0] : dieselStock[0];

        if (currentStock + fuelAmount > 10000) {
            int canAdd = 10000 - currentStock;
            JOptionPane.showMessageDialog(this, "Cannot refill more than 10,000 for " + fuelType + "! Can add only: " + canAdd);
        } else {
            if (fuelType.equals("Petrol")) {
                petrolStock[0] += fuelAmount;
                addNewStockRowToDatabase("Petrol", fuelAmount);
            } else if (fuelType.equals("Diesel")) {
                dieselStock[0] += fuelAmount;
                addNewStockRowToDatabase("Diesel", fuelAmount);
            }

            updateStockDisplay(fuelType); // Refresh stock display
        }
    }


  private void addNewStockRowToDatabase(String fuelType, int fuelAmount) {
      
      String tableName = fuelType.equals("Petrol") ? "petrolstock" : "dieselstock";

        try (Connection con = DBConnection.getdbconnection();
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO " + tableName + " (amount, datetime) VALUES (?, ?)")) {

            // Set the fuel amount and the current datetime
            stmt.setInt(1, fuelAmount);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            // Execute the insert
            
              int rowsInserted = stmt.executeUpdate();
              System.out.println("Rows inserted in " + tableName + ": " + rowsInserted);
        
            if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "New stock row added for " + fuelType + ": " + fuelAmount);

            // Record the transaction in the respective stock table
            if (fuelType.equals("Petrol")) {
                recordPetrolStockTransaction(fuelAmount);
            } else {
                
                recordDieselStockTransaction(fuelAmount);
                
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add new stock row for " + fuelType);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error adding new stock row for " + fuelType + " in database: " + e.getMessage());
    }
    }
  
  private void recordPetrolStockTransaction(int fuelAmount) {
    try (Connection con = DBConnection.getdbconnection();
         PreparedStatement stmt = con.prepareStatement(
                 "INSERT INTO petrolstocktable (amount, datetime) VALUES (?, ?)")) {

        stmt.setInt(1, fuelAmount);
        stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

        stmt.executeUpdate();
        System.out.println("Transaction recorded in petrolstocktable: " + fuelAmount);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error recording petrol transaction: " + e.getMessage());
    }
}
  
 private void recordDieselStockTransaction(int fuelAmount) {
    try (Connection con = DBConnection.getdbconnection();
         PreparedStatement stmt = con.prepareStatement(
                 "INSERT INTO dieselstocktable (amount, datetime) VALUES (?, ?)")) {

        stmt.setInt(1, fuelAmount);
        stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

        stmt.executeUpdate();
        System.out.println("Transaction recorded in dieselstocktable: " + fuelAmount);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error recording diesel transaction: " + e.getMessage());
    }
}
  
  // Method to reduce fuel stock after pumping
    public void reduceFuelStock(String fuelType, int fuelAmount) {
        if (fuelType.equals("Petrol")) {
            if (petrolStock[0] >= fuelAmount) {
                petrolStock[0] -= fuelAmount;
                System.out.println("Pumped Petrol. Updated Petrol stock: " + petrolStock[0]);
            } else {
                System.out.println("Not enough Petrol in stock!");
            }
        } else if (fuelType.equals("Diesel")) {
            if (dieselStock[0] >= fuelAmount) {
                dieselStock[0] -= fuelAmount;
                System.out.println("Pumped Diesel. Updated Diesel stock: " + dieselStock[0]);
            } else {
                System.out.println("Not enough Diesel in stock!");
            }
        }
    }         
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        FuelType_Combo = new javax.swing.JComboBox<>();
        Refill_text = new javax.swing.JTextField();
        btn_refill = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        btn_clr = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        availablestock_txt = new javax.swing.JTextField();
        spacestock_txt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FuelType_Combo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FuelType_Combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Petrol", "Diesel" }));
        FuelType_Combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FuelType_ComboActionPerformed(evt);
            }
        });
        jPanel1.add(FuelType_Combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, 140, 40));

        Refill_text.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel1.add(Refill_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 140, 40));

        btn_refill.setBackground(new java.awt.Color(178, 0, 0));
        btn_refill.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_refill.setForeground(new java.awt.Color(255, 255, 255));
        btn_refill.setText("ReFill");
        btn_refill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refillActionPerformed(evt);
            }
        });
        jPanel1.add(btn_refill, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 430, 170, 40));

        jPanel2.setBackground(new java.awt.Color(178, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PETRO POINT");
        jPanel2.add(jLabel1);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 40));

        jPanel3.setBackground(new java.awt.Color(178, 0, 0));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 670, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Enter Refill Amount");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 140, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Select Fuel Type");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 90, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Fuel Capacity   :  10 000L");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, 140, -1));

        btn_back.setBackground(new java.awt.Color(178, 0, 0));
        btn_back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_back.setForeground(new java.awt.Color(255, 255, 255));
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        jPanel1.add(btn_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 170, 40));

        btn_clr.setBackground(new java.awt.Color(178, 0, 0));
        btn_clr.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_clr.setForeground(new java.awt.Color(255, 255, 255));
        btn_clr.setText("Clear");
        btn_clr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clrActionPerformed(evt);
            }
        });
        jPanel1.add(btn_clr, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 430, 170, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Available Stock");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Free Stock");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 160, -1, -1));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 190, 10));

        availablestock_txt.setEditable(false);
        availablestock_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availablestock_txtActionPerformed(evt);
            }
        });
        jPanel1.add(availablestock_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, 110, 30));

        spacestock_txt.setEditable(false);
        jPanel1.add(spacestock_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 150, 110, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(684, 532));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_refillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refillActionPerformed
         try {
            int fuelAmount = Integer.parseInt(Refill_text.getText());
            String selectedFuelType = FuelType_Combo.getSelectedItem().toString();
            RefillFuel(selectedFuelType, fuelAmount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }

    }//GEN-LAST:event_btn_refillActionPerformed

    private void FuelType_ComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FuelType_ComboActionPerformed
         String selectedFuelType = FuelType_Combo.getSelectedItem().toString();
        updateStockDisplay(selectedFuelType);

    }//GEN-LAST:event_FuelType_ComboActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        new HomePage().setVisible(true);
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_clrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_clrActionPerformed

    private void availablestock_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availablestock_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_availablestock_txtActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PetroPointInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PetroPointInterface().setVisible(true);
            }
        }); 
      
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> FuelType_Combo;
    private javax.swing.JTextField Refill_text;
    private javax.swing.JTextField availablestock_txt;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_clr;
    private javax.swing.JButton btn_refill;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField spacestock_txt;
    // End of variables declaration//GEN-END:variables
}
