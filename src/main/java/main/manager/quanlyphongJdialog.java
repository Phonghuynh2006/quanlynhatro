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

    @Override
public void chooseImage() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String fileName = file.getName();
        // Hiển thị ảnh
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image img = icon.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
        lblAnh.setIcon(new ImageIcon(img));
        
        // Gán tên ảnh vào biến hoặc text field nếu cần
//        tenFileAnh = fileName; // hoặc txtAnhPhong.setText(fileName);
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
        .moTa(jTextArea1.getText().trim()) // << dùng jTextArea1 cho mô tả
        .trangThai(trangThai)
        .anhPhong("") // nếu có ảnh
        .build();
}



@Override
public void setForm(Phong p) {
    txtMaPhong.setText(p.getMaPhong());
    txtGiaTien.setText(String.valueOf(p.getGiaTien()));
    txtDienTich.setText(String.valueOf(p.getDienTich()));
    txtDiaChi.setText(p.getDiaChi());
    txtLienHe.setText(p.getLienHe());
    jTextArea1.setText(p.getMoTa()); // << hiển thị mô tả

    switch (p.getTrangThai()) {
        case "Trống" -> rdoTrong.setSelected(true);
        case "Đang thuê" -> rdoDangThue.setSelected(true);
        case "Đang sửa" -> rdoDangSua.setSelected(true);
    }

    if (p.getAnhPhong() != null) {
        loadImage(p.getAnhPhong());
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
        lblAnh.setIcon(null); // hoặc đặt ảnh mặc định
        return;
    }

    ImageIcon icon = new ImageIcon("images/" + imagePath); // thư mục chứa ảnh
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ));
        jScrollPane1.setViewportView(tblPhong);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        tabs.addTab("Danh Sách", jPanel1);

        lblAnh.setText("ảnh");

        jLabel2.setText("Mã phòng");

        txtMaPhong.setText("jTextField1");

        jLabel3.setText("Trạng thái");

        buttonGroup1.add(rdoTrong);
        rdoTrong.setText("trong");

        buttonGroup1.add(rdoDangThue);
        rdoDangThue.setText("rdoDangThue");

        buttonGroup1.add(rdoDangSua);
        rdoDangSua.setText("rdoDangSua");

        jLabel4.setText("Giá tiền");

        txtGiaTien.setText("jTextField2");

        jLabel5.setText("diện tích");

        txtDienTich.setText("jTextField3");

        jLabel6.setText("địa chỉ");

        jLabel7.setText("liện hệ");

        jLabel8.setText("mô tả");

        txtDiaChi.setText("jTextField4");

        txtLienHe.setText("jTextField5");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton1.setText("lưu");

        jButton2.setText("cập nhật");

        jButton3.setText("xóa");

        jButton4.setText("tạo mới");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtGiaTien, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                        .addComponent(txtMaPhong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rdoDangSua, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(rdoTrong, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rdoDangThue, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(77, 77, 77)
                                        .addComponent(jLabel5))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(69, 69, 69)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtLienHe, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDienTich, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(128, 128, 128)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap(279, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoTrong)
                            .addComponent(rdoDangThue))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoDangSua)
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGiaTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDienTich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLienHe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(21, 21, 21))
        );

        tabs.addTab("Biểu Mẫu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
