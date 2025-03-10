package cohort46.gracebakeryapi.accounting.security.JWT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SafeAESUtil {
    private static final String SECRET_KEY = "MySecretAESKey16"; // ДОЛЖНО быть 16, 24 или 32 символа

    // Шифрование строки
    public static String encrypt(String plainText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return "ERROR"; // Всегда возвращаем строку, даже при сбое
        }
    }

    // Дешифрование строки
    public static String decrypt(String encryptedText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            return new String(cipher.doFinal(decodedBytes));
        } catch (Exception e) {
            return "ERROR"; // Всегда возвращаем строку, даже при ошибке
        }
    }
/*
    public static void main(String[] args) {
        String originalText = "HelloWorld";

        String encryptedText = encrypt(originalText); // Шифруем
        String decryptedText = decrypt(encryptedText); // Дешифруем обратно

        System.out.println("Исходный текст: " + originalText);
        System.out.println("Зашифрованный текст: " + encryptedText);
        System.out.println("Расшифрованный текст: " + decryptedText);

        // Пример ошибки: расшифруем некорректные данные
        System.out.println("Некорректные данные: " + decrypt("abcdef"));
    }

 */
}
