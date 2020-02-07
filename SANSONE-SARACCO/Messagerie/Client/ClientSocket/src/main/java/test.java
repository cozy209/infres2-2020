import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class Test {

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


        Message msg = new Message("toto".getBytes(),"tutu".getBytes());

        msg.toString();

    }
}
