import java.security.SecureRandom;
import java.sql.*;
import java.util.Properties;
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

        while (doesNotExist(username)){
            System.out.print("Ce nom d'utilisateur n'existe pas. Veuillez entrer un nom d'utilisateur valide. \nNom d'utilisateur : ");
            username = new Scanner(System.in).nextLine();
        }

        System.out.print("Mot de passe : ");
        String password = new Scanner(System.in).nextLine();

        fetchSalt(con);
        fetchOthersHashedPassword(con);

        myHashedPassword = CryptoService.getSaltedHashedValueOf(password,salt);
    }

    private void fetchOthersHashedPassword(Connection con){
        othersHashedPassword = ""; //TODO : bdd command

        PreparedStatement stmt= null;
        try {
            stmt = con.prepareStatement("select usr_hash_pwd from users where usr_name!=?");

       stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();

           while(rs.next()){
                System.out.println(rs.getString(1));
            }


    } catch (SQLException e) {
        e.printStackTrace();
    }}

    private void fetchSalt(Connection con){
        salt = "".getBytes(); //TODO : bdd command

        PreparedStatement stmt= null;
        try {
            stmt = con.prepareStatement("select usr_salt from users where usr_name=?");

            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                System.out.println(rs.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean doesNotExist(String username) {
        boolean doesNotExist = false;

        // TODO : bdd command

        return doesNotExist;
    }

    public String getChallenge(){
        SecureRandom random = new SecureRandom();
        byte challenge[] = new byte[20];
        random.nextBytes(challenge);

        return challenge.toString();
    }

    public String doOthersChallenge(String challenge){
        return CryptoService.getSaltedHashedValueOf(othersHashedPassword,challenge);
    }

    public String doMyChallenge(String challenge){
        return CryptoService.getSaltedHashedValueOf(myHashedPassword,challenge);
    }

    public boolean compareValues(String clientChallenge, String serverChallenge){
        boolean isIdenticalValues = false;

        if (clientChallenge.equals(serverChallenge)){
            isIdenticalValues = true;
        }

        return isIdenticalValues;
    }
}
