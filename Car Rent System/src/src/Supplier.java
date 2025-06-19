/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ilma
 */
import java.io.File;
import java.sql.*;

import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class Supplier extends javax.swing.JFrame {

    /**
     * Creates new form Supplier
     */
    String url = "jdbc:mysql://localhost:3306/car";
    String user = "root";
    String password = "AMJU";
        private String username; // Class variable to hold the current user's username

    private DefaultListModel<String> suppListModel; // Model for the JList

    public Supplier() {
        initComponents();
        loadSupplierNames();
        jList1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Check if the selection is final
                updateFieldsWithSelectedSupplier();
            }
        });
        
        
        
        jList1.addListSelectionListener(new ListSelectionListener() {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // Check if the event is not adjusting to avoid duplicate calls
        if (!e.getValueIsAdjusting()) {
            updateFieldsWithsupplier(); // Call the method when a supplier is selected
        }
    }
});


    }

    // Method to validate phone number (10 digits)
    private boolean isValidPhone(String phone) {
        return Pattern.matches("\\d{10}", phone);  // Accepts exactly 10 digits
    }

    
    int selectedSupId=-1;
    
private void updateFieldsWithsupplier() {
    String selectedSup = jList1.getSelectedValue(); // Get the selected supplier name

    if (selectedSup != null) {
        String query = "SELECT * FROM supplier WHERE sup_name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedSup); // Set the parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Store the unique supplier ID
                    selectedSupId = rs.getInt("sup_id"); // Corrected to sup_id

                    // Update fields with the retrieved data
                    textname.setText(rs.getString("sup_name"));
                    textadd.setText(rs.getString("sup_address"));
                    txtphone.setText(rs.getString("sup_phone"));
                    txtmail.setText(rs.getString("sup_email"));
                    txtRemark.setText(rs.getString("sup_remark"));
                } else {
                    JOptionPane.showMessageDialog(this, "Supplier not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching Supplier details from the database.");
        }
    }
}


    // Method to validate email address
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    private void loadSupplierNames() {
        String selectQuery = "SELECT sup_name FROM supplier";

        suppListModel = new DefaultListModel<>(); // Initialize the model for the JList
        jList1.setModel(suppListModel); // Set the model to the JList

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(selectQuery); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String carNumber = rs.getString("sup_name");
                suppListModel.addElement(carNumber); // Add car number to the model
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading supplier names from the database.");
        }
    }

    private void updateFieldsWithSelectedSupplier() {
        String selectedsupplier = jList1.getSelectedValue(); // Get the selected car number

        if (selectedsupplier != null) {
            String query = "SELECT * FROM supplier WHERE sup_name = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, selectedsupplier); // Set the car number

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Update fields with the retrieved data
                        textname.setText(rs.getString("sup_name"));
                        textadd.setText(rs.getString("sup_address")); // Set selected brand
                        txtphone.setText(rs.getString("sup_phone")); // Set selected model
                        txtmail.setText(rs.getString("sup_email"));
                        txtRemark.setText(rs.getString("sup_remark"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching car details from the database.");
            }

        }
    }

    private void clearFields() {
        textname.setText("");
        textadd.setText("");
        txtphone.setText("");
        txtmail.setText("");
        txtRemark.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        textname = new javax.swing.JTextField();
        txtmail = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        txtphone = new javax.swing.JTextField();
        textadd = new javax.swing.JTextField();

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
        jLabel35.setText("Name");

        jLabel36.setBackground(new java.awt.Color(32, 42, 57));
        jLabel36.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel36.setText("Address");

        jLabel38.setBackground(new java.awt.Color(32, 42, 57));
        jLabel38.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel38.setText("Phone");

        jLabel39.setBackground(new java.awt.Color(32, 42, 57));
        jLabel39.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel39.setText("Email");

        jLabel1.setBackground(new java.awt.Color(32, 42, 57));
        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(32, 42, 57));
        jLabel1.setText("Supplier Management");

        textname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textnameActionPerformed(evt);
            }
        });

        txtmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmailActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton1.setForeground(new java.awt.Color(32, 42, 57));
        jButton1.setText("Add Supplier");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 204, 204));
        jButton3.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton3.setForeground(new java.awt.Color(32, 42, 57));
        jButton3.setText("Delete Supplier");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton2.setForeground(new java.awt.Color(32, 42, 57));
        jButton2.setText("Update Supplier");
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

        txtRemark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRemarkActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton5.setText("Clear");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        txtphone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtphoneActionPerformed(evt);
            }
        });

        textadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textaddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(98, 98, 98))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textname, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(textadd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtmail, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                                .addComponent(txtRemark))
                                            .addComponent(txtphone, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 68, Short.MAX_VALUE)))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textadd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtphone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 770, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textnameActionPerformed

    private void txtmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmailActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      String name = textname.getText();
