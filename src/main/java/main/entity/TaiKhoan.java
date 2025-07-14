/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 *
 * @author PHONG
 */
public class TaiKhoan {

    private String tenDangNhap;      // TenDangNhap
    private String matKhau;          // MatKhau
    private boolean vaiTro;          // true = Admin, false = Người thuê
    private String fullname;         // Fullname
    @Builder.Default
    private String photo = "photo.png"; // Photo mặc định nếu không có
    
}
