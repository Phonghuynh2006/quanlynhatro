/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main.ChuTro;

import App.Main.LichSuNguoiDungJDialog;
import App.Main.ThongTinNguoiDungJDialog;
import App.Main.dangkyJdialog;
import App.Main.dangnhapJdialog;
import App.Utils.XDialog;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author PHONG
 */
public interface MainChuTroController {
//    void loadDashboard();
//    void openPhong();
//    void openHopDong();
//    void openHoaDon();
//    void openTaiKhoan();
//    
//    
        default void exit(){
        if(XDialog.confirm("Bạn muốn kết thúc?")){
            System.exit(0);
        }
    }
    default void showJDialog(JDialog dialog){
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    default void showPhongJDialog(JFrame frame){
        this.showJDialog(new PhongJDialog(frame, true));
    }
    default void showHopDongJDialog(JFrame frame){
        this.showJDialog(new HopDongJDialog(frame, true));
    }
    default void showHoaDonJDialog(JFrame frame){
        this.showJDialog(new HoaDonJDialog(frame, true));
    }
    default void showTaiKhoanJDialog(JFrame frame){
        this.showJDialog(new TaiKhoanJDialog(frame, true));
    }
    default void showThongTinNguoiDungJDialog(JFrame frame){
        this.showJDialog(new ThongTinNguoiDungJDialog(frame, true));
    }
    default void showLichSuNguoiDungJDialog(JFrame frame){
        this.showJDialog(new LichSuNguoiDungJDialog(frame, true));
    }
}
