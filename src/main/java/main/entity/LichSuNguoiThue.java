/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import java.util.Date;

/**
 *
 * @author Pham Thanh Luan
 */
public class LichSuNguoiThue {
      private int maLichSu;
    private String maKhach;
    private String maPhong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String ghiChu;

    // Getters + Setters
    public int getMaLichSu() { return maLichSu; }
    public void setMaLichSu(int maLichSu) { this.maLichSu = maLichSu; }

    public String getMaKhach() { return maKhach; }
    public void setMaKhach(String maKhach) { this.maKhach = maKhach; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

}
