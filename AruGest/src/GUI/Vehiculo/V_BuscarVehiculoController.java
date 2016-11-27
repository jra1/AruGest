package GUI.Vehiculo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import GUI.Contabilidad.V_NuevaFacturaController;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class V_BuscarVehiculoController {

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
	private TableColumn<Vehiculo, String> columnaCliente;

	@FXML
	private Label lblTipoVehiculo;
	@FXML
	private Label lblMatricula;
	@FXML
	private Label lblMarca;
	@FXML
	private Label lblModelo;
	@FXML
	private Label lblVersion;
	@FXML
	private Label lblAnio;
	@FXML
	private Label lblBastidor;
	@FXML
	private Label lblLetrasMotor;
	@FXML
	private Label lblColor;
	@FXML
	private Label lblCodRadio;

	@FXML
	private Button btnHacerFactura;

	private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
	// private int tipoVehiculo = 1; // 1=Particular, 2=Empresa

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
		comboTipoVehiculo.getItems().addAll("Turismo", "Furgoneta", "Camión", "Autobús", "Autocaravana", "Moto",
				"Remolque");
		comboTipoVehiculo.setValue("Turismo");

		tableVehiculos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarDetallesVehiculo(newValue));
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
				columnaCliente.setCellValueFactory(cellData -> Inicio.CONEXION
						.leerClientePorID(cellData.getValue().getClienteID()).nombreProperty());
				tableVehiculos.setItems(listaVehiculos);

			}
		}
	}

	/**
	 * Muestra los detalles del vehículo seleccionado. Si el vehiculo es null,
	 * se limpian los campos.
	 * 
	 * @param vehiculo
	 *            o null
	 */
	private void mostrarDetallesVehiculo(Vehiculo v) {
		if (v != null) {
			Inicio.CLIENTE_ID = v.getClienteID();
			lblTipoVehiculo.setText(Utilidades.tipoIDtoString(v.getTipoID()));
			lblMatricula.setText(v.getMatricula());
			lblMarca.setText(v.getMarca());
			lblModelo.setText(v.getModelo());
			lblVersion.setText(v.getVersion());
			lblAnio.setText("" + v.getAnio());
			lblBastidor.setText(v.getBastidor());
			lblLetrasMotor.setText(v.getLetrasmotor());
			lblColor.setText(v.getColor());
			lblCodRadio.setText(v.getCodradio());
		} else {
			Inicio.CLIENTE_ID = 0;
			lblTipoVehiculo.setText("Selecciona un vehículo");
			lblMatricula.setText("-");
			lblMarca.setText("-");
			lblModelo.setText("-");
			lblVersion.setText("-");
			lblAnio.setText("-");
			lblBastidor.setText("-");
			lblLetrasMotor.setText("-");
			lblColor.setText("-");
			lblCodRadio.setText("-");
		}
	}

	/**
	 * Edita el vehículo seleccionado
	 */
	@FXML
	private void editarVehiculo() {
		Vehiculo v = tableVehiculos.getSelectionModel().getSelectedItem();
		if (v != null) {
			boolean okClicked = Inicio.mostrarEditorVehiculo(v);
			if (okClicked) {
				if (Inicio.CONEXION.editarVehiculo(v)) {
					Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Error", "Vehículo modificado con éxito", "");
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el vehículo",
							"Ocurrió un error al guardar el vehículo en la base de datos.");
				}
				mostrarDetallesVehiculo(v);
			}
		} else {
			// Si no ha seleccionado un vehículo de la tabla
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún vehículo seleccionado",
					"Selecciona el vehículo que quieras editar.");
		}
	}

	/**
	 * Elimina el vehículo seleccionado
	 */
	@FXML
	private void eliminarVehiculo() {
		int selectedIndex = tableVehiculos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar vehículo",
					"Se eliminará todo lo asociado a este vehículo (facturas, presupuestos... )\n¿Estás seguro que quieres eliminar este vehículo?",
					"");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION
						.eliminarVehiculo(tableVehiculos.getSelectionModel().getSelectedItem().getIdvehiculo())) {
					tableVehiculos.getItems().remove(selectedIndex);
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar el vehículo",
							"Ocurrió un error al eliminar el vehículo de la base de datos.");
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún vehículo seleccionado",
					"Selecciona el vehículo que quieras eliminar.");
		}
	}

	/**
	 * Carga la ventana de nueva factura con los datos del cliente y vehiculo
	 * seleccionado
	 */
	@FXML
	private void hacerFactura() {
		int selectedIndex = tableVehiculos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			try {
				// Cargar la vista de nueva factura
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_NuevaFactura.fxml"));
				AnchorPane nuevaFactura = (AnchorPane) loader.load();

				// Poner la nueva vista en el centro del root
				main.getRoot().setCenter(nuevaFactura);

				// Poner el controlador de la nueva vista.
				V_NuevaFacturaController controller = loader.getController();
				controller.setMainAPP(main);
				controller.cargarDatosClienteVehiculo(Inicio.CONEXION.leerClientePorID(Inicio.CLIENTE_ID),
						tableVehiculos.getSelectionModel().getSelectedItem());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún vehículo seleccionado",
					"Selecciona el vehículo al que quieras hacer una factura.");
		}
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
