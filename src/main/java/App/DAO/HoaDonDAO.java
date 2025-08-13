/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.DAO;

import App.Entity.HoaDon;
import java.util.Date;
import java.util.List;

/**
 *
 * @author PHONG
 */
public interface HoaDonDAO extends CrudDAO<HoaDon, String> {
    List<HoaDon> findByMaHopDong(String maHopDong);
    List<HoaDon> findChuaThanhToanByMaHopDong(String maHopDong);
    void capNhatTrangThaiThanhToan(String maHoaDon, Date ngayThanhToan); // set Đã thanh toán + ngày TT
}
// tunh