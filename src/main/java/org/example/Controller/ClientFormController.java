package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.EmojiPicker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientFormController {
    public AnchorPane ancClientForm;
    public Label lblUserName;
    public ScrollPane ScrollPaneMsgArea;
    public VBox msgVbox;
    public TextField txtMsgType;

    LoginFormController loginFormController = new LoginFormController();

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

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
                   // ServerFormController.receiveMessage(clientName+" joined.");

                    while (socket.isConnected()){
                        String receivingMsg = dataInputStream.readUTF();
                      //  receiveMessage(receivingMsg, ClientFormController.this.msgVbox);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

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
//    }
    public void btnImojiOnAction(ActionEvent actionEvent) {
    }

    public void btnGalleryOnAction(ActionEvent actionEvent) {
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
    }

    private void emoji() {
        // Create the EmojiPicker
        EmojiPicker emojiPicker = new EmojiPicker();

        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150,300);
        vBox.setLayoutX(400);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 30");

        ancClientForm.getChildren().add(vBox);

        // Set the emoji picker as hidden initially
        emojiPicker.setVisible(false);

        // Show the emoji picker when the button is clicked
        emojiButton.setOnAction(event -> {
            if (emojiPicker.isVisible()){
                emojiPicker.setVisible(false);
            }else {
                emojiPicker.setVisible(true);
            }
        });

        // Set the selected emoji from the picker to the text field
        emojiPicker.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsgType.setText(txtMsgType.getText()+selectedEmoji);
            }
            emojiPicker.setVisible(false);
        });
    }
}
