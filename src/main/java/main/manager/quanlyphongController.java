/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.manager;

import java.util.List;
import main.entity.Phong;
/**
 *
 * @author PHONG
 */
public interface quanlyphongController {


    /** Mở form ở giữa màn hình */
    void open();

    /** Đóng form */
    void close();

    /** Load danh sách phòng từ DB lên bảng */
    void loadTable();

    /** Thêm phòng mới */
    void insert();

    /** Cập nhật phòng đã chọn */
    void update();

    /** Xóa phòng */
    void delete();

    /** Xóa trắng form */
    void clearForm();

    /** Đổ dữ liệu phòng vào form để sửa */
    void setForm(Phong p);

    /** Lấy dữ liệu từ form để tạo/ghi vào đối tượng Phong */
    Phong getForm();

    /** Khi người dùng click vào JTable để xem chi tiết phòng */
    void editFromTable();

    /** Kiểm tra form có hợp lệ không trước khi insert/update */
    boolean isValidForm();

    /** Load ảnh từ đường dẫn lên giao diện nếu có */
    void loadImage(String path);

    /** Khi người dùng chọn hình ảnh để thay ảnh phòng */
    void chooseImage();
    void setTab(int index); // ✅ thêm dòng này


}
