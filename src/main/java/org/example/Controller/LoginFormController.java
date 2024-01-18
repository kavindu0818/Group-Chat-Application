package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    }

    public String sendUserName(){
        return clientName;
    }


}

