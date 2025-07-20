/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import java.sql.Date;

/**
 *
 * @author PHONG
 */
public class HopDong {
    private String maHopDong;
    private String maPhong;
    private String maKhach;
    private Date ngayBatDau;
    private Date ngayKetThuc;

    public HopDong() {
    }

    public HopDong(String maHopDong, String maPhong, String maKhach, Date ngayBatDau, Date ngayKetThuc) {
        this.maHopDong = maHopDong;
        this.maPhong = maPhong;
        this.maKhach = maKhach;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

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

    public String getMaKhach() {
        return maKhach;
    }

    public void setMaKhach(String maKhach) {
        this.maKhach = maKhach;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}

