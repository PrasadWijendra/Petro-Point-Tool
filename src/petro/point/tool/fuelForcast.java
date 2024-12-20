
package petro.point.tool;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;


public class fuelForcast extends javax.swing.JFrame {

   
    public fuelForcast() {
        initComponents();
    }
    
private void generateForecast() {
        String forecastPeriod = jComboBox1.getSelectedItem().toString(); // Weekly/Monthly
        String fuelType = jComboBox2.getSelectedItem().toString(); // Petrol/Diesel

        String pumpTable = fuelType.equals("Petrol") ? "petrolpump" : "dieselpump";
        String stockTable = fuelType.equals("Petrol") ? "petrolstock" : "dieselstock";

        int totalUsage = 0;
        int availableStock = getAvailableStock(stockTable);

        try (Connection con = DBConnection.getdbconnection();
             PreparedStatement stmt = con.prepareStatement(generateQuery(pumpTable, forecastPeriod))) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalUsage += rs.getInt("amount");
            }

            // Calculate forecast based on average usage
            int days = forecastPeriod.equals("Weekly") ? 7 : 30;
            int averageUsagePerDay = totalUsage / days;
            int requiredFuel = averageUsagePerDay * days;

            // Set the calculated forecast into the text field
            jTextField1.setText(String.valueOf(requiredFuel+"L"));

            // Show a message if available stock is low
            if (availableStock < requiredFuel) {
                JOptionPane.showMessageDialog(this, "Warning: Fuel stock is insufficient! Required: "
                        + requiredFuel + "L, Available: " + availableStock + "L", "Low Stock Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating forecast: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to generate SQL query
    private String generateQuery(String pumpTable, String forecastPeriod) {
        LocalDate startDate;

        if (forecastPeriod.equals("Weekly")) {
            startDate = LocalDate.now().minusWeeks(1);
        } else {
            startDate = LocalDate.now().minusMonths(1);
        }

        String formattedDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return "SELECT amount FROM " + pumpTable + " WHERE datetime >= '" + formattedDate + "'";
    }

    // Helper method to get available stock from stock table
    private int getAvailableStock(String stockTable) {
        int availableStock = 0;

        // Get the database connection from DBConnection class
        Connection con = DBConnection.getdbconnection(); // Call the DBConnection class to get the connection
        
        // Check if the connection is null (i.e., failed to establish a connection)
        if (con == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed.");
            return availableStock;  // Return 0 if no connection
        }

        try {
            // SQL query to get the available stock
            String query = "SELECT SUM(amount) AS TotalStock FROM " + stockTable;
            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    availableStock = rs.getInt("TotalStock");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching available stock: " + e.getMessage());
            e.printStackTrace();
        }

        return availableStock;
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
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btn_generateForcast = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Weekly", "Monthly" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, 140, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Select Forcast Period");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 170, 30));

        btn_generateForcast.setBackground(new java.awt.Color(178, 0, 0));
        btn_generateForcast.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_generateForcast.setForeground(new java.awt.Color(255, 255, 255));
        btn_generateForcast.setText("Generate Forcast");
        btn_generateForcast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_generateForcastActionPerformed(evt);
            }
        });
        jPanel1.add(btn_generateForcast, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 331, 160, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Required Fuel Amount");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 180, -1));

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 200, 140, 30));

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Petrol", "Diesel" }));
        jPanel1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 140, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Select Fuel Type");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 130, 20));

        jPanel2.setBackground(new java.awt.Color(178, 0, 0));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PETRO POINT");
        jPanel2.add(jLabel4);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 40));

        jPanel3.setBackground(new java.awt.Color(178, 0, 0));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 670, 30));

        btn_back.setBackground(new java.awt.Color(178, 0, 0));
        btn_back.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_back.setForeground(new java.awt.Color(255, 255, 255));
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        jPanel1.add(btn_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 160, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_generateForcastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_generateForcastActionPerformed
        generateForecast();
    }//GEN-LAST:event_btn_generateForcastActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        new HomePage().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_backActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(fuelForcast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fuelForcast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fuelForcast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fuelForcast.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fuelForcast().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_generateForcast;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
