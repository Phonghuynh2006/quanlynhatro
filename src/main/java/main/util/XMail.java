/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 *
 * @author PHONG
 */
public class XMail {

    // Phương thức tĩnh để gửi email
    public static boolean sendMail(String to, String code) {
        final String username = "phonghuynh031226@gmail.com"; // Địa chỉ email gửi
        final String password = "ochcazzetzicifbp"; // Mật khẩu ứng dụng Gmail

        // Cấu hình các thuộc tính cho mail server
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Máy chủ gửi mail (SMTP)
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Bật TLS
        props.put("mail.smtp.port", "587"); // Cổng SMTP

        // Khởi tạo session và gửi email
        try {
            // Tạo một Session với thông tin đăng nhập
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Tạo thông tin email
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username)); // Địa chỉ email gửi
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Địa chỉ email nhận
            msg.setSubject("Mã xác nhận đổi mật khẩu"); // Tiêu đề email
            msg.setText("Mã xác nhận của bạn là: " + code); // Nội dung email

            // Gửi email
            Transport.send(msg);
            return true; // Gửi thành công, trả về true
        } catch (MessagingException e) {
            e.printStackTrace(); // In ra lỗi nếu có
            return false; // Gửi thất bại, trả về false
        }
    }

    public static void main(String[] args) {
        // Test gửi email (có thể xóa sau khi hoàn thiện)
        boolean result = sendMail("recipient@example.com", "123456");
        if (result) {
            System.out.println("Email gửi thành công!");
        } else {
            System.out.println("Gửi email thất bại.");
        }
    }
}
