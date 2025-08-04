//package main.impl;
//
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//import main.dao.LichSuNguoiThueDAO;
//import main.entity.LichSuNguoiThue;
//import main.util.XJdbc;
//
//public class LichSuNguoiThueDAOImpl implements LichSuNguoiThueDAO {
//
//    @Override
//    public void insert(LichSuNguoiThue ls) {
//        String sql = "INSERT INTO LichSuNguoiThue (MaKhach, MaPhong, NgayBatDau, NgayKetThuc, GhiChu) "
//                   + "VALUES (?, ?, ?, ?, ?)";
//        XJdbc.executeUpdate(sql,
//                ls.getMaKhach(),
//                ls.getTenTaiKhoan(), // chỗ này lưu MaPhong hoặc TenTaiKhoan tùy bạn, nếu cần đổi mình chỉnh tiếp
//                ls.getNgayBatDau(),
//                ls.getNgayKetThuc(),
//                null // Ghi chú chưa dùng
//        );
//    }
//
//    @Override
//    public void update(LichSuNguoiThue ls) {
//        String sql = "UPDATE LichSuNguoiThue SET MaPhong=?, NgayBatDau=?, NgayKetThuc=?, GhiChu=? WHERE MaKhach=?";
//        XJdbc.executeUpdate(sql,
//                ls.getTenTaiKhoan(), // MaPhong
//                ls.getNgayBatDau(),
//                ls.getNgayKetThuc(),
//                null,
//                ls.getMaKhach()
//        );
//    }
//
//    @Override
//    public void delete(String maKhach) {
//        String sql = "DELETE FROM LichSuNguoiThue WHERE MaKhach=?";
//        XJdbc.executeUpdate(sql, maKhach);
//    }
//
//    @Override
//    public List<LichSuNguoiThue> selectAll() {
//        String sql = "SELECT ls.MaKhach, kt.TenKhach, kt.CMND AS CCCD, kt.SoDienThoai, kt.TenTaiKhoan, " +
//                     "ls.NgayBatDau, ls.NgayKetThuc " +
//                     "FROM LichSuNguoiThue ls " +
//                     "JOIN KhachThue kt ON ls.MaKhach = kt.MaKhach";
//        return selectBySql(sql);
//    }
//
// @Override
//public List<LichSuNguoiThue> selectByMaKhach(String maKhach) {
//    String sql = "SELECT ls.MaKhach, kt.TenKhach, kt.CMND AS CCCD, kt.SoDienThoai, kt.TenTaiKhoan, " +
//                 "ls.NgayBatDau, ls.NgayKetThuc " +
//                 "FROM LichSuNguoiThue ls " +
//                 "JOIN KhachThue kt ON ls.MaKhach = kt.MaKhach " +
//                 "WHERE ls.MaKhach = ?";
//    return selectBySql(sql, maKhach);
//}
//
//    private List<LichSuNguoiThue> selectBySql(String sql, Object... args) {
//        List<LichSuNguoiThue> list = new ArrayList<>();
//        try {
//            ResultSet rs = XJdbc.executeQuery(sql, args);
//            while (rs.next()) {
//                LichSuNguoiThue ls = new LichSuNguoiThue(
//                        rs.getString("MaKhach"),
//                        rs.getString("TenKhach"),
//                        rs.getString("CCCD"),
//                        rs.getString("SoDienThoai"),
//                        rs.getString("TenTaiKhoan"),
//                        rs.getString("NgayBatDau"),
//                        rs.getString("NgayKetThuc")
//                );
//                list.add(ls);
//            }
//            rs.getStatement().getConnection().close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return list;
//    }
//
//    // Thêm hàm hỗ trợ lấy MaKhach từ TenTaiKhoan
//    public String getMaKhachByTenTaiKhoan(String tenTaiKhoan) {
//        String sql = "SELECT MaKhach FROM KhachThue WHERE TenTaiKhoan = ?";
//        try {
//            ResultSet rs = XJdbc.executeQuery(sql, tenTaiKhoan);
//            if (rs.next()) {
//                return rs.getString("MaKhach");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
package main.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.dao.LichSuNguoiThueDAO;
import main.entity.LichSuNguoiThue;
import main.util.XJdbc;

public class LichSuNguoiThueDAOImpl implements LichSuNguoiThueDAO {

    @Override
    public void insert(LichSuNguoiThue ls) {
        String sql = "INSERT INTO LichSuNguoiThue (MaKhach, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?)";
        XJdbc.executeUpdate(sql,
                ls.getMaKhach(),
                ls.getNgayBatDau(),
                ls.getNgayKetThuc()
        );
    }

    @Override
    public void update(LichSuNguoiThue ls) {
        String sql = "UPDATE LichSuNguoiThue SET NgayBatDau=?, NgayKetThuc=? WHERE MaKhach=?";
        XJdbc.executeUpdate(sql,
                ls.getNgayBatDau(),
                ls.getNgayKetThuc(),
                ls.getMaKhach()
        );
    }

    @Override
    public void delete(String maKhach) {
        String sql = "DELETE FROM LichSuNguoiThue WHERE MaKhach=?";
        XJdbc.executeUpdate(sql, maKhach);
    }

    @Override
    public List<LichSuNguoiThue> selectAll() {
        String sql = "SELECT ls.MaKhach, kt.TenKhach, kt.CMND AS CCCD, kt.SoDienThoai, kt.TenTaiKhoan, " +
                     "ls.NgayBatDau, ls.NgayKetThuc " +
                     "FROM LichSuNguoiThue ls " +
                     "JOIN KhachThue kt ON ls.MaKhach = kt.MaKhach";
        return selectBySql(sql);
    }

    @Override
    public List<LichSuNguoiThue> selectByMaKhach(String maKhach) {
        String sql = "SELECT ls.MaKhach, kt.TenKhach, kt.CMND AS CCCD, kt.SoDienThoai, kt.TenTaiKhoan, " +
                     "ls.NgayBatDau, ls.NgayKetThuc " +
                     "FROM LichSuNguoiThue ls " +
                     "JOIN KhachThue kt ON ls.MaKhach = kt.MaKhach " +
                     "WHERE ls.MaKhach = ?";
        return selectBySql(sql, maKhach);
    }

    @Override
    public List<LichSuNguoiThue> selectByTenTaiKhoan(String tenTaiKhoan) {
        String sql = "SELECT ls.MaKhach, kt.TenKhach, kt.CMND AS CCCD, kt.SoDienThoai, kt.TenTaiKhoan, " +
                     "ls.NgayBatDau, ls.NgayKetThuc " +
                     "FROM LichSuNguoiThue ls " +
                     "JOIN KhachThue kt ON ls.MaKhach = kt.MaKhach " +
                     "WHERE kt.TenTaiKhoan = ?";
        return selectBySql(sql, tenTaiKhoan);
    }

    private List<LichSuNguoiThue> selectBySql(String sql, Object... args) {
        List<LichSuNguoiThue> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                LichSuNguoiThue ls = new LichSuNguoiThue(
                        rs.getString("MaKhach"),
                        rs.getString("TenKhach"),
                        rs.getString("CCCD"),
                        rs.getString("SoDienThoai"),
                        rs.getString("TenTaiKhoan"),
                        rs.getString("NgayBatDau"),
                        rs.getString("NgayKetThuc")
                );
                list.add(ls);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
