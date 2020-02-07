import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Scanner;


public class Server {

    static Connection con;
    static Authentification authentification;
    static SecretKeySpec key;

    static ObjectInputStream inputReader;
    static ObjectOutputStream outputWriter;

    public static void main(String args[]) {

        ServerSocket serverSocket = null;
        Socket client = null;
        Message serverResponse;
        Message clientRequest;

        //bdd
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/server", "nini", "patate");


        } catch (Exception e) {
            e.printStackTrace();
        }


        // Creer le server socket
        try {
            serverSocket = new ServerSocket(9090);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            System.out.println("Server inputReader waiting to accept user...");

            // Accepter la connection client
            client = serverSocket.accept();
            System.out.println("Accept a client!");

            // Ouvrir les input et output streams
            inputReader = new ObjectInputStream(client.getInputStream());
            outputWriter = new ObjectOutputStream(client.getOutputStream());

            // Mise en place chiffrement


            Properties properties = new Properties();
            String filePath = new File("").getAbsolutePath();


            filePath = filePath.concat("/src/main/resources/salt.properties");
            properties.load(new FileInputStream(filePath));

            System.out.print("Entrez la clé de chiffrement : ");
            String pwd = new Scanner(System.in).nextLine();
            key = CryptoService.getKey(pwd, properties);


            // Authentification


            if (authentification()) {

                String connect = "Vous êtes désormais connecté.";
                System.out.println(connect);

                // Conversation
                while (true) {
                    // Lire la requete client
                    clientRequest = (Message) inputReader.readObject();

                    PreparedStatement stmt = null;
                    stmt = con.prepareStatement("Insert into messages(msg_user_id,msg_message) " +
                            "VALUES ((select usr_id from users where usr_name!=?),?)  ");


                    stmt.setString(1, authentification.getUsername());
                    stmt.setObject(2, clientRequest);

                    System.out.println(stmt.toString());


                    stmt.executeUpdate();

                    System.out.println("Client: " + CryptoService.decrypt(clientRequest, key));

                    // Lire la réponse a envoyer depuis le prompt
                    String serverInput = new Scanner(System.in).nextLine();


                    //Chiffrement
                    serverResponse = CryptoService.encrypt(serverInput.getBytes(), key);


                    stmt = con.prepareStatement("Insert into messages(msg_user_id,msg_message) " +
                            "VALUES ((select usr_id from users where usr_name=?),?)  ");


                    stmt.setString(1, authentification.getUsername());
                    stmt.setObject(2, serverResponse);

                    System.out.println(stmt.toString());


                    stmt.executeUpdate();

                    // Envoi de la réponse au client
                    outputWriter.writeObject(serverResponse);
                    outputWriter.flush();


                    // Fin de la conversation quand le client envoie QUIT
                    if (CryptoService.decrypt(clientRequest, key).equals("QUIT")) {
                        String finConv = "Fin de la conversation";
                        serverResponse = CryptoService.encrypt(finConv.getBytes(), key);
                        outputWriter.writeObject(serverResponse);
                        outputWriter.flush();
                        break;
                    }
                }
            } else {
                System.out.println("Mots de passe differents");
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Sever stopped!");
    }


    static boolean authentification() throws IOException {

        boolean passed = false;

        try {

            // Authentification du client
            authentification = new Authentification(con);

            // Envoie le random
            String clientRandom = authentification.getChallenge();
            outputWriter.writeObject(CryptoService.encrypt(clientRandom.getBytes(), key));
            outputWriter.flush();

            String serverResult = authentification.doOthersChallenge(clientRandom);

            // reçoit la reponse et compare
            Message clientResult = (Message) inputReader.readObject();
            passed = authentification.compareValues(CryptoService.decrypt(clientResult, key), serverResult);

            // Authentification du server

            // Reçoit le random
            Message serverRandom = (Message) inputReader.readObject();

            String clientChallenge = authentification.doMyChallenge(CryptoService.decrypt(serverRandom, key));

            // Envoie sa solution
            outputWriter.writeObject(CryptoService.encrypt(clientChallenge.getBytes(), key));
            outputWriter.flush();


        } catch (IOException e) {
            System.out.println("Authentification : " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Authentification : " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Authentification : " + e);
            e.printStackTrace();
        }


        return passed;
    }
}