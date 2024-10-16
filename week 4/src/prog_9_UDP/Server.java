package prog_9_UDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;

public class Server {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static byte[] snapshot;

    public static void main(String[] args) {
        DatagramPacket packet;
        String receivedMsg;
        Calendar calender;

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
                try (ByteArrayInputStream bais =
                             new ByteArrayInputStream(packet.getData());
                     ObjectInputStream ois =
                             new ObjectInputStream(bais)) {
                    receivedMsg = (String) ois.readObject();
                }catch (Exception e){
                    System.out.println("Erro ao deserializar o objecto:\n\t"+e);
                    return;
                }

                System.out.println("Recebido \"" + receivedMsg + "\" de " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
                if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                    continue;
                }

                calender = Calendar.getInstance();

                // b)
                // [PT] Serializar o object Calendar para um array de bytes
                // [EN] Serialize the Calendar object into a byte array
                try(ByteArrayOutputStream baos =
                            new ByteArrayOutputStream();
                    ObjectOutputStream oos =
                            new ObjectOutputStream(baos)) {
                    oos.writeObject(calender);
                    snapshot = baos.toByteArray();
                }catch (Exception e){
                    System.out.println("Erro ao serializar o objecto:\n\t"+e);
                }


                // c)
                // [PT] Adicionar os dados e o tamanho ao packet
                // [EN] Add the data and the length to the packet
                packet = new DatagramPacket(snapshot, snapshot.length, packet.getAddress(), packet.getPort());


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
        }
    }
}