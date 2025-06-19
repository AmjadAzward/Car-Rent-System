/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ilma
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;


public class Rent extends javax.swing.JFrame {
    String url = "jdbc:mysql://localhost:3306/car";
    String user = "root";
    String password = "AMJU";
    private String username; // Class variable to hold the current user's username



    public Rent() {
        initComponents();
        loadOrderIDs();
        loadCustomerNames();
        loadCarNumbers();

        // Add action listeners for JComboBox2 to fetch supplier name and price per day
        jComboBox2.addActionListener(e -> {
            loadSupplierName();
            loadPricePerDay();
        });
        
         jList1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {  // Check if the selection is final
                loadRentDetails();  // Load the details of the selected rent record
            }
        });

    }
    

    private void loadRentDetails() {
    String selectedRentID = jList1.getSelectedValue();
    if (selectedRentID == null) return;

    String query = "SELECT * FROM Rent WHERE rent_id = ?";
    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, selectedRentID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            jComboBox2.setSelectedItem(rs.getString("car_reg_no"));
            jComboBox1.setSelectedItem(getCustomerName(rs.getInt("customer_id"))); // Helper method to get customer name
            jTextField4.setText(String.valueOf(rs.getDouble("price_per_day")));
            jTextField5.setText(String.valueOf(rs.getInt("no_of_days")));
            jTextField7.setText(String.valueOf(rs.getDouble("total_payable")));

            // Load the date into the JDateChooser
            java.sql.Date sqlDate = rs.getDate("borrowed_date"); // Adjust this field name as needed
            if (sqlDate != null) {
                jDateChooser1.setDate(new java.util.Date(sqlDate.getTime())); // Convert SQL date to util date
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
    
    
    private void deleteRentRecord() {
    String selectedRentID = jList1.getSelectedValue();
    if (selectedRentID == null) {
        JOptionPane.showMessageDialog(this, "Please select a rent record to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this rent record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        String deleteQuery = "DELETE FROM Rent WHERE rent_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, selectedRentID);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Rent record deleted successfully!");
                loadOrderIDs(); // Refresh the JList
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete rent record. Please try again.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the rent record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    
    
    
    
    
  private String getCustomerName(int customerId) {
        String customerName = "";
        String query = "SELECT cus_name FROM customer WHERE cus_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customerName = rs.getString("cus_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerName;
    }
    
     private void updateRentDetails() {
        try {
            String selectedRentID = jList1.getSelectedValue();
            if (selectedRentID == null) {
                JOptionPane.showMessageDialog(this, "Please select a rent ID to edit.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String carRegNo = (String) jComboBox2.getSelectedItem();
            String selectedCustomerName = (String) jComboBox1.getSelectedItem();
            int selectedCustomerId = getCustomerId(selectedCustomerName);
            int selectedSupplierId = getSelectedSupplierId();

            // Get date from the date chooser
            java.util.Date selectedDate = jDateChooser1.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid date.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            // Get number of days and price per day
            int noOfDays = Integer.parseInt(jTextField5.getText());
            double pricePerDay = Double.parseDouble(jTextField4.getText());
            double totalPayable = pricePerDay * noOfDays;

            // Update Rent table
            String updateQuery = "UPDATE Rent SET car_reg_no = ?, supplier_id = ?, customer_id = ?, borrowed_date = ?, no_of_days = ?, price_per_day = ?, total_payable = ? WHERE rent_id = ?";
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

                pstmt.setString(1, carRegNo);
                pstmt.setInt(2, selectedSupplierId);
                pstmt.setInt(3, selectedCustomerId);
                pstmt.setDate(4, sqlDate);
                pstmt.setInt(5, noOfDays);
                pstmt.setDouble(6, pricePerDay);
                pstmt.setDouble(7, totalPayable);
                pstmt.setString(8, selectedRentID); // Set the rent ID for the update

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    jTextField7.setText(String.format("%.2f", totalPayable)); // Update total payable field
                    JOptionPane.showMessageDialog(this, "Rent details updated successfully!");
                    loadOrderIDs();
                    clearFormFields();// Refresh the list after update
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Method to get selected supplier ID based on the selected car number
private int getSelectedSupplierId() {
    int supplierId = -1;
    String selectedCarNumber = (String) jComboBox2.getSelectedItem();
    if (selectedCarNumber == null) return supplierId; // Exit if no car selected

    String query = "SELECT sup_id FROM car_purchase WHERE car_number = ?";
    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, selectedCarNumber);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            supplierId = rs.getInt("sup_id");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return supplierId;
}


    // Method to load order IDs into jList1
    private void loadOrderIDs() {
        DefaultListModel<String> model = new DefaultListModel<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT rent_id FROM Rent")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addElement(rs.getString("rent_id"));
            }
            jList1.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load customer names into jComboBox1
    private void loadCustomerNames() {
        jComboBox1.removeAllItems(); // Clear existing items
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT cus_name, cus_id FROM customer")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jComboBox1.addItem(rs.getString("cus_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load car numbers into jComboBox2
    private void loadCarNumbers() {
        jComboBox2.removeAllItems(); // Clear existing items
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT car_number FROM car_purchase")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jComboBox2.addItem(rs.getString("car_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void clearFormFields() {
    jComboBox1.setSelectedIndex(-1); // Reset customer combo box
    jComboBox2.setSelectedIndex(-1); // Reset car number combo box
    jTextField4.setText("");          // Clear price per day field
    jTextField5.setText("");          // Clear number of days field
    jTextField6.setText("");          // Clear supplier name field
    jTextField7.setText("");          // Clear total payable field
    jDateChooser1.setDate(null);      // Reset date chooser
    jList1.clearSelection();          // Deselect any selected item in JList
}


    // Method to load the price per day based on the selected car registration number
    private void loadPricePerDay() {
        String selectedCarNumber = (String) jComboBox2.getSelectedItem();
        if (selectedCarNumber == null) return; // Exit if no car selected

        String query = "SELECT price_per_day FROM Cars WHERE car_number = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, selectedCarNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                jTextField4.setText(rs.getString("price_per_day"));
            } else {
                jTextField4.setText(""); // Clear if no price found
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load supplier name based on the selected car
    private void loadSupplierName() {
        String selectedCarNumber = (String) jComboBox2.getSelectedItem();
        if (selectedCarNumber == null) return; // Exit if no car selected

        String query = "SELECT sup_name FROM car_purchase "
                     + "JOIN supplier ON car_purchase.sup_id = supplier.sup_id "
                     + "WHERE car_number = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, selectedCarNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                jTextField6.setText(rs.getString("sup_name"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

// Method to insert rent details into the Rent table
// Method to insert rent details into the Rent table
// Method to insert rent details into the Rent table
private void insertRentDetails() {
    try {
        String carRegNo = (String) jComboBox2.getSelectedItem();
        String selectedCustomerName = (String) jComboBox1.getSelectedItem();
        
        // Get the selected customer ID based on the name
        int selectedCustomerId = getCustomerId(selectedCustomerName);
        int selectedSupplierId = getSelectedSupplierId();

        // Get date from the date chooser
        java.util.Date selectedDate = jDateChooser1.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

        // Validate and get number of days from jTextField5
        String noOfDaysText = jTextField5.getText();
        if (noOfDaysText.isEmpty() || !noOfDaysText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of days.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int noOfDays = Integer.parseInt(noOfDaysText);
        
        // Validate and get price per day from jTextField4
        String priceText = jTextField4.getText();
        if (priceText.isEmpty() || !priceText.matches("\\d+(\\.\\d{1,2})?")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price per day.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double pricePerDay = Double.parseDouble(priceText);

        // Calculate total payable
        double totalPayable = pricePerDay * noOfDays;

        // Insert into Rent table
        String insertQuery = "INSERT INTO Rent (car_reg_no, supplier_id, customer_id, borrowed_date, no_of_days, price_per_day, total_payable) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, carRegNo);
            pstmt.setInt(2, selectedSupplierId); // Use the retrieved supplier ID
            pstmt.setInt(3, selectedCustomerId); // Use the retrieved customer ID
            pstmt.setDate(4, sqlDate);
            pstmt.setInt(5, noOfDays);
            pstmt.setDouble(6, pricePerDay);
            pstmt.setDouble(7, totalPayable); // Set the calculated total payable

            // Execute the update
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                // Set total payable on jTextField7 before showing the success message
                jTextField7.setText(String.format("%.2f", totalPayable)); // Format to two decimal places
                JOptionPane.showMessageDialog(this, "Rent details saved successfully!");
                loadOrderIDs();
                clearFormFields();
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace(); // Log other exceptions
    }
}



    // Method to get customer ID based on the selected customer name
    private int getCustomerId(String customerName) {
        int customerId = -1;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT cus_id FROM customer WHERE cus_name = ?")) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customerId = rs.getInt("cus_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerId;
    }

    // Method to get selected supplier ID based on the selected supplier name




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel41 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jLabel35.setText("Car Reg No");

        jLabel36.setBackground(new java.awt.Color(32, 42, 57));
        jLabel36.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel36.setText("Customer");

        jLabel38.setBackground(new java.awt.Color(32, 42, 57));
        jLabel38.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel38.setText("Date");

        jLabel39.setBackground(new java.awt.Color(32, 42, 57));
        jLabel39.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel39.setText("Price per day");

        jLabel1.setBackground(new java.awt.Color(32, 42, 57));
        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(32, 42, 57));
        jLabel1.setText("Car Rent Management");

        jTextField4.setText("                  genarated by system");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(204, 204, 204));
        jButton9.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton9.setForeground(new java.awt.Color(32, 42, 57));
        jButton9.setText("Add Car");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(204, 204, 204));
        jButton10.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton10.setForeground(new java.awt.Color(32, 42, 57));
        jButton10.setText("Delete Car");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(204, 204, 204));
        jButton11.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton11.setForeground(new java.awt.Color(32, 42, 57));
        jButton11.setText("Update data");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(204, 204, 204));
        jButton12.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton12.setForeground(new java.awt.Color(32, 42, 57));
        jButton12.setText("View all Rents");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel40.setBackground(new java.awt.Color(32, 42, 57));
        jLabel40.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel40.setText("No of Days");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toyota", "Ford", "Honda", "Chevrolet", "BMW", "Mercedes-Benz", "Audi", "Tesla", "Nissan", "Hyundai" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "aqua", "fit ", "vezel", " " }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(204, 204, 204));
        jButton13.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton13.setText("Clear");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel41.setBackground(new java.awt.Color(32, 42, 57));
        jLabel41.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel41.setText("Total Payable");

        jTextField7.setEditable(false);
        jTextField7.setText("                  genarated by system");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel42.setBackground(new java.awt.Color(32, 42, 57));
        jLabel42.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel42.setText("Supplier");

        jTextField6.setText("                  genarated by system");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(204, 204, 204));
        jButton15.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jButton15.setText("Add");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(204, 204, 204));
        jButton16.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jButton16.setText("Add");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField5))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(35, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1)
                            .addComponent(jButton16))
                        .addGap(195, 195, 195)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(49, 49, 49))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 770, 540));

        jPanel3.setBackground(new java.awt.Color(137, 139, 139));

        jButton1.setBackground(new java.awt.Color(137, 139, 139));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home-button (1).png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(16, 16, 16))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jButton1)
                .addContainerGap(456, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
insertRentDetails();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
    String selectedRentID = jList1.getSelectedValue();
    if (selectedRentID == null) {
        JOptionPane.showMessageDialog(this, "Please select a rent record to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this rent record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        String deleteQuery = "DELETE FROM Rent WHERE rent_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, selectedRentID);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Rent record deleted successfully!");
                loadOrderIDs();
                clearFormFields();// Refresh the JList
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete rent record. Please try again.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the rent record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
updateRentDetails();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
displayRent myProfile = new displayRent(); // Pass the current username
        myProfile.setVisible(true); // Show the MyProfile form
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
       
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        CustomerManagement myProfile = new CustomerManagement(); // Pass the current username
        myProfile.setVisible(true); // Show the MyProfile form
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        Carpurchase myProfile = new Carpurchase(); // Pass the current username
        myProfile.setVisible(true); // Show the MyProfile form
        this.dispose();// TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        clearFormFields();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Dashboard myProfile = new Dashboard(username); // Pass the current username
        myProfile.setVisible(true); // Show the MyProfile form
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.util.logging.Logger.getLogger(Rent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(Rent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(Rent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(Rent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Rent().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
