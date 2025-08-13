/*
 * HoaDonDAOImpl - tự sinh mã HDONxxx, giữ nguyên các API mở rộng
 */
package App.Impl;

import App.DAO.HoaDonDAO;
import App.Entity.HoaDon;
import App.Utils.XJdbc;
import App.Utils.XQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class HoaDonDAOImpl implements HoaDonDAO {

    // --- SQL ---
    private static final String SQL_INSERT =
        "INSERT INTO HoaDon (MaHoaDon, MaHopDong, TienPhong, TienDien, TienNuoc, TrangThai, NgayThanhToan, NgayTaoHoaDon) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE HoaDon SET MaHopDong=?, TienPhong=?, TienDien=?, TienNuoc=?, TrangThai=?, NgayThanhToan=?, NgayTaoHoaDon=? " +
        "WHERE MaHoaDon=?";

    private static final String SQL_DELETE     = "DELETE FROM HoaDon WHERE MaHoaDon=?";
    private static final String SQL_FIND_ALL   = "SELECT * FROM HoaDon";
    private static final String SQL_FIND_ID    = "SELECT * FROM HoaDon WHERE MaHoaDon=?";
    private static final String SQL_BY_HD      = "SELECT * FROM HoaDon WHERE MaHopDong=? ORDER BY NgayTaoHoaDon DESC";
    private static final String SQL_UNPAID     = "SELECT * FROM HoaDon WHERE MaHopDong=? AND TrangThai = N'Chưa thanh toán'";
    private static final String SQL_MARKPAID   = "UPDATE HoaDon SET TrangThai=N'Đã thanh toán', NgayThanhToan=? WHERE MaHoaDon=?";

    // Lấy số lớn nhất phần số trong mã HDONxxx
    private static final String SQL_MAX_NUM =
        "SELECT ISNULL(MAX(CAST(SUBSTRING(MaHoaDon, 5, 10) AS INT)), 0) " +
        "FROM HoaDon WHERE MaHoaDon LIKE 'HDON%'";

    // --- CRUD ---
    @Override
    public HoaDon create(HoaDon e) {
        // Tự sinh mã nếu chưa có
        String id = e.getMaHoaDon();
        if (id == null || id.trim().isEmpty()) {
            id = nextId();
            e.setMaHoaDon(id);
        }
        Object[] v = {
            id, e.getMaHopDong(), e.getTienPhong(), e.getTienDien(), e.getTienNuoc(),
            e.getTrangThai(), e.getNgayThanhToan(), e.getNgayTaoHoaDon()
        };
        XJdbc.executeUpdate(SQL_INSERT, v);
        return e;
    }

    @Override
    public void update(HoaDon e) {
        Object[] v = {
            e.getMaHopDong(), e.getTienPhong(), e.getTienDien(), e.getTienNuoc(),
            e.getTrangThai(), e.getNgayThanhToan(), e.getNgayTaoHoaDon(), e.getMaHoaDon()
        };
        XJdbc.executeUpdate(SQL_UPDATE, v);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public List<HoaDon> findAll() {
        return XQuery.getEntityList(HoaDon.class, SQL_FIND_ALL);
    }

    @Override
    public HoaDon findById(String id) {
        return XQuery.getSingleBean(HoaDon.class, SQL_FIND_ID, id);
    }

    // --- Mở rộng ---
    @Override
    public List<HoaDon> findByMaHopDong(String maHopDong) {
        return XQuery.getEntityList(HoaDon.class, SQL_BY_HD, maHopDong);
    }

    @Override
    public List<HoaDon> findChuaThanhToanByMaHopDong(String maHopDong) {
        return XQuery.getEntityList(HoaDon.class, SQL_UNPAID, maHopDong);
    }

    @Override
    public void capNhatTrangThaiThanhToan(String maHoaDon, Date ngayThanhToan) {
        XJdbc.executeUpdate(SQL_MARKPAID, ngayThanhToan, maHoaDon);
    }

    // --- Helper: sinh mã HDONxxx ---
    private String nextId() {
        int maxNum = 0;
        try (ResultSet rs = XJdbc.executeQuery(SQL_MAX_NUM)) {
            if (rs.next()) maxNum = rs.getInt(1);
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi lấy số thứ tự mã hóa đơn: " + ex.getMessage(), ex);
        }
        return String.format("HDON%03d", maxNum + 1);
    }
}
