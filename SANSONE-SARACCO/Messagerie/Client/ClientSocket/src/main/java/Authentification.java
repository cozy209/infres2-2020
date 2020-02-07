import java.security.SecureRandom;
import java.util.Properties;
import java.util.Scanner;

public class Authentification {

    String username;
    String myHashedPassword;
    String othersHashedPassword;
    byte[] salt;

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

        fetchSalt();
        fetchOthersHashedPassword();

        myHashedPassword = CryptoService.getSaltedHashedValueOf(password,salt);

        String others = properties.getProperty("MDP");

        othersHashedPassword = others;
    }

    private void fetchOthersHashedPassword(){
        othersHashedPassword = ""; //TODO : bdd command
    }

    private void fetchSalt(){
        salt = "".getBytes(); //TODO : bdd command
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
        return CryptoService.getSaltedHashedValueOf(othersHashedPassword,challenge.getBytes());
    }

    public String doMyChallenge(String challenge){
        return CryptoService.getSaltedHashedValueOf(myHashedPassword,challenge.getBytes());
    }

    public boolean compareValues(String clientChallenge, String serverChallenge){
        boolean isIdenticalValues = false;

        if (clientChallenge.equals(serverChallenge)){
            isIdenticalValues = true;
        }

        return isIdenticalValues;
    }
}
