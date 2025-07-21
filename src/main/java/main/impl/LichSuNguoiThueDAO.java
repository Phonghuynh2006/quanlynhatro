/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.impl;

import java.util.List;
import main.entity.LichSuNguoiThue;
import main.util.XJdbc;

/**
 *
 * @author Pham Thanh Luan
 */
public class LichSuNguoiThueDAO {
    
    public List<LichSuNguoiThue> selectAll() {
        String sql = "SELECT * FROM LichSuNguoiThue";
        return XJdbc.getBeanList(LichSuNguoiThue.class, sql);
    }
}
