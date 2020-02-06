import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) {

        ServerSocket serverSocket = null;
        String clientRequest;
        String serverResponse;
        BufferedReader inputReader;
        BufferedWriter outputWriter;
        Socket socketOfServer = null;


        // Create serverSocket
        try {
            serverSocket = new ServerSocket(9090);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            System.out.println("Server is waiting to accept user...");

            // Accept client connection request
            socketOfServer = serverSocket.accept();
            System.out.println("Accept a client!");

            // Open input and output streams
            inputReader = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            outputWriter = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));


            while (true) {
                // Read data to the server (sent from client).
                serverResponse = inputReader.readLine();
                System.out.println("Client: " + serverResponse);

                clientRequest = new Scanner(System.in).nextLine();

                //byte[] cryptedToto = CryptoService.encrypt(toto.getBytes(),key,CryptoService.getIV());

                outputWriter.write(clientRequest);
                outputWriter.newLine();
                outputWriter.flush();



                // If users send QUIT (To end conversation).
                if (serverResponse.equals("QUIT")) {
                    outputWriter.write(">> OK");
                    outputWriter.newLine();
                    outputWriter.flush();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }
}