/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import main.dao.TaiKhoanDAO;
import main.entity.TaiKhoan;
import main.util.XJdbc;
import main.util.XQuery;

/**
 *
 * @author PHONG
 */
public class TaiKhoanDAOImpl implements TaiKhoanDAO{

//    String createSql = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, VaiTro) VALUES (?, ?, ?)";
//    String updateSql = "UPDATE TaiKhoan SET MatKhau = ?, VaiTro = ? WHERE TenDangNhap = ?";
//    String deleteSql = "DELETE FROM TaiKhoan WHERE TenDangNhap = ?";
//    String findAllSql = "SELECT * FROM TaiKhoan";
//    String findByIdSql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ?";
//
//    @Override
//    public TaiKhoan create(TaiKhoan entity) {
//        Object[] values = {
//            entity.getTenDangNhap(),
//            entity.getMatKhau(),
//            entity.isVaiTro()
//        };
//        XJdbc.executeUpdate(createSql, values);
//        return entity;
//    }
//
//    @Override
//    public void update(TaiKhoan entity) {
//        Object[] values = {
//            entity.getMatKhau(),
//            entity.isVaiTro(),
//            entity.getTenDangNhap()
//        };
//        XJdbc.executeUpdate(updateSql, values);
//    }
//
//    @Override
//    public void deleteById(String id) {
//        XJdbc.executeUpdate(deleteSql, id);
//    }
//
//    @Override
//    public List<TaiKhoan> findAll() {
//        return XQuery.getEntityList(TaiKhoan.class, findAllSql);
//    }
//
//    @Override
//    public TaiKhoan findById(String id) {
//        return XQuery.getSingleBean(TaiKhoan.class, findByIdSql, id);
//    }
//
//    @Override
//    public boolean isTenDangNhapTonTai(String tenDangNhap) {
//        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE TenDangNhap = ?";
//        try (ResultSet rs = XJdbc.executeQuery(sql, tenDangNhap)) {
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Lỗi kiểm tra tên đăng nhập: " + e.getMessage(), e);
//        }
//        return false;
//    }
// SQL queries
       private final String createSql = "INSERT INTO TaiKhoan (TenTaiKhoan, MatKhau, HoTen, NgaySinh, GioiTinh, CCCD, DienThoai, Email, DiaChi, VaiTro, TrangThai, HinhAnh) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String updateSql = "UPDATE TaiKhoan SET MatKhau=?, HoTen=?, NgaySinh=?, GioiTinh=?, CCCD=?, DienThoai=?, Email=?, DiaChi=?, VaiTro=?, TrangThai=?, HinhAnh=? "
            + "WHERE MaNguoiDung=?";

    private final String deleteSql = "DELETE FROM TaiKhoan WHERE MaNguoiDung=?";
    private final String findAllSql = "SELECT * FROM TaiKhoan";
    private final String findByIdSql = "SELECT * FROM TaiKhoan WHERE MaNguoiDung=?";
    private final String findByTenTaiKhoanSql = "SELECT * FROM TaiKhoan WHERE TenTaiKhoan=?";
    private final String checkTenTaiKhoanSql = "SELECT COUNT(*) FROM TaiKhoan WHERE TenTaiKhoan = ?";
    private final String loginSql = "SELECT * FROM TaiKhoan WHERE TenTaiKhoan=? AND MatKhau=?";

    @Override
    public TaiKhoan create(TaiKhoan entity) {
        Object[] values = {
            entity.getTenTaiKhoan(),
            entity.getMatKhau(),
            entity.getHoTen(),
            entity.getNgaySinh(),
            entity.getGioiTinh(),
            entity.getCccd(),
            entity.getDienThoai(),
            entity.getEmail(),
            entity.getDiaChi(),
            entity.getVaiTro(),
            entity.getTrangThai(),
            entity.getHinhAnh()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(TaiKhoan entity) {
        Object[] values = {
            entity.getMatKhau(),
            entity.getHoTen(),
            entity.getNgaySinh(),
            entity.getGioiTinh(),
            entity.getCccd(),
            entity.getDienThoai(),
            entity.getEmail(),
            entity.getDiaChi(),
            entity.getVaiTro(),
            entity.getTrangThai(),
            entity.getHinhAnh(),
            entity.getMaNguoiDung()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<TaiKhoan> findAll() {
        return XQuery.getEntityList(TaiKhoan.class, findAllSql);
    }

    @Override
    public TaiKhoan findById(Integer id) {
        return XQuery.getSingleBean(TaiKhoan.class, findByIdSql, id);
    }

    @Override
    public TaiKhoan findByTenTaiKhoan(String tenTaiKhoan) {
        return XQuery.getSingleBean(TaiKhoan.class, findByTenTaiKhoanSql, tenTaiKhoan);
    }

    @Override
    public boolean isTenDangNhapTonTai(String tenDangNhap) {
        try (ResultSet rs = XJdbc.executeQuery(checkTenTaiKhoanSql, tenDangNhap)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiểm tra tên đăng nhập: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public TaiKhoan findByTenTaiKhoanAndMatKhau(String tenTaiKhoan, String matKhau) {
        return XQuery.getSingleBean(TaiKhoan.class, loginSql, tenTaiKhoan, matKhau);
    }
}
