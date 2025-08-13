/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.DAO;

import App.Entity.Phong;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author PHONG
 */
public interface PhongDAO extends CrudDAO<Phong, String> {
    List<Phong> findByTrangThai(String trangThai);              // ví dụ: "Trống", "Đang thuê"
    List<Phong> findPhongTrong();                               // tiện alias cho findByTrangThai("Trống")
    void updateTrangThai(String maPhong, String trangThai);
    List<Phong> search(String tuKhoa,                           // tìm theo địa chỉ/mô tả/liên hệ
                       BigDecimal giaMin, BigDecimal giaMax,
                       BigDecimal dienTichMin, BigDecimal dienTichMax);
}
