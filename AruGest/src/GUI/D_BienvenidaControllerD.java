package GUI;

import Logica.Hilo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class D_BienvenidaControllerD {
	// Variables de la vista
	@FXML
	private Label lblBienvenida;
	@FXML
	public ProgressBar pb;
	@FXML
	private Label lblCargando;

	// Resto de variables
	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		lblBienvenida.setVisible(true);
		pb.setVisible(true);
		lblCargando.setVisible(false);
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
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 * 
	 * @throws InterruptedException
	 */
	@FXML
	private void handleOk() throws InterruptedException {
		if (lblBienvenida.isVisible()) {
			empiezaCrearBD();
		} else {
			okClicked = true;
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
	 * Llama al hilo para crear la BD
	 */
	private void empiezaCrearBD() throws InterruptedException {
		lblBienvenida.setVisible(false);
		pb.setVisible(true);
		lblCargando.setVisible(true);
		llamaHilo();
	}

	private void llamaHilo() {
		if (Hilo.hilo_CreaBD(pb)) {
			lblCargando.setText("Base de datos creada");
		}
	}
}
