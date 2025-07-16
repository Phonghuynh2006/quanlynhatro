/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package main.manager;

import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import main.dao.PhongDAO;
import main.entity.Phong;
import main.impl.PhongDAOImpl;
import main.util.XDialog;
/**
 *
 * @author PHONG
 */
public class quanlyphongJdialog extends javax.swing.JDialog implements quanlyphongController{

    /**
     * Creates new form quanlyphongJdialog
     */
    public quanlyphongJdialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
                setLocationRelativeTo(null);
        loadTable();
    }
    @Override
public void close() {
    this.dispose();
}
@Override
public void open() {
    this.setLocationRelativeTo(null); // căn giữa
    this.setVisible(true);
}


@Override
public void editFromTable() {
    int row = tblPhong.getSelectedRow(); // tblPhong là JTable bạn đặt
    if (row >= 0) {
        String maPhong = (String) tblPhong.getValueAt(row, 0); // Cột 0 là Mã Phòng
        Phong p = dao.findById(maPhong); // dao là PhongDAOImpl đã khởi tạo

        if (p != null) {
            setForm(p);
            tabs.setSelectedIndex(1); // Hiển thị tab Biểu Mẫu
        }
    } else {
        XDialog.alert("Vui lòng chọn dòng cần sửa!");
    }
}


    private final PhongDAO dao = new PhongDAOImpl();
    int index = -1;

//    @Override
//public void chooseImage() {
//    JFileChooser fileChooser = new JFileChooser();
//    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//        File file = fileChooser.getSelectedFile();
//        String fileName = file.getName();
//        // Hiển thị ảnh
//        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
//        Image img = icon.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
//        lblAnh.setIcon(new ImageIcon(img));
//        
//        // Gán tên ảnh vào biến hoặc text field nếu cần
////        tenFileAnh = fileName; // hoặc txtAnhPhong.setText(fileName);
//    }
//}
@Override
public void chooseImage() {
    JFileChooser fileChooser = new JFileChooser("D:\\code\\java\\QUANLYNHATRO\\src\\main\\resources\\main\\icon");
    
    fileChooser.setDialogTitle("Chọn ảnh phòng");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String fileName = file.getName();

        // Hiển thị ảnh
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image img = icon.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
        lblAnh.setIcon(new ImageIcon(img));

        lblAnh.setName(fileName); // Lưu tên file để gán vào DB

        // (Tùy chọn) Xóa text nếu có dòng "Click để chọn ảnh"
        lblAnh.setText(null);
    }
}


    @Override
    public void loadTable() {
        DefaultTableModel model = (DefaultTableModel) tblPhong.getModel();
        model.setRowCount(0);
        try {
            List<Phong> list = dao.findAll();
            for (Phong p : list) {
                model.addRow(new Object[]{
                    p.getMaPhong(),
                    p.getTrangThai(),
                    p.getGiaTien(),
                    p.getDienTich(),
                    p.getDiaChi(),
                    p.getLienHe(),
                    p.getMoTa(),
                    p.getAnhPhong()
                });
            }
        } catch (Exception e) {
            XDialog.alert("Lỗi truy vấn dữ liệu!");
            e.printStackTrace();
        }
    }

@Override
public Phong getForm() {
    String trangThai = "";
    if (rdoTrong.isSelected()) {
        trangThai = "Trống";
    } else if (rdoDangThue.isSelected()) {
        trangThai = "Đang thuê";
    } else if (rdoDangSua.isSelected()) {
        trangThai = "Đang sửa";
    }

    return Phong.builder()
        .maPhong(txtMaPhong.getText().trim())
        .giaTien(Double.parseDouble(txtGiaTien.getText()))
        .dienTich(Double.parseDouble(txtDienTich.getText()))
        .diaChi(txtDiaChi.getText().trim())
        .lienHe(txtLienHe.getText().trim())
        .moTa(jTextArea1.getText().trim())
        .trangThai(trangThai)
        .anhPhong(lblAnh.getName() != null ? lblAnh.getName() : "") // ✅ Sửa ở đây
        .build();
}




@Override
public void setForm(Phong p) {
    txtMaPhong.setText(p.getMaPhong());
    txtGiaTien.setText(String.valueOf(p.getGiaTien()));
    txtDienTich.setText(String.valueOf(p.getDienTich()));
    txtDiaChi.setText(p.getDiaChi());
    txtLienHe.setText(p.getLienHe());
    jTextArea1.setText(p.getMoTa());

    switch (p.getTrangThai()) {
        case "Trống" -> rdoTrong.setSelected(true);
        case "Đang thuê" -> rdoDangThue.setSelected(true);
        case "Đang sửa" -> rdoDangSua.setSelected(true);
    }

    if (p.getAnhPhong() != null && !p.getAnhPhong().isEmpty()) {
        loadImage(p.getAnhPhong());
        lblAnh.setName(p.getAnhPhong()); // ✅ Gán lại tên ảnh vào lblAnh
    } else {
        lblAnh.setIcon(null);
        lblAnh.setName(null);
    }
}



