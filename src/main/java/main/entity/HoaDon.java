/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

/**
 *
 * @author Pham Thanh Luan
 */
public class HoaDon {



    private String maHoaDon;
    private String maHopDong;
    private int thang;
    private int nam;
    private double tienPhong;
    private double tienDien;
    private double tienNuoc;
 public HoaDon() {
    }
    // Constructor đầy đủ tham số
    public HoaDon(String maHoaDon, String maHopDong, int thang, int nam, double tienPhong, double tienDien, double tienNuoc) {
        this.maHoaDon = maHoaDon;
        this.maHopDong = maHopDong;
        this.thang = thang;
        this.nam = nam;
        this.tienPhong = tienPhong;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
    }

    // Getter + Setter (nếu chưa có, nên thêm đầy đủ)
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(String maHopDong) {
        this.maHopDong = maHopDong;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(double tienPhong) {
        this.tienPhong = tienPhong;
    }

    public double getTienDien() {
        return tienDien;
    }

    public void setTienDien(double tienDien) {
        this.tienDien = tienDien;
    }

    public double getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(double tienNuoc) {
        this.tienNuoc = tienNuoc;
    }
}


