/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
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
    default void showChangePasswordJDialog(JFrame frame){
        this.showJDialog(new doimatkhauJdialog(frame, true));
    }
//    default void showSalesJDialog(JFrame frame){
//        this.showJDialog(new SalesJDialog(frame, true));
//    }
//    default void showHistoryJDialog(JFrame frame){
//        this.showJDialog(new HistoryJDialog(frame, true));
//    }
//    
//    default void showDrinkManagerJDialog(JFrame frame){
//        this.showJDialog(new DrinkManagerJDialog(frame, true));
//    }
//    default void showCategoryManagerJDialog(JFrame frame){
//        this.showJDialog(new CategoryManagerJDialog(frame, true));
//    }
//    default void showCardManagerJDialog(JFrame frame){
//        this.showJDialog(new CardManagerJDialog(frame, true));
//    }
//    default void showBillManagerJDialog(JFrame frame){
//        this.showJDialog(new BillManagerJDialog(frame, true));
//    }
//    default void showUserManagerJDialog(JFrame frame){
//        this.showJDialog(new UserManagerJDialog(frame, true));
//    }
//    default void showRevenueManagerJDialog(JFrame frame){
//        this.showJDialog(new RevenueManagerJDialog(frame, true));
//    }

}
