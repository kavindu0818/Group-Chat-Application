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

  //private ClientFormController clientFormController = new ClientFormController();

//    public void btnOpenBrowserOnAction(ActionEvent actionEvent) throws IOException {
//        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
//        dialog.setMode(FileDialog.LOAD);
//        dialog.setVisible(true);
//        String file = dialog.getDirectory()+dialog.getFile();
//        dialog.dispose();
//        clientFormController.sendImageUser(file);
//        System.out.println(file + " chosen.");
//
//    }

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


    public void btnOpenBrowserOnAction(ActionEvent actionEvent) {
    }
}

