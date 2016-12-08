package GUI;

import Logica.Inicio;
import Logica.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class D_OpcionesController {
	@FXML
	private TextField txtPrecioHora;
	@FXML
	private TextField txtIva;
	@FXML
	private TextField txtNumPresupuesto;
	@FXML
	private TextField txtNumFactura;

	private float precioHora;
	private float iva;
	private int numPresupuesto;
	private int numFactura;

	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Cargar los datos
		txtPrecioHora.setText(Inicio.PRECIO_HORA + "");
		txtIva.setText(Inicio.PRECIO_IVA + "");
		txtNumPresupuesto.setText(Inicio.NUM_PRESUPUESTO + "");
		txtNumFactura.setText(Inicio.NUM_FACTURA + "");
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
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			// Cambiar las opciones en la BD
			if (Inicio.CONEXION.actualizarOpciones(precioHora, iva, numPresupuesto, numFactura)) {
				Inicio.PRECIO_HORA = precioHora;
				Inicio.PRECIO_IVA = iva;
				Inicio.NUM_PRESUPUESTO = numPresupuesto;
				Inicio.NUM_FACTURA = numFactura;
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Las opciones han sido cambiadas", "");
			}

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
		String mensaje = "";
		try {
			precioHora = Float.parseFloat(txtPrecioHora.getText().replace(",", "."));
		} catch (NumberFormatException e) {
			mensaje = "Formato del precio hora incorrecto. Introduce un valor numérico.";
		}
		try {
			iva = Float.parseFloat(txtIva.getText().replace(",", "."));
		} catch (NumberFormatException e) {
			mensaje = "Formato de iva incorrecto. Introduce un valor numérico.";
		}
		try {
			numPresupuesto = Integer.parseInt(txtNumPresupuesto.getText());
		} catch (NumberFormatException e) {
			mensaje = "Formato del número de presupuesto incorrecto. Introduce un valor numérico.";
		}
		try {
			numFactura = Integer.parseInt(txtNumFactura.getText());
		} catch (NumberFormatException e) {
			mensaje = "Formato del número de factura incorrecto. Introduce un valor numérico.";
		}
		if (mensaje.equalsIgnoreCase("")) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Datos incorrectos", mensaje);
			return false;
		}
	}
}
