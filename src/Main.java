import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    static final int PORT = 9966;
    static ServerSocket serverSocket ;
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT) ;
            System.out.println("Server is running");
            while (true) {
                Socket s = serverSocket.accept();
                Connection connection = new Connection(s);
                connection.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}