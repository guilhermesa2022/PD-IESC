package ex5;

import java.io.IOException;
import java.net.*;

public class client {
    public static final String STRING = "TIME";
    public static final int MAX_SIZE = 256;
    public static void main(String[] args) {
        InetAddress addr;
        DatagramPacket packet;
        DatagramSocket socket;

        if (args.length != 2) {
            System.out.println("\nUsage: java client <server> <port>");
            return;
        }

        int port = Integer.parseInt(args[1]);
        try {
            addr = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("\nInvalid address \n | " + e);
            return;
        }

        packet = new DatagramPacket(STRING.getBytes(), STRING.length(), addr, port);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("\nSocket error \n | " + e);
            return;
        }

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("\nError sending packet \n | " + e);
            return;
        }

        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);

        try {
            socket.receive(packet);
        } catch (IOException e) {
            System.out.println("\nError receiving packet \n | " + e);
            return;
        }

        System.out.println("\nServer response: " + new String(packet.getData()) + "\n");

        if(!socket.isClosed())
            socket.close();

    }
}