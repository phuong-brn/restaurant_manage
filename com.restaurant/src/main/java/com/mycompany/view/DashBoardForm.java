/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.view;

import com.mycompany.view.POSForm;
import com.mycompany.model.Admin;
import com.mycompany.model.Bill;
import com.mycompany.model.Dish;
import com.mycompany.service.AdminService;
import com.mycompany.service.BillService;
import com.mycompany.service.DishService;
import com.mycompany.util.DateUtil;
import com.mycompany.util.HandleImage;
import com.mycompany.util.ImageRenderer;
import com.mycompany.util.TableActionCellEditor;
import com.mycompany.util.TableActionCellRender;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Admin
 */
public class DashBoardForm extends javax.swing.JFrame {

    /**
     * Creates new form DashBoardForm
     */
    private final Admin admin;
    private DateUtil billTimestamp;

    public DashBoardForm(Admin admin) {
        this.admin = admin;
        this.billTimestamp = new DateUtil();
        initComponents();
        handleRole();
        costomComponents();

//        JPanel grid = new JPanel();
//        GridLayout lo = new GridLayout(0, 10);
//        grid.setLayout(lo);
//        for (int i = 0; i < 20; i++) {
//            grid.add(dish);
//        }
//
//    // Assuming menuScroll is a JScrollPane
//        menuScroll.setViewportView(grid);
    }

    public void handleMenuTable() {
        List<Dish> menu = DishService.getAll();
        DefaultTableModel model = (DefaultTableModel) menuTable.getModel();
        model.setRowCount(0);
        menuTable.getColumn("Image").setCellRenderer(new CellRenderer());

        String imagePath = "/image/image.png";

        for (Dish dish : menu) {
            JLabel imageLabel = new JLabel();
            BufferedImage originalImage = null;

            if (dish.getImage() == null) {
                URL url = getClass().getResource(imagePath);
                File file = new File(url.getPath());

                try {
                    originalImage = ImageIO.read(file);
                } catch (IOException ex) {
                    Logger.getLogger(DashBoardForm.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                originalImage = dish.getImageAsBufferedImage();
            }
            Image scaledImage = HandleImage.getScaledImage(originalImage, 100, 50);
            ImageIcon icon = new ImageIcon(scaledImage);
            imageLabel.setIcon(icon);
            model.addRow(new Object[]{String.valueOf(menu.indexOf(dish) + 1), dish.getId(), dish.getName(), String.valueOf(dish.getPrice()), dish.getCategory(), imageLabel});
        }
        menuTable.setDefaultRenderer(Object.class, new CenterTextRenderer());

    }

    class CellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            TableColumn tb = menuTable.getColumn("Image");
            tb.setMaxWidth(100);
            tb.setMinWidth(50);

            menuTable.setRowHeight(60);

            return (Component) value;
        }

    }

    /**
     *
     */
    public void handlStaffTable() {
        List<Admin> admins = AdminService.getAll();
        DefaultTableModel model = (DefaultTableModel) staffTable.getModel();
        model.setRowCount(0);

        for (Admin admin : admins) {
            model.addRow(new Object[]{String.valueOf(admins.indexOf(admin) + 1), admin.getId(), admin.getName(), admin.getRole(), admin.getPhone()});
        }
        staffTable.setDefaultRenderer(Object.class, new CenterTextRenderer());
    }

    public void handlTableTable() {

    }

