/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package App.Main;

import App.Utils.XDialog;
//import static App.Main.ChuTro.Main_ChuTro.main;
import javax.swing.JDialog;

/**
 *
 * @author PHONG
 */
public interface dangnhapController {
void open(); 
void login(); 
default void exit(){ 
if(XDialog.confirm("Bạn muốn kết thúc?")){ 
System.exit(0); 
        } 
    } 
    default void showJDialog(JDialog dialog){
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
//    default void showdangkyJDialog(JFrame frame){
//        this.showJDialog(new dangkyJdialog(frame, true));
//    }

}
