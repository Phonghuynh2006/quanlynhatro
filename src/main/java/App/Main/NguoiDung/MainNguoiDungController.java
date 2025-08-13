/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main.NguoiDung;

import App.Main.NguoiDung.HoaDonJDialog;
import App.Main.NguoiDung.HopDongJDialog;
import App.Main.LichSuNguoiDungJDialog;
import App.Main.ThongTinNguoiDungJDialog;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author PHONG
 */
public interface MainNguoiDungController {

    /** Nạp danh sách phòng TRỐNG vào bảng. */
    void loadPhongTrong();

    /** Hiển thị thông tin phần header (ID phòng, tên người thuê, hóa đơn mới nhất, tháng...). */
    void showHeaderInfo();

    /** Khi chọn 1 dòng trong bảng phòng trống. */
    void tableRowClick(int row);

    /** Xử lý đăng ký phòng cho người dùng hiện tại. */
    void dangKyPhong();

    /** Reset mini-form bên trái (clear ô nhập, bỏ chọn bảng). */
    void resetMiniForm();
    
    default void showJDialog(JDialog dialog){
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    default void showHopDongJDialog(JFrame frame){
        this.showJDialog(new HopDongJDialog(frame, true));
    }
    default void showHoaDonJDialog(JFrame frame){
        this.showJDialog(new HoaDonJDialog(frame, true));
    }
    default void showThongTinNguoiDungJDialog(JFrame frame){
        this.showJDialog(new ThongTinNguoiDungJDialog(frame, true));
    }
    default void showLichSuNguoiDungJDialog(JFrame frame){
        this.showJDialog(new LichSuNguoiDungJDialog(frame, true));
    }
}
