/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.view;

import com.mycompany.dao.DishDAO;
import com.mycompany.model.Dish;
import com.mycompany.util.*;
import com.mycompany.service.DishService;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import com.mycompany.view.DashBoardForm;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Tomioka
 */
public class POSForm extends javax.swing.JFrame {
    private DashBoardForm dashBoard;
    private DishService dishService;
    
    /**
     * Creates new form POSForm
     */
    public POSForm() {
        initComponents();
    }
    
    public POSForm(DashBoardForm dashBoard){
        setTitle("POS");
        this.dashBoard = dashBoard;
        initComponents();
        customComponents();
        
    }
    public void productHandleTable() {
        List<Dish> dishList = DishDAO.getAll();
        DefaultTableModel proModel = (DefaultTableModel) prodctTable.getModel();
        proModel.setRowCount(0);

        // Đặt số lượng cột hiển thị ảnh
        int numImageColumns = 5;

        // Điều chỉnh kích thước của ảnh và panel
        int imageWidth = 120;
        int imageHeight = 120;
        int panelWidth = imageWidth + 10; // Thêm khoảng trắng giữa ảnh và mô tả
        int panelHeight = imageHeight + 20; // Đủ để chứa cả label mô tả

        // Tạo một mảng để lưu trữ tất cả các panel
        JPanel[][] imagePanels = new JPanel[dishList.size()][numImageColumns];

        // Điền mảng với panel từ danh sách Dish
        int rowCounter = 0;
        int columnCounter = 0;
        for (Dish dish : dishList) {
            // Tạo panel chứa ảnh và mô tả
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());

            // Tạo label chứa ảnh
            JLabel imageLabel = new JLabel();
            BufferedImage originalImage = null;
            String imagePath = "/image/image.png";
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
            Image scaledImage = HandleImage.getScaledImage(originalImage, 100, imageHeight);
            ImageIcon icon = new ImageIcon(scaledImage);
            imageLabel.setIcon(icon);

            // Tạo label chứa mô tả
            JLabel descriptionLabel = new JLabel(dish.getName());
            JLabel priceLabel = new JLabel("Price: " + dish.getPrice()); 

            imagePanel.add(descriptionLabel,BorderLayout.CENTER);
            imagePanel.add(imageLabel, BorderLayout.NORTH);

            imagePanel.add(priceLabel, BorderLayout.SOUTH);
            imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Sử dụng BoxLayout để sắp xếp các thành phần theo chiều dọc


            // Thêm panel vào mảng
            imagePanels[rowCounter][columnCounter] = imagePanel;

            columnCounter++;
            if (columnCounter == numImageColumns) {
                columnCounter = 0;
                rowCounter++;
            }
        }

        // Thêm dữ liệu vào mô hình bảng
        for (int row = 0; row < imagePanels.length; row++) {
            proModel.addRow(imagePanels[row]);
        }

        // Đặt renderer cho tất cả các cột chứa hình ảnh và mô tả
        for (int i = 0; i < numImageColumns; i++) {
            prodctTable.getColumnModel().getColumn(i).setCellRenderer(new ImageCellRenderer());
        }

