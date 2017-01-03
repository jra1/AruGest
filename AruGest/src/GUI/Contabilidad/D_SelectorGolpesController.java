package GUI.Contabilidad;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class D_SelectorGolpesController {
	// Variables de la vista
	@FXML
	private TextField texto;

	// Resto de variables
	private Stage dialogStage;
	private int idseleccionado = 0;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * 
	 */
	public void cargarGolpes() {

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
	 * Devuelve el ID del golpe seleccionado
	 * 
	 * @return
	 */
	public int getId() {
		return idseleccionado;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {

			idseleccionado = 1;// *******************************
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validaciones de los campos introducidos por el cliente
	 * 
	 * @return true si los campos son correctos
	 */
	private boolean isInputValid() {
		return true;
	}
}
