/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
/**
 *
 * @author PHONG
 */
public class ThongTinNguoiThue {




    private String tenDangNhap;      // Khóa ngoại đến TaiKhoan
    private String hoVaTen;
    private String cccd;
    private boolean gioiTinh;
    private LocalDate ngaySinh;
    private String diaChi;
    private String soDienThoai;
    private String email;
    private String photo;


}
