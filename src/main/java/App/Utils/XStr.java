/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App.Utils;
import java.util.Base64;
import java.util.UUID;
/**
 *
 * @author PHONG
 */
public class XStr {

    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static String valueOf(Object object) {
        return object == null ? "" : String.valueOf(object);
    }

    public static String encodeB64(String text) {
        byte[] data = text.getBytes();
        return Base64.getEncoder().encodeToString(data);
    }

    public static String decodeB64(String text) {
        byte[] data = Base64.getDecoder().decode(text);
        return new String(data);
    }

    public static String getKey(String... args) {
        if (args.length == 0) {
            args = new String[]{UUID.randomUUID().toString(), String.valueOf(System.currentTimeMillis())};
        }
        int hashCode = String.join("-", args).hashCode();
        String key = "XXXXXXXX" + Integer.toHexString(Math.abs(hashCode)).toUpperCase();
        return key.substring(key.length() - 8);
    }

    public static void main(String[] args) {
        String encodedText = XStr.encodeB64("Nguyễn Nghiệm");
        System.out.println(encodedText); // => Tmd1eT9uIE5naGk/bQ==

        String decodedText = XStr.decodeB64("Tmd1eT9uIE5naGk/bQ==");
        System.out.println(decodedText); // => Nguyễn Nghiệm

        String key1 = XStr.getKey();
        System.out.println(key1); // => XXXXXXXX (8 random characters)

        String key2 = XStr.getKey("Nguyễn", "Nghiệm", "PolyHCM");
        System.out.println(key2); // => 329FB580
    }


}
