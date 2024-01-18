package org.example.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;

public class ServerFormController {
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
