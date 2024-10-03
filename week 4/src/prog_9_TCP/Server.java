package prog_9_TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {
        int listeningPort;
        String receivedMsg;
        Time response;

        // Verifica se a sintaxe correta foi fornecida
        if (args.length != 1) {
            System.out.println("Sintaxe: java Server listeningPort");
            return;
        }

        // Tenta converter o argumento de porta em um número inteiro
        try {
            listeningPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
            return;
        }

        // Inicia o servidor
        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
            System.out.println("TCP Time Server iniciado...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    // Deserializa o objeto do tipo String recebido no InputStream disponibilizado pelo socket
                    try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                        receivedMsg = (String) ois.readObject();
                    } catch (Exception e) {
                        System.out.println("Erro ao deserializar o objeto:\n\t" + e);
                        continue; // Continue se ocorrer um erro
                    }

                    System.out.println("Recebido \"" + receivedMsg + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                    // Verifica se a mensagem recebida é um pedido de tempo
                    if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                        continue; // Continue se a mensagem não for TIME_REQUEST
                    }

                    // Cria e serializa o objeto do tipo Time para o OutputStream disponibilizado pelo socket
                    response = new Time(12, 30, 0);
                    try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
                        oos.writeObject(response);
                        oos.flush();
                    } catch (Exception e) {
                        System.out.println("Erro ao serializar o objeto:\n\t" + e);
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nível do serverSocket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
        }
    }
}
