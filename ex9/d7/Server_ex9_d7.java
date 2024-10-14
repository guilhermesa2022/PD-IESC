package ex9.d7;

import ex9.Time;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;

public class Server_ex9_d7 {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {
        DatagramPacket packet;
        String receivedMsg;

        if (args.length != 1) {
            System.out.println("Sintaxe: java Servidor listeningPort");
            return;
        }

        try(DatagramSocket socket = new DatagramSocket(Integer.parseInt(args[0]))) {

            System.out.println("UDP Time Server iniciado...");

            while (true) {
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                // a)
                // [PT] Deserializar os array de bytes recebidos para um objeto do tipo String
                // [EN] Deserialize the received byte array into a String object
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                receivedMsg = (String) ois.readObject();

                System.out.println("Recebido \"" + receivedMsg + "\" de " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                    continue;
                }

                // b)
                // [PT] Criar e serializar o objecto do tipo Time para um array de bytes
                // [EN] Create and serialize an object of type Time into a byte array
                Calendar calendar = Calendar.getInstance();
                Time time = new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(time);
                // c)
                // [PT] Adicionar os dados e o tamanho ao packet
                // [EN] Add the data and the length to the packet
                packet.setData(baos.toByteArray());
                packet.setLength(baos.size());

                // d)
                // [PT] Questão? Não especificámos o IP e Porto do client, como é que o socket sabe para onde enviar o pacote?
                // [EN] Question? We didn't set the IP and Port of the client so, how can the socket know where to send the packet?
                socket.send(packet);
            }
        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }
}