package ex5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;



public class Server {
    public static final String STRING = "MSG";
    public static final int MAX_SIZE = 256;
    public static void main(String[] args) {
        DatagramPacket packet;
        DatagramSocket socket;

        if (args.length != 1) {
            System.out.println("\nUsage: java server <port>");
            return;
        }

        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);

        try{
            socket = new DatagramSocket(Integer.parseInt(args[0]));
        } catch (SocketException e) {
            System.out.println("\nSocket error \n | " + e);
            return;
        }

        while(true) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("\nError receiving packet \n | " + e);
                return;
            }

            if (new String(packet.getData(), 0, packet.getLength()).equalsIgnoreCase("TIME")) {
                try {
                    packet = new DatagramPacket(STRING.getBytes(), STRING.length(), packet.getAddress(), packet.getPort());
                    socket.send(packet);
                } catch (IOException e) {
                    System.out.println("\nError sending packet \n | " + e);
                    return;
                }
            }else{
                System.out.println("\nServer response: " + new String(packet.getData()) + "||\n" + new String(packet.getData()).equalsIgnoreCase(new String("TIME")));
                System.out.println("\nInvalid request\n");
            }
        }
    }
}


