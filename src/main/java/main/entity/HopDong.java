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
    private String maKhach;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String tenNhanVien;

    // Constructor chỉ cho DAO
    public HopDong(String maHopDong, String maPhong, String maKhach, Date ngayBatDau, Date ngayKetThuc) {
        this.maHopDong = maHopDong;
        this.maPhong = maPhong;
        this.maKhach = maKhach;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    
    public Object[] toRow() {
        return new Object[] {
            maHopDong, maPhong, maKhach, ngayBatDau, ngayKetThuc, tenNhanVien
        };
    }
}
