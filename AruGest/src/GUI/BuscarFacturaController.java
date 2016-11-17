package GUI;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import Logica.Inicio;
import Modelo.FacturaClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

/**
 * Clase controlador de la ventana BuscarFactura, que se utiliza para buscar en
 * la base de datos las facturas que coincidan con los parámetros que introduce
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
	private TableColumn<FacturaClienteVehiculo, String> columnaFecha;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaImporte;

	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();
	
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
	 * Busca las facturas, presupuestos, órdenes o resguardos que coincidan con
	 * los parámetros de búsqueda y pone las encontradas en la tabla
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
			alert.setTitle("Atención");
			alert.setHeaderText("No encontrado");
			alert.setContentText("No hay facturas con los parámetros de búsqueda introducidos.");

			alert.showAndWait();
		} else {
			/*Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Encontrado!");
			alert.setContentText("SE HA ENCONTRADO ALGO!");
			alert.showAndWait();
			*/
			for(FacturaClienteVehiculo fce : lista){
				listaFacturas.add(fce);
				columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaVehiculo.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
				columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
				columnaFecha.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaPropertyFormat());
				columnaImporte.setCellValueFactory(cellData -> cellData.getValue().getFactura().importeTotalProperty());
				tableFacturas.setItems(listaFacturas);
			}
		}
	}
	
	/**
	 * Función para cargar la factura seleccionada
	 */
	@FXML
	private void cargarFactura(){
		int selectedIndex = tableFacturas.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			try {
	            // Cargar la vista de nueva factura
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(Inicio.class.getResource("/GUI/NuevaFactura.fxml"));
	            AnchorPane nuevaFactura = (AnchorPane) loader.load();
	        	
	            // Poner la nueva vista en el centro del root
	            main.getRoot().setCenter(nuevaFactura);
	            
	            // Poner el controlador de la nueva vista.
	            NuevaFacturaController controller = loader.getController();
	            controller.setMainAPP(main);
	            controller.cargaFactura(listaFacturas.get(selectedIndex));

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		} else {
			// Nada seleccionado.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Ninguna factura seleccionada");
			alert.setContentText("Selecciona la factura que quieras cargar.");

			alert.showAndWait();
		}
	}
}
