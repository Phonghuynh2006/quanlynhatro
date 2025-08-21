/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main;

import App.Utils.XDialog;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author PHONG
 */
public interface TrangChuChungController {
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
    default void showdangkyJDialog(JFrame frame){
        this.showJDialog(new dangkyJdialog(frame, true));
    }
    default void showdangnhapJDialog(JFrame frame){
        this.showJDialog(new dangnhapJdialog(frame, true));
    }

}
