import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

public class Client {

    static SecretKeySpec key;

    static ObjectOutputStream outputWriter = null;
    static ObjectInputStream inputReader = null;

    public static void main(String[] args) {

        String serverHost = "127.0.0.1";
        Socket server = null;
        String clientInput="";
        Message messageRequest;
        Message messageResponse;

        try {
            // Creation du socket
            server = new Socket(serverHost, 9090);

            outputWriter = new ObjectOutputStream(server.getOutputStream());
            inputReader = new ObjectInputStream(server.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu : " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Probleme input output : " + serverHost);
            return;
        }

        try {

            // Mise en place chiffrement
            System.out.print("Entrez la clé de chiffrement : ");
            String pwd = new Scanner(System.in).nextLine();
            key = CryptoService.getKey(pwd, null); // TODO : properties

            if (authentification()) {

                String connect = "Vous êtes désormais connecté.";
                System.out.println(connect);

                // Conversation
                while (!clientInput.equals("QUIT")) {

                    clientInput = new Scanner(System.in).nextLine();


                    // Chiffrement
                    messageRequest = CryptoService.encrypt(clientInput.getBytes(),key);

                    // Envoi du message
                    outputWriter.writeObject(messageRequest);
                    outputWriter.flush();


                    //Reception et affichage de la réponse
                    messageResponse = (Message) inputReader.readObject();
                    System.out.println("Server: " + CryptoService.decrypt(messageResponse,key));
                }
            }

            // Fermeture de la connection et des streams
            outputWriter.close();
            inputReader.close();
            server.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (Exception e){
            System.err.println("Exception:  " + e);
        }
    }

    static boolean authentification(){

        boolean passed = false;

        try {
            // Authentification du client
            Properties properties = new Properties();
            String propertiesfile = "/Users/Nini/Documents/Java/ClientSocket/src/main/resources/salt.properties";
            properties.load(new FileInputStream(propertiesfile));

            Authentification authentification = new Authentification(properties);

            // Reçoit le random
            Message messageClientRandom = (Message) inputReader.readObject();
            String clientRandom = CryptoService.decrypt(messageClientRandom,key);

            // Fait le challenge et l'envoie
            String serverChallenge = authentification.doMyChallenge(clientRandom);
            outputWriter.writeObject(CryptoService.encrypt(serverChallenge.getBytes(),key));
            outputWriter.flush();

            // Authentification du server
            String serverRandom = authentification.getChallenge();

            // Envoie le random
            outputWriter.writeObject(CryptoService.encrypt(serverRandom.getBytes(),key));
            outputWriter.flush();
            String clientResult = authentification.doOthersChallenge(serverRandom);

            // reçoit la reponse du server et compare
            Message serverResult = (Message) inputReader.readObject();
            passed = authentification.compareValues(CryptoService.decrypt(serverResult,key), clientResult);

            //properties.store(new FileOutputStream(propertiesfile),"");

        } catch (IOException e){
            System.out.println( "Authentification : " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println( "Authentification : " + e);
            e.printStackTrace();
        } catch (Exception e){
            System.out.println( "Authentification : " + e);
            e.printStackTrace();
        }

        return passed;
    }
    
}