String address = textadd.getText();
String phone = txtphone.getText();
String email = txtmail.getText();
String remark = txtRemark.getText();

// SQL queries to insert data and check for duplicates
String insertQuery = "INSERT INTO supplier (sup_name, sup_address, sup_phone, sup_email, sup_remark) VALUES (?, ?, ?, ?, ?)";
String checkNameQuery = "SELECT COUNT(*) FROM supplier WHERE sup_name = ?";
String checkPhoneQuery = "SELECT COUNT(*) FROM supplier WHERE sup_phone = ?";
String checkEmailQuery = "SELECT COUNT(*) FROM supplier WHERE sup_email = ?";

try (Connection conn = DriverManager.getConnection(url, user, password);
     PreparedStatement checkNameStmt = conn.prepareStatement(checkNameQuery);
     PreparedStatement checkPhoneStmt = conn.prepareStatement(checkPhoneQuery);
     PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery);
     PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

    // Check if the supplier name already exists
    checkNameStmt.setString(1, name);
    ResultSet rsName = checkNameStmt.executeQuery();
    if (rsName.next() && rsName.getInt(1) > 0) {
        JOptionPane.showMessageDialog(this, "A supplier with this name already exists. Please use a different name.");
        return;
    }

    // Check if the phone number already exists
    checkPhoneStmt.setString(1, phone);
    ResultSet rsPhone = checkPhoneStmt.executeQuery();
    if (rsPhone.next() && rsPhone.getInt(1) > 0) {
        JOptionPane.showMessageDialog(this, "A supplier with this phone number already exists. Please use a different phone number.");
        return;
    }

    // Check if the email already exists
    checkEmailStmt.setString(1, email);
    ResultSet rsEmail = checkEmailStmt.executeQuery();
    if (rsEmail.next() && rsEmail.getInt(1) > 0) {
        JOptionPane.showMessageDialog(this, "A supplier with this email already exists. Please use a different email.");
        return;
    }

    // Validate required fields
    if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all required fields correctly.");
        return;
    }

    if (!isValidPhone(phone)) {
        JOptionPane.showMessageDialog(this, "Invalid phone number. Please enter a valid 10-digit number.");
        return;
    }

    if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(this, "Invalid email address. Please enter a valid email.");
        return;
    }

    // Set parameters for the insert statement
    insertStmt.setString(1, name);
    insertStmt.setString(2, address);
    insertStmt.setString(3, phone);
    insertStmt.setString(4, email);
    insertStmt.setString(5, remark);

    // Execute the insert statement
    int rowsInserted = insertStmt.executeUpdate();
    if (rowsInserted > 0) {
        JOptionPane.showMessageDialog(this, "Supplier data saved successfully!");
        loadSupplierNames(); // Refresh the supplier list
        clearFields();       // Clear the input fields
    }

} catch (SQLException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error saving supplier data.");
}

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       String name = textname.getText(); // Assuming the supplier's name is used to find the corresponding sup_id

    // Check if a supplier name is entered
    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the supplier name to delete.");
        return;
    }

    // Confirm deletion
    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the supplier ? if you delete the supplier related purchase and rent information will be deleted ",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return; // User chose not to delete
    }

    // SQL queries
    String getIdQuery = "SELECT sup_id FROM supplier WHERE sup_name = ?";
    String checkCarPurchaseQuery = "SELECT COUNT(*) FROM car_purchase WHERE sup_id = ?";
    String deleteCarPurchaseQuery = "DELETE FROM car_purchase WHERE sup_id = ?";
    String deleteRentQuery = "DELETE FROM rent WHERE supplier_id = ?";
    String deleteSupplierQuery = "DELETE FROM supplier WHERE sup_name = ?"; // Or use sup_id if available

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement getIdStmt = conn.prepareStatement(getIdQuery);
         PreparedStatement checkCarPurchaseStmt = conn.prepareStatement(checkCarPurchaseQuery);
         PreparedStatement deleteCarPurchaseStmt = conn.prepareStatement(deleteCarPurchaseQuery);
         PreparedStatement deleteRentStmt = conn.prepareStatement(deleteRentQuery);
         PreparedStatement deleteSupplierStmt = conn.prepareStatement(deleteSupplierQuery)) {

        // Get the supplier ID
        getIdStmt.setString(1, name);
        ResultSet rs = getIdStmt.executeQuery();
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Supplier not found.");
            return;
        }
        int supId = rs.getInt("sup_id");

        // Check for related purchases in car_purchase
        checkCarPurchaseStmt.setInt(1, supId);
        rs = checkCarPurchaseStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            // If there are related purchases, delete them first
            deleteCarPurchaseStmt.setInt(1, supId);
            deleteCarPurchaseStmt.executeUpdate();
        }

        // Delete related rent records
        deleteRentStmt.setInt(1, supId);
        deleteRentStmt.executeUpdate();

        // Now delete the supplier
        deleteSupplierStmt.setString(1, name);
        int rowsDeleted = deleteSupplierStmt.executeUpdate();
        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(this, "Supplier deleted successfully!");
            clearFields();
            loadSupplierNames(); // Refresh the supplier list
        } else {
            JOptionPane.showMessageDialog(this, "Supplier not found. Deletion failed.");
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Check console for detailed error
        JOptionPane.showMessageDialog(this, "Error deleting supplier: " + e.getMessage());
    }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
  // Check if a supplier is selected and fields are filled
