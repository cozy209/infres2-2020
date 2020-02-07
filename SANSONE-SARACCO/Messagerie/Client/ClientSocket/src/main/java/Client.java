import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;
import java.sql.*;

public class Client {

    static SecretKey key;
    static Connection con;

    static BufferedWriter outputWriter = null;
    static BufferedReader inputReader = null;

    public static void main(String[] args) {

        String serverHost = "127.0.0.1";
        Socket socketOfClient = null;
        String messageRequest="";
        String messageResponse;

        //bdd
        try{
            Class.forName("com.mysql.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/client","nini","patate");


        }catch(Exception e){ e.printStackTrace();}
        // Generer clé
        try {
            key = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e){
            System.err.println(e.toString());
        }

        try {
            // Creation du socket
            socketOfClient = new Socket(serverHost, 9090);

            outputWriter = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            inputReader = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverHost);
            return;
        }

        try {



            if (authentification()) {

                String connect = "Vous êtes désormais connecté.";
                System.out.println(connect);

                // Conversation
                while (!messageRequest.equals("QUIT")) {

                    messageRequest = new Scanner(System.in).nextLine();


                    // Chiffrement
                    //byte[] cryptedToto = CryptoService.encrypt(messageRequest.getBytes(),key,CryptoService.getNewIV());


                    // Envoi du message
                    outputWriter.write(messageRequest);
                    outputWriter.newLine();
                    outputWriter.flush();


                    //Reception et affichage de la réponse
                    messageResponse = inputReader.readLine();
                    System.out.println("Server: " + messageResponse);
                }
            }

            // Fermeture de la connection et des streams
            outputWriter.close();
            inputReader.close();
            socketOfClient.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (Exception e){
            System.err.println("Exception:  " + e);
        }
    }

    static boolean authentification()throws IOException{

        // Authentification du client
        Properties properties = new Properties();
        String filePath = new File("").getAbsolutePath();


        filePath=filePath.concat("/src/main/resources/salt.properties");
        properties.load(new FileInputStream(filePath));

        Authentification authentification = new Authentification(con);
        String clientRandom = inputReader.readLine();
        String serverChallenge = authentification.doMyChallenge(clientRandom);
        outputWriter.write(serverChallenge);
        outputWriter.newLine();
        outputWriter.flush();

        // Authentification du server
        String serverRandom = authentification.getChallenge();
        outputWriter.write(serverRandom);
        outputWriter.newLine();
        outputWriter.flush();
        String clientResult = authentification.doOthersChallenge(serverRandom);
        String serverResult = inputReader.readLine();

        boolean passed = authentification.compareValues(serverResult, clientResult);

        properties.store(new FileOutputStream(filePath),"");

        return passed;
    }
    
}
