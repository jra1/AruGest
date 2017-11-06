package GUI;

import Logica.Inicio;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class D_AyudaController {

	@FXML
	private Label lblVersion;
	
	// Resto de variables
	private Stage dialogStage;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		lblVersion.setText("Versión " + Inicio.DBVERSION);
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		dialogStage.close();
	}
}
