package Logica;

import java.io.IOException;
import java.security.Principal;

import IGU.InicioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Pincipal extends Application {

private Stage escenario; //Donde se cargan las escenas (interfaces)

@Override
public void start(Stage primaryStage) {
     escenario= primaryStage;
     FXMLLoader loader = new FXMLLoader(Principal.class.getResource("/IGU/Inicio.fxml"));
     try {
          AnchorPane panel = (AnchorPane) loader.load();
          Scene escena = new Scene(panel);
          escenario.setScene(escena);
          escenario.show();
          InicioController controlador = loader.getController();
          controlador.setmainAPP(this);
     } catch (IOException e) {
          e.printStackTrace();
     }

}

public static void main(String[] args) {
     launch(args);
}

}
