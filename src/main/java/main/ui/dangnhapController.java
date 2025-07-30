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
