/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main.ChuTro;

import App.Entity.HopDong;



/**
 *
 * @author PHONG
 */
public interface HopDongController {
    void loadTable();
    void search(String keyword);
    void clearForm();
    void setForm(HopDong h);
    HopDong getForm();
    void add();
    void update();
    void delete();
    void tableRowClick(int row);
}
