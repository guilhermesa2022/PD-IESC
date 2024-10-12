package ex6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    public static final int MAX_SIZE = 256;
    public static void main(String[] args){
        File localDirectory;
        InetAddress addr;
        DatagramSocket socket;
        DatagramPacket packet;
        int contador = 0;
        FileOutputStream localFileOutputStream = null;
        int port;
        if (args.length != 4) {
               System.out.println("\nUsage: java client <server> <port> <fileToGet> <localDirectory>");
            return;
        }

        String fileName = args[2].trim();
        System.out.println("\nFile name: " + fileName + "\n");
        localDirectory = new File(args[3].trim());

        if(!localDirectory.exists() && !localDirectory.isDirectory() && !localDirectory.canWrite()){
            System.out.println("\nLocal directory does not exist");
            return;
        }


        try {
            String localFilePath = localDirectory.getCanonicalPath() + File.separator + fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(localFilePath);
        }catch(FileNotFoundException e){
            System.out.println("\nError creating file \n | " + e);
            return;
        }catch (Exception e){
            System.out.println("\nError getting local directory path \n | + " + e);
            return;
        }

        try{
            addr = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
        }catch (Exception e){
            System.out.println("\nInvalid address or port \n | " + e);
            return;
        }

        try{
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            packet = new DatagramPacket(fileName.getBytes(), fileName.length(), addr, port);
            socket.send(packet);
        }catch (SocketException e){

            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            do{
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);
                if (packet.getPort() == port && packet.getAddress().equals(addr)) {
                    localFileOutputStream.write(packet.getData(), 0, packet.getLength());
                    contador++;
                }
            }while(packet.getLength() > 0);

        }catch (Exception e){

            return;
        }finally {
            socket.close();
        }
    }
}
