/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.DAO;

import App.Entity.LichSuNguoiDung;
import java.util.List;

/**
 *
 * @author PHONG
 */
public interface LichSuNguoiDungDAO extends CrudDAO<LichSuNguoiDung, Integer> {
    void log(Integer maNguoiDung, String hanhDong);     // ghi nhanh lịch sử
    List<LichSuNguoiDung> findByMaNguoiDung(Integer maNguoiDung);
}