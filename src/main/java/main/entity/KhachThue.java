/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

/**
 *
 * @author PHONG
 */
public class KhachThue {
    private String maKhach;
    private String hoTen;
    private String cmnd;
    private String soDienThoai;
    private String tenDangNhap;

    public KhachThue() {
    }

    public KhachThue(String maKhach, String hoTen, String cmnd, String soDienThoai, String tenDangNhap) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.cmnd = cmnd;
        this.soDienThoai = soDienThoai;
        this.tenDangNhap = tenDangNhap;
    }

    public String getMaKhach() {
        return maKhach;
    }

    public void setMaKhach(String maKhach) {
        this.maKhach = maKhach;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    @Override
    public String toString() {
        return this.hoTen; // có thể đổi sang return maKhach nếu cần
    }
}

