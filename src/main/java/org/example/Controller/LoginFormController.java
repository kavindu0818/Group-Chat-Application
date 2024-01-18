package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public TextField txtUserName;
    public ImageView imageProfilePicture;

  public static String clientName = "Client";

    public void btnOpenBrowserOnAction(ActionEvent actionEvent) {
    }

    public void btnChatStrartOnAction(ActionEvent actionEvent) throws IOException {
        clientName = txtUserName.getText();

       AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/View/ClientForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(clientName);
        stage.centerOnScreen();
        stage.show();

        txtUserName.clear();


//       if (!txtUserName.getText().isEmpty() && txtUserName.getText().matches("[A-Za-z0-9]+")) {
//            Stage primaryStage = new Stage();
//          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ClientForm.fxml"));
//            ClientFromController controller = new ClientFromController();
//            controller.setUserName(txtUserName.getText());
//            fxmlLoader.setController(controller);
//
//
//            primaryStage.setScene(new Scene(fxmlLoader.load()));
//            primaryStage.setTitle(txtUserName.getText());
//            primaryStage.setResizable(false);
//            primaryStage.centerOnScreen();
//            primaryStage.setOnCloseRequest(windowEvent -> {
//                controller.shutdown();
//            });
//            primaryStage.show();
//
//            txtUserName.clear();
//        } else {
//            new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
//        }
//
//        // Assuming you are checking var1 elsewhere in your code
//        String var1 = "someValue";  // Replace this with your actual variable
//
//        if (var1 != null && var1.startsWith("#")) {
//            // Handle the condition where var1 starts with "#"
//            System.out.println("var1 starts with #");
//        }
    }

    public String sendUserName(){
        return clientName;
    }


}

