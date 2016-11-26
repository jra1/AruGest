package GUI.Vehiculo;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import Logica.Utilidades;
import Modelo.Vehiculo;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class D_EditVehiculoController {

	@FXML
	private ComboBox<String> comboTipoVehiculo;
	@FXML
	private TextField txtTipoVehiculo;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtMarca;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtVersion;
	@FXML
	private TextField txtAnio;
	@FXML
	private TextField txtBastidor;
	@FXML
	private TextField txtLetrasMotor;
	@FXML
	private TextField txtColor;
	@FXML
	private TextField txtCodRadio;
	@FXML
	private CheckBox chckboxVehiculoSusti;

	private Stage dialogStage;
	private Vehiculo v;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		comboTipoVehiculo.getItems().addAll("Turismo", "Furgoneta", "Camión", "Autobús", "Autocaravana", "Moto",
				"Remolque");
		comboTipoVehiculo.setValue("Turismo");
		chckboxVehiculoSusti.setSelected(false);
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
	 * Coloca el vehículo a ser editado
	 * 
	 * @param vehiculo
	 *            a ser editado
	 */
	public void setVehiculo(Vehiculo v) {
		this.v = v;
		
		if(v.getTipoID() > 0){
			comboTipoVehiculo.setValue(Utilidades.tipoIDtoString(v.getTipoID()));			
		}
		txtMatricula.setText(v.getMatricula());
		if(v.getMatricula().isEmpty()){
			txtMatricula.setEditable(true);			
		}else{
			txtMatricula.setEditable(false);
		}
		txtMarca.setText(v.getMarca());
		txtModelo.setText(v.getModelo());
		txtVersion.setText(v.getVersion());
		txtAnio.setText("" + v.getAnio());
		txtBastidor.setText(v.getBastidor());
		txtLetrasMotor.setText(v.getLetrasmotor());
		txtColor.setText(v.getColor());
		txtCodRadio.setText(v.getCodradio());
		chckboxVehiculoSusti.setSelected(v.isEsVehiculoSustitucion());
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
			v.setTipoID(Utilidades.StringToTipoID(comboTipoVehiculo.getValue()));
			v.setMatricula(txtMatricula.getText());
			v.setMarca(txtMarca.getText());
			v.setModelo(txtModelo.getText());
			v.setVersion(txtVersion.getText());
			v.setAnio(Integer.parseInt(txtAnio.getText()));
			v.setBastidor(txtBastidor.getText());
			v.setLetrasmotor(txtLetrasMotor.getText());
			v.setColor(txtColor.getText());
			v.setCodradio(txtCodRadio.getText());
			v.setEsVehiculoSustitucion(chckboxVehiculoSusti.isSelected());
			
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

		if (txtMatricula.getText().length() == 0) {
			errorMessage += "Introduce la matrícula del vehículo\n";
		}
		if (txtMarca.getText().length() == 0 || txtModelo.getText().length() == 0) {
			errorMessage += "Introduce la marca y el modelo del vehículo";
		}
		if(txtAnio.getText().length() > 0){
			try {
				Integer.parseInt(txtAnio.getText());
			} catch (NumberFormatException e) {
				errorMessage = "Año no válido.\n Introduce únicamente números";
			}			
		}
		
		if (errorMessage.length() == 0) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inváidos", "Por favor corrige los campos", errorMessage);
			return false;
		}
	}
}
