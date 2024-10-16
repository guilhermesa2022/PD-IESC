package ex9.d8;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import ex9.Time;

public class Client_ex9_d8 {
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10;

    public static void main(String[] args) {
        InetAddress serverAddr;
        int serverPort;
        Time response;

        if (args.length != 2) {
            System.out.println("Sintaxe: java Client serverAddress serverPort");
            return;
        }

        try {
            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            try (Socket socket = new Socket(serverAddr, serverPort)) {
                socket.setSoTimeout(TIMEOUT * 1000);

                // a)
                // [PT] Serializar o objecto do tipo String TIME_REQUEST para o OutputStream disponibilizado pelo socket
                // [EN] Serialize the object of type String TIME_REQUEST to the OutputStream available on the socket
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(TIME_REQUEST);

                // b)
                // [PT] Deserializa o objecto do tipo Time recebido no InputStream disponibilizado pelo socket
                // [EN] Deserialize the object of type Time received on the InputStream available on the socket
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                response = (Time) ois.readObject();

                System.out.println("Resposta: " + response.toString());
            }
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }
}