import java.net.ServerSocket;

public class Server {
    private static final int PORT = 6000;
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}
