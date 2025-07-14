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

    String createSql = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, VaiTro, Fullname, Photo) VALUES (?, ?, ?, ?, ?)";
    String updateSql = "UPDATE TaiKhoan SET MatKhau = ?, VaiTro = ?, Fullname = ?, Photo = ? WHERE TenDangNhap = ?";
    String deleteSql = "DELETE FROM TaiKhoan WHERE TenDangNhap = ?";
    String findAllSql = "SELECT * FROM TaiKhoan";
    String findByIdSql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ?";

    @Override
    public TaiKhoan create(TaiKhoan entity) {
        Object[] values = {
            entity.getTenDangNhap(),
            entity.getMatKhau(),
            entity.isVaiTro(),
            entity.getFullname(),
            entity.getPhoto()
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(TaiKhoan entity) {
        Object[] values = {
            entity.getMatKhau(),
            entity.isVaiTro(),
            entity.getFullname(),
            entity.getPhoto(),
            entity.getTenDangNhap()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<TaiKhoan> findAll() {
        return XQuery.getEntityList(TaiKhoan.class, findAllSql);
    }

    @Override
    public TaiKhoan findById(String id) {
        return XQuery.getSingleBean(TaiKhoan.class, findByIdSql, id);
    }

    @Override
    public boolean isTenDangNhapTonTai(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE TenDangNhap = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, tenDangNhap);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra tên đăng nhập: " + e.getMessage());
        }
        return false;
    }

}
