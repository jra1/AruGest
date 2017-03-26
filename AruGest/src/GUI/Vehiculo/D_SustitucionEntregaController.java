package GUI.Vehiculo;

import java.time.LocalDate;
import java.util.ArrayList;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class D_SustitucionEntregaController {

	@FXML
	private TableView<Vehiculo> tableDisponibles;
	@FXML
	private TableColumn<Vehiculo, String> columnaVehiculoDisponibles;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatriculaDisponibles;
	@FXML
	private DatePicker txtFecha;
	@FXML
	private TextArea txtObservaciones;

	private ObservableList<Vehiculo> listaDisponibles = FXCollections.observableArrayList();

	private VehiculoSustitucionClienteVehiculo vscv;
	// private VehiculoSustitucion vs;
	private Vehiculo v = new Vehiculo(0);
	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		txtFecha.setValue(LocalDate.now());
		cargarDisponibles();
	}

	/**
	 * Carga los vehículos de sustitución que hay disponibles actualmente en la
	 * tabla correspondiente
	 */
	private void cargarDisponibles() {
		listaDisponibles.clear();
		tableDisponibles.getItems().clear();
		ArrayList<Vehiculo> lista = Inicio.CONEXION.buscarDisponibles();
		if (!lista.isEmpty()) {
			for (Vehiculo v : lista) {
				listaDisponibles.add(v);
				columnaVehiculoDisponibles.setCellValueFactory(cellData -> cellData.getValue().marcaModeloProperty());
				columnaMatriculaDisponibles.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
				tableDisponibles.setItems(listaDisponibles);
			}
		}
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
			v = tableDisponibles.getSelectionModel().getSelectedItem();

			vscv.setFechacoge(Utilidades.LocalDateADate(txtFecha.getValue()));
			vscv.setFechadevuelve(null);
			vscv.setClienteID(Inicio.CLIENTE_ID);
			vscv.setVehiculoID(v.getIdvehiculo());
			vscv.setObservaciones(txtObservaciones.getText());
			vscv.setVehiculo(v);

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
		int selectedIndex = tableDisponibles.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			errorMessage += "Debes seleccionar un vehículo de sustitución en la tabla \n";
		}
		if (txtFecha.getValue() == null) {
			errorMessage += "Introduce la fecha \n";
		}
		if (errorMessage.isEmpty()) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inváidos", "Por favor corrige los campos",
					errorMessage);
			return false;
		}
	}
}
