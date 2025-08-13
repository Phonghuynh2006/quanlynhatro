/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main;

import App.Entity.TaiKhoan;

/**
 *
 * @author PHONG
 */
public interface ThongTinNguoiDungController {
    void     loadByUsername(String username);
    void     setForm(TaiKhoan tk);
    TaiKhoan getForm();
    void     update();
}