        // Đặt chiều cao của dòng trong bảng
        prodctTable.setRowHeight(panelHeight + 20);
        productMouseListener();
    }    
    public void getProductByCatergory(String catergory) {
        List<Dish> dishList = DishService.getByCategory(catergory);
        DefaultTableModel proModel = (DefaultTableModel) prodctTable.getModel();
        proModel.setRowCount(0);

        // Đặt số lượng cột hiển thị ảnh
        int numImageColumns = 5;

        // Điều chỉnh kích thước của ảnh và panel
        int imageWidth = 120;
        int imageHeight = 120;
        int panelWidth = imageWidth + 10; // Thêm khoảng trắng giữa ảnh và mô tả
        int panelHeight = imageHeight + 20; // Đủ để chứa cả label mô tả

        // Tạo một mảng để lưu trữ tất cả các panel
        JPanel[][] imagePanels = new JPanel[dishList.size()][numImageColumns];

        // Điền mảng với panel từ danh sách Dish
        int rowCounter = 0;
        int columnCounter = 0;
        for (Dish dish : dishList) {
            // Tạo panel chứa ảnh và mô tả
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());

            // Tạo label chứa ảnh
            JLabel imageLabel = new JLabel();
            BufferedImage originalImage = null;
            String imagePath = "/image/image.png";
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
            Image scaledImage = HandleImage.getScaledImage(originalImage, 100, imageHeight);
            ImageIcon icon = new ImageIcon(scaledImage);
            imageLabel.setIcon(icon);

            // Tạo label chứa mô tả
            JLabel descriptionLabel = new JLabel(dish.getName());
            JLabel priceLabel = new JLabel("Price: " + dish.getPrice()); 

            imagePanel.add(descriptionLabel,BorderLayout.CENTER);
            imagePanel.add(imageLabel, BorderLayout.NORTH);

            imagePanel.add(priceLabel, BorderLayout.SOUTH);
            imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Sử dụng BoxLayout để sắp xếp các thành phần theo chiều dọc


            // Thêm panel vào mảng
            imagePanels[rowCounter][columnCounter] = imagePanel;

            columnCounter++;
            if (columnCounter == numImageColumns) {
                columnCounter = 0;
                rowCounter++;
            }
        }

        // Thêm dữ liệu vào mô hình bảng
        for (int row = 0; row < imagePanels.length; row++) {
            proModel.addRow(imagePanels[row]);
        }

        // Đặt renderer cho tất cả các cột chứa hình ảnh và mô tả
        for (int i = 0; i < numImageColumns; i++) {
            prodctTable.getColumnModel().getColumn(i).setCellRenderer(new ImageCellRenderer());
        }

        // Đặt chiều cao của dòng trong bảng
        prodctTable.setRowHeight(panelHeight + 20);
