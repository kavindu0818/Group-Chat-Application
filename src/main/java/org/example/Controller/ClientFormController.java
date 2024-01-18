package org.example.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.example.EmojiPicker;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClientFormController {
    public AnchorPane ancClientForm;
    public Label lblUserName;
    public ScrollPane ScrollPaneMsgArea;
    public VBox msgVbox;
    public TextField txtMsgType;
    public ImageView emjiButton;
    public Button emojibuttonId;
    public Button imgSelectBtn;
    public FileInputStream fileInputStream;
    public Image image;

    LoginFormController loginFormController = new LoginFormController();

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    BufferedReader bufferedReader;

    ServerFormController serverFormController =new ServerFormController();

    private String cName = loginFormController.sendUserName();

    private ArrayList<ClientFormController> clients;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientFormController() {
        // Initialization logic if needed
    }

    public ClientFormController(Socket socket, ArrayList<ClientFormController> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize(){
        lblUserName.setText(loginFormController.sendUserName());

        new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                socket = new Socket("localhost", 3001);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Client connected");

                while (socket.isConnected()){
                    String receivingMsg = dataInputStream.readUTF();
                    receiveMessage(receivingMsg, ClientFormController.this.msgVbox);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }).start();

        this.msgVbox.heightProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            ScrollPaneMsgArea.setVvalue((Double) newValue);
        }
    });

    emoji();

}

//    public void initialize(){
//        lblUserName.setText(loginFormController.sendUserName());
//
//        new Thread(() -> {
//            try {
//                socket = new Socket("localhost", 3000);
//
//                // Output stream used for writing data to the server
//                dataOutputStream = new DataOutputStream(socket.getOutputStream());
//
//                // Input stream used for reading data from the server
//                dataInputStream = new DataInputStream(socket.getInputStream());
//
//                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//
//                ServerFormController.receiveMessage(clientName+" joined.");

//                String message = "";
//
//
//                while (!message.equals("finish")) {
//                    // Read user input
//
//                    // Send the user input to the server
//
//                    // Receive and print the server's response
//                    message = dataInputStream.readUTF();
//                   //   msgVbox.setAlignment(message);
//                    receiveMessage(message, ClientFormController.this.msgVbox);//, ClientFormController.this.msgVbox);
//                   // txtM.appendText("\n Server: " + message);
//
//                }
//
//                // Close streams and socket
//                dataOutputStream.close();
//                dataInputStream.close();
//                socket.close();
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }).start();
//
//        emoji();
//
//    }


        public void receiveMessage(String msgFromClient, VBox msgVbox) {
            if (msgFromClient.matches(".*\\.(png|jpe?g|gif)$")){
                HBox hBoxName = new HBox();
                hBoxName.setAlignment(Pos.CENTER_LEFT);
                Text textName = new Text(msgFromClient.split("[-]")[0]);
                TextFlow textFlowName = new TextFlow(textName);
                hBoxName.getChildren().add(textFlowName);

                Image image = new Image(msgFromClient.split("[-]")[1]);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5,5,5,10));
                hBox.getChildren().add(imageView);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        msgVbox.getChildren().add(hBoxName);
                        msgVbox.getChildren().add(hBox);
                    }
                });

            }else {
                String name = msgFromClient.split("-")[0];
                String msgFromServer = msgFromClient.split("-")[1];

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5,5,5,10));

                HBox hBoxName = new HBox();
                hBoxName.setAlignment(Pos.CENTER_LEFT);
                Text textName = new Text(name);
                TextFlow textFlowName = new TextFlow(textName);
                hBoxName.getChildren().add(textFlowName);

                Text text = new Text(msgFromServer);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0,0,0));

                hBox.getChildren().add(textFlow);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        msgVbox.getChildren().add(hBoxName);
                        msgVbox.getChildren().add(hBox);
                    }
                });
            }


}
    public void btnImojiOnAction(ActionEvent actionEvent) {
    }

    public void btnGalleryOnAction(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        File file =chooser.showOpenDialog(imgSelectBtn.getScene().getWindow());
        try {
            fileInputStream=new FileInputStream(file);
            image=new Image(fileInputStream);
           // img.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMsg(txtMsgType.getText());
    }

    private void emoji() {
        EmojiPicker emojiPicker = new EmojiPicker();

        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150,300);
        vBox.setLayoutX(200);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 30");

        ancClientForm.getChildren().add(vBox);

        emojiPicker.setVisible(false);

        emojibuttonId.setOnAction(event -> {
            if (emojiPicker.isVisible()) {
                emojiPicker.setVisible(false);
            } else {
                emojiPicker.setVisible(true);
            }
        });

        emojiPicker.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsgType.setText(txtMsgType.getText()+selectedEmoji);
            }
            emojiPicker.setVisible(false);
        });
    }

    private void sendMsg(String msgToSend) {
        if (!msgToSend.isEmpty()) {
            if (!msgToSend.matches(".*\\.(png|jpe?g|gif)$")) {

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new javafx.geometry.Insets(5, 5, 0, 10));

                javafx.scene.text.Text text = new javafx.scene.text.Text(msgToSend);
                //Text text = new Text(msgToSend);
                text.setStyle("-fx-font-size: 14");
                TextFlow textFlow = new TextFlow(text);

//              #0693e3 #37d67a #40bf75
                textFlow.setStyle("-fx-background-color: #0693e3; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
                textFlow.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));


                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                //org.w3c.dom.Text time = new org.w3c.dom.Text(stringTime);
                time.setStyle("-fx-font-size: 8");

                hBoxTime.getChildren().add(time);

                msgVbox.getChildren().add(hBox);
                msgVbox.getChildren().add(hBoxTime);


                try {
                    dataOutputStream.writeUTF(cName + "-" + msgToSend);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                txtMsgType.clear();
            }
        }

