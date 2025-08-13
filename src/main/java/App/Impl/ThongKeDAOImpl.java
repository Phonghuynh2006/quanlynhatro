/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App.Impl;

import App.DAO.ThongKeDAO;
import App.Utils.XJdbc;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 *
 * @author PHONG
 */
public class ThongKeDAOImpl implements ThongKeDAO {


    @Override
    public BigDecimal getTongDoanhThu() throws SQLException {
        // SQL Server: dùng N'...' cho Unicode, COALESCE để tránh null
        String sql =
            "SELECT COALESCE(SUM(CAST(tienPhong AS DECIMAL(18,2)) " +
            "                + CAST(tienDien  AS DECIMAL(18,2)) " +
            "                + CAST(tienNuoc  AS DECIMAL(18,2))), 0) " +
            "FROM HoaDon WHERE trangThai = N'Đã thanh toán'";
        BigDecimal v = XJdbc.getValue(sql);
        return v != null ? v : BigDecimal.ZERO;
    }

    @Override
    public int getSoKhachDangThue() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Phong WHERE trangThai = N'Đang thuê'";
        Integer c = XJdbc.getValue(sql);
        return c == null ? 0 : c;
    }

    @Override
    public int getSoHopDongSapHetHan(int soNgay) throws SQLException {
        // Chỉ tính hợp đồng có ngày kết thúc từ hôm nay đến hôm nay + soNgay
        String sql =
            "SELECT COUNT(*) FROM HopDong " +
            "WHERE CONVERT(date, ngayKetThuc) BETWEEN CONVERT(date, GETDATE()) " +
            "AND DATEADD(day, ?, CONVERT(date, GETDATE()))";
        Integer c = XJdbc.getValue(sql, soNgay);
        return c == null ? 0 : c;
    }

    @Override
    public int getSoHoaDonChuaThanhToan() throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE trangThai = N'Chưa thanh toán'";
        Integer c = XJdbc.getValue(sql);
        return c == null ? 0 : c;
    }

}