if (selectedSupId > 0) {
    String name = textname.getText().trim();
    String address = textadd.getText().trim();
    String phone = txtphone.getText().trim();
    String email = txtmail.getText().trim();
    String remark = txtRemark.getText().trim();

    // Validate required fields
    if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all required fields.");
        return;
    }

    // SQL queries to check for unique phone and email excluding the current supplier
    String checkPhoneQuery = "SELECT COUNT(*) FROM supplier WHERE sup_phone = ? AND sup_id != ?";
    String checkEmailQuery = "SELECT COUNT(*) FROM supplier WHERE sup_email = ? AND sup_id != ?";
    String updateQuery = "UPDATE supplier SET sup_name = ?, sup_address = ?, sup_phone = ?, sup_email = ?, sup_remark = ? WHERE sup_id = ?";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement checkPhoneStmt = conn.prepareStatement(checkPhoneQuery);
         PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery);
         PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

        // Check if the phone number is unique (excluding current supplier)
        checkPhoneStmt.setString(1, phone);
        checkPhoneStmt.setInt(2, selectedSupId);
        ResultSet rsPhone = checkPhoneStmt.executeQuery();
        if (rsPhone.next() && rsPhone.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "This phone number is already associated with another supplier. Please use a different phone number.");
            return;
        }

        // Check if the email is unique (excluding current supplier)
        checkEmailStmt.setString(1, email);
        checkEmailStmt.setInt(2, selectedSupId);
        ResultSet rsEmail = checkEmailStmt.executeQuery();
        if (rsEmail.next() && rsEmail.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "This email is already associated with another supplier. Please use a different email.");
            return;
        }

        // Set parameters for the update statement
        updateStmt.setString(1, name);
        updateStmt.setString(2, address);
        updateStmt.setString(3, phone);
        updateStmt.setString(4, email);
        updateStmt.setString(5, remark);
        updateStmt.setInt(6, selectedSupId); // Use the selected supplier ID

        // Execute the update
        int rowsUpdated = updateStmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Supplier details updated successfully!");
            loadSupplierNames(); // Refresh the supplier list if applicable
        } else {
            JOptionPane.showMessageDialog(this, "No changes made to the supplier.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating supplier details.");
    }
} else {
    JOptionPane.showMessageDialog(this, "No supplier selected for update.");
}


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

                try {
            String reportSource = "C:\\Users\\amjad\\Documents\\New folder\\Supplier.jrxml";

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

    private void txtRemarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRemarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRemarkActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        clearFields();    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtphoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtphoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtphoneActionPerformed

    private void textaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textaddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textaddActionPerformed

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
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Supplier().setVisible(true);
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
    private javax.swing.JTextField textadd;
    private javax.swing.JTextField textname;
    private javax.swing.JTextField txtRemark;
    private javax.swing.JTextField txtmail;
    private javax.swing.JTextField txtphone;
    // End of variables declaration//GEN-END:variables
}
