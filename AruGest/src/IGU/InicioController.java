package IGU;

import Logica.Pincipal;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class InicioController {
@FXML
private Label etiqueta;

@FXML
private TextField texto;

@FXML
private Accordion acor;

@FXML
private TitledPane tPane;

private Pincipal main;

public void setmainAPP(Pincipal p){
     main=p;
     
     acor.setExpandedPane(tPane); //Para que al iniciar se expanda el panel de contabilidad (Se puede poner el que quiera)
}

@FXML
private void incrementarPulsado(){
     int n =Integer.parseInt(texto.getText());
     int numero = Integer.parseInt(etiqueta.getText());
     etiqueta.setText(n+numero+"");
    
}

}
