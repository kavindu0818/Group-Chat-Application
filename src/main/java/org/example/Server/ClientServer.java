package org.example.Server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

    public class ClientServer extends Thread {
        private Socket socket;
        private ArrayList<ClientServer> clients;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientServer (Socket socket, ArrayList<ClientServer> clients) {
            try {
                this.socket = socket;
                this.clients = clients;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String message, ClientServer sender) {
            for (ClientServer client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }

        private void sendMessage(String message) {
            writer.println(message);
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    broadcast(message, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }

        private void closeConnection() {
            try {
                clients.remove(this);
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

