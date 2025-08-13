package App.Entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Phong {
    private String maPhong;
    private String trangThai;     // Trống, Đang thuê
    private BigDecimal giaTien;   // DECIMAL(18,0)
    private BigDecimal dienTich;  // DECIMAL(18,2)
    private String diaChi;
    private String lienHe;
    private String moTa;
    private String anhPhong;
}
