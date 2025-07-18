/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.dao;
import java.util.List;
import main.entity.HopDong;


/**
 *
 * @author PHONG
 */
public interface HopDongDAO {
     List<HopDong> getAll();
    void insert(HopDong hopDong);
    void update(HopDong hopDong);
    void delete(String maHopDong);
    HopDong findById(String maHopDong);
}
