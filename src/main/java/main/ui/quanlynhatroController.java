/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import main.manager.doanhthuJdialog;
import main.manager.lichsuadminJdialog;
import main.manager.lichsunguoithueJdialog;
import main.manager.quanlyhoadonJdialog;
import main.manager.quanlyhopdongJdialog;
import main.manager.quanlykhachthueJdialog;
import main.manager.quanlyphongJdialog;
import main.manager.quanlytaikhoanJdialog;
import main.manager.thongtinnguoithueJdialog;
import main.manager.xemhoadonJdialog;
import main.manager.xemphongJdialog;
import main.util.XDialog;

/**
 *
 * @author PHONG
 */
public interface quanlynhatroController {
    /**
     * Hiển thị cửa sổ chào
     * Hiển thị cửa sổ đăng nhập
     * Hiển thị thông tin user đăng nhập
     * Disable/Enable các thành phần tùy thuộc vào vai trò đăng nhập
     */
    void init();
    
    default void exit(){
        if(XDialog.confirm("Bạn muốn kết thúc?")){
            System.exit(0);
        }
    }
    default void showJDialog(JDialog dialog){
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    default void showWelcomeJDialog(JFrame frame){
        this.showJDialog(new xinchaoJdialog(frame, true));
    }
    default void showLoginJDialog(JFrame frame){
        this.showJDialog(new dangnhapJdialog(frame, true));
    }
//        default void showdangkyJDialog(JFrame frame){
//        this.showJDialog(new dangkyJdialog(frame, true));
//    }
    default void showChangePasswordJDialog(JFrame frame){
        this.showJDialog(new doimatkhauJdialog(frame, true));
    }
    default void showxemphongJDialog(JFrame frame){
        this.showJDialog(new xemphongJdialog(frame, true));
    }
    default void showxemhoadonJDialog(JFrame frame){
        int maNguoiDung = 1; // hoặc lấy từ người dùng đăng nhập
new xemhoadonJdialog(new JFrame(), true, maNguoiDung);

    }
    
    default void showthongtinnguoithueJdialog(JFrame frame){
        this.showJDialog(new thongtinnguoithueJdialog(frame, true));
    }
    default void showquanlytaikhoanJdialog(JFrame frame){
        this.showJDialog(new quanlytaikhoanJdialog(frame, true));
    }
    default void showquanlyphongJdialog(JFrame frame){
        this.showJDialog(new quanlyphongJdialog(frame, true));
    }
    default void showquanlykhachthueJdialog(JFrame frame){
        this.showJDialog(new quanlykhachthueJdialog(frame, true));
    }
    default void showquanlyhopdongJdialog(JFrame frame){
        this.showJDialog(new quanlyhopdongJdialog(frame, true));
    }
    default void showquanlyhoadonJdialog(JFrame frame){
        this.showJDialog(new quanlyhoadonJdialog(frame, true));
    }
    
        default void showdoanhthuJdialog(JFrame frame){
        this.showJDialog(new doanhthuJdialog(frame, true));
    }
    default void showlichsuadminJdialog(JFrame frame){
        this.showJDialog(new lichsuadminJdialog(frame, true));
    }
    default void showlichsunguoithueJdialog(JFrame frame){
        this.showJDialog(new lichsunguoithueJdialog(frame, true));
    }

}
