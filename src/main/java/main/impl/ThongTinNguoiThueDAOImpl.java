/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;

/**
 *
 * @author PHONG
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import main.dao.ThongTinNguoiThueDAO;
import main.entity.ThongTinNguoiThue;
import main.util.XJdbc;
import main.util.XQuery;

public class ThongTinNguoiThueDAOImpl implements ThongTinNguoiThueDAO {

    String insertSql = """
        INSERT INTO ThongTinNguoiThue 
        (TenDangNhap, HoVaTen, CCCD, GioiTinh, NgaySinh, DiaChi, SoDienThoai, Email, Photo) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    String updateSql = """
        UPDATE ThongTinNguoiThue SET 
        HoVaTen = ?, CCCD = ?, GioiTinh = ?, NgaySinh = ?, DiaChi = ?, 
        SoDienThoai = ?, Email = ?, Photo = ? 
        WHERE TenDangNhap = ?
    """;

    String selectSql = "SELECT * FROM ThongTinNguoiThue WHERE TenDangNhap = ?";

    @Override
    public ThongTinNguoiThue findByTenDangNhap(String tenDangNhap) {
        return XQuery.getSingleBean(ThongTinNguoiThue.class, selectSql, tenDangNhap);
    }

    @Override
    public void insert(ThongTinNguoiThue entity) {
        Object[] args = {
            entity.getTenDangNhap(),
            entity.getHoVaTen(),
            entity.getCccd(),
            entity.isGioiTinh(),
            entity.getNgaySinh(),
            entity.getDiaChi(),
            entity.getSoDienThoai(),
            entity.getEmail(),
            entity.getPhoto()
        };
        XJdbc.executeUpdate(insertSql, args);
    }

    @Override
    public void update(ThongTinNguoiThue entity) {
        Object[] args = {
            entity.getHoVaTen(),
            entity.getCccd(),
            entity.isGioiTinh(),
            entity.getNgaySinh(),
            entity.getDiaChi(),
            entity.getSoDienThoai(),
            entity.getEmail(),
            entity.getPhoto(),
            entity.getTenDangNhap()
        };
        XJdbc.executeUpdate(updateSql, args);
    }

}
