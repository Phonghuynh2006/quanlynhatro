/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.dao;

import java.util.List;
import main.entity.HoaDon;
/**
 *
 * @author Pham Thanh Luan
 */
public interface HoaDonDAO {




    void insert(HoaDon hd);
    void update(HoaDon hd);
    void delete(String maHoaDon);
    List<HoaDon> selectAll();
}


