package App.Impl;

import App.DAO.HopDongDAO;
import App.Entity.HopDong;
import App.Utils.XJdbc;
import App.Utils.XQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HopDongDAOImpl implements HopDongDAO {

    // --- SQL ---
    private static final String SQL_INSERT =
        "INSERT INTO HopDong (MaHopDong, MaNguoiDung, MaPhong, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE HopDong SET MaNguoiDung=?, MaPhong=?, NgayBatDau=?, NgayKetThuc=? WHERE MaHopDong=?";

    private static final String SQL_DELETE    = "DELETE FROM HopDong WHERE MaHopDong=?";
    private static final String SQL_FIND_ALL  = "SELECT * FROM HopDong";
    private static final String SQL_FIND_ID   = "SELECT * FROM HopDong WHERE MaHopDong=?";
    private static final String SQL_FIND_USER = "SELECT * FROM HopDong WHERE MaNguoiDung=?";

    // Hợp đồng đang hiệu lực theo phòng
    private static final String SQL_FIND_ACTIVE_BY_ROOM =
        "SELECT TOP 1 * FROM HopDong " +
        "WHERE MaPhong=? AND (NgayKetThuc IS NULL OR NgayKetThuc >= CAST(GETDATE() AS DATE)) " +
        "ORDER BY NgayBatDau DESC";

    // Lấy số lớn nhất phần số của mã (HDxxx)
    private static final String SQL_MAX_NUM =
        "SELECT ISNULL(MAX(CAST(SUBSTRING(MaHopDong, 3, 10) AS INT)), 0) " +
        "FROM HopDong WHERE MaHopDong LIKE 'HD%'";

    // --- CRUD ---
    @Override
    public HopDong create(HopDong e) {
        String id = e.getMaHopDong();
        if (id == null || id.trim().isEmpty()) {
            id = nextId();            // tự sinh HDxxx
            e.setMaHopDong(id);
        }
        XJdbc.executeUpdate(SQL_INSERT,
                id, e.getMaNguoiDung(), e.getMaPhong(), e.getNgayBatDau(), e.getNgayKetThuc());
        return e;
    }

    @Override
    public void update(HopDong e) {
        XJdbc.executeUpdate(SQL_UPDATE,
                e.getMaNguoiDung(), e.getMaPhong(), e.getNgayBatDau(), e.getNgayKetThuc(), e.getMaHopDong());
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public List<HopDong> findAll() {
        return XQuery.getEntityList(HopDong.class, SQL_FIND_ALL);
    }

    @Override
    public HopDong findById(String id) {
        return XQuery.getSingleBean(HopDong.class, SQL_FIND_ID, id);
    }

    // --- Mở rộng theo interface ---
    @Override
    public List<HopDong> findByMaNguoiDung(Integer maNguoiDung) {
        return XQuery.getEntityList(HopDong.class, SQL_FIND_USER, maNguoiDung);
    }

    @Override
    public HopDong findDangHieuLucByMaPhong(String maPhong) {
        return XQuery.getSingleBean(HopDong.class, SQL_FIND_ACTIVE_BY_ROOM, maPhong);
    }

    // --- Helper sinh mã HDxxx ---
    private String nextId() {
        int maxNum = 0;
        try (ResultSet rs = XJdbc.executeQuery(SQL_MAX_NUM)) {
            if (rs.next()) maxNum = rs.getInt(1);
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi lấy số thứ tự mã hợp đồng: " + ex.getMessage(), ex);
        }
        return String.format("HD%03d", maxNum + 1); // HD001, HD002, ...
    }
}
