/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.dao;

import java.util.List;
import main.entity.KhachThue;

/**
 *
 * @author PHONG
 */
public interface KhachThueDAO extends CrudDAO<CrudDAO, String>{
    void insert(KhachThue kt);
    void update(KhachThue kt);
    void delete(String maKhach);
    KhachThue selectById(String maKhach);
    List<KhachThue> selectAll();
}
