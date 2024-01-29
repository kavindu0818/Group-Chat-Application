package org.example.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
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
import org.example.ClientHandle;
import org.example.EmojiPicker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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
    public ImageView userImage;

    LoginFormController loginFormController = new LoginFormController();

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    // BufferedReader bufferedReader;

    ServerFormController serverFormController = new ServerFormController();

    private String cName = loginFormController.sendUserName();

    private ArrayList<ClientFormController> clients;
    private BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    private PrintWriter writer;

    ClientHandle clientHandle;
    String receivingMsg = null;

    public void initialize() {
        lblUserName.setText(loginFormController.sendUserName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 3001);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    serverFormController.receiveMessage(cName + " joined.");

                    while (socket.isConnected()) {
                        String messageType = dataInputStream.readUTF();


                             receiveMessage(messageType, ClientFormController.this.msgVbox);
//                        if (messageType.equalsIgnoreCase("image")) {
//                            int imageSize = dataInputStream.readInt();
//                            byte[] imageBytes = new byte[imageSize];
//                            dataInputStream.readFully(imageBytes);
//
//                            Image image = convertBytesToJavaFXImage(imageBytes);
//                            receiveImage(image);
//                        }
                    }
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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

    public void receiveImage(Image ima){
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(imageView);

        Platform.runLater(() -> msgVbox.getChildren().add(hBox));
    }
    public byte[] imagenToByte(Image imgId) {
        BufferedImage bufferimage = SwingFXUtils.fromFXImage(imgId, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferimage, "jpg", output);
            ImageIO.write(bufferimage, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = output.toByteArray();
        return data;
    }


    public Image convertBytesToJavaFXImage(byte[] imageData) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(bis);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void emoji() {

        EmojiPicker emojiPicker = new EmojiPicker();

        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150, 400);
        vBox.setLayoutX(100);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 30");

        ancClientForm.getChildren().add(vBox);    //  jzest
        emojiPicker.setVisible(false);

        emojibuttonId.setOnAction(event -> {
            if (emojiPicker.isVisible()) {
                emojiPicker.setVisible(false);
            } else {
                emojiPicker.setVisible(true);
            }
        });

        // Set the selected emoji from the picker to the text field
        emojiPicker.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsgType.setText(txtMsgType.getText() + selectedEmoji);
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

                Text text = new Text(msgToSend);
                text.setStyle("-fx-font-size: 14");
                TextFlow textFlow = new TextFlow(text);

//              #0693e3 #37d67a #40bf75
                textFlow.setStyle("-fx-background-color: #0693e3; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
                textFlow.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
                text.setFill(javafx.scene.paint.Color.color(1, 1, 1));

                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new javafx.geometry.Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
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
    }

    private void sendImage(String photo) throws IOException {
        Image image = new Image(photo);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
//        TextFlow textFlow = new TextFlow(imageView);
        HBox hBox = new HBox();
        hBox.setPadding(new javafx.geometry.Insets(5,5,5,10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        msgVbox.getChildren().add(hBox);

        try {
            byte[] bIma = imagenToByte(image);

            // Send the image type and size first
            dataOutputStream.writeUTF("image");
            dataOutputStream.writeInt(bIma.length);
            dataOutputStream.flush();

            // Send the image data
            dataOutputStream.write(bIma);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void receiveMessage(String msg, VBox vBox) throws IOException {

        if (msg.matches(".*\\.(png|jpe?g|gif)$")) {
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(msg.split("[-]")[0]);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            String imagePath = msg.split("[-]")[1];
            Image image = new Image(new File(imagePath).toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new javafx.geometry.Insets(5, 5, 5, 10));
            hBox.getChildren().add(imageView);

            Platform.runLater(() -> {
                vBox.getChildren().add(hBoxName);
                vBox.getChildren().add(hBox);
            });

        }else {
            String name = msg.split("-")[0];
            String msgFromServer = msg.split("-")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new javafx.geometry.Insets(5,5,5,10));

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
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }


    public void btnImojiOnAction(ActionEvent actionEvent) {

    }

    public void btnGalleryOnAction(ActionEvent actionEvent) throws IOException {

        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getDirectory()+dialog.getFile();
        dialog.dispose();
        sendImage(file);
    }


    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMsg(txtMsgType.getText());
    }
}



