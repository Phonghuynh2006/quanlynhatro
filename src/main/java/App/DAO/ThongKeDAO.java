/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.DAO;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 *
 * @author PHONG
 */
public interface ThongKeDAO {
    BigDecimal getTongDoanhThu() throws SQLException;          // SUM(tienPhong + tienDien + tienNuoc) của hóa đơn ĐÃ thanh toán
    int getSoKhachDangThue() throws SQLException;               // Đếm phòng có trangThai = 'Đang thuê'
    int getSoHopDongSapHetHan(int soNgay) throws SQLException;  // Hợp đồng kết thúc trong N ngày tới
    int getSoHoaDonChuaThanhToan() throws SQLException;         // Hóa đơn trạng thái 'Chưa thanh toán'

}
