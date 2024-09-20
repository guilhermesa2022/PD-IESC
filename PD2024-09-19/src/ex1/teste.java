package ex1;
import java.net.*;
import java.io.*;

public class teste {
    public static void main(String args[]) {
        String somehost = "10.204.128.128";
        try {

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(new byte[256], 256);
        packet.setAddress(InetAddress.getByName(somehost));
        packet.setPort(6000);
        boolean finished = false;
        while (!finished) {
            // Write data to packet buffer and set data length
            // ...
            System.out.println("Sending packet to " + somehost + " on port 6000");
            socket.send(packet);
            // Do something else, like read other packets, or check to
            // see if no more packets to send
            // ...
        }
        socket.close();
        } catch (IOException e) {
            System.err.println("Error - " + e);
        }
    }
}
