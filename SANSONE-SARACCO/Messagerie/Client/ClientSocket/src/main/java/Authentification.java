import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Authentification {

    String username;
    String myHashedPassword;
    String othersHashedPassword;
    String salt;

    public Authentification(Connection con) {

        System.out.println("Bienvenue sur la messagerie sécurisée. Merci de vous connecter :");
        System.out.print("Nom d'utilisateur : ");
        username = new Scanner(System.in).nextLine();

        while (doesNotExist(username, con)) {
            System.out.print("Ce nom d'utilisateur n'existe pas. Veuillez entrer un nom d'utilisateur valide. \nNom d'utilisateur : ");
            username = new Scanner(System.in).nextLine();
        }

        System.out.print("Mot de passe : ");
        String password = new Scanner(System.in).nextLine();

        fetchSalt(con);
        fetchOthersHashedPassword(con);

        myHashedPassword = CryptoService.getSaltedHashedValueOf(password, salt);
        System.out.println(myHashedPassword);
    }

    private void fetchOthersHashedPassword(Connection con) {

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("select usr_hash_pwd from users where usr_name!=?");

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1));
                othersHashedPassword = rs.getString(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchSalt(Connection con) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("select usr_salt from users where usr_name=?");

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1));
                salt = rs.getString(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private boolean doesNotExist(String username, Connection con) {
        boolean doesNotExist = true;

        // TODO : bdd command
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("select count(usr_id) from users where usr_name=?");

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getBoolean(1)) {
                    System.out.println(rs.getBoolean(1));
                    doesNotExist = false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doesNotExist;
    }

    public String getChallenge() {
        SecureRandom random = new SecureRandom();
        byte challenge[] = new byte[20];
        random.nextBytes(challenge);

        return challenge.toString();
    }

    public String doOthersChallenge(String challenge) {
        return CryptoService.getSaltedHashedValueOf(othersHashedPassword, challenge);
    }

    public String doMyChallenge(String challenge) {
        return CryptoService.getSaltedHashedValueOf(myHashedPassword, challenge);
    }

    public boolean compareValues(String clientChallenge, String serverChallenge) {
        boolean isIdenticalValues = false;

        if (clientChallenge.equals(serverChallenge)) {
            isIdenticalValues = true;
        }

        return isIdenticalValues;
    }

    public String getUsername() {
        return username;
    }
}
