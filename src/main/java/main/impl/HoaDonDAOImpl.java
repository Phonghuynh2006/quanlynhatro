package main.impl;

import main.dao.HoaDonDAO;
import main.entity.HoaDon;
import java.util.List;
import main.util.XJdbc;

public class HoaDonDAOImpl implements HoaDonDAO{

    @Override
    public void insert(HoaDon hd) {
        String sql = """
            INSERT INTO HoaDon (MaHoaDon, MaHopDong, Thang, Nam, TienPhong, TienDien, TienNuoc)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        XJdbc.executeUpdate(sql,
                hd.getMaHoaDon(),
                hd.getMaHopDong(),
                hd.getThang(),
                hd.getNam(),
                hd.getTienPhong(),
                hd.getTienDien(),
                hd.getTienNuoc()
        );
    }

    @Override
    public void update(HoaDon hd) {
        String sql = """
            UPDATE HoaDon
            SET MaHopDong=?, Thang=?, Nam=?, TienPhong=?, TienDien=?, TienNuoc=?
            WHERE MaHoaDon=?
        """;
        XJdbc.executeUpdate(sql,
                hd.getMaHopDong(),
                hd.getThang(),
                hd.getNam(),
                hd.getTienPhong(),
                hd.getTienDien(),
                hd.getTienNuoc(),
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
String sql = """
    SELECT MaHoaDon   AS maHoaDon,
           MaHopDong  AS maHopDong,
           Thang      AS thang,
           Nam        AS nam,
           TienPhong  AS tienPhong,
           TienDien   AS tienDien,
           TienNuoc   AS tienNuoc
    FROM HoaDon
    ORDER BY Nam DESC, Thang DESC
""";
return XJdbc.getBeanList(HoaDon.class, sql);

}

    @Override
public List<HoaDon> selectByUserId(int maNguoiDung) {
    String sql = """
        SELECT 
            hd.MaHoaDon   AS maHoaDon,
            hd.MaHopDong  AS maHopDong,
            hd.Thang      AS thang,
            hd.Nam        AS nam,
            hd.TienPhong  AS tienPhong,
            hd.TienDien   AS tienDien,
            hd.TienNuoc   AS tienNuoc
        FROM HoaDon hd
        JOIN HopDong h ON hd.MaHopDong = h.MaHopDong
        JOIN KhachThue kt ON kt.MaKhach = h.MaKhach
        JOIN TaiKhoan tk ON tk.TenTaiKhoan = kt.TenTaiKhoan
        WHERE tk.MaNguoiDung = ?
        ORDER BY hd.Nam DESC, hd.Thang DESC
    """;
    return XJdbc.getBeanList(HoaDon.class, sql, maNguoiDung);
}
}
