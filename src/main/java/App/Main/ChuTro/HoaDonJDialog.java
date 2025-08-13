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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(40, 46, 62));

        jPanel2.setBackground(new java.awt.Color(46, 56, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 31), 2), "Tìm kiếm ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 205, 31))); // NOI18N

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

        btnTimKiem.setBackground(new java.awt.Color(255, 205, 31));
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

        jPanel3.setBackground(new java.awt.Color(207, 243, 243));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(40, 46, 62));
        jLabel1.setText("HÓA ĐƠN");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(40, 46, 62));
        jLabel2.setText("Mã hóa đơn:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(40, 46, 62));
        jLabel3.setText("Mã hợp đồng:");

        lblIDHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblIDHoaDon.setForeground(new java.awt.Color(255, 205, 31));
        lblIDHoaDon.setText("XX");

        txtIDHopDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtIDHopDong.setForeground(new java.awt.Color(40, 46, 62));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(40, 46, 62));
        jLabel5.setText("Ngày tạo:");

        jdcNgayTao.setBackground(new java.awt.Color(255, 255, 255));
        jdcNgayTao.setForeground(new java.awt.Color(40, 46, 62));
        jdcNgayTao.setDateFormatString("dd-MM-yyyy");
        jdcNgayTao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(40, 46, 62));
        jLabel6.setText("Ngày thanh toán:");

        jdcNgaythanhtoan.setBackground(new java.awt.Color(255, 255, 255));
        jdcNgaythanhtoan.setForeground(new java.awt.Color(40, 46, 62));
        jdcNgaythanhtoan.setDateFormatString("dd-MM-yyyy");
        jdcNgaythanhtoan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(40, 46, 62));
        jLabel8.setText("Số điện:");

        txtSoDien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoDien.setForeground(new java.awt.Color(40, 46, 62));
        txtSoDien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSoDienMouseClicked(evt);
            }
        });

        txttiendien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttiendien.setForeground(new java.awt.Color(40, 46, 62));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(40, 46, 62));
        jLabel9.setText("Tiền điện:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(40, 46, 62));
        jLabel10.setText("Số nước:");

        txtSoNuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoNuoc.setForeground(new java.awt.Color(40, 46, 62));
        txtSoNuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSoNuocMouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(40, 46, 62));
        jLabel11.setText("Tiền nước");

        txttiennuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttiennuoc.setForeground(new java.awt.Color(40, 46, 62));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(40, 46, 62));
        jLabel16.setText("Tổng cộng:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(40, 46, 62));
        jLabel17.setText("Tình trạng:");

        rbDaThanhtoan.setBackground(new java.awt.Color(207, 243, 243));
        buttonGroup1.add(rbDaThanhtoan);
        rbDaThanhtoan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbDaThanhtoan.setForeground(new java.awt.Color(40, 46, 62));
        rbDaThanhtoan.setText("Đã thanh toán");

        rbChuaThanhToan.setBackground(new java.awt.Color(207, 243, 243));
        buttonGroup1.add(rbChuaThanhToan);
        rbChuaThanhToan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbChuaThanhToan.setForeground(new java.awt.Color(40, 46, 62));
        rbChuaThanhToan.setText("Chưa thanh toán");

        lblTongCong.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongCong.setForeground(new java.awt.Color(255, 205, 31));
        lblTongCong.setText("XX");
        lblTongCong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTongCongMouseClicked(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(40, 46, 62));
        jLabel18.setText("Giá phòng:");

        txttienphong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txttienphong.setForeground(new java.awt.Color(40, 46, 62));
        txttienphong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txttienphongMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIDHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jdcNgaythanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoDien, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTongCong)
                                    .addComponent(txttienphong, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblIDHoaDon))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(18, 18, 18)
                                    .addComponent(txttiennuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txttiendien, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jdcNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel17)
                        .addGap(49, 49, 49)
                        .addComponent(rbDaThanhtoan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbChuaThanhToan)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(lblIDHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdcNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtIDHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdcNgaythanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtSoDien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txttiendien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSoNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txttiennuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txttienphong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblTongCong))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(rbDaThanhtoan)
                    .addComponent(rbChuaThanhToan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(46, 56, 86));

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
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
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
                .addContainerGap(134, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1126, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 632, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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