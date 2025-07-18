/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

/**
 *
 * @author PHONG
 */
public class HopDong {
   private String maHopDong;
    private String maPhong;
    private String tenKhachThue;
    private String ngayBatDau;
    private String ngayKetThuc;
    private String giaThue;
    private String trangThai;

    // Constructor không tham số (bắt buộc khi làm việc với JTable/form)
    public HopDong() {
    }

    // Constructor đầy đủ
    public HopDong(String maHopDong, String maPhong, String tenKhachThue,
                   String ngayBatDau, String ngayKetThuc, String giaThue, String trangThai) {
        this.maHopDong = maHopDong;
        this.maPhong = maPhong;
        this.tenKhachThue = tenKhachThue;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giaThue = giaThue;
        this.trangThai = trangThai;
    }

    // Getters & Setters
    public String getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(String maHopDong) {
        this.maHopDong = maHopDong;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenKhachThue() {
        return tenKhachThue;
    }

    public void setTenKhachThue(String tenKhachThue) {
        this.tenKhachThue = tenKhachThue;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(String giaThue) {
        this.giaThue = giaThue;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "HopDong{" +
                "maHopDong='" + maHopDong + '\'' +
                ", maPhong='" + maPhong + '\'' +
                ", tenKhachThue='" + tenKhachThue + '\'' +
                ", ngayBatDau='" + ngayBatDau + '\'' +
                ", ngayKetThuc='" + ngayKetThuc + '\'' +
                ", giaThue='" + giaThue + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}

