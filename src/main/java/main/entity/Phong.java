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
public class Phong {
    private String maPhong;       // Mã phòng (PK)
    private String trangThai;     // Trạng thái: Trống, Đang thuê, Đang sửa
    private double giaTien;       // Giá tiền
    private double dienTich;      // Diện tích (m²)
    private String diaChi;        // Địa chỉ
    private String lienHe;        // Số điện thoại liên hệ
    private String moTa;          // Mô tả phòng
    @Builder.Default
    private String anhPhong = "1.png"; // Ảnh mặc định

}
