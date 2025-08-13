/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.DAO;

import App.Entity.HopDong;
import java.util.List;

/**
 *
 * @author PHONG
 */
public interface HopDongDAO extends CrudDAO<HopDong, String> {
    List<HopDong> findByMaNguoiDung(Integer maNguoiDung);
    HopDong findDangHieuLucByMaPhong(String maPhong);  // hợp đồng còn hiệu lực của phòng
}
