package GUI.Vehiculo;

import java.util.ArrayList;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Cliente;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucion;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class V_VehiculosSustitucionController {

	@FXML
	private TableView<Vehiculo> tableDisponibles;
	@FXML
	private TableColumn<Vehiculo, String> columnaVehiculoDisponibles;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatriculaDisponibles;

	@FXML
	private TableView<VehiculoSustitucionClienteVehiculo> tablePrestados;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaVehiculoPrestados;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaMatriculaPrestados;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaClientePrestados;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaFechaEntregaPrestados;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaObservacionesPrestados;

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
	private TableView<VehiculoSustitucionClienteVehiculo> tableHistorico;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaVehiculoHistorico;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaMatriculaHistorico;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaClienteHistorico;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaFechaEntregaHistorico;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaFechaDevolucionHistorico;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaObservacionesHistorico;

	private ObservableList<Vehiculo> listaDisponibles = FXCollections.observableArrayList();
	private ObservableList<VehiculoSustitucionClienteVehiculo> listaPrestados = FXCollections.observableArrayList();
	private ObservableList<VehiculoSustitucionClienteVehiculo> listaHistorico = FXCollections.observableArrayList();

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
		// MENU AL PULSAR EL BOTON DERECHO SOBRE TABLA DISPONIBLES
		ContextMenu context = new ContextMenu();
		MenuItem item1 = new MenuItem("Prestar este vehículo");
		context.getItems().addAll(item1);
		tableDisponibles.setContextMenu(context);
		tableDisponibles.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.SECONDARY) {
					context.show(tableDisponibles, t.getScreenX(), t.getScreenY());
				}
			}
		});
		// ATAJO DE TECLADO
		item1.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
		// PONER TOOLTIP
		tableDisponibles.setTooltip(new Tooltip("Vehículos de sustitución disponibles"));

		
		
		// Cargar disponibles
		cargarDisponibles();
		// Cargar prestados
		cargarPrestados();
		// Cargar histórico 
		cargarHistorico();
		
		//FILTRO AL ESCRIBIR EN EL TEXTO DEL NOMBRE EL NOMBRE DEL CLIENTE O LA MATRÍCULA DEL VEHÍCULO
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<VehiculoSustitucionClienteVehiculo> filteredData = new FilteredList<>(listaHistorico, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getCliente().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getVehiculo().getMatricula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<VehiculoSustitucionClienteVehiculo> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableHistorico.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableHistorico.setItems(sortedData);
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
				c = Inicio.CONEXION.leerClientePorID(vs.getClienteID());
				v = Inicio.CONEXION.leerVehiculoPorID(vs.getVehiculoID());
				VehiculoSustitucionClienteVehiculo vscv = new VehiculoSustitucionClienteVehiculo(vs, c, v);
				listaPrestados.add(vscv);
				columnaVehiculoPrestados
						.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
				columnaMatriculaPrestados
						.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
				columnaClientePrestados
						.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaFechaEntregaPrestados.setCellValueFactory(
						cellData -> cellData.getValue().getVehiculoSustitucion().fechacogePropertyFormat());
				columnaObservacionesPrestados.setCellValueFactory(
						cellData -> cellData.getValue().getVehiculoSustitucion().observacionesProperty());
				tablePrestados.setItems(listaPrestados);
			}
		}
	}

	/**
	 * Carga el histórico de los vehículos de sustitución
	 */
	private void cargarHistorico() {
		listaHistorico.clear();
		tableHistorico.getItems().clear();
		ArrayList<VehiculoSustitucion> lista = Inicio.CONEXION.buscarHistorico();
		if (!lista.isEmpty()) {
			Cliente c;
			Vehiculo v;
			for (VehiculoSustitucion vs : lista) {
				c = Inicio.CONEXION.leerClientePorID(vs.getClienteID());
				v = Inicio.CONEXION.leerVehiculoPorID(vs.getVehiculoID());
				VehiculoSustitucionClienteVehiculo vscv = new VehiculoSustitucionClienteVehiculo(vs, c, v);
				listaHistorico.add(vscv);
				columnaVehiculoHistorico
						.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
				columnaMatriculaHistorico
						.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
				columnaClienteHistorico
						.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaFechaEntregaHistorico.setCellValueFactory(
						cellData -> cellData.getValue().getVehiculoSustitucion().fechacogePropertyFormat());
				columnaFechaDevolucionHistorico.setCellValueFactory(
						cellData -> cellData.getValue().getVehiculoSustitucion().fechadevuelvePropertyFormat());
				columnaObservacionesHistorico.setCellValueFactory(
						cellData -> cellData.getValue().getVehiculoSustitucion().observacionesProperty());
				tableHistorico.setItems(listaHistorico);
			}
		}
	}

	/**
	 * Marca el vehículo seleccionado como devuelto, añadiéndole la fecha de
	 * devolución
	 */
	@FXML
	private void marcarDevuelto() {
		int selectedIndex = tablePrestados.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			VehiculoSustitucionClienteVehiculo vscv = tablePrestados.getSelectionModel().getSelectedItem();
			if (Inicio.mostrarD_SustitucionDevolucion(vscv)) {
				if (Inicio.CONEXION.actualizarVehiculoSustitucion("D", vscv)) {
					listaHistorico.add(vscv);
					tableHistorico.refresh();
					listaDisponibles.add(vscv.getVehiculo());
					tableDisponibles.refresh();
					listaPrestados.remove(vscv);
					// Si veo que añadiendolos a las listas no se actualizan,
					// cargar las tablas de nuevo hasta que resuelva el error:
					cargarDisponibles();
					cargarHistorico();
					Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Éxito", "Vehículo marcado como devuelto", "");
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al marcar el vehículo",
							"Ocurrió un error al marcar el vehículo como devuelto en la base de datos.");
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún vehículo seleccionado",
					"Selecciona el vehículo que quieras marcar como devuelto.");
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
