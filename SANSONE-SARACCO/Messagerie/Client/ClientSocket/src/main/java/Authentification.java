import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

public class Authentification {

    String username;
    String hashedPassword;

    public Authentification(Properties properties) {

        System.out.println("Bienvenue sur la messagerie sécurisée. Merci de vous connecter :");
        System.out.print("Nom d'utilisateur : ");
        username = new Scanner(System.in).nextLine();

        while (doesNotExist(username)){
            System.out.print("Ce nom d'utilisateur n'existe pas. Veuillez entrer un nom d'utilisateur valide. \nNom d'utilisateur : ");
            username = new Scanner(System.in).nextLine();
        }

        System.out.print("Mot de passe : ");
        String password = new Scanner(System.in).nextLine();

        String salt = properties.getProperty("SALT");

        hashedPassword = getSecurePassword(password,salt.getBytes());
    }

    private boolean doesNotExist(String username) {
        boolean doesNotExist = false;

        // bdd command

        return doesNotExist;
    }

    private static String getSecurePassword(String passwordToHash, byte[] salt) {

        String generatedPassword = null;
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(salt);

            byte[] bytes = md.digest(passwordToHash.getBytes());

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
}
