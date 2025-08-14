/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package App.Main.NguoiDung;

// ====== thêm vào đầu class ======
import App.Utils.XJdbc;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author WINDOWS
 */
public class MainNguoiDung extends javax.swing.JFrame implements MainNguoiDungController{
// 
//        public MainNguoiDung() {
//                initComponents();
//
//            }
//        
private static final java.math.BigDecimal DON_GIA_DIEN = new java.math.BigDecimal("3500");
private static final java.math.BigDecimal DON_GIA_NUOC = new java.math.BigDecimal("10000");

    
    // ===== user hiện tại (truyền từ màn đăng nhập) =====
    private final int currentUserId;
    private final String currentUserName;

    // ===== model cho bảng =====
    private DefaultTableModel model;

public MainNguoiDung() { this(0, "Họ tên"); }
public MainNguoiDung(int userId, String hoTen) {
    this.currentUserId = userId;
    this.currentUserName = (hoTen != null ? hoTen : "Họ tên");
    initComponents();
    afterInit();
}

    // ===== khởi tạo sau initComponents() =====
    private void afterInit() {

model = (javax.swing.table.DefaultTableModel) tblPhongTrong.getModel();
model.setColumnIdentifiers(new Object[]{"ID Phòng","Tên phòng","Diện tích (m²)","Giá phòng","Địa chỉ","Mô tả"});
btnDangKy.addActionListener(e -> dangKyPhong());
tblPhongTrong.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) tableRowClick(tblPhongTrong.getSelectedRow());
});
loadPhongTrong();
showHeaderInfo();

    }

    private void onDangKy(ActionEvent e) {
        dangKyPhong();
    }

    // ===== helper format =====
    private static String fmtMoney(BigDecimal v) {
        if (v == null) return "0";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(0);
        return nf.format(v);
    }

    private static String fmtArea(BigDecimal v) {
        if (v == null) return "";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);
        return nf.format(v);
    }

@Override
public void loadPhongTrong() {
    model.setRowCount(0);
    var sql = """
        SELECT maPhong, dienTich, giaTien, diaChi, moTa
        FROM Phong WHERE trangThai = N'Trống' ORDER BY giaTien ASC
    """;
    try (var rs = App.Utils.XJdbc.executeQuery(sql)) {
        while (rs.next()) {
            var mp  = rs.getString("maPhong");
            var dt  = (java.math.BigDecimal) rs.getObject("dienTich");
            var gia = (java.math.BigDecimal) rs.getObject("giaTien");
            model.addRow(new Object[]{ mp, "Phòng "+mp, fmtArea(dt), fmtMoney(gia), rs.getString("diaChi"), rs.getString("moTa")});
        }
    } catch (Exception ex) {
        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải phòng trống: " + ex.getMessage());
    }
}


@Override
public void showHeaderInfo() {
    lblTenNguoiThue.setText(currentUserName);
    var sqlHD = """
        SELECT TOP 1 maPhong FROM HopDong
        WHERE maNguoiDung=? AND CONVERT(date,GETDATE()) BETWEEN CONVERT(date,ngayBatDau) AND CONVERT(date,ngayKetThuc)
        ORDER BY ngayBatDau DESC
    """;
    try (var rs = App.Utils.XJdbc.executeQuery(sqlHD, currentUserId)) {
        lblIDPhong.setText(rs.next() ? rs.getString("maPhong") : "XX");
    } catch (Exception ex) { lblIDPhong.setText("XX"); }
    var now = java.time.LocalDate.now();
    lblThang.setText(String.format("THÁNG %02d/%d", now.getMonthValue(), now.getYear()));
}

@Override
public void tableRowClick(int row) {
    if (row < 0) return;
    txtIDPhong.setText(String.valueOf(model.getValueAt(row, 0)));
}


