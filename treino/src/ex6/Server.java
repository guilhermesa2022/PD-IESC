package ex6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    public static final int MAX_SIZE = 256;

    public static void main(String[] args){
        int port;
        File localDirectory;
        String name, localFilePath = null;
        DatagramSocket socket;
        DatagramPacket packet;
        FileInputStream fileInputStream;
        String file;
        if(args.length != 2){
            System.out.println("\nUsage: java server <port> <localDirectory>");
            return;
        }

        localDirectory = new File(args[1].trim());
        port = Integer.parseInt(args[0]);

        if(!localDirectory.exists() || !localDirectory.isDirectory() || !localDirectory.canRead()){
            System.out.println("\nLocal directory does not exist");
            return;
        }


        try{
            socket = new DatagramSocket(port);
            socket.setSoTimeout(5000);
        }catch(Exception e){
            System.out.println("\nError creating socket \n | " + e);
            return;
        }
        try {
            while (true) {
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                System.out.println("\nReceived packet from " + new String(packet.getData(), 0, packet.getLength()) + " | " + packet.getPort() + "\n");
                localFilePath = localDirectory.getCanonicalPath() + File.separator + new String(packet.getData(), 0, packet.getLength());
                try {
                    file = new File(localFilePath).getCanonicalPath();
                }catch(Exception e){
                    System.out.println("\nError creating file \n | " + e);
                    return;
                }

                if (file.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                    fileInputStream = new FileInputStream(file);
                    int bytesRead;
                    do {
                        bytesRead = fileInputStream.read(packet.getData());
                        if (bytesRead > 0) {
                            packet = new DatagramPacket(packet.getData(), bytesRead, packet.getAddress(), packet.getPort());
                            socket.send(packet);
                        }

                        if (bytesRead == -1) {
                            break;
                        }

                        socket.send(packet);
                    } while (bytesRead > 0);
                }else{
                    System.out.println("\nFile does not exist or is not readable\n");
                }
            }
        }catch(Exception e){
            System.out.println("\nError receiving packet \n | " + e);
            return;
        }
    }
}
