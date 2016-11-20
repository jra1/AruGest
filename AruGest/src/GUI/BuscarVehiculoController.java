package GUI;

import java.io.IOException;
import java.util.ArrayList;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class BuscarVehiculoController {

	@FXML
	private ComboBox<String> comboTipoVehiculo;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtMarca;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtNombre;
	@FXML
	private Button btnBuscar;

	@FXML
	private TableView<Vehiculo> tableVehiculos;
	@FXML
	private TableColumn<Vehiculo, String> columnaTipo;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatricula;
	@FXML
	private TableColumn<Vehiculo, String> columnaMarca;
	@FXML
	private TableColumn<Vehiculo, String> columnaModelo;
	@FXML
	private TableColumn<Vehiculo, Number> columnaAnio;

	@FXML
	private Button btnVerVehiculo;
	@FXML
	private Button btnHacerFactura;

	private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
	private int tipoVehiculo = 1; // 1=Particular, 2=Empresa

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
		comboTipoVehiculo.getItems().addAll("Turismo", "Furgoneta", "Camión", "Autobús", "Autocaravana", "Moto",
				"Remolque");
		comboTipoVehiculo.setValue("Turismo");
	}

	/**
	 * Busca los vehiculos que coincidan con los parámetros de búsqueda y pone
	 * los encontrados en la tabla
	 */
	@FXML
	private void buscarVehiculo() {
		listaVehiculos.clear();
		tableVehiculos.getItems().clear();
		ArrayList<Vehiculo> lista = Inicio.CONEXION.buscarVehiculos(
				Utilidades.StringToTipoID(comboTipoVehiculo.getValue()), txtMatricula.getText(), txtMarca.getText(),
				txtModelo.getText(), txtNombre.getText());
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No encontrado",
					"No hay vehículos con los parámetros de búsqueda introducidos.");
		} else {
			for (Vehiculo v : lista) {
				listaVehiculos.add(v);
				columnaTipo.setCellValueFactory(
						cellData -> Utilidades.tipoIDtoStringProperty(cellData.getValue().getTipoID()));
				columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
				columnaMarca.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
				columnaModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
				columnaAnio.setCellValueFactory(cellData -> cellData.getValue().anioProperty());
				tableVehiculos.setItems(listaVehiculos);
			}
		}
	}

	/**
	 * Función para cargar el cliente seleccionado
	 */
	/*
	 * @FXML private void cargarVehiculo() { int selectedIndex =
	 * tableClientes.getSelectionModel().getSelectedIndex(); if (selectedIndex
	 * >= 0) { try { // Cargar la vista de Cliente FXMLLoader loader = new
	 * FXMLLoader();
	 * loader.setLocation(Inicio.class.getResource("/GUI/Cliente.fxml"));
	 * AnchorPane cliente = (AnchorPane) loader.load();
	 * 
	 * // Poner la nueva vista en el centro del root
	 * main.getRoot().setCenter(cliente);
	 * 
	 * // Poner el controlador de la nueva vista. ClienteController controller =
	 * loader.getController(); controller.setMainAPP(main); Inicio.CLIENTE_ID =
	 * listaClientes.get(selectedIndex).getCliente().getIdcliente();
	 * controller.cargaCliente(listaClientes.get(selectedIndex));
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } } else { // Nada
	 * seleccionado. Alert alert = new Alert(AlertType.INFORMATION);
	 * alert.setTitle("Atención"); alert.setHeaderText(
	 * "Ningún cliente seleccionado"); alert.setContentText(
	 * "Selecciona el cliente que quieras cargar.");
	 * 
	 * alert.showAndWait(); } }
	 */
}
