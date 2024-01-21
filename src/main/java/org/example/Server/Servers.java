package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servers {

    private ServerSocket serverSocket;
    private Socket socket;
    private static Servers servers;

    private List<ClientServer> clients = new ArrayList<>();

    private Servers() throws IOException {
        serverSocket = new ServerSocket(3001);
    }

    public static Servers getInstance() throws IOException {
        return servers!=null? servers:(servers=new Servers());
    }

    public void makeSocket(){
        while (!serverSocket.isClosed()){
            try{
                socket = serverSocket.accept();
                ClientServer clientHandler = new ClientServer(socket,clients);
                clients.add(clientHandler);
                System.out.println("client socket accepted "+socket.toString());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