@Override
public void dangKyPhong() {
    var maPhong = txtIDPhong.getText().trim();
    if (maPhong.isEmpty()) { javax.swing.JOptionPane.showMessageDialog(this,"Hãy nhập/chọn ID phòng."); return; }

    java.math.BigDecimal giaPhong = null; String trangThai = null;
    try (var rs = App.Utils.XJdbc.executeQuery("SELECT giaTien,trangThai FROM Phong WHERE maPhong=?", maPhong)) {
        if (rs.next()) { giaPhong = (java.math.BigDecimal) rs.getObject("giaTien"); trangThai = rs.getString("trangThai"); }
    } catch (Exception ex) { javax.swing.JOptionPane.showMessageDialog(this, "Lỗi lấy phòng: "+ex.getMessage()); return; }
    if (giaPhong == null) { javax.swing.JOptionPane.showMessageDialog(this,"Không tìm thấy phòng "+maPhong); return; }
    if (!"Trống".equalsIgnoreCase(trangThai)) { javax.swing.JOptionPane.showMessageDialog(this,"Phòng không còn trống."); return; }

    var dlg = new App.Main.NguoiDung.DangKyPhongJDialog(this, true, maPhong, giaPhong,
            DON_GIA_DIEN, DON_GIA_NUOC, currentUserId, currentUserName);
    dlg.setLocationRelativeTo(this);
    dlg.setVisible(true);
    if (dlg.isAccepted()) { resetMiniForm(); loadPhongTrong(); showHeaderInfo(); }
}

    @Override
    public void resetMiniForm() {
        txtIDPhong.setText("");
        tblPhongTrong.clearSelection();
    }





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnView = new javax.swing.JPanel();
        lblNull = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPhongTrong = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblThang = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtIDPhong = new javax.swing.JTextField();
        btnDangKy = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblIDPhong = new javax.swing.JLabel();
        lblTenNguoiThue = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuTrangChu = new javax.swing.JMenu();
        jMenuHopDong = new javax.swing.JMenu();
        jMenuHoaDon = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenu();
        jMenuNguoiDung = new javax.swing.JMenu();
        jMenuThongTin = new javax.swing.JMenu();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuLichSuHD = new javax.swing.JMenu();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        btnThoat = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        btnDangXuat = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jpnView.setBackground(new java.awt.Color(40, 46, 62));
        jpnView.setPreferredSize(new java.awt.Dimension(1100, 630));
        jpnView.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jpnView.add(lblNull, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        tblPhongTrong.setBackground(new java.awt.Color(207, 243, 243));
        tblPhongTrong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPhongTrong.setGridColor(new java.awt.Color(255, 255, 255));
        tblPhongTrong.setSelectionBackground(new java.awt.Color(40, 46, 62));
        tblPhongTrong.setSelectionForeground(new java.awt.Color(255, 205, 31));
        tblPhongTrong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhongTrongMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPhongTrong);

        jpnView.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 206, 971, 400));

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        lblThang.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblThang.setForeground(new java.awt.Color(255, 205, 31));
        lblThang.setText("MỚI NHẤT");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("HÓA ĐƠN");

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 205, 31));
        lblTongTien.setText("XXXXXXX");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblThang)
                .addGap(64, 64, 64))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(lblTongTien)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblThang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTongTien)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jpnView.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(721, 23, -1, -1));

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("ID Phòng:");

        txtIDPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtIDPhong.setForeground(new java.awt.Color(40, 46, 62));

        btnDangKy.setBackground(new java.awt.Color(0, 51, 255));
        btnDangKy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDangKy.setForeground(new java.awt.Color(40, 46, 62));
        btnDangKy.setText("Đăng ký");
        btnDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangKyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtIDPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnDangKy)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtIDPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDangKy))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jpnView.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 23, -1, -1));

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("ID phòng:");
        jpnView.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 97, -1, -1));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Tên người thuê:");
        jpnView.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 123, -1, -1));

        lblIDPhong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblIDPhong.setForeground(new java.awt.Color(255, 51, 0));
        lblIDPhong.setText("XX");
        jpnView.add(lblIDPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 97, -1, -1));

        lblTenNguoiThue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTenNguoiThue.setForeground(new java.awt.Color(255, 51, 0));
        lblTenNguoiThue.setText("Họ tên");
        jpnView.add(lblTenNguoiThue, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 123, -1, -1));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("DANH SÁCH PHÒNG TRỐNG");
        jpnView.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 180, -1, -1));

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/nen02.jpg"))); // NOI18N
        jpnView.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 630));

        jMenuBar1.setBackground(new java.awt.Color(46, 56, 86));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));
        jMenuBar1.setForeground(new java.awt.Color(40, 46, 62));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(615, 70));

        jMenuTrangChu.setForeground(new java.awt.Color(255, 255, 255));
        jMenuTrangChu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/home (1).png"))); // NOI18N
        jMenuTrangChu.setText("Trang chủ");
        jMenuTrangChu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuBar1.add(jMenuTrangChu);

        jMenuHopDong.setForeground(new java.awt.Color(255, 255, 255));
        jMenuHopDong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/rent (1).png"))); // NOI18N
        jMenuHopDong.setText("Hợp đồng");
        jMenuHopDong.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuHopDong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuHopDongMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuHopDong);

        jMenuHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        jMenuHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/bill (1).png"))); // NOI18N
        jMenuHoaDon.setText("Hóa đơn");
        jMenuHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenuHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuHoaDonMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuHoaDon);

        jMenuAbout.setForeground(new java.awt.Color(255, 255, 255));
        jMenuAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/info (1).png"))); // NOI18N
        jMenuAbout.setText("About");
        jMenuAbout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        jMenuNguoiDung.setForeground(new java.awt.Color(40, 46, 62));
        jMenuNguoiDung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/user (1).png"))); // NOI18N
        jMenuNguoiDung.setText("Người dùng");
        jMenuNguoiDung.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuThongTin.setForeground(new java.awt.Color(40, 46, 62));
        jMenuThongTin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/user (1).png"))); // NOI18N
        jMenuThongTin.setText("Thông tin cá nhân");
        jMenuThongTin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuThongTin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuThongTinMouseClicked(evt);
            }
        });
        jMenuNguoiDung.add(jMenuThongTin);
        jMenuNguoiDung.add(jSeparator5);

        jMenuLichSuHD.setForeground(new java.awt.Color(40, 46, 62));
        jMenuLichSuHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/history.png"))); // NOI18N
        jMenuLichSuHD.setText("Lịch sử hoạt động");
        jMenuLichSuHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuLichSuHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuLichSuHDMouseClicked(evt);
            }
        });
        jMenuNguoiDung.add(jMenuLichSuHD);

        jMenuAbout.add(jMenuNguoiDung);
        jMenuAbout.add(jSeparator4);

        btnThoat.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        btnThoat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThoat.setForeground(new java.awt.Color(40, 46, 62));
        btnThoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/logout (1).png"))); // NOI18N
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });
        jMenuAbout.add(btnThoat);
        jMenuAbout.add(jSeparator6);

        btnDangXuat.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(40, 46, 62));
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TroViet/Icon/return.png"))); // NOI18N
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        jMenuAbout.add(btnDangXuat);

        jMenuBar1.add(jMenuAbout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn muốn đóng ứng dụng?",
                "Thoát",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) System.exit(0);
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn muốn đăng xuất?",
                "Đăng xuất",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            // TODO: mở màn hình đăng nhập
            // new DangNhapJFrame().setVisible(true);
        }
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void tblPhongTrongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhongTrongMouseClicked
        // TODO add your handling code here:
        int row = tblPhongTrong.getSelectedRow();
        if (row >= 0) tableRowClick(row);
    }//GEN-LAST:event_tblPhongTrongMouseClicked

    private void btnDangKyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangKyActionPerformed
        // TODO add your handling code here:
        dangKyPhong();
    }//GEN-LAST:event_btnDangKyActionPerformed

    private void jMenuHopDongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuHopDongMouseClicked
        // TODO add your handling code here:
                        this.showHopDongJDialog(this); 
    }//GEN-LAST:event_jMenuHopDongMouseClicked

    private void jMenuHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuHoaDonMouseClicked
        // TODO add your handling code here:
                        this.showHoaDonJDialog(this); 
