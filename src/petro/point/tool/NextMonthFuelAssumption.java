package petro.point.tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONObject;


public class NextMonthFuelAssumption extends javax.swing.JFrame {

    public NextMonthFuelAssumption() {
        initComponents();
    double initialCrudePrice = fetchLiveCrudeOilPrice();
    if (initialCrudePrice > 0) {
        crude_oil_txt.setText(String.format("%.2f", initialCrudePrice));
    } else {
        crude_oil_txt.setText("100.00"); // Fallback value
    }
    startCrudeOilPriceUpdater();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        crude_oil_lbl = new javax.swing.JLabel();
        crude_oil_txt = new javax.swing.JTextField();
        cal_btn = new javax.swing.JButton();
        nxt_month_fuel_txt = new javax.swing.JLabel();
        fuel_type_combo = new javax.swing.JComboBox<>();
        show_fuel_price_txt = new javax.swing.JTextField();
        sl_price_lbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sl_price_txt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        crude_oil_lbl.setText("Today Crude Price");

        crude_oil_txt.setEditable(false);
        crude_oil_txt.setToolTipText("");

        cal_btn.setText("Calculate");
        cal_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cal_btnActionPerformed(evt);
            }
        });

        nxt_month_fuel_txt.setText("Next Month Fuel Price");

        fuel_type_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Petrol", "Diesel" }));

        show_fuel_price_txt.setEditable(false);

        sl_price_lbl.setText("Sri Lankan Price");

        jLabel2.setText("MRP = V1 + V2 + V3 + V4 + V5 + V6 + V7");

        sl_price_txt.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(cal_btn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(crude_oil_lbl)
                            .addComponent(nxt_month_fuel_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sl_price_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(crude_oil_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(fuel_type_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71)
                                .addComponent(show_fuel_price_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(sl_price_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(crude_oil_lbl)
                            .addComponent(crude_oil_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addComponent(nxt_month_fuel_txt))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fuel_type_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(show_fuel_price_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addComponent(sl_price_lbl)
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addGap(29, 29, 29)
                .addComponent(sl_price_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(cal_btn)
                .addGap(46, 46, 46))
        );

        crude_oil_lbl.getAccessibleContext().setAccessibleName("Today Crude Oil Price");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cal_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cal_btnActionPerformed
        String selectedFuelType = (String) fuel_type_combo.getSelectedItem();
    double crudeOilPrice;

    if (crude_oil_txt.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Crude oil price is not available!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        crudeOilPrice = Double.parseDouble(crude_oil_txt.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid crude oil price!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    double averageStock = fetchAverageStock(selectedFuelType.equals("Petrol") ? "petrolstock" : "dieselstock");
    double predictedPrice = calculateNextMonthFuelPrice(crudeOilPrice, averageStock);
    show_fuel_price_txt.setText(String.format("%.2f", predictedPrice));

    // Calculate Sri Lankan Price based on selected fuel type
    double v1 = crudeOilPrice * 124; // Landed cost per liter (assumed conversion to LKR)
    double v2 = selectedFuelType.equals("Petrol") ? 5.0 : 3.5; // Processing cost
    double v3 = 2.0; // Stockholding cost
    double v4 = selectedFuelType.equals("Petrol") ? 50.0 : 40.0; // Taxation
    double v5 = 0.02 * (v1 + v2 + v3 + v4); // 2% other cost
    double v6 = 0.04 * (v1 + v2 + v3 + v4 + v5); // 4% profit margin
    double v7 = 1.0; // Cost saving from refinery production (fixed value)

    double sriLankanPrice = v1 + v2 + v3 + v4 + v5 + v6 + v7;
    sl_price_txt.setText(String.format("%.2f", sriLankanPrice));
    }//GEN-LAST:event_cal_btnActionPerformed

        private double fetchAverageStock(String tableName) {
        double totalStock = 0.0;
        int count = 0;

        try {
            Statement st = DBConnection.getdbconnection().createStatement();
            String query = "SELECT amount FROM " + tableName;
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                totalStock += rs.getDouble("amount");
                count++;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching stock data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return count > 0 ? totalStock / count : 0.0;
    }

    private double calculateNextMonthFuelPrice(double crudeOilPrice, double averageStock) {
        double basePriceFactor = 0.1;
        double stockImpactFactor = 0.01;
        return crudeOilPrice * basePriceFactor - averageStock * stockImpactFactor;
    }

    private void startCrudeOilPriceUpdater() {
        new javax.swing.Timer(120000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            double liveCrudeOilPrice = fetchLiveCrudeOilPrice();
            if (liveCrudeOilPrice > 0) {
                crude_oil_txt.setText(String.format("%.2f", liveCrudeOilPrice));
            } else {
                crude_oil_txt.setText("100.00"); // Default fallback value
                System.out.println("Using fallback crude oil price.");
            }
        }
    }).start();
    }

    private double fetchLiveCrudeOilPrice() {
        String apiKey = "CFJPODERNWT3595H"; // Replace with your API key
    String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=WTI&interval=1min&apikey=" + apiKey;

    try {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject timeSeries = jsonResponse.getJSONObject("Time Series (1min)");
            String latestTimestamp = timeSeries.keys().next();
            double crudeOilPrice = timeSeries.getJSONObject(latestTimestamp).getDouble("1. open");

            System.out.println("Fetched Crude Oil Price: " + crudeOilPrice); // Debug log
            return crudeOilPrice;
        } else {
            System.err.println("API Error: HTTP code " + conn.getResponseCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0.0; // Return 0.0 if fetching fails
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new NextMonthFuelAssumption().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cal_btn;
    private javax.swing.JLabel crude_oil_lbl;
    private javax.swing.JTextField crude_oil_txt;
    private javax.swing.JComboBox<String> fuel_type_combo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nxt_month_fuel_txt;
    private javax.swing.JTextField show_fuel_price_txt;
    private javax.swing.JLabel sl_price_lbl;
    private javax.swing.JTextField sl_price_txt;
    // End of variables declaration//GEN-END:variables
}
