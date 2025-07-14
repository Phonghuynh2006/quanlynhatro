/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.util;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 *
 * @author PHONG
 */
public class XIcon {
    /**
     * Đọc icon từ resource hoặc file
     * @param path
     * @param 
     * @return ImageIcon
     */
//    public static ImageIcon getIcon(String path) {
//        if(!path.contains("/") && !path.contains("\\")){ // resource name
//            return XIcon.getIcon("/polycafe/cafe/icons/" + path);
//        }
//        if(path.startsWith("/")){ // resource path
//            return new ImageIcon(XIcon.class.getResource(path));
//        }
//        return new ImageIcon(path);
//    }
    public static ImageIcon getIcon(String path) {
    if (!path.contains("/") && !path.contains("\\")) { // chỉ tên file, không có dấu /
        return XIcon.getIcon("D:/code/java/QUANLYNHATRO/src/main/resources/main/icon/" + path);
    }

    if (path.startsWith("/")) { // path dạng resource
        java.net.URL url = XIcon.class.getResource(path);
        if (url == null) {
            System.err.println("❌ Không tìm thấy ảnh trong resource: " + path);
            return new ImageIcon(); // Trả về icon rỗng để tránh crash
        }
        return new ImageIcon(url);
    }

    // Nếu là đường dẫn file (thực tế)
    File file = new File(path);
    if (!file.exists()) {
        System.err.println("❌ Không tìm thấy file ảnh ngoài: " + path);
        return new ImageIcon();
    }
    return new ImageIcon(path);
}

    /**
     * Đọc icon theo kích thước
     * @param path đường dẫn file hoặc tài nguyên
     * @param width chiều rộng
     * @param height chiều cao
     * @return Icon
     */
    public static ImageIcon getIcon(String path, int width, int height) {
        Image image = getIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
    /**
     * Thay đổi icon của JLabel
     * @param label JLabel cần thay đổi
     * @param path đường dẫn file hoặc tài nguyên
     */
    public static void setIcon(JLabel label, String path) {
        label.setIcon(XIcon.getIcon(path, label.getWidth(), label.getHeight()));
    }
    /**
     * Thay đổi icon của JLabel
     * @param label JLabel cần thay đổi
     * @param file file icon
     */
    public static void setIcon(JLabel label, File file) {
        XIcon.setIcon(label, file.getAbsolutePath());
    }
    /**
     * Sao chép file vào thư mục với tên file mới là duy nhất
     * @param fromFile file cần sao chép
     * @param folder thư mục đích
     * @return File đã sao chép
     */
    public static File copyTo(File fromFile, String folder) {
        String fileExt = fromFile.getName().substring(fromFile.getName().lastIndexOf("."));
        File toFile = new File(folder, XStr.getKey() + fileExt);
        toFile.getParentFile().mkdirs();
        try {
            Files.copy(fromFile.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return toFile;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static File copyTo(File fromFile) {
        return copyTo(fromFile, "files");
    }

}
