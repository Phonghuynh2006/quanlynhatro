package main.entity;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HopDong {
   private String maHopDong;
    private String maPhong;
    private int maNguoiThue;     // đổi thành int
    private int maNhanVien;      // đổi thành int
    private Date ngayBatDau;
    private Date ngayKetThuc;

    private String tenNhanVien;  // JOIN để hiển thị
    private String tenNguoiThue; // (nếu bạn cần JOIN để hiển thị)

    // Constructor cho DAO không join
    public HopDong(String maHopDong, String maPhong, int maNguoiThue, int maNhanVien, Date ngayBatDau, Date ngayKetThuc) {
        this.maHopDong = maHopDong;
        this.maPhong = maPhong;
        this.maNguoiThue = maNguoiThue;
        this.maNhanVien = maNhanVien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public Object[] toRow() {
        return new Object[] {
            maHopDong, maPhong, maNguoiThue, maNhanVien, ngayBatDau, ngayKetThuc, tenNhanVien
        };
    }
}
