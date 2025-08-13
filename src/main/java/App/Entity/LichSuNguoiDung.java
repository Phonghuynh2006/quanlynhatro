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
public class LichSuNguoiDung {
    private Integer id;
    private Integer maNguoiDung;
    private Date thoiGian;     // DATETIME
    private String hanhDong;
}
// líchu
