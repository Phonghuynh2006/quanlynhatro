/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.dao;

import main.entity.ThongTinNguoiThue;
/**
 *
 * @author PHONG
 */
public interface ThongTinNguoiThueDAO {


    ThongTinNguoiThue findByTenDangNhap(String tenDangNhap);
    
    void insert(ThongTinNguoiThue entity);
    void update(ThongTinNguoiThue entity);

}
