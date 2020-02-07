import sun.net.ConnectionResetException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

public class test {

    public static void main(String[] args) throws Exception {

        /*KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(CryptoService.AES_KEY_SIZE);

        // Generate Key
        SecretKey key = keyGenerator.generateKey();
        byte[] IV = new byte[CryptoService.GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        String toto="";
        while (!toto.equals("QUIT")) {
            toto = new Scanner(System.in).nextLine();

            byte[] cipherText = CryptoService.encrypt(toto.getBytes(), key, IV);
            System.out.println(Base64.getEncoder().encodeToString(cipherText));

            String decryptedText = CryptoService.decrypt(cipherText, key, IV);
            System.out.println(decryptedText);
        }

        Authentification authent = new Authentification(null);

        String challenge = authent.getChallenge();


        String serverChallenge = authent.doChallenge(challenge);
        String clientChallenge = authent.doChallenge(challenge);

        boolean ok = authent.compareValues(clientChallenge,serverChallenge);

        System.out.println(ok);*/

        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        System.out.println(new String(salt));

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/client","nini","patate");

            PreparedStatement stmt= null;

                stmt = con.prepareStatement("Insert into user VALUES (,,),(,,)");


                stmt.executeQuery();


        }catch(Exception e){ e.printStackTrace();}
    }
}