@Override
public void clearForm() {
    txtMaPhong.setText("");
    buttonGroup1.clearSelection(); // clear lựa chọn radio button

    // Nếu muốn chọn mặc định là Trống:
    rdoTrong.setSelected(true);

    txtGiaTien.setText("");
    txtDienTich.setText("");
    txtDiaChi.setText("");
    txtLienHe.setText("");
    jTextArea1.setText(""); // mô tả
    index = -1;
}


    @Override
    public void insert() {
        if (!isValidForm()) return;
        Phong p = getForm();
        try {
            dao.create(p);
            loadTable();
            XDialog.alert("Thêm thành công!");
        } catch (Exception e) {
            XDialog.alert("Thêm thất bại!");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!isValidForm()) return;
        Phong p = getForm();
        try {
            dao.update(p);
            loadTable();
            XDialog.alert("Cập nhật thành công!");
        } catch (Exception e) {
            XDialog.alert("Cập nhật thất bại!");
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        String ma = txtMaPhong.getText();
        try {
            dao.deleteById(ma);
            loadTable();
            clearForm();
            XDialog.alert("Xóa thành công!");
        } catch (Exception e) {
            XDialog.alert("Xóa thất bại!");
            e.printStackTrace();
        }
    }

    @Override
    public void setTab(int index) {
        tabs.setSelectedIndex(index);
    }

    @Override
    public boolean isValidForm() {
        if (txtMaPhong.getText().isEmpty()) {
            XDialog.alert("Vui lòng nhập mã phòng!");
            return false;
        }
        if (txtGiaTien.getText().isEmpty() || txtDienTich.getText().isEmpty()) {
            XDialog.alert("Vui lòng nhập đầy đủ giá tiền và diện tích!");
            return false;
        }
        return true;
    }

    @Override
public void loadImage(String imagePath) {
    if (imagePath == null || imagePath.isEmpty()) {
        lblAnh.setIcon(null);
        return;
    }

    // Đường dẫn chính xác đến thư mục chứa ảnh
    File file = new File("D:\\code\\java\\QUANLYNHATRO\\src\\main\\resources\\main\\icon\\" + imagePath);
    if (!file.exists()) {
        lblAnh.setIcon(null);
        System.out.println("Không tìm thấy ảnh: " + file.getAbsolutePath()); // debug
        return;
    }

    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
    Image img = icon.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
    lblAnh.setIcon(new ImageIcon(img));
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPhong = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lblAnh = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaPhong = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        rdoTrong = new javax.swing.JRadioButton();
        rdoDangThue = new javax.swing.JRadioButton();
        rdoDangSua = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        txtGiaTien = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDienTich = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        txtLienHe = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnluu = new javax.swing.JButton();
        btncapnhat = new javax.swing.JButton();
        btnxoa = new javax.swing.JButton();
        btntaomoi = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Phòng", "Trạng Thái", "Giá Thuê", "Diện Tích", "Địa CHỉ", "Liên Hệ", "Mô Tả", "Ảnh PHòng", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblPhong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhongMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPhong);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
        );

        tabs.addTab("Danh Sách", jPanel1);

        lblAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/manager/1.jpg"))); // NOI18N
        lblAnh.setText("ảnh");
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        jLabel2.setText("Mã phòng");

        jLabel3.setText("Trạng thái");

        buttonGroup1.add(rdoTrong);
        rdoTrong.setText("Trống");

        buttonGroup1.add(rdoDangThue);
        rdoDangThue.setText("Đang Thuê");

        buttonGroup1.add(rdoDangSua);
        rdoDangSua.setText("Đang Sửa");

        jLabel4.setText("Giá Tiền");

        jLabel5.setText("Diện Tích");

        jLabel6.setText("Địa Chỉ");

        jLabel7.setText("Liện Hệ");

        jLabel8.setText("Mô Tả");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        btnluu.setText("lưu");
        btnluu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnluuActionPerformed(evt);
            }
        });

        btncapnhat.setText("cập nhật");
        btncapnhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncapnhatActionPerformed(evt);
            }
        });

        btnxoa.setText("xóa");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        btntaomoi.setText("tạo mới");
        btntaomoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntaomoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaTien, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaPhong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(rdoTrong)
                                                .addGap(8, 8, 8)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rdoDangThue)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rdoDangSua))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel7))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(txtDienTich))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(txtLienHe))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addGap(95, 95, 95))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(btnluu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btncapnhat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnxoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btntaomoi)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTrong)
                            .addComponent(rdoDangThue)
                            .addComponent(rdoDangSua))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGiaTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDienTich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLienHe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnluu, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btncapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnxoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btntaomoi, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Biểu Mẫu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnluuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnluuActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnluuActionPerformed

    private void btncapnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncapnhatActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btncapnhatActionPerformed

    private void btnxoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnxoaActionPerformed

    private void btntaomoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntaomoiActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btntaomoiActionPerformed

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        // TODO add your handling code here:
        chooseImage();
    }//GEN-LAST:event_lblAnhMouseClicked

    private void tblPhongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhongMouseClicked
        // TODO add your handling code here:
                if (evt.getClickCount() == 2) { 
            this.editFromTable();
                    } 
    }//GEN-LAST:event_tblPhongMouseClicked

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
            java.util.logging.Logger.getLogger(quanlyphongJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(quanlyphongJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(quanlyphongJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(quanlyphongJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                quanlyphongJdialog dialog = new quanlyphongJdialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncapnhat;
    private javax.swing.JButton btnluu;
    private javax.swing.JButton btntaomoi;
    private javax.swing.JButton btnxoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JRadioButton rdoDangSua;
    private javax.swing.JRadioButton rdoDangThue;
    private javax.swing.JRadioButton rdoTrong;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblPhong;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDienTich;
    private javax.swing.JTextField txtGiaTien;
    private javax.swing.JTextField txtLienHe;
    private javax.swing.JTextField txtMaPhong;
    // End of variables declaration//GEN-END:variables

}
