package GUI.Contabilidad;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Golpe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class D_SelectorGolpesController {
	// Variables de la vista
	@FXML
	private TableView<Golpe> tablaGolpes;
	@FXML
	private TableColumn<Golpe, Number> columnaID;
	@FXML
	private TableColumn<Golpe, String> columnaNombre;

	// Resto de variables
	private Stage dialogStage;
	private ObservableList<Golpe> listaGolpes = FXCollections.observableArrayList();
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
		listaGolpes = Inicio.CONEXION.buscarGolpes();
		columnaID.setCellValueFactory(cellData -> cellData.getValue().idgolpeProperty());
		columnaNombre.setCellValueFactory(cellData -> cellData.getValue().nombreGolpeProperty());
		tablaGolpes.setItems(listaGolpes);
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
			// idseleccionado = 1;
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
		if (tablaGolpes.getSelectionModel().getSelectedIndex() >= 0) {
			idseleccionado = tablaGolpes.getSelectionModel().getSelectedItem().getIdgolpe();
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún golpe seleccionado",
					"Selecciona en la tabla el golpe que desee añadir");
			return false;
		}
	}
}
