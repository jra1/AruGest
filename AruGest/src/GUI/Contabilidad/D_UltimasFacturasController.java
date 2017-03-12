package GUI.Contabilidad;

import Logica.Inicio;
import Modelo.FacturaClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class D_UltimasFacturasController {
	// Variables de la vista
	@FXML
	private TableView<FacturaClienteVehiculo> tableFacturas;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaNumFactura;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaFecha;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaNombre;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaVehiculo;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaMatricula;

	// Resto de variables
	private Stage dialogStage;
	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		cargarFacturas();
	}

	/**
	 * Carga las últimas facturas guardadas
	 */
	public void cargarFacturas() {
		listaFacturas = Inicio.CONEXION.buscarUltimasFacturas();
		columnaNumFactura.setCellValueFactory(cellData -> cellData.getValue().getFactura().numfacturaProperty());
		columnaFecha.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaPropertyFormat());
		columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
		columnaVehiculo.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
		columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
		tableFacturas.setItems(listaFacturas);
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
