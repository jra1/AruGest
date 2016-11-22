package GUI;

import java.util.ArrayList;

import Logica.Inicio;
import Modelo.Cliente;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VehiculosSustitucionController {

	@FXML
	private TableView<Vehiculo> tableDisponibles;
	@FXML
	private TableColumn<Vehiculo, String> columnaVehiculoDisponibles;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatriculaDisponibles;

	@FXML
	private TableView<VehiculoSustitucion> tablePrestados;
	@FXML
	private TableColumn<Vehiculo, String> columnaVehiculoPrestados;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatriculaPrestados;
	@FXML
	private TableColumn<Cliente, String> columnaClientePrestados;
	@FXML
	private TableColumn<VehiculoSustitucion, String> columnaFechaEntregaPrestados;
	@FXML
	private TableColumn<VehiculoSustitucion, String> columnaObservacionesPrestados;

	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtVehiculo;
	@FXML
	private DatePicker txtFechaEntrega;
	@FXML
	private DatePicker txtFechaDevolucion;
	@FXML
	private Button btnFiltrar;

	@FXML
	private TableView<Vehiculo> tableHistorico;
	@FXML
	private TableColumn<Vehiculo, String> columnaVehiculoHistorico;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatriculaHistorico;
	@FXML
	private TableColumn<Cliente, String> columnaClienteHistorico;
	@FXML
	private TableColumn<VehiculoSustitucion, String> columnaFechaEntregaHistorico;
	@FXML
	private TableColumn<VehiculoSustitucion, String> columnaFechaDevolucionHistorico;
	@FXML
	private TableColumn<VehiculoSustitucion, String> columnaObservacionesHistorico;

	private ObservableList<Vehiculo> listaDisponibles = FXCollections.observableArrayList();
	private ObservableList<VehiculoSustitucion> listaPrestados = FXCollections.observableArrayList();
	private ObservableList<VehiculoSustitucion> listaHistorico = FXCollections.observableArrayList();

	private Inicio main;

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Cargar disponibles
		cargarDisponibles();
		// Cargar prestados
		cargarPrestados();
		// Cargar histórico ¿¿??

		// tableVehiculos.getSelectionModel().selectedItemProperty()
		// .addListener((observable, oldValue, newValue) ->
		// mostrarDetallesVehiculo(newValue));
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
	 * Carga los vehículos de sustitución que hay prestados actualmente en la
	 * tabla correspondiente
	 */
	private void cargarPrestados() {
		listaPrestados.clear();
		tablePrestados.getItems().clear();
		ArrayList<VehiculoSustitucion> lista = Inicio.CONEXION.buscarPrestados();
		if (!lista.isEmpty()) {
			Cliente c;
			Vehiculo v;
			for (VehiculoSustitucion vs : lista) {
				listaPrestados.add(vs);
				c = Inicio.CONEXION.leerClientePorID(vs.getClienteID());
				v = Inicio.CONEXION.leerVehiculoPorID(vs.getVehiculoID());
				//columnaVehiculoPrestados.setCellValueFactory(cellData -> cellData.getValue().marcaModeloProperty());
				//columnaMatriculaPrestados.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
				// columnaClientePrestados.setCellValueFactory(cellData ->
				// cellData.getValue().nombreProperty());
				columnaFechaEntregaPrestados
						.setCellValueFactory(cellData -> cellData.getValue().fechacogePropertyFormat());
				// FALTA AÑADIR LA COLUMNA DE COMENTARIOS
				tablePrestados.setItems(listaPrestados);
			}
		}
	}

	/**
	 * Filtra los vehiculos de sustitución que coincidan con los parámetros de
	 * búsqueda y pone los encontrados en la tabla
	 */
	@FXML
	private void filtrar() {
		// listaVehiculos.clear();
		// tableVehiculos.getItems().clear();
		// ArrayList<Vehiculo> lista = Inicio.CONEXION.buscarVehiculos(
		// Utilidades.StringToTipoID(comboTipoVehiculo.getValue()),
		// txtMatricula.getText(), txtMarca.getText(),
		// txtModelo.getText(), txtNombre.getText());
		// if (lista.isEmpty()) {
		// Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No
		// encontrado",
		// "No hay vehículos con los parámetros de búsqueda introducidos.");
		// } else {
		// for (Vehiculo v : lista) {
		// listaVehiculos.add(v);
		// columnaTipo.setCellValueFactory(
		// cellData ->
		// Utilidades.tipoIDtoStringProperty(cellData.getValue().getTipoID()));
		// columnaMatricula.setCellValueFactory(cellData ->
		// cellData.getValue().matriculaProperty());
		// columnaMarca.setCellValueFactory(cellData ->
		// cellData.getValue().marcaProperty());
		// columnaModelo.setCellValueFactory(cellData ->
		// cellData.getValue().modeloProperty());
		// columnaCliente.setCellValueFactory(cellData -> Inicio.CONEXION
		// .leerClientePorID(cellData.getValue().getClienteID()).nombreProperty());
		// tableVehiculos.setItems(listaVehiculos);
		//
		// }
		// }
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
