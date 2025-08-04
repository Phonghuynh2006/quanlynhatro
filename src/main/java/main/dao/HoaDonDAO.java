package main.dao;

import java.util.List;
import main.entity.HoaDon;

public interface HoaDonDAO {
    
    void insert(HoaDon hd);
    void update(HoaDon hd);
    void delete(String maHoaDon);
    List<HoaDon> selectAll();
    List<HoaDon> selectByUserId(int maNguoiDung);
}