//        DefaultTableModel previewModel = (DefaultTableModel) previewBillTable.getModel();
//        previewModel.setRowCount(0);
//        prodctTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int row = prodctTable.rowAtPoint(e.getPoint());
//                int column = prodctTable.columnAtPoint(e.getPoint());
//                // Kiểm tra xem dòng và cột có hợp lệ không
//                if (row >= 0 && column >= 0 && row < dishList.size() && column < numImageColumns) {
//                    Dish selectedDish = dishList.get(row * numImageColumns + column);
//
//                    // Hiển thị thông tin vào bảng previewBillTable
//
//
//                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//                    for (int i= 0; i<previewBillTable.getColumnCount(); i++){
//                        previewBillTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//                    }
//                    // Thêm thông tin của selectedDish vào bảng previewBillTable
//                    previewModel.addRow(new Object[]{selectedDish.getId(),selectedDish.getName(), selectedDish.getPrice()});
//                }
//            }
//        });
    }



    class ImageCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                return (JLabel) value;
            }
            return (Component) value;
        }
    }
    
    public void productMouseListener(){
        List<Dish> dishList = DishDAO.getAll( );;
        DefaultTableModel previewModel = (DefaultTableModel) previewBillTable.getModel();
        int numImageColumns = 5;
        previewModel.setRowCount(0);
        
        MouseListener[] mouseListeners = prodctTable.getMouseListeners();
        for (MouseListener listener : mouseListeners) {
            prodctTable.removeMouseListener(listener);
        }
        
        prodctTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = prodctTable.rowAtPoint(e.getPoint());
                int column = prodctTable.columnAtPoint(e.getPoint());
                // Kiểm tra xem dòng và cột có hợp lệ không
                if (row >= 0 && column >= 0 && row < dishList.size() && column < numImageColumns) {
                    Dish selectedDish = dishList.get(row * numImageColumns + column);

                    // Hiển thị thông tin vào bảng previewBillTable


                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                    for (int i= 0; i<previewBillTable.getColumnCount(); i++){
                        previewBillTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    // Thêm thông tin của selectedDish vào bảng previewBillTable
                    previewModel.addRow(new Object[]{selectedDish.getId(),selectedDish.getName(), selectedDish.getPrice()});
                }
            }
        });
    }
    
    public void getProductByCateMouseListener(String catergory){
        List<Dish> dishList = DishDAO.getByCategory(catergory);;
        DefaultTableModel previewModel = (DefaultTableModel) previewBillTable.getModel();
        int numImageColumns = 5;
       
        MouseListener[] mouseListeners = prodctTable.getMouseListeners();
        for (MouseListener listener : mouseListeners) {
            prodctTable.removeMouseListener(listener);
        }
       
        prodctTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = prodctTable.rowAtPoint(e.getPoint());
                int column = prodctTable.columnAtPoint(e.getPoint());
                // Kiểm tra xem dòng và cột có hợp lệ không
                if (row >= 0 && column >= 0 && row < dishList.size() && column < numImageColumns) {
                    Dish selectedDish = dishList.get(row * numImageColumns + column);

                    // Hiển thị thông tin vào bảng previewBillTable


                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                    for (int i= 0; i<previewBillTable.getColumnCount(); i++){
                        previewBillTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    // Thêm thông tin của selectedDish vào bảng previewBillTable
                    previewModel.addRow(new Object[]{selectedDish.getId(),selectedDish.getName(), selectedDish.getPrice()});
                }
            }
        });
    }

    
    public void MouseClickListener(){
        MouseListener[] mouseListeners = prodctTable.getMouseListeners();
        for (MouseListener listener : mouseListeners) {
            prodctTable.removeMouseListener(listener);
        }
        allCateLabel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                productHandleTable();
            }
        });
        
        catergList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                JList<String> cateList = (JList<String>) e.getSource();
                int index = cateList.locationToIndex(e.getPoint());
                List<String> selectedCatergory = cateList.getSelectedValuesList();
                if(index != -1){
                    for(String cate : selectedCatergory){
                        getProductByCatergory(cate);
                        getProductByCateMouseListener(cate);
                    }
                }    
            }    
        });
    }
    
    public void customComponents(){
        productHandleTable();
        MouseClickListener();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        menuBar2 = new java.awt.MenuBar();
        menu3 = new java.awt.Menu();
        menu4 = new java.awt.Menu();
        menuBar3 = new java.awt.MenuBar();
        menu5 = new java.awt.Menu();
        menu6 = new java.awt.Menu();
        menuBar4 = new java.awt.MenuBar();
        menu7 = new java.awt.Menu();
        menu8 = new java.awt.Menu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        newBtn = new javax.swing.JPanel();
        newIcon = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        billIcon = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        deliIcon2 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        takeIcon = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        dinInIcon2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        catergList = new javax.swing.JList<>();
        allCatePanel = new javax.swing.JPanel();
        allCateLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        prodctTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        previewBillTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        menu3.setLabel("File");
        menuBar2.add(menu3);

        menu4.setLabel("Edit");
        menuBar2.add(menu4);

        menu5.setLabel("File");
        menuBar3.add(menu5);

        menu6.setLabel("Edit");
        menuBar3.add(menu6);

        menu7.setLabel("File");
        menuBar4.add(menu7);

        menu8.setLabel("Edit");
        menuBar4.add(menu8);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 153, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/POS.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/off.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        newIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("New");

        javax.swing.GroupLayout newBtnLayout = new javax.swing.GroupLayout(newBtn);
        newBtn.setLayout(newBtnLayout);
        newBtnLayout.setHorizontalGroup(
            newBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newBtnLayout.createSequentialGroup()
                .addGroup(newBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newBtnLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(newIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(newBtnLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel3)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        newBtnLayout.setVerticalGroup(
            newBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newBtnLayout.createSequentialGroup()
                .addComponent(newIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        billIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bill.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Bill");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(billIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel5)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(billIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Delivery");

        deliIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deli.png"))); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(deliIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(deliIcon2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(14, 14, 14))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Take Away");

        takeIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/take.png"))); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(takeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(takeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(14, 14, 14))
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Din In");

        dinInIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dinIn.png"))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel14))
                    .addComponent(dinInIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(dinInIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        catergList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        catergList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        catergList.setToolTipText("");
        catergList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        catergList.setFixedCellHeight(25);
        catergList.setSelectionBackground(new java.awt.Color(242, 242, 242));
        catergList.setValueIsAdjusting(true);
        catergList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                catergListMouseClicked(evt);
            }
        });
        catergList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                catergListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(catergList);

        allCatePanel.setBackground(new java.awt.Color(255, 255, 255));
        allCatePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        allCatePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allCatePanelMouseClicked(evt);
            }
        });

        allCateLabel.setText("All Catergories");
        allCateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allCateLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout allCatePanelLayout = new javax.swing.GroupLayout(allCatePanel);
        allCatePanel.setLayout(allCatePanelLayout);
        allCatePanelLayout.setHorizontalGroup(
            allCatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, allCatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(allCateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        allCatePanelLayout.setVerticalGroup(
            allCatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(allCateLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
            .addComponent(allCatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(allCatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        prodctTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", ""
            }
        ));
        prodctTable.setRowHeight(120);
        prodctTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prodctTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(prodctTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        previewBillTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "SR", "Prdct Name", "Price", "Qty", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        previewBillTable.setGridColor(new java.awt.Color(255, 255, 255));
        previewBillTable.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(previewBillTable);
        if (previewBillTable.getColumnModel().getColumnCount() > 0) {
            previewBillTable.getColumnModel().getColumn(0).setMinWidth(28);
            previewBillTable.getColumnModel().getColumn(0).setPreferredWidth(28);
            previewBillTable.getColumnModel().getColumn(0).setMaxWidth(28);
            previewBillTable.getColumnModel().getColumn(1).setMinWidth(112);
            previewBillTable.getColumnModel().getColumn(1).setPreferredWidth(112);
            previewBillTable.getColumnModel().getColumn(1).setMaxWidth(112);
            previewBillTable.getColumnModel().getColumn(2).setMinWidth(56);
            previewBillTable.getColumnModel().getColumn(2).setPreferredWidth(56);
            previewBillTable.getColumnModel().getColumn(2).setMaxWidth(56);
            previewBillTable.getColumnModel().getColumn(3).setMinWidth(28);
            previewBillTable.getColumnModel().getColumn(3).setPreferredWidth(28);
            previewBillTable.getColumnModel().getColumn(3).setMaxWidth(28);
            previewBillTable.getColumnModel().getColumn(4).setMinWidth(56);
            previewBillTable.getColumnModel().getColumn(4).setPreferredWidth(56);
            previewBillTable.getColumnModel().getColumn(4).setMaxWidth(56);
        }

        jButton1.setText("Check out");

        jButton2.setText("Cancel");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jButton1)
                        .addGap(42, 42, 42)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 16, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1)
                                    .addComponent(jButton2))))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        POSForm posForm = new POSForm();
        posForm.setDefaultCloseOperation(DashBoardForm.DISPOSE_ON_CLOSE);
        this.setVisible(false);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void catergListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_catergListValueChanged
        List<String> list = DishService.getCategory();
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String cate : list){
            model.addElement(cate);
        }
        catergList.setModel(model);
        catergList.setCellRenderer(new CustomCellRenderer());
        catergList = new javax.swing.JList<>();
    }//GEN-LAST:event_catergListValueChanged

    private void catergListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_catergListMouseClicked

    }//GEN-LAST:event_catergListMouseClicked

    private void prodctTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prodctTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_prodctTableMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        DefaultTableModel previewModel = (DefaultTableModel) previewBillTable.getModel();
        previewModel.setRowCount(0); // Xóa dữ liệu cũ
    }//GEN-LAST:event_jButton2MouseClicked

    private void allCateLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allCateLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_allCateLabelMouseClicked

    private void allCatePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allCatePanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_allCatePanelMouseClicked
    private static class CustomCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Thiết lập viền cho JLabel
            label.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

            // Căn giữa nội dung của JLabel
            label.setHorizontalAlignment(JLabel.CENTER);

            return label;
        }
    }
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
            java.util.logging.Logger.getLogger(POSForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(POSForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(POSForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(POSForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new POSForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel allCateLabel;
    private javax.swing.JPanel allCatePanel;
    private javax.swing.JLabel billIcon;
    private javax.swing.JList<String> catergList;
    private javax.swing.JLabel deliIcon2;
    private javax.swing.JLabel dinInIcon2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.Menu menu3;
    private java.awt.Menu menu4;
    private java.awt.Menu menu5;
    private java.awt.Menu menu6;
    private java.awt.Menu menu7;
    private java.awt.Menu menu8;
    private java.awt.MenuBar menuBar1;
    private java.awt.MenuBar menuBar2;
    private java.awt.MenuBar menuBar3;
    private java.awt.MenuBar menuBar4;
    private javax.swing.JPanel newBtn;
    private javax.swing.JLabel newIcon;
    private javax.swing.JTable previewBillTable;
    private javax.swing.JTable prodctTable;
    private javax.swing.JLabel takeIcon;
    // End of variables declaration//GEN-END:variables
}
