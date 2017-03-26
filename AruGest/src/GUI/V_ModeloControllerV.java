package GUI;

import Logica.Inicio;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class V_ModeloControllerV {

	// Variables de la vista
	@FXML
	private Button btnPantalla1;

	// Resto de variables
	private Inicio main;

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * Funciones que se llamen desde la vista, con @FXML delante
	 */
	@FXML
	private void funcion() {
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
