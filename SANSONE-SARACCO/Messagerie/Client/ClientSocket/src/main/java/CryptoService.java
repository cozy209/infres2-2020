import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoService {


    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;
    public static final String SALT_KEY = "SALT";


    public static void main(String[] args) throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);

        // Generate Key
        SecretKey key = keyGenerator.generateKey();
    }

    static byte[] getNewIv(){
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        return IV;
    }

    public static Message encrypt(byte[] plaintext, SecretKeySpec key) throws Exception {

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create GCMParameterSpec
        byte[]IV= getNewIv();
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

        // Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        Message message= new Message(IV,cipherText);

        return message;
    }

    public static String decrypt(Message message, SecretKeySpec key) throws Exception {

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");


        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, message.getIV());

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(message.getMessage());

        return new String(decryptedText);
    }

    public static String getSaltedHashedValueOf(String valueToHash, byte[] salt) {

        String generatedPassword = null;
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(salt);

            byte[] bytes = md.digest(valueToHash.getBytes());

            // Convert the decimal values of the byte[] in hexadecimal
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }

    public static SecretKeySpec getKey(String keyPassword, Properties properties) throws Exception{
        //String salt = properties.getProperty(SALT_KEY);
        String salt = "toto";

        KeySpec spec = new PBEKeySpec(keyPassword.toCharArray(), salt.getBytes(), 65536, 256); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
        SecretKeySpec keyToReturn = new SecretKeySpec(key, "AES");

        return  keyToReturn;
    }
}