    public void handleBillTable() {
        String choseDate = dateRegularOption.getSelectedItem().toString();
        List<Bill> list = null;
        if(billTimestamp.getPrevousNumber() == 0){
            currentBtn.setEnabled(false);
            nextBtn.setEnabled(false);
        }
        else{
            currentBtn.setEnabled(true);
            nextBtn.setEnabled(true);
        }
        if (choseDate.equals("Daily")) {
            LocalDate[] date = billTimestamp.getDailyDate();
            dateLabel.setText(date[0].toString());
            list = BillService.getWithDate(date[0], date[1]);
        } else if (choseDate.equals("Weekly")) {
            LocalDate[] date = billTimestamp.getWeeklyDate();
            dateLabel.setText(date[0].toString() + " to " + date[1].toString());
            list = BillService.getWithDate(date[0], date[1]);

        } else if (choseDate.equals("Montly")) {
            LocalDate[] date = billTimestamp.getMonthlyDate();
            dateLabel.setText(date[0].toString() + " to " + date[1].toString());
            list = BillService.getWithDate(date[0], date[1]);

        } else if (choseDate.equals("Yearly")) {
            LocalDate[] date = billTimestamp.getYearlyDate();
            dateLabel.setText(date[0].toString() + " to " + date[1].toString());
            list = BillService.getWithDate(date[0], date[1]);

        }
        else if(choseDate.equals("All Time")){
            list = BillService.getAll();
            dateLabel.setText("");
            currentBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            previousBtn.setEnabled(false);
        }
        if(list == null){
            return;
        }
        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
        model.setRowCount(0);
        BigDecimal total = BigDecimal.ZERO;
        for (Bill bill : list) {
            total = total.add(bill.calculateTotal());
            model.addRow(new Object[]{String.valueOf(list.indexOf(bill) + 1), bill.getId(), bill.getTime(), bill.calculateTotal().toString() + "$"});
        }
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        billTable.setDefaultRenderer(Object.class, new CenterTextRenderer());
        revenueLabel.setText(total.toString() + "$");

    }

    public void handleRole() {
        if (this.admin.getRole().equals("staff")) {
            addDishBtn.setEnabled(false);
            updateDishBtn.setEnabled(false);
            removeDishBtn.setEnabled(false);

            addStaffBtn.setEnabled(false);
            updateStaffBtn.setEnabled(false);
            removeStaffBtn.setEnabled(false);
        }
    }