//                            new App.Main.NguoiDung.HoaDonJDialog(this, true).setVisible(true);
    }//GEN-LAST:event_jMenuHoaDonMouseClicked

    private void jMenuThongTinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuThongTinMouseClicked
        // TODO add your handling code here:
//                        this.showThongTinNguoiDungJDialog(this); 
                        // cần import App.Utils.XAuth;
String username = (App.Utils.XAuth.user != null) ? App.Utils.XAuth.user.getTenTaiKhoan() : null;
new App.Main.ThongTinNguoiDungJDialog(this, true, username).setVisible(true);

    }//GEN-LAST:event_jMenuThongTinMouseClicked

    private void jMenuLichSuHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuLichSuHDMouseClicked
        // TODO add your handling code here:
                        this.showLichSuNguoiDungJDialog(this); 
    }//GEN-LAST:event_jMenuLichSuHDMouseClicked

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
                if ("FlatLaf Light".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainNguoiDung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainNguoiDung().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangKy;
    private javax.swing.JMenuItem btnDangXuat;
    private javax.swing.JMenuItem btnThoat;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenuAbout;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuHoaDon;
    private javax.swing.JMenu jMenuHopDong;
    private javax.swing.JMenu jMenuLichSuHD;
    private javax.swing.JMenu jMenuNguoiDung;
    private javax.swing.JMenu jMenuThongTin;
    private javax.swing.JMenu jMenuTrangChu;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPanel jpnView;
    private javax.swing.JLabel lblIDPhong;
    private javax.swing.JLabel lblNull;
    private javax.swing.JLabel lblTenNguoiThue;
    private javax.swing.JLabel lblThang;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTable tblPhongTrong;
    private javax.swing.JTextField txtIDPhong;
    // End of variables declaration//GEN-END:variables
}
