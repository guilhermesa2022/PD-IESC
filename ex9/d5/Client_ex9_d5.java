package ex9.d5;

import java.io.*;
import java.net.*;
import java.util.Calendar;

public class Client_ex9_d5 {
    public static final int MAX_SIZE = 10000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10;

    public static void main(String[] args) throws UnknownHostException {
        InetAddress serverAddr;
        int serverPort;
        DatagramPacket packet;
        Calendar response;

        if (args.length != 2) {
            System.out.println("Sintaxe: java Client serverAddress serverUdpPort");
            return;
        }

        try(DatagramSocket socket = new DatagramSocket()) {

            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            socket.setSoTimeout(TIMEOUT * 1000);


            // a)
            // [PT] Serializar o objecto do tipo String TIME_REQUEST para um array de bytes
            // [EN] Serialize the object of type String TIME_REQUEST into a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(TIME_REQUEST);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //byte[] serializedTimeRequest = TIME_REQUEST.getBytes();

            // b)
            // [PT] Construir o packet com o resultado da serialização
            // [EN] Build the packet with the result from the serialization
            //packet = new DatagramPacket(serializedTimeRequest,serializedTimeRequest.length, serverAddr, serverPort);
            packet = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), serverAddr, serverPort);
            socket.send(packet);

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);


            // c)
            // [PT] Deserializar os array de bytes recebidos para um objeto do tipo Calendar
            // [EN] Deserialize the received byte array into a Calendar object
            socket.receive(packet);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            response = (Calendar) ois.readObject();


            System.out.println("Resposta: " + response.getTime());

        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }
}