/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.dao;

import java.util.List;
import main.entity.Phong;
/**
 *
 * @author PHONG
 */
public interface PhongDAO extends CrudDAO<Phong, String> {
    
    // Tìm các phòng theo trạng thái (ví dụ: Trống, Đang thuê...)
    List<Phong> findByTrangThai(String trangThai);

    // Tìm các phòng trong khoảng giá
    List<Phong> findByGiaTienRange(double min, double max);

    // Tìm phòng theo địa chỉ (gợi ý tìm gần đúng)
    List<Phong> findByDiaChi(String keyword);


}
