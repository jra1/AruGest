package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDate;

import Logica.Utilidades;
import Modelo.VehiculoSustitucionClienteVehiculo;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class DialogoSustitucionController {

	@FXML
	private Label lblFecha;
	@FXML
	private DatePicker txtFecha;
	@FXML
	private TextArea txtObservaciones;

	private VehiculoSustitucionClienteVehiculo vscv;
	private String tipo = ""; //D = Devolucion ; E = Entrega
	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		if(tipo.equalsIgnoreCase("D")){
			lblFecha.setText("Fecha devolución");
		}else if(tipo.equalsIgnoreCase("E")){
			lblFecha.setText("Fecha entrega");
		}
		txtFecha.setValue(LocalDate.now());
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setVehiculoSustitucion(VehiculoSustitucionClienteVehiculo vscv) {
		this.vscv = vscv;
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
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			vscv.getVehiculoSustitucion().setFechadevuelve(Utilidades.LocalDateADate(txtFecha.getValue()));
			vscv.getVehiculoSustitucion().setObservaciones(vscv.getVehiculoSustitucion().getObservaciones() + " " + txtObservaciones.getText());
						
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
	 * Validaciones de los campos introducidos por el cliente
	 * 
	 * @return true si los campos son correctos
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (txtFecha.getValue() == null) {
			errorMessage += "Introduce la fecha \n";
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inváidos", "Por favor corrige los campos", errorMessage);
			return false;
		}else{
			return true;			
		}
	}
}
