/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.DAO;

import App.Entity.TaiKhoan;

/**
 *
 * @author PHONG
 */
public interface TaiKhoanDAO extends CrudDAO<TaiKhoan, Integer> {
    TaiKhoan findByTenTaiKhoanAndMatKhau(String tenTaiKhoan, String matKhau);
    boolean isTenDangNhapTonTai(String tenDangNhap);
    TaiKhoan findByTenTaiKhoan(String tenTaiKhoan);
}
