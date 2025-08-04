//package main.dao;
//
//import java.util.List;
//import main.entity.LichSuNguoiThue;
//
//public interface LichSuNguoiThueDAO {
//    void insert(LichSuNguoiThue ls);
//    void update(LichSuNguoiThue ls);
//    void delete(String maKhach);
//    List<LichSuNguoiThue> selectAll();
//   List<LichSuNguoiThue> selectByMaKhach(String maKhach);
// // đổi tên hàm
//}

package main.dao;

import java.util.List;
import main.entity.LichSuNguoiThue;

public interface LichSuNguoiThueDAO {
    void insert(LichSuNguoiThue ls);
    void update(LichSuNguoiThue ls);
    void delete(String maKhach);
    List<LichSuNguoiThue> selectAll();
    List<LichSuNguoiThue> selectByMaKhach(String maKhach);
    List<LichSuNguoiThue> selectByTenTaiKhoan(String tenTaiKhoan);
}
