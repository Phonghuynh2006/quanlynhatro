package App.Entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HoaDon {
    private String maHoaDon; // ma hoa dơn
    private String maHopDong;
    private BigDecimal tienPhong;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private String trangThai;      // Chưa thanh toán / Đã thanh toán
    private Date ngayThanhToan;    // nullable
    private Date ngayTaoHoaDon;
}
