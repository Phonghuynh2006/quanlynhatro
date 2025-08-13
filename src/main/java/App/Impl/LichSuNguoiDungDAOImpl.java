/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App.Impl;

import App.DAO.LichSuNguoiDungDAO;
import App.Entity.LichSuNguoiDung;
import App.Utils.XJdbc;
import App.Utils.XQuery;
import java.util.List;
//lich su
/**
 *
 * @author PHONG
 */
public class LichSuNguoiDungDAOImpl implements LichSuNguoiDungDAO {

    // --- SQL ---
    private static final String SQL_INSERT =
        "INSERT INTO LichSuNguoiDung (MaNguoiDung, ThoiGian, HanhDong) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE LichSuNguoiDung SET MaNguoiDung=?, ThoiGian=?, HanhDong=? WHERE ID=?";

    private static final String SQL_DELETE   = "DELETE FROM LichSuNguoiDung WHERE ID=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM LichSuNguoiDung ORDER BY ThoiGian DESC";
    private static final String SQL_FIND_ID  = "SELECT * FROM LichSuNguoiDung WHERE ID=?";
    private static final String SQL_BY_USER  = "SELECT * FROM LichSuNguoiDung WHERE MaNguoiDung=? ORDER BY ThoiGian DESC";

    // --- CRUD ---
    @Override
    public LichSuNguoiDung create(LichSuNguoiDung e) {
        Object[] v = { e.getMaNguoiDung(), e.getThoiGian(), e.getHanhDong() };
        XJdbc.executeUpdate(SQL_INSERT, v);
        return e;
    }

    @Override
    public void update(LichSuNguoiDung e) {
        Object[] v = { e.getMaNguoiDung(), e.getThoiGian(), e.getHanhDong(), e.getId() };
        XJdbc.executeUpdate(SQL_UPDATE, v);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public List<LichSuNguoiDung> findAll() {
        return XQuery.getEntityList(LichSuNguoiDung.class, SQL_FIND_ALL);
    }

    @Override
    public LichSuNguoiDung findById(Integer id) {
        return XQuery.getSingleBean(LichSuNguoiDung.class, SQL_FIND_ID, id);
    }

    // --- Mở rộng ---
    @Override
    public void log(Integer maNguoiDung, String hanhDong) {
        XJdbc.executeUpdate("INSERT INTO LichSuNguoiDung (MaNguoiDung, HanhDong) VALUES (?, ?)", maNguoiDung, hanhDong);
    }

    @Override
    public List<LichSuNguoiDung> findByMaNguoiDung(Integer maNguoiDung) {
        return XQuery.getEntityList(LichSuNguoiDung.class, SQL_BY_USER, maNguoiDung);
    }
}
