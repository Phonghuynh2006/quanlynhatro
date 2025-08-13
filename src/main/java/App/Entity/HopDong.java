package App.Entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HopDong {
    private String maHopDong;
    private Integer maNguoiDung;
    private String maPhong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
}
