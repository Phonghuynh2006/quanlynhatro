/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

/**
 *
 * @author PHONG
 */
public class App {
        public static void main(String[] args) {
        new xinchaoJdialog(null, true).setVisible(true);

        dangnhapJdialog login = new dangnhapJdialog(null, true);
        login.setVisible(true);

        if (login.isDangNhapThanhCong()) {
//            new quanlynhatro().setVisible(true);
//        } else {
            System.out.println("Đăng nhập thất bại hoặc bị hủy");
        }
    }

}
