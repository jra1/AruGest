package IGU;

import Logica.Pincipal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InicioController {
@FXML
private Label etiqueta;

@FXML
private TextField texto;

private Pincipal main;

public void setmainAPP(Pincipal p){
     main=p;
}

@FXML
private void incrementarPulsado(){
     int n =Integer.parseInt(texto.getText());
     int numero = Integer.parseInt(etiqueta.getText());
     etiqueta.setText(n+numero+"");
}

}
