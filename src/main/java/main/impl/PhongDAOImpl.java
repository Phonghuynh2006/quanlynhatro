/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import main.dao.PhongDAO;
import main.entity.Phong;
import main.util.XJdbc;
import main.util.XQuery;

/**
 *
 * @author PHONG
 */
public class PhongDAOImpl implements PhongDAO{

    final String insertSql = "INSERT INTO Phong (MaPhong, TrangThai, GiaTien, DienTich, DiaChi, LienHe, MoTa, AnhPhong) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    final String updateSql = "UPDATE Phong SET TrangThai = ?, GiaTien = ?, DienTich = ?, DiaChi = ?, LienHe = ?, MoTa = ?, AnhPhong = ? WHERE MaPhong = ?";
    final String deleteSql = "DELETE FROM Phong WHERE MaPhong = ?";
    final String findAllSql = "SELECT * FROM Phong";
    final String findByIdSql = "SELECT * FROM Phong WHERE MaPhong = ?";

    @Override
    public Phong create(Phong entity) {
        XJdbc.executeUpdate(insertSql,
                entity.getMaPhong(),
                entity.getTrangThai(),
                entity.getGiaTien(),
                entity.getDienTich(),
                entity.getDiaChi(),
                entity.getLienHe(),
                entity.getMoTa(),
                entity.getAnhPhong());
        return entity;
    }

    @Override
    public void update(Phong entity) {
        XJdbc.executeUpdate(updateSql,
                entity.getTrangThai(),
                entity.getGiaTien(),
                entity.getDienTich(),
                entity.getDiaChi(),
                entity.getLienHe(),
                entity.getMoTa(),
                entity.getAnhPhong(),
                entity.getMaPhong());
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Phong> findAll() {
        return XQuery.getEntityList(Phong.class, findAllSql);
    }

    @Override
    public Phong findById(String id) {
        return XQuery.getSingleBean(Phong.class, findByIdSql, id);
    }

    @Override
    public List<Phong> findByTrangThai(String trangThai) {
        String sql = "SELECT * FROM Phong WHERE TrangThai = ?";
        return XQuery.getEntityList(Phong.class, sql, trangThai);
    }

    @Override
    public List<Phong> findByGiaTienRange(double min, double max) {
        String sql = "SELECT * FROM Phong WHERE GiaTien BETWEEN ? AND ?";
        return XQuery.getEntityList(Phong.class, sql, min, max);
    }

    @Override
    public List<Phong> findByDiaChi(String keyword) {
        String sql = "SELECT * FROM Phong WHERE DiaChi LIKE ?";
        return XQuery.getEntityList(Phong.class, sql, "%" + keyword + "%");
    }


}
