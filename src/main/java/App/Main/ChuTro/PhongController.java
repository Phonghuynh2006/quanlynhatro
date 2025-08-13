/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main.ChuTro;

import App.Entity.Phong;


/**
 *
 * @author PHONG
 */
public interface PhongController {
    void loadTable();
    void search(String keyword);
    void clearForm();
    void setForm(Phong p);
    Phong getForm();
    void add();
    void update();
    void delete();
    void tableRowClick(int row);

}