//    public static void receiveMessage(String msg, VBox vBox) throws IOException {
//        if (msg.matches(".*\\.(png|jpe?g|gif)$")){
//            HBox hBoxName = new HBox();
//            hBoxName.setAlignment(Pos.CENTER_LEFT);
//            Text textName = new Text(msg.split("[-]")[0]);
//            TextFlow textFlowName = new TextFlow(textName);
//            hBoxName.getChildren().add(textFlowName);
//
//            java.awt.Image image = new java.awt.Image(msg.split("[-]")[1]);
//            ImageView imageView = new ImageView(image);
//            imageView.setFitHeight(200);
//            imageView.setFitWidth(200);
//            HBox hBox = new HBox();
//            hBox.setAlignment(Pos.CENTER_LEFT);
//            hBox.setPadding(new Insets(5,5,5,10));
//            hBox.getChildren().add(imageView);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    vBox.getChildren().add(hBoxName);
//                    vBox.getChildren().add(hBox);
//                }
//            });
//
//        }else {
//            String name = msg.split("-")[0];
//            String msgFromServer = msg.split("-")[1];
//
//            HBox hBox = new HBox();
//            hBox.setAlignment(Pos.CENTER_LEFT);
//            hBox.setPadding(new Insets(5,5,5,10));
//
//            HBox hBoxName = new HBox();
//            hBoxName.setAlignment(Pos.CENTER_LEFT);
//            Text textName = new Text(name);
//            TextFlow textFlowName = new TextFlow(textName);
//            hBoxName.getChildren().add(textFlowName);
//
//            Text text = new Text(msgFromServer);
//            TextFlow textFlow = new TextFlow(text);
//            textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
//            textFlow.setPadding(new Insets(5,10,5,10));
//            text.setFill(Color.color(0,0,0));
//
//            hBox.getChildren().add(textFlow);
//
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    vBox.getChildren().add(hBoxName);
//                    vBox.getChildren().add(hBox);
//                }
//            });
//        }
//    }

    }
}
