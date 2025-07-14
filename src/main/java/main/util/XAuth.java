/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.util;

import main.entity.TaiKhoan;

/**
 *
 * @author PHONG
 */
public class XAuth {
//    public static TaiKhoan user = TaiKhoan.builder() 
//            .username("user1@") 
//            .password("123") 
//            .enabled(true) 
//            .manager(true) 
//            .fullname("Nguyễn Văn Tèo") 
//            .photo("trump.png") 
//            .build(); // biến user này sẽ được thay thế sau khi đăng nhập 


    // Biến toàn cục dùng để lưu thông tin tài khoản đăng nhập hiện tại
    public static TaiKhoan user;

    // Gán tạm giá trị test (có thể xóa đi khi có login thực tế)
//    static {
//        user = TaiKhoan.builder()
//                .tenDangNhap("admin1")
//                .matKhau("123456")
//                .vaiTro(true) // true = Admin, false = Người thuê
//                .fullname("Nguyễn Văn Tèo")
//                .photo("admin.png")
//                .build();
//    }

    // Kiểm tra đã đăng nhập hay chưa
    public static boolean isLogin() {
        return user != null;
    }

    // Đăng xuất
    public static void logoff() {
        user = null;
    }

}