    public void costomComponents() {
        adminNameLabel.setText(adminNameLabel.getText() + admin.getName());
        adminRoleLabel.setText(adminRoleLabel.getText() + admin.getRole());
        handlStaffTable();
        handleMenuTable();
        handleBillTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    static class CenterTextRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set the text alignment to center
            setHorizontalAlignment(JLabel.CENTER);

            return rendererComponent;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dish = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        mainBoard = new javax.swing.JTabbedPane();
        menuTab = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        menuTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        addDishBtn = new javax.swing.JButton();
        removeDishBtn = new javax.swing.JButton();
        updateDishBtn = new javax.swing.JButton();
        stafftab = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        staffTable = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        addStaffBtn = new javax.swing.JButton();
        removeStaffBtn = new javax.swing.JButton();
        updateStaffBtn = new javax.swing.JButton();
        tabletab = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        staffTable1 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        addTableBtn = new javax.swing.JButton();
        removeTableBtn = new javax.swing.JButton();
        updateTableBtn = new javax.swing.JButton();
        billTab = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        billTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        dateRegularOption = new javax.swing.JComboBox<>();
        viewBill = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        revenueLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        nextBtn = new javax.swing.JButton();
        currentBtn = new javax.swing.JButton();
        previousBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        menuItem = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        staffItem = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tableItem = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tableItem1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tableItem2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        adminNameLabel = new javax.swing.JLabel();
        adminRoleLabel = new javax.swing.JLabel();

        dish.setBackground(new java.awt.Color(226, 215, 255));
        dish.setVerifyInputWhenFocusTarget(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Chicken");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("100$");

        javax.swing.GroupLayout dishLayout = new javax.swing.GroupLayout(dish);
        dish.setLayout(dishLayout);
        dishLayout.setHorizontalGroup(
            dishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dishLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(dishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        dishLayout.setVerticalGroup(
            dishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dishLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(40, 40, 40))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(233, 233, 235));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 700));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 400));

        title.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        title.setText("Menu");
        jPanel12.add(title);

        mainBoard.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        menuTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "SR", "ID", "Name", "Price", "Category", "Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(menuTable);
        if (menuTable.getColumnModel().getColumnCount() > 0) {
            menuTable.getColumnModel().getColumn(0).setMinWidth(40);
            menuTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            menuTable.getColumnModel().getColumn(0).setMaxWidth(40);
            menuTable.getColumnModel().getColumn(1).setMinWidth(40);
            menuTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            menuTable.getColumnModel().getColumn(1).setMaxWidth(40);
            menuTable.getColumnModel().getColumn(2).setResizable(false);
            menuTable.getColumnModel().getColumn(2).setPreferredWidth(130);
            menuTable.getColumnModel().getColumn(2).setHeaderValue("Name");
            menuTable.getColumnModel().getColumn(3).setResizable(false);
            menuTable.getColumnModel().getColumn(4).setResizable(false);
            menuTable.getColumnModel().getColumn(5).setResizable(false);
            menuTable.getColumnModel().getColumn(5).setHeaderValue("Image");
        }

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        addDishBtn.setText("Add");
        addDishBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDishBtnActionPerformed(evt);
            }
        });
        jPanel6.add(addDishBtn);

        removeDishBtn.setText("Remove");
        removeDishBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDishBtnActionPerformed(evt);
            }
        });
        jPanel6.add(removeDishBtn);

        updateDishBtn.setText("Update");
        updateDishBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDishBtnActionPerformed(evt);
            }
        });
        jPanel6.add(updateDishBtn);

        javax.swing.GroupLayout menuTabLayout = new javax.swing.GroupLayout(menuTab);
        menuTab.setLayout(menuTabLayout);
        menuTabLayout.setHorizontalGroup(
            menuTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(menuTabLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        menuTabLayout.setVerticalGroup(
            menuTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuTabLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainBoard.addTab("tab1", menuTab);

        staffTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "SR", "ID", "Name", "Status", "Phone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        staffTable.setRowHeight(40);
        jScrollPane1.setViewportView(staffTable);
        if (staffTable.getColumnModel().getColumnCount() > 0) {
            staffTable.getColumnModel().getColumn(0).setMinWidth(40);
            staffTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            staffTable.getColumnModel().getColumn(0).setMaxWidth(40);
            staffTable.getColumnModel().getColumn(1).setMinWidth(40);
            staffTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            staffTable.getColumnModel().getColumn(1).setMaxWidth(40);
            staffTable.getColumnModel().getColumn(2).setResizable(false);
            staffTable.getColumnModel().getColumn(3).setResizable(false);
            staffTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        addStaffBtn.setText("Add");
        addStaffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStaffBtnActionPerformed(evt);
            }
        });
        jPanel11.add(addStaffBtn);

        removeStaffBtn.setText("Remove");
        removeStaffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeStaffBtnActionPerformed(evt);
            }
        });
        jPanel11.add(removeStaffBtn);

        updateStaffBtn.setText("Update");
        updateStaffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStaffBtnActionPerformed(evt);
            }
        });
        jPanel11.add(updateStaffBtn);

        javax.swing.GroupLayout stafftabLayout = new javax.swing.GroupLayout(stafftab);
        stafftab.setLayout(stafftabLayout);
        stafftabLayout.setHorizontalGroup(
            stafftabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(stafftabLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        stafftabLayout.setVerticalGroup(
            stafftabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, stafftabLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainBoard.addTab("tab2", stafftab);

        staffTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "SR", "ID", "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(staffTable1);
        if (staffTable1.getColumnModel().getColumnCount() > 0) {
            staffTable1.getColumnModel().getColumn(0).setResizable(false);
            staffTable1.getColumnModel().getColumn(0).setPreferredWidth(40);
            staffTable1.getColumnModel().getColumn(1).setResizable(false);
            staffTable1.getColumnModel().getColumn(1).setPreferredWidth(40);
            staffTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        addTableBtn.setText("Add");
        addTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTableBtnActionPerformed(evt);
            }
        });
        jPanel10.add(addTableBtn);

        removeTableBtn.setText("Remove");
        removeTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTableBtnActionPerformed(evt);
            }
        });
        jPanel10.add(removeTableBtn);

        updateTableBtn.setText("Update");
        updateTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTableBtnActionPerformed(evt);
            }
        });
        jPanel10.add(updateTableBtn);

        javax.swing.GroupLayout tabletabLayout = new javax.swing.GroupLayout(tabletab);
        tabletab.setLayout(tabletabLayout);
        tabletabLayout.setHorizontalGroup(
            tabletabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(tabletabLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        tabletabLayout.setVerticalGroup(
            tabletabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabletabLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainBoard.addTab("tab2", tabletab);

        billTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "SR", "ID", "Date", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billTable.setRowHeight(25);
        jScrollPane3.setViewportView(billTable);
        if (billTable.getColumnModel().getColumnCount() > 0) {
            billTable.getColumnModel().getColumn(0).setMinWidth(70);
            billTable.getColumnModel().getColumn(0).setPreferredWidth(70);
            billTable.getColumnModel().getColumn(0).setMaxWidth(70);
            billTable.getColumnModel().getColumn(1).setMinWidth(70);
            billTable.getColumnModel().getColumn(1).setPreferredWidth(70);
            billTable.getColumnModel().getColumn(1).setMaxWidth(70);
            billTable.getColumnModel().getColumn(2).setResizable(false);
            billTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        dateRegularOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Yearly", "All Time" }));
        dateRegularOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateRegularOptionActionPerformed(evt);
            }
        });

        viewBill.setText("Detail");
        viewBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBillActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Revenue: ");

        revenueLabel.setText("1");

        dateLabel.setText("d1");

        nextBtn.setText("Next");
        nextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBtnActionPerformed(evt);
            }
        });

        currentBtn.setText("Current");
        currentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentBtnActionPerformed(evt);
            }
        });

        previousBtn.setText("Previous");
        previousBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout billTabLayout = new javax.swing.GroupLayout(billTab);
        billTab.setLayout(billTabLayout);
        billTabLayout.setHorizontalGroup(
            billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(billTabLayout.createSequentialGroup()
                .addGroup(billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billTabLayout.createSequentialGroup()
                        .addComponent(viewBill)
                        .addGap(0, 0, 0)
                        .addComponent(dateRegularOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billTabLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGroup(billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billTabLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billTabLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(revenueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addGroup(billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billTabLayout.createSequentialGroup()
                        .addComponent(previousBtn)
                        .addGap(0, 0, 0)
                        .addComponent(nextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(currentBtn))
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        billTabLayout.setVerticalGroup(
            billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billTabLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(revenueLabel)
                    .addComponent(jLabel10)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(billTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewBill)
                    .addComponent(dateRegularOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextBtn)
                    .addComponent(currentBtn)
                    .addComponent(previousBtn))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainBoard.addTab("tab2", billTab);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainBoard)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 400));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(53, 47, 68));
        jPanel3.setPreferredSize(new java.awt.Dimension(30, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Dashboard");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel3.add(jLabel1);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(185, 180, 199));
        jPanel4.setPreferredSize(new java.awt.Dimension(520, 440));

        jPanel7.setLayout(new java.awt.GridLayout(0, 1));

        menuItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuItemMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Menu");
        menuItem.add(jLabel6);

        jPanel7.add(menuItem);

        staffItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        staffItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        staffItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                staffItemMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Staff");
        staffItem.add(jLabel4);

        jPanel7.add(staffItem);

        tableItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tableItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableItemMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Table");
        tableItem.add(jLabel3);

        jPanel7.add(tableItem);

        tableItem1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tableItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableItem1MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Bill");
        tableItem1.add(jLabel5);

        jPanel7.add(tableItem1);

        tableItem2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tableItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableItem2MouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("POS");
        tableItem2.add(jLabel9);

        jPanel7.add(tableItem2);

        jPanel5.setLayout(new java.awt.BorderLayout());

        adminNameLabel.setText("Name:  ");
        adminNameLabel.setPreferredSize(new java.awt.Dimension(35, 25));
        jPanel5.add(adminNameLabel, java.awt.BorderLayout.PAGE_START);

        adminRoleLabel.setText("Role: ");
        adminRoleLabel.setPreferredSize(new java.awt.Dimension(24, 25));
        jPanel5.add(adminRoleLabel, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel9.add(jPanel1, java.awt.BorderLayout.WEST);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void staffItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffItemMouseClicked
        mainBoard.setSelectedIndex(1);
        title.setText("Staff");
    }//GEN-LAST:event_staffItemMouseClicked

    private void tableItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableItemMouseClicked
        mainBoard.setSelectedIndex(2);
        title.setText("Table");
    }//GEN-LAST:event_tableItemMouseClicked

    private void menuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuItemMouseClicked
        title.setText("Menu");
        mainBoard.setSelectedIndex(0);    }//GEN-LAST:event_menuItemMouseClicked

    private void addStaffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStaffBtnActionPerformed
        AddStaffForm addStaffForm = new AddStaffForm(this);
        addStaffForm.setLocationRelativeTo(null);
        addStaffForm.setVisible(true);
        addStaffForm.setDefaultCloseOperation(addStaffForm.HIDE_ON_CLOSE);
    }//GEN-LAST:event_addStaffBtnActionPerformed

    private void addDishBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDishBtnActionPerformed
        AddDishForm addDishForm = new AddDishForm(this);
        addDishForm.setLocationRelativeTo(null);
        addDishForm.setVisible(true);
        addDishForm.setDefaultCloseOperation(addDishForm.HIDE_ON_CLOSE);
    }//GEN-LAST:event_addDishBtnActionPerformed

    private void removeDishBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDishBtnActionPerformed
        if (menuTable.getSelectedRowCount() == 1) {
            String id = String.valueOf(menuTable.getValueAt(menuTable.getSelectedRow(), 1));
            DishService.delete(Integer.valueOf(id));
            handleMenuTable();
        } else {
            JOptionPane.showMessageDialog(this, "You must select a row in the table below before implementing this action!", "Note", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_removeDishBtnActionPerformed

    private void removeStaffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeStaffBtnActionPerformed
        if (staffTable.getSelectedRowCount() == 1) {

            String id = String.valueOf(staffTable.getValueAt(staffTable.getSelectedRow(), 1));
            AdminService.delete(Integer.valueOf(id));
            handlStaffTable();
        } else {
            JOptionPane.showMessageDialog(this, "You must select a row in the table below before implementing this action!", "Note", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_removeStaffBtnActionPerformed

    private void tableItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableItem1MouseClicked
        mainBoard.setSelectedIndex(3);
        title.setText("Bill");
    }//GEN-LAST:event_tableItem1MouseClicked

    private void tableItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableItem2MouseClicked
        POSForm posForm = new POSForm(this);
        posForm.setLocationRelativeTo(null);
        posForm.setDefaultCloseOperation(POSForm.DISPOSE_ON_CLOSE);
        posForm.setVisible(true);
    }//GEN-LAST:event_tableItem2MouseClicked

    private void addTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTableBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addTableBtnActionPerformed

    private void removeTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTableBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeTableBtnActionPerformed

    private void viewBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBillActionPerformed
          if (billTable.getSelectedRowCount() == 1) {
            int id = Integer.parseInt(billTable.getValueAt(billTable.getSelectedRow(), 1).toString());
            System.out.println(id);
            Bill bill =BillService.getById(id);
            bill.printBill();
            BillForm f = new BillForm(bill);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            f.setResizable(false);
            f.setDefaultCloseOperation(f.HIDE_ON_CLOSE);

        } else {
            JOptionPane.showMessageDialog(this, "You must select a row in the table below before implementing this action!", "Note", JOptionPane.INFORMATION_MESSAGE);

        }
  
    }//GEN-LAST:event_viewBillActionPerformed

    private void updateTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTableBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateTableBtnActionPerformed

    private void updateStaffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStaffBtnActionPerformed
        if (staffTable.getSelectedRowCount() == 1) {
            int id = Integer.parseInt(staffTable.getValueAt(staffTable.getSelectedRow(), 1).toString());
            System.out.println("Id of admin update " + String.valueOf(id));
            Admin admin = AdminService.getById(id);
            System.out.println("Get first: " + admin.toString());
            UpdateStaffForm f = new UpdateStaffForm(this, admin);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            f.setResizable(false);
            f.setDefaultCloseOperation(f.HIDE_ON_CLOSE);

        } else {
            JOptionPane.showMessageDialog(this, "You must select a row in the table below before implementing this action!", "Note", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_updateStaffBtnActionPerformed

    private void updateDishBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDishBtnActionPerformed
        if (menuTable.getSelectedRowCount() == 1) {
            int id = Integer.parseInt(menuTable.getValueAt(menuTable.getSelectedRow(), 1).toString());
            Dish dish = DishService.getById(id);
            UpdateDishForm f = new UpdateDishForm(this, dish);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            f.setResizable(false);
            f.setDefaultCloseOperation(f.HIDE_ON_CLOSE);

        } else {
            JOptionPane.showMessageDialog(this, "You must select a row in the table below before implementing this action!", "Note", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_updateDishBtnActionPerformed

    private void dateRegularOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateRegularOptionActionPerformed
        String value = dateRegularOption.getSelectedItem().toString();
        this.billTimestamp.getCurrent();
        handleBillTable();
    }//GEN-LAST:event_dateRegularOptionActionPerformed

    private void previousBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousBtnActionPerformed
        this.billTimestamp.getPrevious();
        handleBillTable();
    }//GEN-LAST:event_previousBtnActionPerformed

    private void currentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentBtnActionPerformed
        this.billTimestamp.getCurrent();
        handleBillTable();
    }//GEN-LAST:event_currentBtnActionPerformed

    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBtnActionPerformed
        this.billTimestamp.getNext();
        handleBillTable();

    }//GEN-LAST:event_nextBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDishBtn;
    private javax.swing.JButton addStaffBtn;
    private javax.swing.JButton addTableBtn;
    private javax.swing.JLabel adminNameLabel;
    private javax.swing.JLabel adminRoleLabel;
    private javax.swing.JPanel billTab;
    private javax.swing.JTable billTable;
    private javax.swing.JButton currentBtn;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JComboBox<String> dateRegularOption;
    private javax.swing.JPanel dish;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane mainBoard;
    private javax.swing.JPanel menuItem;
    private javax.swing.JPanel menuTab;
    private javax.swing.JTable menuTable;
    private javax.swing.JButton nextBtn;
    private javax.swing.JButton previousBtn;
    private javax.swing.JButton removeDishBtn;
    private javax.swing.JButton removeStaffBtn;
    private javax.swing.JButton removeTableBtn;
    private javax.swing.JLabel revenueLabel;
    private javax.swing.JPanel staffItem;
    private javax.swing.JTable staffTable;
    private javax.swing.JTable staffTable1;
    private javax.swing.JPanel stafftab;
    private javax.swing.JPanel tableItem;
    private javax.swing.JPanel tableItem1;
    private javax.swing.JPanel tableItem2;
    private javax.swing.JPanel tabletab;
    private javax.swing.JLabel title;
    private javax.swing.JButton updateDishBtn;
    private javax.swing.JButton updateStaffBtn;
    private javax.swing.JButton updateTableBtn;
    private javax.swing.JButton viewBill;
    // End of variables declaration//GEN-END:variables
}
