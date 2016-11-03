package GUI;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import Logica.Inicio;
import Modelo.Factura;
import Modelo.FacturaClienteVehiculo;
import Modelo.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * Clase controlador de la ventana BuscarFactura, que se utiliza para buscar en
 * la base de datos las facturas que coincidan con los par�metros que introduce
 * el usuario
 * 
 * @author Joseba
 *
 */
public class BuscarFacturaController {

	@FXML
	private CheckBox chckbxFacturas;
	@FXML
	private TextField txtNumfactura;
	@FXML
	private CheckBox chckbxPresupuestos;
	@FXML
	private TextField txtNumPresupuesto;
	@FXML
	private CheckBox chckbxOrdenDeReparacion;
	@FXML
	private TextField txtNumOrden;
	@FXML
	private CheckBox chckbxResguardoDeposito;
	@FXML
	private TextField txtNumResguardo;

	@FXML
	private TextField txtNombreCliente;
	@FXML
	private TextField txtFijo;
	@FXML
	private TextField txtMovil;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtMatricula;
	@FXML
	private DatePicker txtFechaDesde;
	@FXML
	private DatePicker txtFechaHasta;

	@FXML
	private TableView<FacturaClienteVehiculo> tableFacturas;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaNombre;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaVehiculo;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaMatricula;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Date> columnaFecha;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaImporte;

	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();
	private Factura factura;

	private Inicio main;

	public void setMainAPP(Inicio p) {
		main = p;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Inicializar los checkbox marcados
		chckbxFacturas.setSelected(true);
		chckbxPresupuestos.setSelected(true);
		chckbxOrdenDeReparacion.setSelected(true);
		chckbxResguardoDeposito.setSelected(true);
		txtFechaHasta.setValue(LocalDate.now());
		txtFechaDesde.setValue(txtFechaHasta.getValue().minusDays(7));
	}

	/**
	 * Busca las facturas, presupuestos, �rdenes o resguardos que coincidan con
	 * los par�metros de b�squeda y pone las encontradas en la tabla
	 */
	@FXML
	private void buscarFacturas() {
		listaFacturas.clear();
		tableFacturas.getItems().clear();
		int numFactura = 0;
		int numPresupuesto = 0;
		int numOrden = 0;
		int numResguardo = 0;
		if (!txtNumfactura.getText().isEmpty()) {
			numFactura = Integer.parseInt(txtNumfactura.getText());
		}
		if (!txtNumPresupuesto.getText().isEmpty()) {
			numPresupuesto = Integer.parseInt(txtNumPresupuesto.getText());
		}
		if (!txtNumOrden.getText().isEmpty()) {
			numOrden = Integer.parseInt(txtNumOrden.getText());
		}
		if (!txtNumResguardo.getText().isEmpty()) {
			numResguardo = Integer.parseInt(txtNumResguardo.getText());
		}
		ArrayList<FacturaClienteVehiculo> lista = Inicio.CONEXION.buscarFacturas(numFactura, numPresupuesto, numOrden,
				numResguardo, txtNombreCliente.getText(), txtModelo.getText(), txtMatricula.getText(),
				txtFijo.getText(), txtMovil.getText(), txtFechaDesde.getValue(), txtFechaHasta.getValue());
		if (lista.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atenci�n");
			alert.setHeaderText("No encontrado");
			alert.setContentText("No hay facturas con los par�metros de b�squeda introducidos.");

			alert.showAndWait();
		} else {
			/*Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atenci�n");
			alert.setHeaderText("Encontrado!");
			alert.setContentText("SE HA ENCONTRADO ALGO!");
			alert.showAndWait();
			*/
			for(FacturaClienteVehiculo fce : lista){
				listaFacturas.add(fce);
				columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaVehiculo.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().modeloProperty());
				columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
				columnaFecha.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaProperty());
				columnaImporte.setCellValueFactory(cellData -> cellData.getValue().getFactura().importeTotalProperty());
				tableFacturas.setItems(listaFacturas);
				
			}
		}
	}
}
