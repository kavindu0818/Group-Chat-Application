package org.example.Controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.ClientHandle;
import org.example.EmojiPicker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    public ImageView userImage;

    LoginFormController loginFormController = new LoginFormController();

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    // BufferedReader bufferedReader;

    ServerFormController serverFormController = new ServerFormController();

    private String cName;

    private ArrayList<ClientFormController> clients;
    private BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    private PrintWriter writer;

    ClientHandle clientHandle;
    String receivingMsg = null;

    String msg;

    public void initialize() {
        cName=loginFormController.sendUserName();
        lblUserName.setText(loginFormController.sendUserName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 3001);

                  //  dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    System.out.println(cName + "join");
                    dataInputStream = new DataInputStream(socket.getInputStream());
                  //  serverFormController.receiveMessage(cName + " joined.");

                    while (true) {
                        String type = dataInputStream.readUTF();
                        if (type.equals("img")) {

                            receiveImage(dataInputStream);

                        }else {
                           // System.out.println("sms");
                            String sms = type;
                            System.out.println("print" + sms);
                            receiveMessage(sms,msgVbox);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        this.msgVbox.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            ScrollPaneMsgArea.setVvalue(newValue.doubleValue());
        });

        emoji();
    }

    public void receiveImage(DataInputStream dataInputStream) {
        //Image image = new Image(image3);
        try {

           // String name = dataInputStream.readUTF();
            //The dis.read() method reads the length of the image data
            int imageDataLength = dataInputStream.readInt();
            //Creating a byte array using the length of the image data
            byte[] imageData = new byte[imageDataLength];
            dataInputStream.readFully(imageData);

            //Converting the byte array to a buffered image object
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(bais);

            //Convert BufferedImage to JavaFX Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

           // String cn = dataInputStream.readUTF();
//            System.out.println("1234" + name);
//
//            Text text = new Text(name);
//
//            HBox hBoxName = new HBox(text);
//            hBoxName.setAlignment(Pos.CENTER_LEFT);
//            Text textName = new Text();
//            TextFlow textFlowName = new TextFlow(textName);
//            hBoxName.getChildren().add(textFlowName);
            // Create an ImageView with the Image
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            //ADD A scroll pane to the image container
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(imageView);

            //Append the ImageView to the imageContainer
            Platform.runLater(() -> msgVbox.getChildren().add(imageView));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(cName + "-" + msgToSend);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                txtMsgType.clear();
            }
        }


    private void sendImage(String photo) throws IOException {
        Image image = new Image("file:" + photo);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 10));

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        hBox.getChildren().add(imageView);

        msgVbox.getChildren().add(hBox);

        try {
            byte[] imageBytes = imagenToByte(image);

            dataOutputStream.writeUTF("image");
            dataOutputStream.writeInt(imageBytes.length);
            dataOutputStream.write(imageBytes);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void handleImage(String image) {

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

        } else {
            String name = msg.split("-")[0];
            String msgFromServer = msg.split("-")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new javafx.geometry.Insets(5, 5, 5, 10));

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(name);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Text text = new Text(msgFromServer);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0, 0, 0));

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) imgSelectBtn.getScene().getWindow();
        File image = fileChooser.showOpenDialog(stage);
        if (image != null) {
            try {
                //Reading the image!
                // Reads the contents of the image file and creates a BufferedImage object called bufferedImage
                BufferedImage bufferedImage = ImageIO.read(image);

                //This line creates a ByteArrayOutputStream object called byteArrayOutputStream. This stream is used to write the image data as bytes
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                //This line writes the image data from the bufferedImage to the byteArrayOutputStream. The ImageIO.write() method takes three parameters: the image to be written (bufferedImage), the format of the image ("jpg" in this case), and the output stream to which the image data will be written (byteArrayOutputStream)
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

                //Below line can get the byte image data from the byteArrayOutPutStream and convert it to a byte array
                byte[] imageData = byteArrayOutputStream.toByteArray();

                //Sending the image through output stream
//                DataOutputStream dos2 = new DataOutputStream(socket.getOutputStream());
                //Letting the server know an image is being sent
                DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
                dataOutputStream1.writeUTF("img");
               // dataOutputStream1.writeUTF(cName);
                //Writing the length of the image data
                dataOutputStream1.writeInt(imageData.length);
                dataOutputStream1.write(imageData);
                dataOutputStream1.flush();


                //Appending the image to the text area
                // Convert the image data to an Image
                ByteArrayInputStream imageStream = new ByteArrayInputStream(imageData);
                Image image1 = new Image(imageStream);

                // Convert BufferedImage to JavaFX Image
                Image image2 = SwingFXUtils.toFXImage(bufferedImage, null);

                // Create an ImageView with the Image
                ImageView imageView = new ImageView(image2);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);


                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                hBox.getChildren().add(imageView);


                //ADD A scroll pane to the image container
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(hBox);

                // Append the ImageView to the imageContainer
                Platform.runLater(() -> msgVbox.getChildren().add(hBox));


            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error while reading the image data !");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while selecting the image !");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getScene().getStylesheets().add(getClass().getResource("/style/notification.css").toExternalForm());
            alert.showAndWait();
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMsg(txtMsgType.getText());
    }
}



