package ex1;
import java.net.*;
import java.io.*;

public class cliente {
    public static void main(String args[]) {
// CHECK FOR VALID NUMBER OF PARAMETERS
        int argc = args.length;
        if (argc != 2) {
            System.out.println("Syntax :");
            System.out.println("java PacketSendDemo hostname");
            return;
        }

        String hostname = args[0];
        String port = args[1];

        try{
            System.out.println ("Binding to a local port");
            DatagramSocket socket = new DatagramSocket();
            System.out.println ("Bound to local port " + socket.getLocalPort());
            byte[] barray = "o que se !".getBytes();
            InetAddress addr = InetAddress.getByName(hostname);
            DatagramPacket packet = new DatagramPacket( barray, barray.length);
            System.out.println ("Looking up hostname " + hostname );
            System.out.println ("Hostname resolved as "+addr.getHostAddress());
            packet.setAddress(addr);
            packet.setPort(Integer.parseInt(port));
            socket.send(packet);
            System.out.println ("Packet sent!");
            socket.close();
        }catch (UnknownHostException e){
            System.err.println ("Can't find host " + hostname);
        }catch (IOException e){
            System.err.println ("Error - " + e);
        }
    }
}
