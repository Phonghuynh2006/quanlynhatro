/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package App.Main.ChuTro;

import App.DAO.HoaDonDAO;
import App.DAO.HopDongDAO;
import App.DAO.PhongDAO;
import App.Entity.HoaDon;
import App.Entity.HopDong;
import App.Entity.Phong;
import App.Impl.HoaDonDAOImpl;
import App.Impl.HopDongDAOImpl;
import App.Impl.PhongDAOImpl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
// ở đầu file (phần import), thêm:
import java.math.RoundingMode;
import java.text.NumberFormat;
import javax.swing.JDialog;

/**
 *
 * @author PHONG
 */
public class HoaDonJDialog extends javax.swing.JDialog implements HoaDonController{

    /**
     * Creates new form HoaDonJDialog
     */
    public HoaDonJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        afterInit();
                setLocationRelativeTo(null);
//    // Căn giữa màn hình
//    setLocationRelativeTo(null);
//    // Ẩn nút Maximize
//    setResizable(false);
//    // Tuỳ chọn: ẩn luôn nút Close (X) nếu muốn
//    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    // ===== DAO & STATE =====
    private final HoaDonDAO hoaDonDAO = new HoaDonDAOImpl();
    private final HopDongDAO hopDongDAO = new HopDongDAOImpl();
    private final PhongDAO  phongDAO   = new PhongDAOImpl();
    private DefaultTableModel model;

    // Đơn giá mặc định (VND) để tính tiền điện, nước từ "số điện/số nước"
    private static final BigDecimal DON_GIA_DIEN = new BigDecimal("3500");   // /kWh
    private static final BigDecimal DON_GIA_NUOC = new BigDecimal("10000");  // /m3

    // ===== INIT =====
    private void afterInit() {
        model = (DefaultTableModel) tblHoaDon.getModel();
        model.setColumnIdentifiers(new Object[]{
            "Mã hóa đơn","Mã hợp đồng","Ngày tạo","Ngày thanh toán",
            "Số điện","Tiền điện","Số nước","Tiền nước","Giá phòng","Tổng cộng","Trạng thái"
        });

        txtTimKiem.setText(PLACEHOLDER);


        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) tableRowClick(tblHoaDon.getSelectedRow());
        });

        txtIDHopDong.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { autoFillGiaPhong(); }
        });

        var recompute = new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { recomputeFromInputs(); }
        };
        txtSoDien.addKeyListener(recompute);
        txtSoNuoc.addKeyListener(recompute);
        txttienphong.addKeyListener(recompute);

        // mặc định ngày tạo = hôm nay
        jdcNgayTao.setDate(new Date());

        loadTable();
    }

    /* ================= Controller ================= */
@Override
public void loadTable() {
    model.setRowCount(0);
    List<HoaDon> list = hoaDonDAO.findAll();
    for (HoaDon h : list) {
        BigDecimal soDien = safeDiv(nz(h.getTienDien()), DON_GIA_DIEN);
        BigDecimal soNuoc = safeDiv(nz(h.getTienNuoc()), DON_GIA_NUOC);
        BigDecimal tong   = nz(h.getTienPhong()).add(nz(h.getTienDien())).add(nz(h.getTienNuoc()));

        model.addRow(new Object[]{
            h.getMaHoaDon(),
            h.getMaHopDong(),
            h.getNgayTaoHoaDon(),
            h.getNgayThanhToan(),
            fmtQty(soDien),              // Số điện
            fmtBD(h.getTienDien()),      // Tiền điện
            fmtQty(soNuoc),              // Số nước
            fmtBD(h.getTienNuoc()),      // Tiền nước
            fmtBD(h.getTienPhong()),     // Giá phòng
            fmtBD(tong),
            h.getTrangThai()
        });
    }
}


