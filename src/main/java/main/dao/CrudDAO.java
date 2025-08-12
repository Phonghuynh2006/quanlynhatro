/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.dao;
import java.util.List;


/**
 *
 * @author PHONG
 * @param <T>
 * @param <ID>
 */
public interface CrudDAO<T, ID> {
        T create(T entity); 
    void update(T entity); 
    void deleteById(ID id); 
    List<T> findAll(); 
    T findById(ID id); 

}
