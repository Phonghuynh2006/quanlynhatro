package main.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDon {
    private String maHoaDon;
    private String maHopDong;
    private int thang;
    private int nam;
    private double tienPhong;
    private double tienDien;
    private double tienNuoc;
    public String getMaHoaDon() {
    return maHoaDon;
}

public void setMaHoaDon(String maHoaDon) {
    this.maHoaDon = maHoaDon;
}

}
