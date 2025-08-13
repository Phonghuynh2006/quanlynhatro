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
public class TaiKhoan {
    private Integer maNguoiDung;
    private String tenTaiKhoan;
    private String matKhau;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String cccd;
    private String dienThoai;
    private String email;
    private String diaChi;
    private Integer vaiTro;     // 1=Admin, 2=Người thuê
    private Integer trangThai;  // 1=Hoạt động, 0=Tạm khóa
    private String hinhAnh;
}
