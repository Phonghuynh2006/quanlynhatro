package main.entity;
import java.math.BigDecimal;

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
    private BigDecimal tienPhong;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private String trangThaiThanhToan;


}


