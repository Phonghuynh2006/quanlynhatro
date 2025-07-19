/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.dao.CrudDAO;
import main.dao.KhachThueDAO;
import main.entity.KhachThue;
import main.util.XJdbc;
/**
 *
 * @author PHONG
 */


public class KhachThueDAOImpl implements KhachThueDAO {

    @Override
    public void insert(KhachThue kh) {
        String sql = "INSERT INTO KhachThue (MaKhach, HoTen, CMND, SoDienThoai, TenDangNhap) VALUES (?, ?, ?, ?, ?)";
        try (
            Connection con = XJdbc.openConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, kh.getMaKhach());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getCmnd());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getTenDangNhap());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(KhachThue kh) {
        String sql = "UPDATE KhachThue SET HoTen=?, CMND=?, SoDienThoai=?, TenDangNhap=? WHERE MaKhach=?";
        try (
            Connection con = XJdbc.openConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getCmnd());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getTenDangNhap());
            ps.setString(5, kh.getMaKhach());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String maKhach) {
        String sql = "DELETE FROM KhachThue WHERE MaKhach=?";
        try (
            Connection con = XJdbc.openConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, maKhach);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public KhachThue selectById(String maKhach) {
        String sql = "SELECT * FROM KhachThue WHERE MaKhach=?";
        try (
            Connection con = XJdbc.openConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, maKhach);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhachThue(
                    rs.getString("MaKhach"),
                    rs.getString("HoTen"),
                    rs.getString("CMND"),
                    rs.getString("SoDienThoai"),
                    rs.getString("TenDangNhap")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<KhachThue> selectAll() {
        List<KhachThue> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachThue";
        try (
            Connection con = XJdbc.openConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                KhachThue kh = new KhachThue(
                    rs.getString("MaKhach"),
                    rs.getString("HoTen"),
                    rs.getString("CMND"),
                    rs.getString("SoDienThoai"),
                    rs.getString("TenDangNhap")
                );
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public CrudDAO create(CrudDAO entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(CrudDAO entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CrudDAO> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CrudDAO findById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


