/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;
import main.dao.HoaDonDAO;
import main.entity.HoaDon;
import main.util.XJdbc;
import java.util.List;
/**
 *
 * @author PHONG
 */
public class HoaDonDAOImpl implements HoaDonDAO{
 





    @Override
    public void insert(HoaDon hd) {
        String sql = "INSERT INTO HoaDon VALUES (?, ?, ?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql,
            hd.getMaHoaDon(), hd.getMaHopDong(), hd.getThang(),
            hd.getNam(), hd.getTienPhong(), hd.getTienDien(), hd.getTienNuoc()
        );
    }

    @Override
    public void update(HoaDon hd) {
        String sql = "UPDATE HoaDon SET MaHopDong=?, Thang=?, Nam=?, TienPhong=?, TienDien=?, TienNuoc=? WHERE MaHoaDon=?";
        XJdbc.executeUpdate(sql,
            hd.getMaHopDong(), hd.getThang(), hd.getNam(),
            hd.getTienPhong(), hd.getTienDien(), hd.getTienNuoc(),
            hd.getMaHoaDon()
        );
    }

    @Override
    public void delete(String maHoaDon) {
        String sql = "DELETE FROM HoaDon WHERE MaHoaDon=?";
        XJdbc.executeUpdate(sql, maHoaDon);
    }

    @Override
    public List<HoaDon> selectAll() {
        String sql = "SELECT * FROM HoaDon";
        return XJdbc.getBeanList(HoaDon.class, sql);
    }
}

