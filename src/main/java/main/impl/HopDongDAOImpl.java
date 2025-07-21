/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.dao.HopDongDAO;
import main.entity.HopDong;

/**
 *
 * @author PHONG
 */
public class HopDongDAOImpl  implements  HopDongDAO{
    private Connection conn;

    public HopDongDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insert(HopDong hd) {
        String sql = "INSERT INTO HopDong (MaHopDong, MaPhong, MaKhach, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHopDong());
            ps.setString(2, hd.getMaPhong());
            ps.setString(3, hd.getMaKhach());
            ps.setDate(4, new java.sql.Date(hd.getNgayBatDau().getTime()));
            ps.setDate(5, new java.sql.Date(hd.getNgayKetThuc().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(HopDong hd) {
        String sql = "UPDATE HopDong SET MaPhong = ?, MaKhach = ?, NgayBatDau = ?, NgayKetThuc = ? WHERE MaHopDong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaPhong());
            ps.setString(2, hd.getMaKhach());
            ps.setDate(3, new java.sql.Date(hd.getNgayBatDau().getTime()));
            ps.setDate(4, new java.sql.Date(hd.getNgayKetThuc().getTime()));
            ps.setString(5, hd.getMaHopDong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String maHopDong) {
        String sql = "DELETE FROM HopDong WHERE MaHopDong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHopDong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public HopDong findById(String maHopDong) {
        String sql = "SELECT * FROM HopDong WHERE MaHopDong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHopDong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HopDong(
                    rs.getString("MaHopDong"),
                    rs.getString("MaPhong"),
                    rs.getString("MaKhach"),
                    rs.getDate("NgayBatDau"),
                    rs.getDate("NgayKetThuc")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HopDong> findAll() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT * FROM HopDong";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                HopDong hd = new HopDong(
                    rs.getString("MaHopDong"),
                    rs.getString("MaPhong"),
                    rs.getString("MaKhach"),
                    rs.getDate("NgayBatDau"),
                    rs.getDate("NgayKetThuc")
                );
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
