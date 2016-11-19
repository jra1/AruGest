package GUI;

import java.io.IOException;
import java.util.Optional;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class ClienteController {
	// Datos cliente
	@FXML
	private Label lblTipoCliente;
	@FXML
	private TextField txtNombre;
	@FXML
	private CheckBox chkboxEsProveedor;
	@FXML
	private Label lblApellidos;
	@FXML
	private TextField txtApellidos;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtTelf1;
	@FXML
	private TextField txtTelf2;
	@FXML
	private TextField txtTelf3;
	@FXML
	private TextField txtCalle;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtPiso;
	@FXML
	private TextField txtLetra;
	@FXML
	private TextField txtCPostal;
	@FXML
	private TextField txtPoblacion;
	@FXML
	private TextField txtProvincia;
	@FXML
	private Button btnModificarCliente;
	@FXML
	private Button btnEliminarCliente;

	// Datos Vehiculo
	@FXML
	private TableView<Vehiculo> tableVehiculo;
	@FXML
	private TableColumn<Vehiculo, String> columnaMarca;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatricula;

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

	private Inicio main;

	private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
	private ClienteParticularEmpresaDireccion cped;

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/**
	 * Carga una factura
	 */
	public void cargaCliente(ClienteParticularEmpresaDireccion cped) {
		// btnGuardar.setVisible(false);
		Inicio.CLIENTE_ID = cped.getCliente().getIdcliente();
		this.cped = cped;

		// Cargar datos cliente
		if (cped.getParticular() != null) {
			lblTipoCliente.setText("Particular");
			txtDni.setText(cped.getParticular().getNif());
			txtNombre.setText(cped.getParticular().getNombre());
			lblApellidos.setVisible(true);
			chkboxEsProveedor.setVisible(false);
			txtApellidos.setVisible(true);
			txtApellidos.setText(cped.getParticular().getApellidos());
		} else if (cped.getEmpresa() != null) {
			lblTipoCliente.setText("Empresa");
			txtDni.setText(cped.getEmpresa().getCif());
			txtNombre.setText(cped.getEmpresa().getNombre());
			lblApellidos.setVisible(false);
			chkboxEsProveedor.setVisible(true);
			chkboxEsProveedor.setSelected(cped.getEmpresa().isEsProveedor());
			;
			txtApellidos.setVisible(false);
		}
		if (cped.getCliente().getDireccionID() != 0) {
			txtCalle.setText(cped.getDireccion().getCalle());
			txtNumero.setText("" + cped.getDireccion().getNumero());
			txtPiso.setText(cped.getDireccion().getPiso());
			txtLetra.setText(cped.getDireccion().getLetra());
			txtCPostal.setText("" + cped.getDireccion().getCpostal());
			txtPoblacion.setText(cped.getDireccion().getLocalidad());
			txtProvincia.setText(cped.getDireccion().getProvincia());
		}
		txtTelf1.setText(cped.getCliente().getTelf1());
		txtTelf2.setText(cped.getCliente().getTelf2());
		txtTelf3.setText(cped.getCliente().getTelf3());

		// CARGAR TABLA DE VEHICULOS
		listaVehiculos = Inicio.CONEXION.buscarVehiculosPorClienteID(Inicio.CLIENTE_ID);
		columnaMarca.setCellValueFactory(cellData -> cellData.getValue().marcaModeloProperty());
		columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
		tableVehiculo.setItems(listaVehiculos);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// btnGuardar.setVisible(true);
		tableVehiculo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarDetallesVehiculo(newValue));

		/*
		 * comboTipoCliente.getSelectionModel().selectedItemProperty()
		 * .addListener((observable, oldValue, newValue) ->
		 * comprobarComboTipoCliente(newValue));
		 * 
		 * comboTipoVehiculo.getSelectionModel().selectedItemProperty()
		 * .addListener((observable, oldValue, newValue) ->
		 * comprobarComboTipoVehiculo(newValue));
		 */
	}

	/**
	 * Se llama cuando el usuario pulsa en Editar cliente
	 */
	@FXML
	private void editarCliente() {
		boolean okClicked = Inicio.mostrarEditorCliente(cped);
		if (okClicked) {
			// EDITAR EN LA BD
			if (Inicio.CONEXION.editarCliente(cped)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Atenci�n");
				alert.setHeaderText("Cliente modificado con �xito");

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error al modificar el cliente");
				alert.setContentText("Ocurri� un error al modificar el cliente en la base de datos.");

				alert.showAndWait();
			}

			cargaCliente(cped);
		}
	}
	
	/**
	 * Se llama cuando el usuario pulsa en Eliminar cliente
	 */
	@FXML
	private void eliminarCliente() {
		if (cped.getCliente().getIdcliente() > 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar cliente", "Se eliminar� todo lo asociado a este cliente (facturas, veh�culos, documentos... )\n�Est�s seguro que quieres eliminar este cliente?", "");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION.eliminarCliente(cped)) {
					try {
						// Cargar la vista de nueva factura
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Inicio.class.getResource("/GUI/BuscarCliente.fxml"));
						AnchorPane buscarCliente = (AnchorPane) loader.load();
						// Poner la nueva vista en el centro del root
						main.getRoot().setCenter(buscarCliente);

						// Poner el controlador de la nueva vista.
						BuscarClienteController controller = loader.getController();
						controller.setMainAPP(main);

					} catch (IOException e) {
						e.printStackTrace();
					}
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "�xito", "Cliente eliminado",
							"El cliente y todo sus datos asociados han sido eliminados con �xito de la base de datos.");
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar el veh�culo",
							"Ocurri� un error al eliminar el veh�culo de la base de datos.");
				}
			}
		}
	}

	/**
	 * Muestra los detalles del veh�culo seleccionado Si el vehiculo es null, se
	 * limpian los campos.
	 * 
	 * @param vehiculo
	 *            o null
	 */
	private void mostrarDetallesVehiculo(Vehiculo v) {
		if (v != null) {
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
			lblTipoVehiculo.setText("Selecciona un veh�culo");
			lblMatricula.setText("");
			lblMarca.setText("");
			lblModelo.setText("");
			lblVersion.setText("");
			lblAnio.setText("");
			lblBastidor.setText("");
			lblLetrasMotor.setText("");
			lblColor.setText("");
			lblCodRadio.setText("");
		}
	}

	/**
	 * Se llama cuando el usuario pulsa en A�adir veh�culo
	 */
	@FXML
	private void nuevoVehiculo() {
		Vehiculo v = new Vehiculo(Inicio.CLIENTE_ID);
		boolean okClicked = Inicio.mostrarEditorVehiculo(v);
		if (okClicked) {
			// Cuando llega aqui son correctos los datos introducidos
			if (Inicio.CONEXION.guardarVehiculo(v)) {
				listaVehiculos.add(v);
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el veh�culo",
						"Ocurri� un error al guardar el veh�culo en la base de datos.");
			}
		}
	}

	/**
	 * Se llama cuando el usuario pulsa en Editar veh�culo
	 */
	@FXML
	private void editarVehiculo() {
		Vehiculo v = tableVehiculo.getSelectionModel().getSelectedItem();
		if (v != null) {
			boolean okClicked = Inicio.mostrarEditorVehiculo(v);
			if (okClicked) {
				// EDITAR EN LA BD
				if (Inicio.CONEXION.editarVehiculo(v)) {
					Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Error", "Veh�culo modificado con �xito", "");
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el veh�culo",
							"Ocurri� un error al guardar el veh�culo en la base de datos.");
				}
				mostrarDetallesVehiculo(v);
			}
		} else {
			// Si no ha seleccionado un veh�culo de la tabla
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atenci�n", "Ning�n veh�culo seleccionado",
					"Selecciona el veh�culo que quieras editar.");
		}
	}

	/**
	 * Se llama cuando el usuario pulsa en Eliminar veh�culo
	 */
	@FXML
	private void eliminarVehiculo() {
		int selectedIndex = tableVehiculo.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar veh�culo", "Se eliminar� todo lo asociado a este veh�culo (facturas, presupuestos... )\n�Est�s seguro que quieres eliminar este veh�culo?", "");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION
						.eliminarVehiculo(tableVehiculo.getSelectionModel().getSelectedItem().getIdvehiculo())) {
					tableVehiculo.getItems().remove(selectedIndex);
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar el veh�culo",
							"Ocurri� un error al eliminar el veh�culo de la base de datos.");
				}
			}
		} else {
			// Nothing selected.
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atenci�n", "Ning�n veh�culo seleccionado",
					"Selecciona el veh�culo que quieras eliminar.");
		}
	}
}
