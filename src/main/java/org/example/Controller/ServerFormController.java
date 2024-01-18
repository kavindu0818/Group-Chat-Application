package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;



//import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class ServerFormController {

    public VBox vBox;
    private static VBox msgBox;

   // private Server server;

    private ScrollPane scrollPane;

    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    BufferedReader bufferedReader;


    private static ArrayList<ClientFormController> clientList = new ArrayList<>();

    public void initialize() {
//        try (ServerSocket serverSocket = new ServerSocket(3004)) {
//            System.out.println("Server started!");
//
//            while (true) {
//                try {
//                    Socket socket = serverSocket.accept();
//                    System.out.println("New client connected!");
//
//                    ClientFormController clients = new ClientFormController(socket, clientList);
//                    clientList.add(clients);
//                    //ClientServer.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


//    public static void receiveMessage(String msgFromClient) {
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.CENTER_LEFT);
//        hBox.setPadding(new Insets(5, 5, 5, 10));
//
//        Text text = new Text(msgFromClient);
//        //Text text = new Text(msgFromClient);
//        TextFlow textFlow = new TextFlow(text);
//        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
//        textFlow.setPadding(new Insets(5, 10, 5, 10));
//        text.setFill(Color.BLACK);
//
//        hBox.getChildren().add(textFlow);
//
//        Platform.runLater(() -> ms.getChildren().add(hBox));
//    }
    public void btnGetStartOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.centerOnScreen();
        stage.show();

}

//    public static void receiveMessage(String msgFromClient){
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.CENTER_LEFT);
//        hBox.setPadding(new Insets(5,5,5,10));
//
//        Text text = new Text(msgFromClient);
//        TextFlow textFlow = new TextFlow(text);
//        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
//        textFlow.setPadding(new Insets(5,10,5,10));
//        text.setFill(Color.color(0,0,0));
//
//        hBox.getChildren().add(textFlow);
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                staticVBox.getChildren().add(hBox);
//            }
//        });
}
