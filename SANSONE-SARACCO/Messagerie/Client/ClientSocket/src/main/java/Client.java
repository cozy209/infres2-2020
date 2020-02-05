import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class Client {

    static SecretKey key;


    public static void main(String[] args) throws NoSuchAlgorithmException {

        String serverHost = "159.31.61.76";
        Socket socketOfClient = null;
        BufferedWriter os = null;
        BufferedReader is = null;

        try {
            key = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e){
            System.err.println(e.toString());
        }

        try {
            socketOfClient = new Socket(serverHost, 9090);

            os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

            System.out.println("That worked");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverHost);
            return;
        }

        try {

            String toto="";

            while (!toto.equals("QUIT")) {

                toto = new Scanner(System.in).nextLine();

                byte[] cryptedToto = CryptoService.encrypt(toto.getBytes(),key,CryptoService.getIV());

                os.write(toto);

                os.newLine();

                os.flush();


                String responseLine;
                responseLine = is.readLine();
                System.out.println("Server: " + responseLine);
            }

            os.close();
            is.close();
            socketOfClient.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (Exception e){
            System.err.println("Exception:  " + e);
        }
    }

}
