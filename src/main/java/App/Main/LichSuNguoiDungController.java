/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main;

/**
 *
 * @author PHONG
 */
public interface LichSuNguoiDungController {
    void loadTable();                 // load toàn bộ lịch sử
    void loadByUser(Integer userId);  // load theo mã người dùng
    void search(String keyword);      // lọc tạm trên client (không bắt buộc dùng)
}
