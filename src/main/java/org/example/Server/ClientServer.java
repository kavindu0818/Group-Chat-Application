package org.example.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientServer extends Thread {

    private Socket socket;
    private List<ClientServer> clients;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ClientServer(Socket socket, List<ClientServer> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();  // Start the thread when the object is created
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                String msg = dataInputStream.readUTF();

                for (ClientServer clientHandler : clients) {
                    if (clientHandler.socket.getPort() != socket.getPort()) {
                        clientHandler.dataOutputStream.writeUTF(msg);
                        clientHandler.dataOutputStream.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Handle client disconnection
            clients.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