@Override
public void searchByHopDong(String kw) {
    String s = kw == null ? "" : kw.trim();
    if (s.isEmpty() || PLACEHOLDER.equals(s)) { loadTable(); return; }
    model.setRowCount(0);
    for (HoaDon h : hoaDonDAO.findByMaHopDong(s)) {
        BigDecimal soDien = safeDiv(nz(h.getTienDien()), DON_GIA_DIEN);
        BigDecimal soNuoc = safeDiv(nz(h.getTienNuoc()), DON_GIA_NUOC);
        BigDecimal tong   = nz(h.getTienPhong()).add(nz(h.getTienDien())).add(nz(h.getTienNuoc()));

        model.addRow(new Object[]{
            h.getMaHoaDon(),
            h.getMaHopDong(),
            h.getNgayTaoHoaDon(),
            h.getNgayThanhToan(),
            fmtQty(soDien),
            fmtBD(h.getTienDien()),
            fmtQty(soNuoc),
            fmtBD(h.getTienNuoc()),
            fmtBD(h.getTienPhong()),
            fmtBD(tong),
            h.getTrangThai()
        });
    }
}


    @Override
    public void clearForm() {
        lblIDHoaDon.setText("XX");
        txtIDHopDong.setText("");
        jdcNgayTao.setDate(new Date());
        jdcNgaythanhtoan.setDate(null);
        txtSoDien.setText("");
        txtSoNuoc.setText("");
        txttiendien.setText("");
        txttiennuoc.setText("");
        txttienphong.setText("");
        lblTongCong.setText("0");
        rbChuaThanhToan.setSelected(true);
        tblHoaDon.clearSelection();
        txtIDHopDong.requestFocus();
    }

    @Override
    public void setForm(HoaDon h) {
        lblIDHoaDon.setText(nvl(h.getMaHoaDon(), "XX"));
        txtIDHopDong.setText(nvl(h.getMaHopDong(), ""));
        jdcNgayTao.setDate(h.getNgayTaoHoaDon());
        jdcNgaythanhtoan.setDate(h.getNgayThanhToan());

        txttiendien.setText(fmtBD(h.getTienDien()));
        txttiennuoc.setText(fmtBD(h.getTienNuoc()));
        txttienphong.setText(fmtBD(h.getTienPhong()));

        BigDecimal tong = nz(h.getTienPhong()).add(nz(h.getTienDien())).add(nz(h.getTienNuoc()));
        lblTongCong.setText(fmtBD(tong));

        if ("Đã thanh toán".equalsIgnoreCase(String.valueOf(h.getTrangThai()))) rbDaThanhtoan.setSelected(true);
        else rbChuaThanhToan.setSelected(true);
    }

    @Override
    public HoaDon getForm() {
        if (txtIDHopDong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,"Nhập MÃ HỢP ĐỒNG"); return null;
        }
        HoaDon h = new HoaDon();
        h.setMaHoaDon("XX".equals(lblIDHoaDon.getText()) ? null : lblIDHoaDon.getText().trim());
        h.setMaHopDong(txtIDHopDong.getText().trim());

        h.setNgayTaoHoaDon(jdcNgayTao.getDate());
        h.setNgayThanhToan(jdcNgaythanhtoan.getDate());

        h.setTienPhong(parseBD(txttienphong.getText()));
        h.setTienDien(parseBD(txttiendien.getText()));
        h.setTienNuoc(parseBD(txttiennuoc.getText()));
        h.setTrangThai(rbDaThanhtoan.isSelected() ? "Đã thanh toán" : "Chưa thanh toán");

        // nếu chọn "Đã thanh toán" mà chưa có ngày TT -> set hôm nay
        if ("Đã thanh toán".equals(h.getTrangThai()) && h.getNgayThanhToan()==null) {
            h.setNgayThanhToan(new Date());
        }
        return h;
    }

    @Override
    public void add() {
        HoaDon h = getForm(); if (h==null) return;
        hoaDonDAO.create(h);
        loadTable(); clearForm();
        JOptionPane.showMessageDialog(this,"Thêm thành công");
    }

    @Override
    public void update() {
        HoaDon h = getForm(); if (h==null) return;
        if (h.getMaHoaDon()==null){ JOptionPane.showMessageDialog(this,"Chọn hóa đơn để sửa"); return; }
        hoaDonDAO.update(h);
        loadTable();
        JOptionPane.showMessageDialog(this,"Cập nhật thành công");
    }

    @Override
    public void delete() {
        int row = tblHoaDon.getSelectedRow();
        if (row<0){ JOptionPane.showMessageDialog(this,"Chọn hóa đơn để xóa"); return; }
        String id = model.getValueAt(row,0).toString();
        if (JOptionPane.showConfirmDialog(this,"Xóa hóa đơn "+id+"?","Xác nhận",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            hoaDonDAO.deleteById(id);
            loadTable(); clearForm();
            JOptionPane.showMessageDialog(this,"Đã xóa");
        }
    }

    @Override
    public void tableRowClick(int row) {
        if (row<0) return;
        String id = model.getValueAt(row,0).toString();
        HoaDon h = hoaDonDAO.findById(id);
        if (h!=null) setForm(h);
    }

    // === LẤY GIÁ PHÒNG TỪ HỢP ĐỒNG ===
    @Override
    public void autoFillGiaPhong() {
        String maHD = txtIDHopDong.getText().trim();
        if (maHD.isEmpty()) return;

        HopDong hd = hopDongDAO.findById(maHD);
        if (hd == null) { JOptionPane.showMessageDialog(this, "Không tìm thấy hợp đồng"); return; }

        String maPhong = hd.getMaPhong();
        if (maPhong == null || maPhong.isBlank()) {
            JOptionPane.showMessageDialog(this, "Hợp đồng chưa gán phòng");
            return;
        }

        Phong p = phongDAO.findById(maPhong);
        if (p == null) { JOptionPane.showMessageDialog(this, "Không tìm thấy phòng: " + maPhong); return; }

        txttienphong.setText(fmtBD(p.getGiaTien()));
        recomputeFromInputs();
    }

    // === TÍNH TIỀN TỪ SỐ ĐIỆN/NƯỚC + GIÁ PHÒNG ===
    @Override
    public void recomputeFromInputs() {
        BigDecimal soDien = parseBD(txtSoDien.getText()); // hiểu là số kWh
        BigDecimal soNuoc = parseBD(txtSoNuoc.getText()); // m3

        BigDecimal tienDien = soDien.multiply(DON_GIA_DIEN);
        BigDecimal tienNuoc = soNuoc.multiply(DON_GIA_NUOC);

        txttiendien.setText(fmtBD(tienDien));
        txttiennuoc.setText(fmtBD(tienNuoc));

        BigDecimal tienPhong = parseBD(txttienphong.getText());
        BigDecimal tong = tienPhong.add(tienDien).add(tienNuoc);
        lblTongCong.setText(fmtBD(tong));
    }

    // ===== Helpers =====
    private static final String PLACEHOLDER = "Nhập mã hợp đồng để tìm kiếm hóa đơn!";

    private BigDecimal parseBD(String s){
        try {
            if (s==null || s.trim().isEmpty()) return BigDecimal.ZERO;
            String normalized = s.replace(".", "").replace(",", "").trim();
            return new BigDecimal(normalized);
        } catch (Exception e){ return BigDecimal.ZERO; }
    }
    private String fmtBD(BigDecimal v){
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new Locale("vi","VN"));
        nf.setMaximumFractionDigits(0);
        return nf.format(v==null?BigDecimal.ZERO:v);
    }
    private BigDecimal nz(BigDecimal v){ return v==null?BigDecimal.ZERO:v; }
    // Trả về def nếu s == null, ngược lại trả về s
