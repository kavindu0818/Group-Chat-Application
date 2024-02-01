package org.example.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Server.Servers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



//import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class ServerFormController {


    private static VBox msgBox;
    public ScrollPane ScrollPaneServerMsgArea;
    public VBox msgVboxServer;
    public AnchorPane ancServerPane;

    private Servers server;

    private ScrollPane scrollPane;

    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    BufferedReader bufferedReader;
    private static VBox vBox;


    private static ArrayList<ClientFormController> clientList = new ArrayList<>();


        public static ArrayList<Socket> socketArrayList = new ArrayList<>();
        public static int index = 0;
        public static ArrayList<Thread> threadList = new ArrayList<>();


        public void initialize() {
            System.out.println("Server started");
            new Thread(() -> {

                try {
                    ServerSocket ss = new ServerSocket(3001);
                    while (true) {

                        Socket socket = ss.accept();
                        socketArrayList.add(socket);

                        //Notifying the other clients who joined the chat
                        for (Socket s : socketArrayList) {
                            if (s.getPort() == socket.getPort()) {
                                //Avoid sending the message to the sender
                                continue;
                            }
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            int index = socketArrayList.indexOf(socket);
                            //dos.writeUTF(LoginFormController.clientsNames.get(index) + " " + "has joined the chat.");
                            dos.flush();
                        }
                        System.out.println("Client connected to the server");

                        //Handling each client from a separate thread
                        handleClient(socket);


                    }
                } catch (IOException e) {
                    Platform.runLater(() -> {
//                    System.out.println("Error while starting the server : " + e.getLocalizedMessage());
                    });
                }

            }).start();


        }

        public static void handleClient(Socket socket) {
            new Thread(() -> {
                //Adding the current thread to an arrayList
                threadList.add(Thread.currentThread());

                String clientMsg = "";
                try {
                    while (true) {
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        clientMsg = dis.readUTF();
                        //Checking if an image has received
                        //Checking if the client sent an image
                        if (clientMsg.equals("img")) {
                            handleReceivedImage(dis, socket);
                        } else {
                            sendMsgToOthers(clientMsg, socket);
                        }


                    }


                } catch (IOException e) {
                    throw new RuntimeException(e);


                }


            }).start();

        }


        public static void handleReceivedImage(DataInputStream dis, Socket senderSocket) {
            try {
                //Reading the image length
                int imageDataLength = dis.readInt();
//                String cn = dis.readUTF();
//                System.out.println(cn + "cn1");
                byte[] imageData = new byte[imageDataLength];
                dis.readFully(imageData);

              //  String userName = dis.readUTF();




                sendImageToOthers(imageData,senderSocket);
            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while handling the received image: " + e.getLocalizedMessage());
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(ServerFormController.class.getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();
                });
            }
        }

        public static void sendImageToOthers(byte[] imageData, Socket senderSocket) {
            for (Socket s : socketArrayList) {
                try {
                    if (s.getPort() == senderSocket.getPort()) {
                        //Avoid sending the image to the sender
                        continue;
                    }

                //    System.out.println("Cn mama" + cn);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    //Sending a special message indicating the start of an image transmission
                    dos.writeUTF("img");
                    //dos.writeUTF(userName);

                    //Letting the server know the size of the image.This is useful to allocate resources properly
                    dos.writeInt(imageData.length);
                    // Sending the image data
                    dos.write(imageData);
                  //  dos.writeUTF(cn);
                    dos.flush();
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error while getting the output stream: " + e.getLocalizedMessage());
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.getScene().getStylesheets().add(ServerFormController.class.getResource("/style/notification.css").toExternalForm());
                        alert.showAndWait();
                    });
                }
            }
        }


        public static void sendMsgToOthers(String msg, Socket socket) {

            for (Socket s : socketArrayList) {
                try {
                    if (s.getPort() == socket.getPort()) {
                        //Avoid sending the message to the sender.
                        continue;

                    }
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    //Since socketArray index == clientsNames array client name index
                    index = socketArrayList.indexOf(socket);
                    dos.writeUTF(msg);
                   //(LoginFormController.clientsN.get(index) + " : " + msg);
                    dos.flush();

                    System.out.println(msg + "Hi mama methnta awa");
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error while getting the output stream : " + e.getLocalizedMessage());
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.getScene().getStylesheets().add(ServerFormController.class.getResource("/style/notification.css").toExternalForm());
                        alert.showAndWait();
                    });
                }

            }

        }

//        public static void handleExitedClient(int exitedClientIndex) {
//            Socket exitedClient = Server.socketArrayList.get(exitedClientIndex);
//            for (Socket s : Server.socketArrayList) {
//                if (s.getPort() == exitedClient.getPort()) {
//                    continue;
//                }
//                try {
//                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//                    dos.writeUTF(LoginFormController.clientsNames.get(exitedClientIndex) + " has left the chat!");
//                    dos.flush();
//                } catch (IOException e) {
//                    Platform.runLater(() -> {
//                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error while handling the client exit! : " + e.getLocalizedMessage());
//                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
//                        alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
//                        alert.showAndWait();
//                    });
//                }
//            }
//            // Closing the socket and interrupting the relevant thread
//            new Thread(() -> {
//                try {
//                    exitedClient.close();
//                    Server.threadList.get(exitedClientIndex).interrupt();
//                } catch (IOException e) {
//                    Platform.runLater(() -> {
//                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error while exiting the client socket. : " + e.getLocalizedMessage());
//                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
//                        alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
//                        alert.showAndWait();
//                    });
//                }
//            }).start();
//        }
//
//    }

    public static void receiveMessage(String msgFromClient) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));


        javafx.scene.text.Text text = new javafx.scene.text.Text(msgFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0, 0, 0));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {vBox.getChildren().add(hBox);
            }
        });
    }

    public void addButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ancServerPane.getScene().getWindow());
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "something went wrong. can't add client").show();
        }
        stage.setTitle("EChat");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void btnGetStartOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ancServerPane.getScene().getWindow());
        try {
           stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "something went wrong. can't add client").show();
        }
        stage.setTitle("K Chat");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}