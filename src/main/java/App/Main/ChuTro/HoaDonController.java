/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main.ChuTro;

import App.Entity.HoaDon;


/**
 *
 * @author PHONG
 */
public interface HoaDonController {
    void loadTable();
    void searchByHopDong(String keyword);
    void clearForm();
    void setForm(HoaDon h);
    HoaDon getForm();
    void add();
    void update();
    void delete();
    void tableRowClick(int row);

    // tiện ích dùng trong màn hình
    void autoFillGiaPhong();
    void recomputeFromInputs();

}
