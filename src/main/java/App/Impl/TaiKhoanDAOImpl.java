/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App.Impl;

import App.DAO.TaiKhoanDAO;
import App.Entity.TaiKhoan;
import App.Utils.XJdbc;
import App.Utils.XQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author PHONG
 */
public class TaiKhoanDAOImpl implements TaiKhoanDAO {

    // --- SQL ---
    private static final String SQL_INSERT =
        "INSERT INTO TaiKhoan (TenTaiKhoan, MatKhau, HoTen, NgaySinh, GioiTinh, CCCD, DienThoai, Email, DiaChi, VaiTro, TrangThai, HinhAnh) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE TaiKhoan SET MatKhau=?, HoTen=?, NgaySinh=?, GioiTinh=?, CCCD=?, DienThoai=?, Email=?, DiaChi=?, VaiTro=?, TrangThai=?, HinhAnh=? " +
        "WHERE MaNguoiDung=?";

    private static final String SQL_DELETE   = "DELETE FROM TaiKhoan WHERE MaNguoiDung=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM TaiKhoan";
    private static final String SQL_FIND_ID  = "SELECT * FROM TaiKhoan WHERE MaNguoiDung=?";
    private static final String SQL_FIND_TK  = "SELECT * FROM TaiKhoan WHERE TenTaiKhoan=?";
    private static final String SQL_CHECK_TK = "SELECT COUNT(*) FROM TaiKhoan WHERE TenTaiKhoan=?";
    private static final String SQL_LOGIN    = "SELECT * FROM TaiKhoan WHERE TenTaiKhoan=? AND MatKhau=?";

    // --- CRUD ---
    @Override
    public TaiKhoan create(TaiKhoan e) {
        Object[] v = {
            e.getTenTaiKhoan(), e.getMatKhau(), e.getHoTen(), e.getNgaySinh(), e.getGioiTinh(),
            e.getCccd(), e.getDienThoai(), e.getEmail(), e.getDiaChi(), e.getVaiTro(),
            e.getTrangThai(), e.getHinhAnh()
        };
        XJdbc.executeUpdate(SQL_INSERT, v);
        return e;
    }

    @Override
    public void update(TaiKhoan e) {
        Object[] v = {
            e.getMatKhau(), e.getHoTen(), e.getNgaySinh(), e.getGioiTinh(), e.getCccd(),
            e.getDienThoai(), e.getEmail(), e.getDiaChi(), e.getVaiTro(), e.getTrangThai(),
            e.getHinhAnh(), e.getMaNguoiDung()
        };
        XJdbc.executeUpdate(SQL_UPDATE, v);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(SQL_DELETE, id);
    }

    @Override
    public List<TaiKhoan> findAll() {
        return XQuery.getEntityList(TaiKhoan.class, SQL_FIND_ALL);
    }

    @Override
    public TaiKhoan findById(Integer id) {
        return XQuery.getSingleBean(TaiKhoan.class, SQL_FIND_ID, id);
    }

    // --- Mở rộng ---
    @Override
    public TaiKhoan findByTenTaiKhoan(String tenTaiKhoan) {
        return XQuery.getSingleBean(TaiKhoan.class, SQL_FIND_TK, tenTaiKhoan);
    }

    @Override
    public boolean isTenDangNhapTonTai(String tenDangNhap) {
        try (ResultSet rs = XJdbc.executeQuery(SQL_CHECK_TK, tenDangNhap)) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi kiểm tra tên tài khoản: " + ex.getMessage(), ex);
        }
    }

    @Override
    public TaiKhoan findByTenTaiKhoanAndMatKhau(String tenTaiKhoan, String matKhau) {
        return XQuery.getSingleBean(TaiKhoan.class, SQL_LOGIN, tenTaiKhoan, matKhau);
    }
}
