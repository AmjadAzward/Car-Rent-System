/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ilma
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class CarsManagement extends javax.swing.JFrame {
// Database connection parameters

    String url = "jdbc:mysql://localhost:3306/car";
    String user = "root";
    String password = "AMJU";
    private String username; // Class variable to hold the current user's username

    private Map<String, String[]> brandModelMap;

    private DefaultListModel<String> carNumberListModel; // Model for the JList

    /**
     * Creates new form CarsManagement
     */
    public CarsManagement() {
        initComponents();
        initializeBrandModelMap();
        setupBrandComboBoxListener();
        loadCarNumbers();
        jList1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Check if the selection is final
                updateFieldsWithSelectedCar();
            }
        });// Load car numbers when the form is initialized

    }

    private void initializeBrandModelMap() {
        brandModelMap = new HashMap<>();
        brandModelMap.put("Toyota", new String[]{"Corolla", "Camry", "RAV4", "Highlander", "Supra"});
        brandModelMap.put("Ford", new String[]{"Mustang", "F-150", "Explorer", "Escape", "Bronco"});
        brandModelMap.put("Honda", new String[]{"Civic", "Accord", "CR-V", "Pilot", "Odyssey"});
        brandModelMap.put("Chevrolet", new String[]{"Camaro", "Silverado", "Malibu", "Equinox", "Corvette"});
        brandModelMap.put("BMW", new String[]{"3 Series", "5 Series", "X5", "X3", "Z4"});
        brandModelMap.put("Mercedes-Benz", new String[]{"C-Class", "E-Class", "GLE", "S-Class", "G-Class"});
        brandModelMap.put("Audi", new String[]{"A4", "Q5", "A6", "Q7", "R8"});
        brandModelMap.put("Tesla", new String[]{"Model S", "Model 3", "Model X", "Model Y", "Cybertruck"});
        brandModelMap.put("Nissan", new String[]{"Altima", "Maxima", "Rogue", "Pathfinder", "GT-R"});
        brandModelMap.put("Hyundai", new String[]{"Elantra", "Sonata", "Santa Fe", "Tucson", "Palisade"});
    }

    // Setup listener for brand combo box selection
    private void setupBrandComboBoxListener() {
        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModelComboBox();
            }
        });
    }

    private void updateFieldsWithSelectedCar() {
        String selectedCarNumber = jList1.getSelectedValue(); // Get the selected car number

        if (selectedCarNumber != null) {
            String query = "SELECT * FROM Cars WHERE car_number = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, selectedCarNumber); // Set the car number

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Update fields with the retrieved data
                        jTextField1.setText(rs.getString("car_number"));
                        jComboBox1.setSelectedItem(rs.getString("brand")); // Set selected brand
                        jComboBox2.setSelectedItem(rs.getString("model")); // Set selected model
                        jTextField6.setText(rs.getString("Remark"));
                        jTextField4.setText(rs.getBigDecimal("price_per_day").toString());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching car details from the database.");
            }

        }
    }

    private boolean isCarNumberExists(String carNumber) {
        String selectQuery = "SELECT COUNT(*) FROM Cars WHERE car_number = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setString(1, carNumber); // Use setString for car number
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count is greater than 0, the car number exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking car number existence.");
        }
        return false; // Default to false if there's an error
    }

    // Update the model combo box based on the selected brand
    private void updateModelComboBox() {
        String selectedBrand = (String) jComboBox1.getSelectedItem();
        if (selectedBrand != null) {
            String[] models = brandModelMap.get(selectedBrand);
            jComboBox2.removeAllItems(); // Clear previous models
            if (models != null) {
                for (String model : models) {
                    jComboBox2.addItem(model); // Add relevant models
                }
            }
        }
    }

    private void loadCarNumbers() {
        String selectQuery = "SELECT car_number FROM Cars";

        carNumberListModel = new DefaultListModel<>(); // Initialize the model for the JList
        jList1.setModel(carNumberListModel); // Set the model to the JList

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(selectQuery); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String carNumber = rs.getString("car_number");
                carNumberListModel.addElement(carNumber); // Add car number to the model
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading car numbers from the database.");
        }
    }

    private void clearFields() {
        jTextField1.setText(""); // Clear car number field
        jComboBox1.setSelectedIndex(-1); // Clear brand combo box
        jComboBox2.removeAllItems(); // Clear model combo box
        jTextField6.setText(""); // Clear remark field
        jTextField4.setText(""); // Clear price per day field
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();

        jScrollPane2.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(137, 139, 139));

        jButton6.setBackground(new java.awt.Color(137, 139, 139));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home-button (1).png"))); // NOI18N
        jButton6.setBorderPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jButton6)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jButton6)
                .addContainerGap(456, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, 540));

        jPanel1.setBackground(new java.awt.Color(32, 42, 57));

        jList1.setBackground(new java.awt.Color(137, 139, 139));
        jList1.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel2.setBackground(new java.awt.Color(137, 139, 139));

        jLabel35.setBackground(new java.awt.Color(32, 42, 57));
        jLabel35.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel35.setText("Reg Number");

        jLabel36.setBackground(new java.awt.Color(32, 42, 57));
        jLabel36.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel36.setText("Brand");

        jLabel38.setBackground(new java.awt.Color(32, 42, 57));
        jLabel38.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel38.setText("Model");

        jLabel39.setBackground(new java.awt.Color(32, 42, 57));
        jLabel39.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel39.setText("Price per day");

        jLabel1.setBackground(new java.awt.Color(32, 42, 57));
        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(32, 42, 57));
        jLabel1.setText("Car Management");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton1.setForeground(new java.awt.Color(32, 42, 57));
        jButton1.setText("Add Car");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 204, 204));
        jButton3.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton3.setForeground(new java.awt.Color(32, 42, 57));
        jButton3.setText("Delete Car");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton2.setForeground(new java.awt.Color(32, 42, 57));
        jButton2.setText("Update data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(204, 204, 204));
        jButton4.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton4.setForeground(new java.awt.Color(32, 42, 57));
        jButton4.setText("Create Report");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel40.setBackground(new java.awt.Color(32, 42, 57));
        jLabel40.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel40.setText("Remark");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toyota", "Ford", "Honda", "Chevrolet", "BMW", "Mercedes-Benz", "Audi", "Tesla", "Nissan", "Hyundai" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "aqua", "fit ", "vezel", " " }));

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton5.setText("Clear");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(191, 191, 191))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                            .addComponent(jTextField6)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(17, 17, 17))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(92, 92, 92)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(49, 49, 49))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 770, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try {
            String reportSource = "C:\\Users\\amjad\\Documents\\New folder\\Car Management.jrxml";

            File file = new File(reportSource);
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);

            // Assuming you have a database connection
            Connection connection = DriverManager.getConnection(url, user, password);
            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);

            // Display the report
            JasperViewer.viewReport(jasperPrint, false);

            // Close the connection
            connection.close();
        } catch (JRException e) {
            System.err.println("JRException: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        // Get the selected car number from the JList
        String oldCarNumber = (String) jList1.getSelectedValue(); // This assumes the car number is stored as a String
        if (oldCarNumber == null) {
            JOptionPane.showMessageDialog(this, "Please select a car from the list to update.");
            return;
        }

        // Get the updated values from the fields
        String newCarNumber = jTextField1.getText().trim(); // Current/updated car number
        String brand = (String) jComboBox1.getSelectedItem();
        String model = (String) jComboBox2.getSelectedItem();
        String remark = jTextField6.getText().trim();
        String pricePerDayStr = jTextField4.getText().trim();

        // Validate that price per day is a number and greater than zero
        double pricePerDay;
        try {
            pricePerDay = Double.parseDouble(pricePerDayStr);
            if (pricePerDay <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price per day must be a valid positive number.");
            return;
        }

        // Prepare to check if the new car number already exists
        String checkCarNumberQuery = "SELECT COUNT(*) FROM Cars WHERE car_number = ? AND car_number != ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement checkStmt = conn.prepareStatement(checkCarNumberQuery)) {
            checkStmt.setString(1, newCarNumber); // Check if new car number exists
            checkStmt.setString(2, oldCarNumber); // Exclude the current car number

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "The car number already exists. Please enter a different car number.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking car numbers in the database.");
            return;
        }

        // Prepare the update SQL query
        String updateQuery = "UPDATE Cars SET car_number = ?, brand = ?, model = ?, Remark = ?, price_per_day = ? WHERE car_number = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newCarNumber); // Set the new car number
            pstmt.setString(2, brand); // Set brand
            pstmt.setString(3, model); // Set model
            pstmt.setString(4, remark); // Set remark
            pstmt.setBigDecimal(5, new BigDecimal(pricePerDayStr)); // Set price per day
            pstmt.setString(6, oldCarNumber); // Set the original car number for the WHERE clause

            int rowsAffected = pstmt.executeUpdate(); // Execute update

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Car details updated successfully.");
                loadCarNumbers();
                clearFields();// Refresh the list after updating
            } else {
                JOptionPane.showMessageDialog(this, "Car number not found. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating car details in the database.");
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// Get the selected car number from the JList
String carNumber = (String) jList1.getSelectedValue(); // Assuming car number is a String
if (carNumber == null) {
    JOptionPane.showMessageDialog(this, "Please select a car from the list to delete.");
    return;
}

// Confirm deletion
int confirmDelete = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the Car ? if you delete the car related rent and purchase information will be deleted ",
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

if (confirmDelete != JOptionPane.YES_OPTION) {
    return; // Exit if user chooses "No"
}

// SQL delete queries for each table
String deleteCarsQuery = "DELETE FROM Cars WHERE car_number = ?";
String deleteRentQuery = "DELETE FROM rent WHERE car_reg_no = ?";
String deleteCarPurchaseQuery = "DELETE FROM car_purchase WHERE car_number = ?";

try (Connection conn = DriverManager.getConnection(url, user, password)) {
    conn.setAutoCommit(false); // Begin transaction

    try (PreparedStatement pstmtCars = conn.prepareStatement(deleteCarsQuery);
         PreparedStatement pstmtRent = conn.prepareStatement(deleteRentQuery);
         PreparedStatement pstmtCarPurchase = conn.prepareStatement(deleteCarPurchaseQuery)) {

        // Set the car number for each query
        pstmtCars.setString(1, carNumber);
        pstmtRent.setString(1, carNumber);
        pstmtCarPurchase.setString(1, carNumber);

        // Execute deletions
        int rowsAffectedCars = pstmtCars.executeUpdate();
        int rowsAffectedRent = pstmtRent.executeUpdate();
        int rowsAffectedCarPurchase = pstmtCarPurchase.executeUpdate();

        if (rowsAffectedCars > 0) {
            conn.commit(); // Commit transaction if all deletions succeed
            JOptionPane.showMessageDialog(this, "Car and related records deleted successfully.");
            loadCarNumbers(); // Refresh the car list
            clearFields();    // Clear any selected fields
        } else {
            JOptionPane.showMessageDialog(this, "Car number not found. Please try again.");
            conn.rollback(); // Rollback if car is not found in Cars table
        }

    } catch (SQLException e) {
        conn.rollback(); // Rollback in case of any error
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error deleting car and related records from the database.");
    }

} catch (SQLException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Database connection error.");
}

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Fetching data from the fields and combo boxes
        String number = jTextField1.getText();

        String brand = jComboBox1.getSelectedItem().toString();   // Retrieving brand from combo box
        String model = jComboBox2.getSelectedItem().toString();   // Retrieving model from combo box
        String remark = jTextField6.getText();
        String pricePerDayStr = jTextField4.getText(); // Text for price per day

        if (isCarNumberExists(number)) {
            JOptionPane.showMessageDialog(this, "Car number already exists. Please enter a different car number.");
            return; // Exit the method if the car number exists
        }
        // Validate price per day must be a number
        double pricePerDay;
        try {
            pricePerDay = Double.parseDouble(pricePerDayStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price per day must be a valid number.");
            return;
        }

        // Check for required fields
        if (brand.isEmpty() || model.isEmpty() || pricePerDay <= 0) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields correctly.");
            return;
        }

        String insertQuery = "INSERT INTO Cars (car_number, brand, model, Remark, price_per_day) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            // Set parameters for the SQL statement
            pstmt.setString(1, number);
            pstmt.setString(2, brand);
            pstmt.setString(3, model);
            pstmt.setString(4, remark);
            pstmt.setDouble(5, pricePerDay);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Car added successfully!");
                loadCarNumbers();
                clearFields();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding car to the database.");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        clearFields();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Dashboard myProfile = new Dashboard(username); // Pass the current username
        myProfile.setVisible(true); // Show the MyProfile form
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(CarsManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CarsManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CarsManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CarsManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CarsManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
