import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Server {

    static BufferedReader inputReader;
    static BufferedWriter outputWriter;

    public static void main(String args[]) {

        ServerSocket serverSocket = null;
        String serverResponse;
        String clientRequest;
        Socket socketOfServer = null;


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
            socketOfServer = serverSocket.accept();
            System.out.println("Accept a client!");

            // Ouvrir les input et output streams
            inputReader = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            outputWriter = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

            // Authentification


            if (authentification()) {

                String connect = "Vous êtes désormais connecté.";
                System.out.println(connect);

                // Conversation
                while (true) {
                    // Lire la requete client
                    clientRequest = inputReader.readLine();
                    System.out.println("Client: " + clientRequest);

                    // Lire la réponse a envoyer depuis le prompt
                    serverResponse = new Scanner(System.in).nextLine();


                    //Chiffrement
                    //byte[] cryptedToto = CryptoService.encrypt(toto.getBytes(),key,CryptoService.getNewIV());

                    // Envoi de la réponse au client
                    outputWriter.write(serverResponse);
                    outputWriter.newLine();
                    outputWriter.flush();


                    // Fin de la conversation quand le client envoie QUIT
                    if (clientRequest.equals("QUIT")) {
                        outputWriter.write("Fin de la conversation");
                        outputWriter.newLine();
                        outputWriter.flush();
                        break;
                    }
                }
            }else {
                System.out.println("Mots de passe differents");
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }


    static boolean authentification()throws IOException{

        String propertiesfile = "/Users/Nini/Documents/Java/ClientSocket/src/main/resources/serversalt.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesfile));

        // Authentification du client
        Authentification authentification = new Authentification(properties);
        String clientRandom = authentification.getChallenge();
        outputWriter.write(clientRandom);
        outputWriter.newLine();
        outputWriter.flush();
        String serverResult = authentification.doOthersChallenge(clientRandom);
        String clientResult = inputReader.readLine();

        boolean passed = authentification.compareValues(clientResult, serverResult);

        // Authentification du server
        String serverRandom = inputReader.readLine();
        String clientChallenge = authentification.doMyChallenge(serverRandom);
        outputWriter.write(clientChallenge);
        outputWriter.newLine();
        outputWriter.flush();

        properties.store(new FileOutputStream(propertiesfile),"");

        return passed;
    }
}