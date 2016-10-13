package Logica;

import java.io.IOException;
//import java.security.Principal;

import GUI.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Inicio extends Application {

private Stage escenario; //Donde se cargan las escenas (interfaces)
private BorderPane root;

@Override
public void start(Stage primaryStage) {
     escenario= primaryStage;
     escenario.setTitle("AruGest Software");
     
   //Ponemos nuestro propio icono de la aplicación
     escenario.getIcons().add(new Image("file:images/logo_coche.png"));
     
     //Maximizado
     escenario.setMaximized(true);
     
     FXMLLoader loader = new FXMLLoader(Inicio.class.getResource("/GUI/Root.fxml"));
     try {
    	 //1.- Crear la escena desde el AnchorPane
          root = (BorderPane) loader.load();
          Scene escena = new Scene(root);
          //2.- Ponerla y mostrarla
          escenario.setScene(escena);
          escenario.show();
          //3.- Poner el controlador de la escena 
          RootController controlador = loader.getController();
          controlador.setMainAPP(this);
          
     } catch (IOException e) {
          e.printStackTrace();
     }

}

public static void main(String[] args) {
     launch(args);
}

public BorderPane getRoot() {
	return root;
}

}
