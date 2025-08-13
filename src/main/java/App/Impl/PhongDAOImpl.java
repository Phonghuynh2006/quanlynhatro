/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App.Impl;

import App.DAO.PhongDAO;
import App.Entity.Phong;
import App.Utils.XJdbc;
import App.Utils.XQuery;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author PHONG
 */
public class PhongDAOImpl implements PhongDAO {

    // --- SQL ---
    private static final String SQL_INSERT =
        "INSERT INTO Phong (MaPhong, TrangThai, GiaTien, DienTich, DiaChi, LienHe, MoTa, AnhPhong) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE Phong SET TrangThai=?, GiaTien=?, DienTich=?, DiaChi=?, LienHe=?, MoTa=?, AnhPhong=? WHERE MaPhong=?";

    private static final String SQL_DELETE   = "DELETE FROM Phong WHERE MaPhong=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Phong";
    private static final String SQL_FIND_ID  = "SELECT * FROM Phong WHERE MaPhong=?";
    private static final String SQL_FIND_TRANGTHAI = "SELECT * FROM Phong WHERE TrangThai=?";
    private static final String SQL_FIND_TRONG     = "SELECT * FROM Phong WHERE TrangThai = N'Trống'";

    // --- CRUD ---
    @Override
    public Phong create(Phong e) {
        Object[] v = {
            e.getMaPhong(), e.getTrangThai(), e.getGiaTien(), e.getDienTich(),
            e.getDiaChi(), e.getLienHe(), e.getMoTa(), e.getAnhPhong()
        };
        XJdbc.executeUpdate(SQL_INSERT, v);
        return e;
    }

    @Override
    public void update(Phong e) {
        Object[] v = {
            e.getTrangThai(), e.getGiaTien(), e.getDienTich(), e.getDiaChi(),
            e.getLienHe(), e.getMoTa(), e.getAnhPhong(), e.getMaPhong()
        };
        XJdbc.executeUpdate(SQL_UPDATE, v);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public List<Phong> findAll() {
        return XQuery.getEntityList(Phong.class, SQL_FIND_ALL);
    }

    @Override
    public Phong findById(String id) {
        return XQuery.getSingleBean(Phong.class, SQL_FIND_ID, id);
    }

    // --- Mở rộng ---
    @Override
    public List<Phong> findByTrangThai(String trangThai) {
        return XQuery.getEntityList(Phong.class, SQL_FIND_TRANGTHAI, trangThai);
    }

    @Override
    public List<Phong> findPhongTrong() {
        return XQuery.getEntityList(Phong.class, SQL_FIND_TRONG);
    }

    @Override
    public void updateTrangThai(String maPhong, String trangThai) {
        XJdbc.executeUpdate("UPDATE Phong SET TrangThai=? WHERE MaPhong=?", trangThai, maPhong);
    }

    @Override
    public List<Phong> search(String tuKhoa,
                              BigDecimal giaMin, BigDecimal giaMax,
                              BigDecimal dienTichMin, BigDecimal dienTichMax) {

        StringBuilder sql = new StringBuilder("SELECT * FROM Phong WHERE 1=1");
        new Object(){};
        // Xây câu where động
        String like = (tuKhoa == null || tuKhoa.trim().isEmpty()) ? null : "%" + tuKhoa.trim() + "%";

        new StringBuilder();
        // Gom tham số
        java.util.ArrayList<Object> params = new java.util.ArrayList<>();

        if (like != null) {
            sql.append(" AND (DiaChi LIKE ? OR MoTa LIKE ? OR LienHe LIKE ?)");
            params.add(like); params.add(like); params.add(like);
        }
        if (giaMin != null) { sql.append(" AND GiaTien >= ?"); params.add(giaMin); }
        if (giaMax != null) { sql.append(" AND GiaTien <= ?"); params.add(giaMax); }
        if (dienTichMin != null) { sql.append(" AND DienTich >= ?"); params.add(dienTichMin); }
        if (dienTichMax != null) { sql.append(" AND DienTich <= ?"); params.add(dienTichMax); }

        return XQuery.getEntityList(Phong.class, sql.toString(), params.toArray());
    }
}
