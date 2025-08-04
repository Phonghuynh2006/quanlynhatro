package main.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.dao.HopDongDAO;
import main.entity.HopDong;

public class HopDongDAOImpl implements HopDongDAO {

    private Connection conn;

    public HopDongDAOImpl() {
        try {
            conn = DriverManager.getConnection(
    "jdbc:sqlserver://localhost:1433;databaseName=DUAN1_QLNhaTro;encrypt=true;trustServerCertificate=true",
    "sa", "291206"
);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insert(HopDong hd) {
        String sql = "INSERT INTO HopDong (MaHopDong, MaPhong, MaKhach, NgayBatDau, NgayKetThuc, TenNhanVien) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHopDong());
            ps.setString(2, hd.getMaPhong());
            ps.setString(3, hd.getMaKhach());
            ps.setDate(4, hd.getNgayBatDau());
            ps.setDate(5, hd.getNgayKetThuc());
             ps.setString(5, hd.getTenNhanVien()); // thêm dòng này
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(HopDong hd) {
        String sql = "UPDATE HopDong SET MaPhong = ?, MaKhach = ?, NgayBatDau = ?, NgayKetThuc = ?, TenNhanVien=? WHERE MaHopDong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaPhong());
            ps.setString(2, hd.getMaKhach());
            ps.setDate(3, hd.getNgayBatDau());
            ps.setDate(4, hd.getNgayKetThuc());
            ps.setString(5, hd.getTenNhanVien()); // thêm dòng này
        ps.setString(6, hd.getMaHopDong());  // moved to thứ 6
            
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
        String sql = """
            SELECT hd.MaHopDong, hd.MaPhong, hd.MaKhach, hd.NgayBatDau, hd.NgayKetThuc, nv.TenNhanVien
            FROM HopDong hd
            LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien
            WHERE hd.MaHopDong = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHopDong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HopDong hd = new HopDong(
                    rs.getString("MaHopDong"),
                    rs.getString("MaPhong"),
                    rs.getString("MaKhach"),
                    rs.getDate("NgayBatDau"),
                    rs.getDate("NgayKetThuc")
                );
                hd.setTenNhanVien(rs.getString("TenNhanVien"));
                return hd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HopDong> findAll() {
        List<HopDong> list = new ArrayList<>();
        String sql = """
            SELECT hd.MaHopDong, hd.MaPhong, hd.MaKhach, hd.NgayBatDau, hd.NgayKetThuc, nv.TenNhanVien
            FROM HopDong hd
            LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HopDong hd = new HopDong(
                    rs.getString("MaHopDong"),
                    rs.getString("MaPhong"),
                    rs.getString("MaKhach"),
                    rs.getDate("NgayBatDau"),
                    rs.getDate("NgayKetThuc")
                );
                hd.setTenNhanVien(rs.getString("TenNhanVien"));
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
public List<HopDong> findByKeyword(String keyword) {
    List<HopDong> list = new ArrayList<>();
    String sql = """
        SELECT hd.MaHopDong, hd.MaPhong, hd.MaKhach, hd.NgayBatDau, hd.NgayKetThuc, nv.TenNhanVien
        FROM HopDong hd
        LEFT JOIN NhanVien nv ON hd.MaNhanVien = nv.MaNhanVien
        WHERE hd.MaHopDong LIKE ? OR hd.MaPhong LIKE ? OR hd.MaKhach LIKE ?
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        String wildcard = "%" + keyword + "%";
        ps.setString(1, wildcard);
        ps.setString(2, wildcard);
        ps.setString(3, wildcard);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            HopDong hd = new HopDong(
                rs.getString("MaHopDong"),
                rs.getString("MaPhong"),
                rs.getString("MaKhach"),
                rs.getDate("NgayBatDau"),
                rs.getDate("NgayKetThuc")
            );
            hd.setTenNhanVien(rs.getString("TenNhanVien"));
            list.add(hd);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    
}
