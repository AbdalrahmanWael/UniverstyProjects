package Server;

import java.io.*;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer {

    private final int port;

    ServerSocket socketServer;

    public ChatServer(int port) {
        this.port = port;
    }

    public void startServer(String dbPort, String dbName, String dbUserName, String dbUserPass, int RMIPORT) {
        DatabaseManager.init(dbPort, dbName, dbUserName, dbUserPass); // Connect to Database
        
        try {
            // RMI
            Registry r = LocateRegistry.createRegistry(RMIPORT);
            DatabaseController db = new DatabaseController();
            r.bind("db", db);
            
            // Socket
            socketServer = new ServerSocket(this.port);
            System.out.println("SERVER STARTED");
            acceptUsers(socketServer);
            
        } catch(RemoteException ex){
            System.out.println("couldn't create registry");
        } catch(AlreadyBoundException ex) {
            System.out.println("Already bound");
        } catch (IOException e) {
            System.err.println("Could not connect to port: " + this.port);
            System.exit(1);
        }

    }

    private void acceptUsers(ServerSocket serverSocket) {
        System.out.println("Server port: " + serverSocket.getLocalSocketAddress());
        while (true) {
            try {
                // Accept a new User
                Socket socket = serverSocket.accept();
                System.out.println("New User Connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException ex) {
                System.out.println("Failed To connect User");
            }
        }
    }

    public void closerServer() {
        try {
            if (socketServer != null) {
                socketServer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
        ChatServer server = new ChatServer(1234);
        server.startServer("1433", "ChatT", "USERNAME", "Password", 4321);
    }
}
