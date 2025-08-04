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
 // Thêm một hợp đồng mới
    boolean insert(HopDong hopDong);

    // Cập nhật thông tin hợp đồng
    boolean update(HopDong hopDong);

    // Xoá hợp đồng theo mã
    boolean delete(String maHopDong);

    // Tìm hợp đồng theo mã
    HopDong findById(String maHopDong);

    // Lấy toàn bộ danh sách hợp đồng
    List<HopDong> findAll();
    
        // 👉 Thêm dòng này:
    List<HopDong> findByKeyword(String keyword);
}