private String nvl(String s, String def) {
    return (s == null) ? def : s;
}



// ===== Helpers bổ sung =====
private BigDecimal safeDiv(BigDecimal a, BigDecimal b) {
    // Chia an toàn: nếu null hoặc b = 0 thì trả 0
    if (a == null || b == null || b.signum() == 0) return BigDecimal.ZERO;
    // làm tròn 0 chữ số (kWh/m3 là số nguyên). Muốn có lẻ thì đổi 0 -> 2 chẳng hạn
    return a.divide(b, 0, RoundingMode.HALF_UP);
}

private String fmtQty(BigDecimal v) {
    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi","VN"));
    nf.setMaximumFractionDigits(0);  // hiển thị số nguyên
    return nf.format(v == null ? BigDecimal.ZERO : v);
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblIDHoaDon = new javax.swing.JLabel();
        txtIDHopDong = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jdcNgayTao = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jdcNgaythanhtoan = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        txtSoDien = new javax.swing.JTextField();
        txttiendien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSoNuoc = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txttiennuoc = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        rbDaThanhtoan = new javax.swing.JRadioButton();
        rbChuaThanhToan = new javax.swing.JRadioButton();
        lblTongCong = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txttienphong = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(40, 46, 62));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Tìm kiếm ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimKiem.setForeground(new java.awt.Color(40, 46, 62));
        txtTimKiem.setText("Nhập mã hợp đồng để tìm kiếm hóa đơn!");
        txtTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKiemFocusLost(evt);
            }
        });

        btnTimKiem.setBackground(new java.awt.Color(0, 0, 255));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(40, 46, 62));
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 6, 900, -1));

        jPanel3.setBackground(new java.awt.Color(207, 243, 243));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(40, 46, 62));
        jLabel1.setText("HÓA ĐƠN");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 2, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(40, 46, 62));
        jLabel2.setText("Mã hóa đơn:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(352, 6, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(40, 46, 62));
        jLabel3.setText("Mã hợp đồng:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 36, -1, -1));

        lblIDHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblIDHoaDon.setForeground(new java.awt.Color(255, 205, 31));
        lblIDHoaDon.setText("XX");
        jPanel3.add(lblIDHoaDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2, -1, -1));

        txtIDHopDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtIDHopDong.setForeground(new java.awt.Color(40, 46, 62));
        jPanel3.add(txtIDHopDong, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 33, 140, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(40, 46, 62));
        jLabel5.setText("Ngày tạo:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 36, 90, -1));

        jdcNgayTao.setBackground(new java.awt.Color(255, 255, 255));
        jdcNgayTao.setForeground(new java.awt.Color(40, 46, 62));
        jdcNgayTao.setDateFormatString("dd-MM-yyyy");
        jdcNgayTao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel3.add(jdcNgayTao, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 37, 130, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(40, 46, 62));
        jLabel6.setText("Ngày thanh toán:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 67, -1, -1));

        jdcNgaythanhtoan.setBackground(new java.awt.Color(255, 255, 255));
        jdcNgaythanhtoan.setForeground(new java.awt.Color(40, 46, 62));
        jdcNgaythanhtoan.setDateFormatString("dd-MM-yyyy");
        jdcNgaythanhtoan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel3.add(jdcNgaythanhtoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 65, 140, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(40, 46, 62));
        jLabel8.setText("Số điện:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 96, -1, -1));

        txtSoDien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoDien.setForeground(new java.awt.Color(40, 46, 62));
        txtSoDien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSoDienMouseClicked(evt);
            }
        });
        jPanel3.add(txtSoDien, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 93, 140, -1));

        txttiendien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttiendien.setForeground(new java.awt.Color(40, 46, 62));
        jPanel3.add(txttiendien, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 130, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(40, 46, 62));
        jLabel9.setText("Tiền điện:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 96, 90, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(40, 46, 62));
        jLabel10.setText("Số nước:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 128, -1, -1));

        txtSoNuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoNuoc.setForeground(new java.awt.Color(40, 46, 62));
        txtSoNuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSoNuocMouseClicked(evt);
            }
        });
        jPanel3.add(txtSoNuoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 125, 140, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(40, 46, 62));
        jLabel11.setText("Tiền nước");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 128, 70, -1));

        txttiennuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttiennuoc.setForeground(new java.awt.Color(40, 46, 62));
        jPanel3.add(txttiennuoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 130, 130, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(40, 46, 62));
        jLabel16.setText("Tổng cộng:");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 193, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(40, 46, 62));
        jLabel17.setText("Tình trạng:");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 222, -1, -1));

        rbDaThanhtoan.setBackground(new java.awt.Color(207, 243, 243));
        buttonGroup1.add(rbDaThanhtoan);
        rbDaThanhtoan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbDaThanhtoan.setForeground(new java.awt.Color(40, 46, 62));
        rbDaThanhtoan.setText("Đã thanh toán");
        jPanel3.add(rbDaThanhtoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 220, -1, -1));

        rbChuaThanhToan.setBackground(new java.awt.Color(207, 243, 243));
        buttonGroup1.add(rbChuaThanhToan);
        rbChuaThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbChuaThanhToan.setForeground(new java.awt.Color(40, 46, 62));
        rbChuaThanhToan.setText("Chưa thanh toán");
        jPanel3.add(rbChuaThanhToan, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, -1, -1));

        lblTongCong.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongCong.setForeground(new java.awt.Color(255, 205, 31));
        lblTongCong.setText("XX");
        lblTongCong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTongCongMouseClicked(evt);
            }
        });
        jPanel3.add(lblTongCong, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 189, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(40, 46, 62));
        jLabel18.setText("Giá phòng:");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 160, -1, -1));

        txttienphong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttienphong.setForeground(new java.awt.Color(40, 46, 62));
        txttienphong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txttienphongMouseClicked(evt);
            }
        });
        jPanel3.add(txttienphong, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 157, 140, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 102, 550, 260));

        jPanel4.setBackground(new java.awt.Color(207, 243, 243));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        btnThem.setBackground(new java.awt.Color(255, 205, 31));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(40, 46, 62));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 205, 31));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(40, 46, 62));
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 205, 31));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(40, 46, 62));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(255, 205, 31));
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnReset.setForeground(new java.awt.Color(40, 46, 62));
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 340, 240));

        tblHoaDon.setBackground(new java.awt.Color(207, 243, 243));
        tblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblHoaDon.setForeground(new java.awt.Color(40, 46, 62));
        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHoaDon.setGridColor(new java.awt.Color(255, 255, 255));
        tblHoaDon.setSelectionBackground(new java.awt.Color(40, 46, 62));
        tblHoaDon.setSelectionForeground(new java.awt.Color(255, 205, 31));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 1093, 227));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/App/Icon/nen03.jpg"))); // NOI18N
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1120, 610));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1112, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemFocusGained
        // TODO add your handling code here:
    if (PLACEHOLDER.equals(txtTimKiem.getText())) txtTimKiem.setText("");
    }//GEN-LAST:event_txtTimKiemFocusGained

    private void txtTimKiemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemFocusLost
        // TODO add your handling code here:
    if (txtTimKiem.getText().trim().isEmpty()) txtTimKiem.setText(PLACEHOLDER);
    }//GEN-LAST:event_txtTimKiemFocusLost

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
    searchByHopDong(txtTimKiem.getText());
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtSoDienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSoDienMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSoDienMouseClicked

    private void txtSoNuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSoNuocMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSoNuocMouseClicked

    private void lblTongCongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTongCongMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_lblTongCongMouseClicked

    private void txttienphongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txttienphongMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_txttienphongMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
    add();

    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
    update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
    clearForm();
    loadTable();
    }//GEN-LAST:event_btnResetActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
    if (evt.getClickCount() >= 1) {
        tableRowClick(tblHoaDon.getSelectedRow());
    }
    }//GEN-LAST:event_tblHoaDonMouseClicked

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
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HoaDonJDialog dialog = new HoaDonJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcNgayTao;
    private com.toedter.calendar.JDateChooser jdcNgaythanhtoan;
    private javax.swing.JLabel lblIDHoaDon;
    private javax.swing.JLabel lblTongCong;
    private javax.swing.JRadioButton rbChuaThanhToan;
    private javax.swing.JRadioButton rbDaThanhtoan;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtIDHopDong;
    private javax.swing.JTextField txtSoDien;
    private javax.swing.JTextField txtSoNuoc;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txttiendien;
    private javax.swing.JTextField txttiennuoc;
    private javax.swing.JTextField txttienphong;
    // End of variables declaration//GEN-END:variables
}
// doreamon ơi
// hary botter