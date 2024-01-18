package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servers {
        private  ArrayList<ClientServer> clientsList = new ArrayList<>();

        public void main(String[] args) {
            try (ServerSocket serverSocket = new ServerSocket(3001)) {
                System.out.println("Server started!");

                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("New client connected!");

                        ClientServer clients = new ClientServer(socket, clientsList);
                        clientsList.add(clients);
                        clients.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

