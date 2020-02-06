import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class test {

    public static void main(String[] args) throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(CryptoService.AES_KEY_SIZE);

        // Generate Key
        SecretKey key = keyGenerator.generateKey();
        byte[] IV = new byte[CryptoService.GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);










        String toto="";
        while (!toto.equals("QUIT")) {
            toto = new Scanner(System.in).nextLine();

            byte[][] Message = CryptoService.encrypt(toto.getBytes(), key);
            System.out.println(Base64.getEncoder().encodeToString(Message[0]));

            String decryptedText = CryptoService.decrypt(Message, key);
            System.out.println(decryptedText);
        }

        //Authentification authent = new Authentification(null);
    }
}
