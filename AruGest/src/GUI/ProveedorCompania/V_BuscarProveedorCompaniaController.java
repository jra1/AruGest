package GUI.ProveedorCompania;

import Logica.Inicio;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class V_BuscarProveedorCompaniaController {

	// Variables de la vista
	@FXML
	private Button btnPantalla1;

	// Resto de variables
	private Inicio main;
	/*
	 * private ScrollPane sp; private AnchorPane ap; private GestorVentana gv;
	 * private String nombre = "";
	 * 
	 * public Button boton1; public Button boton2; public Button boton3;
	 */

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/*
	 * public void setScrollPane(ScrollPane root) { this.sp = root; }
	 */

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
