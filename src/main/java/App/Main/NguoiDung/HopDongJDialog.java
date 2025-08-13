/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package App.Main.NguoiDung;

import App.Utils.XAuth;
import App.Utils.XJdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
/**
 *
 * @author PHONG
 */
public class HopDongJDialog extends javax.swing.JDialog {

    /**
     * Creates new form HopDongJDialog
     */
//    public HopDongJDialog(java.awt.Frame parent, boolean modal) {
//        super(parent, modal);
//        initComponents();
//    }

    // Đơn giá mặc định (VND)
    private static final BigDecimal DON_GIA_DIEN = new BigDecimal("3500");   // /kWh
    private static final BigDecimal DON_GIA_NUOC = new BigDecimal("10000");  // /m3

    private DefaultTableModel model;
    private final NumberFormat moneyFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public HopDongJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        afterInit();
    }

    /* ================= lifecycle ================= */

    private void afterInit() {
        moneyFmt.setMaximumFractionDigits(0);

        // Khoá các ô chi tiết (vì chỉ xem)
        txtMPT.setEditable(false);
        txtMaNguoiDung.setEditable(false);
        txtGiaPhong.setEditable(false);
        txtGiaDien.setEditable(false);
        txtGiaNuoc.setEditable(false);
        jdcNBDT.setEnabled(false);
        jdcNKTT.setEnabled(false);

        // Model bảng
        model = (DefaultTableModel) tbthopdong.getModel();
        model.setColumnIdentifiers(new Object[]{
                "Mã HĐ", "Phòng", "Bắt đầu", "Kết thúc", "Giá phòng", "Trạng thái"
        });

        tbthopdong.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { onRowClick(); }
        });

        loadHopDongCuaToi();
        clearDetail();
        setLocationRelativeTo(getOwner());
    }

    /* ================= data ================= */

    private void loadHopDongCuaToi() {
        model.setRowCount(0);

        if (XAuth.user == null || XAuth.user.getMaNguoiDung() == null) {
            JOptionPane.showMessageDialog(this, "Không xác định được tài khoản đang đăng nhập.");
            return;
        }
        Integer userId = XAuth.user.getMaNguoiDung();

        final String sql = """
            SELECT h.maHopDong, h.maPhong, h.maNguoiDung,
                   h.ngayBatDau, h.ngayKetThuc, p.giaTien
            FROM HopDong h
            JOIN Phong p ON p.maPhong = h.maPhong
            WHERE h.maNguoiDung = ?
            ORDER BY h.ngayBatDau DESC
        """;

        try (ResultSet rs = XJdbc.executeQuery(sql, userId)) {
            while (rs.next()) {
                String maHopDong = rs.getString("maHopDong");
                String maPhong   = rs.getString("maPhong");
                java.sql.Date nbd = rs.getDate("ngayBatDau");
                java.sql.Date nkt = rs.getDate("ngayKetThuc");
                BigDecimal giaPhong = (BigDecimal) rs.getObject("giaTien");

                String trangThai = tinhTrang(nbd, nkt);
                model.addRow(new Object[]{
                        maHopDong,
                        maPhong,
                        nbd,
                        nkt,
                        moneyFmt.format(giaPhong),
                        trangThai
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải hợp đồng: " + ex.getMessage());
        }
    }

    private String tinhTrang(java.util.Date nbd, java.util.Date nkt) {
        LocalDate today = LocalDate.now();
        LocalDate bd = nbd == null ? null : new java.sql.Date(nbd.getTime()).toLocalDate();
        LocalDate kt = nkt == null ? null : new java.sql.Date(nkt.getTime()).toLocalDate();

        if (bd == null || kt == null) return "Chưa cập nhật";
        if (today.isBefore(bd)) return "Chưa hiệu lực";
        if ((today.isEqual(bd) || today.isAfter(bd)) && (today.isEqual(kt) || today.isBefore(kt)))
            return "Đang hiệu lực";
        return "Hết hạn";
    }

    private void onRowClick() {
        int row = tbthopdong.getSelectedRow();
        if (row < 0) return;

        String maHD = String.valueOf(model.getValueAt(row, 0));

        final String sql = """
            SELECT h.maHopDong, h.maPhong, h.maNguoiDung,
                   h.ngayBatDau, h.ngayKetThuc, p.giaTien
            FROM HopDong h
            JOIN Phong p ON p.maPhong = h.maPhong
            WHERE h.maNguoiDung = ? AND h.maHopDong = ?
        """;

        try (ResultSet rs = XJdbc.executeQuery(sql,
                XAuth.user.getMaNguoiDung(), maHD)) {
            if (rs.next()) {
                // đổ chi tiết
lblMaHopDong.setText(rs.getString("maHopDong") == null ? "" : rs.getString("maHopDong"));
txtMPT.setText(rs.getString("maPhong") == null ? "" : rs.getString("maPhong"));

                txtMaNguoiDung.setText(String.valueOf(rs.getInt("maNguoiDung")));
                jdcNBDT.setDate(rs.getDate("ngayBatDau"));
                jdcNKTT.setDate(rs.getDate("ngayKetThuc"));

                BigDecimal giaPhong = (BigDecimal) rs.getObject("giaTien");
                txtGiaPhong.setText(moneyFmt.format(giaPhong));
                txtGiaDien.setText(moneyFmt.format(DON_GIA_DIEN));
                txtGiaNuoc.setText(moneyFmt.format(DON_GIA_NUOC));

                lblStatus.setText(tinhTrang(rs.getDate("ngayBatDau"), rs.getDate("ngayKetThuc")));
            } else {
                clearDetail();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết HĐ: " + ex.getMessage());
        }
    }

    private void clearDetail() {
        lblMaHopDong.setText("00");
        txtMPT.setText("");
        txtMaNguoiDung.setText("");
        jdcNBDT.setDate(null);
        jdcNKTT.setDate(null);
        txtGiaPhong.setText("");
        txtGiaDien.setText(moneyFmt.format(DON_GIA_DIEN));
        txtGiaNuoc.setText(moneyFmt.format(DON_GIA_NUOC));
        lblStatus.setText("Chưa cập nhật");
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMPT = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMaNguoiDung = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jdcNBDT = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jdcNKTT = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        txtGiaPhong = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtGiaDien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtGiaNuoc = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lbl = new javax.swing.JLabel();
        lblMaHopDong = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbthopdong = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(40, 46, 62));
        jPanel1.setPreferredSize(new java.awt.Dimension(1100, 630));

        jPanel2.setBackground(new java.awt.Color(207, 243, 243));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(40, 46, 62));
        jLabel2.setText("Mã phòng trọ:");

        txtMPT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(40, 46, 62));
        jLabel3.setText("Mã người dùng:");

        txtMaNguoiDung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(40, 46, 62));
        jLabel4.setText("Ngày BĐ thuê:");

        jdcNBDT.setForeground(new java.awt.Color(40, 46, 62));
        jdcNBDT.setDateFormatString("dd-MM-yyyy");
        jdcNBDT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(40, 46, 62));
        jLabel5.setText("Ngày KT thuê:");

        jdcNKTT.setForeground(new java.awt.Color(40, 46, 62));
        jdcNKTT.setDateFormatString("dd-MM-yyyy");
        jdcNKTT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(40, 46, 62));
        jLabel7.setText("Giá phòng:");

        txtGiaPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(40, 46, 62));
        jLabel8.setText("Giá điện:");

        txtGiaDien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(40, 46, 62));
        jLabel9.setText("Giá nước");

        txtGiaNuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jPanel4.setBackground(new java.awt.Color(46, 56, 86));

        lbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbl.setForeground(new java.awt.Color(255, 205, 31));
        lbl.setText("MÃ HĐ:");

        lblMaHopDong.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaHopDong.setForeground(new java.awt.Color(255, 205, 31));
        lblMaHopDong.setText("00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMaHopDong)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl)
                    .addComponent(lblMaHopDong))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(40, 46, 62));
        jLabel14.setText("HỢP ĐỒNG");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jdcNBDT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdcNKTT, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(txtGiaPhong, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMPT, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaNguoiDung)
                            .addComponent(txtGiaNuoc, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaDien, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMPT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaNguoiDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jdcNBDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdcNKTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtGiaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtGiaDien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtGiaNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(46, 56, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tình trạng hợp đồng");

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(255, 205, 31));
        lblStatus.setText("Chưa cập nhập");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addGap(41, 41, 41)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Quý khách có nhu cầu chấm dứt hợp đồng trước thời hạn, hoặc muốn đổi phòng, vui lòng liên hệ với chủ trọ để được hướng dẫn cụ thể. Cám ơn!");

        tbthopdong.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbthopdong);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(63, 63, 63)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel10)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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
            java.util.logging.Logger.getLogger(HopDongJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HopDongJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HopDongJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HopDongJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HopDongJDialog dialog = new HopDongJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcNBDT;
    private com.toedter.calendar.JDateChooser jdcNKTT;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblMaHopDong;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tbthopdong;
    private javax.swing.JTextField txtGiaDien;
    private javax.swing.JTextField txtGiaNuoc;
    private javax.swing.JTextField txtGiaPhong;
    private javax.swing.JTextField txtMPT;
    private javax.swing.JTextField txtMaNguoiDung;
    // End of variables declaration//GEN-END:variables
}
