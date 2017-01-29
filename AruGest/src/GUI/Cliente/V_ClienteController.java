package GUI.Cliente;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.guigarage.responsive.ResponsiveHandler;

import GUI.Contabilidad.V_NuevaFacturaController;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.Documento;
import Modelo.FacturaClienteVehiculo;
import Modelo.GestorVentana;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class V_ClienteController {
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
	// @FXML
	// private Label lblSI;

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
	private CheckBox esVehiculoSusti;

	// Datos presupuestos
	@FXML
	private TableView<FacturaClienteVehiculo> tablePresupuestos;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaNumPresupuesto;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaVehiculoPresupuesto;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaMatriculaPresupuesto;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaFechaPresupuesto;
	// @FXML
	// private TableColumn<FacturaClienteVehiculo, Number>
	// columnaSubtotalPresupuesto;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaImportePresupuesto;
	@FXML
	private Button btnEliminarPresupuesto;
	// @FXML
	// private Label lblSIP;

	// Datos facturas
	@FXML
	private TableView<FacturaClienteVehiculo> tableFacturas;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaNumFactura;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaVehiculoFactura;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaMatriculaFactura;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaFechaFactura;
	// @FXML
	// private TableColumn<FacturaClienteVehiculo, Number>
	// columnaSubtotalPresupuesto;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaImporteFactura;
	@FXML
	private Button btnEliminarFactura;
	// @FXML
	// private Label lblSIF;

	// Datos Vehiculo sustitución
	@FXML
	private TableView<VehiculoSustitucionClienteVehiculo> tableSustitucion;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaMarcaSustitucion;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaMatriculaSustitucion;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaFEntregaSustitucion;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaFDevolucionSustitucion;
	@FXML
	private TableColumn<VehiculoSustitucionClienteVehiculo, String> columnaObservacionesSustitucion;
	// @FXML
	// private Label lblSIVS;

	// Datos Documentos
	@FXML
	private TableView<Documento> tableDocumentos;
	@FXML
	private TableColumn<Documento, String> columnaNombreDocumento;
	@FXML
	private TextField txtNombreDocumento;

	// Para saber si pulsa eliminar presupuesto o factura
	private boolean esFactura = false;

	private Inicio main;
	private ScrollPane sp;
	private AnchorPane ap;
	private GestorVentana gv;
	private String nombre = "";

	public Button boton1;
	public Button boton2;
	public Button boton3;

	private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
	private ClienteParticularEmpresaDireccion cped;
	private ObservableList<VehiculoSustitucionClienteVehiculo> listaSustitucion = FXCollections.observableArrayList();
	private ObservableList<FacturaClienteVehiculo> listaPresupuestos = FXCollections.observableArrayList();
	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();
	private ObservableList<Documento> listaDocumentos = FXCollections.observableArrayList();

	public Inicio getMain() {
		return main;
	}

	public void setMainAPP(Inicio p) {
		this.main = p;
	}

	public void setScrollPane(ScrollPane root) {
		this.sp = root;
	}

	/**
	 * Carga un cliente
	 */
	public void cargaCliente(ClienteParticularEmpresaDireccion cped) {
		Inicio.CLIENTE_ID = cped.getCliente().getIdcliente();
		this.cped = cped;

		// Cargar datos cliente
		cargarDatosCliente();

		// Cargar vehículos
		cargarVehiculosCliente();

		// Cargar presupuestos
		cargarPresupuestosCliente();

		// Cargar facturas
		cargarFacturasCliente();

		// Cargar tabla vehículos de sustitución
		cargarVehiculosSustitucionCliente();

		// Cargar documentos
		cargarDocumentos();
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

		// Añadir un listener a los botones de Nuevo Presupuesto y Nueva Factura
		btnEliminarFactura.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				esFactura = true;
				eliminarFactura();
			}
		});
		btnEliminarPresupuesto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				esFactura = false;
				eliminarFactura();
			}
		});

		tableDocumentos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarNombreDocumento(newValue));

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
			if (Inicio.CONEXION.editarCliente(cped)) {
				Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Atención", "Cliente modificado con éxito", "");
				cargaCliente(cped);
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al modificar el cliente",
						"Ocurrió un error al modificar el cliente en la base de datos.");
			}

		}
	}

	/**
	 * Se llama cuando el usuario pulsa en Eliminar cliente
	 */
	@FXML
	private void eliminarCliente() {
		String cliente = cped.getCliente().getNombre();
		if (cped.getCliente().getIdcliente() > 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar cliente",
					"Se eliminará todo lo asociado a este cliente (facturas, vehículos, documentos... )\n¿Estás seguro que quieres eliminar este cliente?",
					"");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION.eliminarCliente(cped)) {
					try {
						// Cargar la vista de buscar cliente
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Inicio.class.getResource("/GUI/Cliente/V_BuscarCliente.fxml"));
						AnchorPane buscarCliente = (AnchorPane) loader.load();
						String nombre = "Buscar Cliente";
						// Poner la nueva vista en el centro del root
						// main.getRoot().setCenter(buscarCliente);

						// **************************************************************************************************
						Utilidades.ajustarResolucionAnchorPane(buscarCliente, Inicio.ANCHO_PANTALLA,
								Inicio.ALTO_PANTALLA);
						// **************************************************************************************************
						sp.setContent(buscarCliente);
						// Esta línea es para que se ejecute la pseudoclase del
						// CSS ya
						ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
						AnchorPane ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
						// Para quitar el botón del gestor de ventanas:
						Utilidades.quitarBoton(cliente);
						GestorVentana gv = new GestorVentana(ap, nombre);
						Utilidades.gestionarPantallas(gv);
						boton1.setVisible(Inicio.BOTON1.isVisible());
						boton1.setText(Inicio.BOTON1.getNombre());
						boton2.setVisible(Inicio.BOTON2.isVisible());
						boton2.setText(Inicio.BOTON2.getNombre());
						boton3.setVisible(Inicio.BOTON3.isVisible());
						boton3.setText(Inicio.BOTON3.getNombre());

						// Poner el controlador de la nueva vista.
						V_BuscarClienteController controller = loader.getController();
						controller.setMainAPP(main);
						controller.setScrollPane(sp);
						controller.boton1 = boton1;
						controller.boton2 = boton2;
						controller.boton3 = boton3;
						controller.setFocus();

						// // Cargar la vista de nueva factura
						// FXMLLoader loader = new FXMLLoader();
						// loader.setLocation(Inicio.class.getResource("/GUI/Cliente/V_BuscarCliente.fxml"));
						// AnchorPane buscarCliente = (AnchorPane)
						// loader.load();
						// // Poner la nueva vista en el centro del root
						// sp.setContent(buscarCliente);
						// // main.getRoot().setCenter(buscarCliente);
						//
						// // Poner el controlador de la nueva vista.
						// V_BuscarClienteController controller =
						// loader.getController();
						// controller.setMainAPP(main);

					} catch (IOException e) {
						e.printStackTrace();
						Utilidades.mostrarError(e);
					}
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Cliente eliminado",
							"El cliente y todo sus datos asociados han sido eliminados con éxito de la base de datos.");
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar el vehículo",
							"Ocurrió un error al eliminar el vehículo de la base de datos.");
				}
			}
		}
	}

	/**
	 * Muestra los detalles del vehículo seleccionado Si el vehiculo es null, se
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
			esVehiculoSusti.setSelected(v.isEsVehiculoSustitucion());
		} else {
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
			esVehiculoSusti.setSelected(false);
		}
	}

	/**
	 * Se llama cuando el usuario pulsa en Añadir vehículo
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
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el vehículo",
						"Ocurrió un error al guardar el vehículo en la base de datos.");
			}
		}
	}

	/**
	 * Edita el vehículo seleccionado
	 */
	@FXML
	private void editarVehiculo() {
		Vehiculo v = tableVehiculo.getSelectionModel().getSelectedItem();
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
		int selectedIndex = tableVehiculo.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar vehículo",
					"Se eliminará todo lo asociado a este vehículo (facturas, presupuestos... )\n¿Estás seguro que quieres eliminar este vehículo?",
					"");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION
						.eliminarVehiculo(tableVehiculo.getSelectionModel().getSelectedItem().getIdvehiculo())) {
					tableVehiculo.getItems().remove(selectedIndex);
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
	 * Elimina el presupuesto / factura seleccionado
	 */
	@FXML
	private void eliminarFactura() {
		if (esFactura) {
			int selectedIndex = tableFacturas.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar factura",
						"¿Está seguro que quiere eliminar esta factura?", "");
				if (result.get() == ButtonType.OK) {
					if (Inicio.CONEXION.eliminarFacturaPorID(
							tableFacturas.getSelectionModel().getSelectedItem().getFactura().getIdfactura())) {
						tableFacturas.getItems().remove(selectedIndex);
						// if (tableFacturas.getItems().isEmpty()) {
						// tableFacturas.setVisible(false);
						// lblSIF.setVisible(true);
						// } else {
						// tableFacturas.setVisible(true);
						// lblSIF.setVisible(false);
						// }
					} else {
						Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar la factura",
								"Ocurrió un error al eliminar la factura de la base de datos.");
					}
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ninguna factura seleccionada",
						"Selecciona la factura que quieras eliminar.");
			}
		} else {
			int selectedIndex = tablePresupuestos.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar presupuesto",
						"¿Está seguro que quiere eliminar este presupuesto?", "");
				if (result.get() == ButtonType.OK) {
					if (Inicio.CONEXION.eliminarFacturaPorID(
							tablePresupuestos.getSelectionModel().getSelectedItem().getFactura().getIdfactura())) {
						tablePresupuestos.getItems().remove(selectedIndex);
						// if (tablePresupuestos.getItems().isEmpty()) {
						// tablePresupuestos.setVisible(false);
						// lblSIP.setVisible(true);
						// } else {
						// tablePresupuestos.setVisible(true);
						// lblSIP.setVisible(false);
						// }
					} else {
						Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar el presupuesto",
								"Ocurrió un error al eliminar el presupuesto de la base de datos.");
					}
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún presupuesto seleccionado",
						"Selecciona el presupuesto que quieras eliminar.");
			}
		}

	}

	/**
	 * Carga la ventana de nueva factura con los datos del cliente y vehiculo
	 * seleccionado
	 */
	@FXML
	private void hacerFactura() {
		int selectedIndex = tableVehiculo.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			try {
				Inicio.VEHICULO_ID = tableVehiculo.getSelectionModel().getSelectedItem().getIdvehiculo();
				// Cargar la vista de nueva factura
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_NuevaFactura.fxml"));
				Inicio.setOpcionNueva("A"); // Para que marque presupuesto y
											// factura
				AnchorPane nuevaFactura = (AnchorPane) loader.load();

				// Poner la nueva vista en el centro del root
				sp.setContent(nuevaFactura);
				// main.getRoot().setCenter(nuevaFactura);

				// Poner el controlador de la nueva vista.
				V_NuevaFacturaController controller = loader.getController();
				controller.setMainAPP(main);
				controller.cargarDatosClienteVehiculo(Inicio.CONEXION.leerClientePorID(Inicio.CLIENTE_ID),
						tableVehiculo.getSelectionModel().getSelectedItem());
			} catch (IOException e) {
				e.printStackTrace();
				Utilidades.mostrarError(e);
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún vehículo seleccionado",
					"Selecciona el vehículo al que quieras hacer una factura.");
		}
	}

	@FXML
	private void addVehiculoSustitucion() {
		VehiculoSustitucionClienteVehiculo vscv = new VehiculoSustitucionClienteVehiculo();
		if (Inicio.mostrarD_SustitucionEntrega(vscv)) {
			if (Inicio.CONEXION.actualizarVehiculoSustitucion("E", vscv)) {
				cargarVehiculosSustitucionCliente();
				Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Éxito", "Vehículo entregado", "");
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al registrar el vehículo",
						"Ocurrió un error al registrar la prestación del vehículo en la base de datos.");
			}
		}

	}

	/**
	 * Marca el vehículo seleccionado como devuelto, añadiéndole la fecha de
	 * devolución
	 */
	@FXML
	private void marcarDevuelto() {
		int selectedIndex = tableSustitucion.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			VehiculoSustitucionClienteVehiculo vscv = tableSustitucion.getSelectionModel().getSelectedItem();
			try {
				vscv.getFechadevuelve().toString();
				Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Vehículo ya devuelto",
						"El vehículo seleccionado ya está marcado como devuelto.");
			} catch (NullPointerException e) {
				if (Inicio.mostrarD_SustitucionDevolucion(vscv)) {
					if (Inicio.CONEXION.actualizarVehiculoSustitucion("D", vscv)) {
						tableSustitucion.refresh();
						Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Vehículo marcado como devuelto", "");
					} else {
						Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al marcar el vehículo",
								"Ocurrió un error al marcar el vehículo como devuelto en la base de datos.");
					}
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún vehículo seleccionado",
					"Selecciona el vehículo que quieras marcar como devuelto.");
		}
	}

	/**
	 * Carga los datos de un cliente en su lugar correspondiente
	 */
	private void cargarDatosCliente() {
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
	}

	/**
	 * Carga los vehículos de un cliente en su tabla correspondiente
	 */
	private void cargarVehiculosCliente() {
		listaVehiculos = Inicio.CONEXION.buscarVehiculosPorClienteID(Inicio.CLIENTE_ID);
		columnaMarca.setCellValueFactory(cellData -> cellData.getValue().marcaModeloProperty());
		columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
		tableVehiculo.setItems(listaVehiculos);
		// if (tableVehiculo.getItems().isEmpty()) {
		// tableVehiculo.setVisible(false);
		// lblSI.setVisible(true);
		// } else {
		// tableVehiculo.setVisible(true);
		// lblSI.setVisible(false);
		// }
	}

	/**
	 * Carga los presupuestos de un cliente en su tabla correspondiente
	 */
	private void cargarPresupuestosCliente() {
		listaPresupuestos = Inicio.CONEXION.buscarPresupuestosPorClienteID(Inicio.CLIENTE_ID);
		columnaNumPresupuesto
				.setCellValueFactory(cellData -> cellData.getValue().getFactura().numpresupuestoProperty());
		columnaVehiculoPresupuesto
				.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
		columnaMatriculaPresupuesto
				.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
		columnaFechaPresupuesto.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaPropertyFormat());
		// columnaSubtotalPresupuesto.setCellValueFactory(cellData ->
		// cellData.getValue().getFactura().);
		columnaImportePresupuesto
				.setCellValueFactory(cellData -> cellData.getValue().getFactura().importeTotalProperty());
		tablePresupuestos.setItems(listaPresupuestos);
		// if (tablePresupuestos.getItems().isEmpty()) {
		// tablePresupuestos.setVisible(false);
		// btnEliminarPresupuesto.setVisible(false);
		// lblSIP.setVisible(true);
		// } else {
		// tablePresupuestos.setVisible(true);
		// btnEliminarPresupuesto.setVisible(true);
		// lblSIP.setVisible(false);
		// }
	}

	/**
	 * Carga las facturas de un cliente en su tabla correspondiente
	 */
	private void cargarFacturasCliente() {
		listaFacturas = Inicio.CONEXION.buscarFacturasPorClienteID(Inicio.CLIENTE_ID);
		columnaNumFactura.setCellValueFactory(cellData -> cellData.getValue().getFactura().numfacturaProperty());
		columnaVehiculoFactura.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
		columnaMatriculaFactura.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
		columnaFechaFactura.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaPropertyFormat());
		// columnaSubtotalPresupuesto.setCellValueFactory(cellData ->
		// cellData.getValue().getFactura().);
		columnaImporteFactura.setCellValueFactory(cellData -> cellData.getValue().getFactura().importeTotalProperty());
		tableFacturas.setItems(listaFacturas);
		// if (tableFacturas.getItems().isEmpty()) {
		// tableFacturas.setVisible(false);
		// btnEliminarFactura.setVisible(false);
		// lblSIF.setVisible(true);
		// } else {
		// tableFacturas.setVisible(true);
		// btnEliminarFactura.setVisible(true);
		// lblSIF.setVisible(false);
		// }
	}

	/**
	 * Carga los vehículos de sustitución del cliente en la tabla
	 * correspondiente
	 */
	private void cargarVehiculosSustitucionCliente() {
		listaSustitucion = Inicio.CONEXION.buscarVehiculosSustitucionPorClienteID(Inicio.CLIENTE_ID);
		columnaMarcaSustitucion
				.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
		columnaMatriculaSustitucion
				.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
		columnaFEntregaSustitucion.setCellValueFactory(cellData -> cellData.getValue().fechacogePropertyFormat());
		columnaFDevolucionSustitucion
				.setCellValueFactory(cellData -> cellData.getValue().fechadevuelvePropertyFormat());
		columnaObservacionesSustitucion.setCellValueFactory(cellData -> cellData.getValue().observacionesProperty());
		tableSustitucion.setItems(listaSustitucion);
		// if (tableSustitucion.getItems().isEmpty()) {
		// tableSustitucion.setVisible(false);
		// lblSIVS.setVisible(true);
		// } else {
		// tableSustitucion.setVisible(true);
		// lblSIVS.setVisible(false);
		// }
	}

	@FXML
	private void mensajeNoModificar() {
		Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
				"Para modificar los datos del cliente utilice el botón 'Modificar'", "");
	}

	/**
	 * Llama a la función para abrir el diálogo para agregar un documento
	 */
	@FXML
	private void agregaDocumento() {
		Documento d = Inicio.abrirAgregaDocumento();
		if (d != null) {
			int id = Inicio.CONEXION.guardarDocumento(d);
			if (id != 0) {
				listaDocumentos.add(d);
				columnaNombreDocumento.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
				tableDocumentos.setItems(listaDocumentos);
				cargarDocumentos();
				// d = Inicio.CONEXION.leerDocumentoPorID(id);
				// File f = Inicio.CONEXION.leerDocumentoPorID(id);
				// if (d != null) {
				// // Abrir el documento:
				// try {
				// // // Crear un archivo temporal
				// // File temp = File.createTempFile("Archivo_TEMP",
				// // ".tmp");
				// //
				// // // Le indicamos que lo eleminte cuando se cierre el
				// // // programa.
				// // temp.deleteOnExit();
				// //
				// // // Escribir contenido con BufferedWriter
				// // BufferedWriter bw = new BufferedWriter(new
				// // FileWriter(temp));
				// // bw.write("This is the temporary file content");
				// // bw.close();
				// //
				// // /* ¨--- ALTERNATIVA --- */
				// //
				// // // Escribir el archivo con un InputStream
				// // InputStream is =
				// // Thread.currentThread().getContextClassLoader()
				// // .getResourceAsStream("com/pack/yourfile");
				// //
				// // FileOutputStream outputStream = new
				// // FileOutputStream(file);
				// // int read = 0;
				// // byte[] bytes = new byte[is.available()];
				// //
				// // while ((read = is.read(bytes)) != -1) {
				// // outputStream.write(bytes, 0, read);
				// // }
				// //
				// // is.close();
				// // outputStream.close();
				//
				// Utilidades.mostrarAlerta(AlertType.INFORMATION, "",
				// f.getAbsolutePath(), "");
				// Runtime.getRuntime().exec("rundll32
				// url.dll,FileProtocolHandler " + f.getAbsolutePath());
				//
				// } catch (IOException e) {
				// Utilidades.mostrarError(e);
				// }
				//
				// }
			}
		}
	}

	/**
	 * Carga los documentos del cliente en la tabla correspondiente
	 */
	private void cargarDocumentos() {
		listaDocumentos = Inicio.CONEXION.buscarDocumentosPorClienteID(Inicio.CLIENTE_ID);
		columnaNombreDocumento.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
		tableDocumentos.setItems(listaDocumentos);
		// if (tableSustitucion.getItems().isEmpty()) {
		// tableSustitucion.setVisible(false);
		// lblSIVS.setVisible(true);
		// } else {
		// tableSustitucion.setVisible(true);
		// lblSIVS.setVisible(false);
		// }
	}

	/**
	 * Abre el documento seleccionado en la tabla
	 */
	@FXML
	private void verDocumento() {
		int selectedIndex = tableDocumentos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Documento d = tableDocumentos.getSelectionModel().getSelectedItem();
			File f = Inicio.CONEXION.leerDocumentoPorID(d.getIddocumento());

			// Abrir el documento:
			try {
				// String ruta = f.getAbsolutePath();
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + f.getAbsolutePath());

			} catch (Exception e) {
				Utilidades.mostrarError(e);
			}

		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún documento seleccionado",
					"Selecciona el documento que quieras abrir.");
		}
	}

	/**
	 * Elimina el documento seleccionado en la tabla
	 */
	@FXML
	private void eliminarDocumento() {
		int selectedIndex = tableDocumentos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar documento",
					"¿Está seguro que quiere eliminar este documento?",
					"Si elimina un elemento de la BD no se podrá recuperar");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION.eliminarDocumentoPorID(
						tableDocumentos.getSelectionModel().getSelectedItem().getIddocumento())) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Documento eliminado",
							"El documento ha sido eliminado de la BD");
					cargarDocumentos();
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar el documento",
							"Ocurrió un error al eliminar el documento de la BD. Puede que no haya sido eliminado correctamente");
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún documento seleccionado",
					"Selecciona el documento que quieras abrir.");
		}
	}

	/**
	 * Muestra el nombre del documento seleccionado en el TextField
	 * correspondiente
	 * 
	 * @param documento
	 *            seleccionado en la tabla
	 */
	private void mostrarNombreDocumento(Documento d) {
		if (d != null) {
			txtNombreDocumento.setText(d.getTitulo());
		}
	}

	/**
	 * Cambia el nombre del documento seleccionado por el introducido en el
	 * TextField
	 */
	@FXML
	private void cambiarNombreDocumento() {
		if (txtNombreDocumento.getText().isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Debe introducir un nombre",
					"Seleccione un documento de la tabla e introduzca el nombre que quiere asignarle");
		} else {
			int selectedIndex = tableDocumentos.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				if (!tableDocumentos.getSelectionModel().getSelectedItem().getTitulo()
						.equals(txtNombreDocumento.getText())) {
					String msg = "Cambiar el nombre del documento de '"
							+ tableDocumentos.getSelectionModel().getSelectedItem().getTitulo() + "' a '"
							+ txtNombreDocumento.getText() + "'";
					Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION,
							"Cambiar nombre documento", "¿Cambiar el nombre de este documento?", msg);
					if (result.get() == ButtonType.OK) {
						if (Inicio.CONEXION.editarTituloDocumento(
								tableDocumentos.getSelectionModel().getSelectedItem().getIddocumento(),
								txtNombreDocumento.getText())) {
							Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Documento modificado",
									"El documento se ha modificado en la BD");
							cargarDocumentos();
						} else {
							Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al modificar el documento",
									"Ocurrió un error al modificar el documento en la BD. Puede que no haya sido modificado correctamente");
						}
					}
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún documento seleccionado",
						"Selecciona el documento al que quiera cambiar el nombre.");
			}
		}
	}

	/**
	 * Función para cargar el presupuesto seleccionado
	 */
	@FXML
	private void cargarPresupuesto() {
		int selectedIndex = tablePresupuestos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			try {
				// Cargar la vista de nueva factura
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_NuevaFactura.fxml"));
				AnchorPane nuevaFactura = (AnchorPane) loader.load();

				// Poner la nueva vista en el centro del root
				// **************************************************************************************************
				Utilidades.ajustarResolucionAnchorPane(nuevaFactura, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
				// **************************************************************************************************
				sp.setContent(nuevaFactura);
				// Esta línea es para que se ejecute la pseudoclase del CSS ya
				ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
				nombre = "Presupuesto: " + listaPresupuestos.get(selectedIndex).getCliente().getNombre();
				ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
				gv = new GestorVentana(ap, nombre);
				Utilidades.gestionarPantallas(gv);
				boton1.setVisible(Inicio.BOTON1.isVisible());
				boton1.setText(Inicio.BOTON1.getNombre());
				boton2.setVisible(Inicio.BOTON2.isVisible());
				boton2.setText(Inicio.BOTON2.getNombre());
				boton3.setVisible(Inicio.BOTON3.isVisible());
				boton3.setText(Inicio.BOTON3.getNombre());
				// main.getRoot().setCenter(nuevaFactura);

				// Poner el controlador de la nueva vista.
				V_NuevaFacturaController controller = loader.getController();
				controller.setMainAPP(main);
				controller.cargaFactura(listaPresupuestos.get(selectedIndex));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún presupuesto seleccionado",
					"Selecciona el presupuesto que quieras cargar");
		}
	}

	/**
	 * Función para cargar la factura seleccionada
	 */
	@FXML
	private void cargarFactura() {
		int selectedIndex = tableFacturas.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			try {
				// Cargar la vista de nueva factura
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_NuevaFactura.fxml"));
				AnchorPane nuevaFactura = (AnchorPane) loader.load();

				// Poner la nueva vista en el centro del root
				// **************************************************************************************************
				Utilidades.ajustarResolucionAnchorPane(nuevaFactura, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
				// **************************************************************************************************
				sp.setContent(nuevaFactura);
				// Esta línea es para que se ejecute la pseudoclase del CSS ya
				ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
				nombre = "Factura: " + listaFacturas.get(selectedIndex).getCliente().getNombre();
				ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
				gv = new GestorVentana(ap, nombre);
				Utilidades.gestionarPantallas(gv);
				boton1.setVisible(Inicio.BOTON1.isVisible());
				boton1.setText(Inicio.BOTON1.getNombre());
				boton2.setVisible(Inicio.BOTON2.isVisible());
				boton2.setText(Inicio.BOTON2.getNombre());
				boton3.setVisible(Inicio.BOTON3.isVisible());
				boton3.setText(Inicio.BOTON3.getNombre());
				// main.getRoot().setCenter(nuevaFactura);

				// Poner el controlador de la nueva vista.
				V_NuevaFacturaController controller = loader.getController();
				controller.setMainAPP(main);
				controller.cargaFactura(listaFacturas.get(selectedIndex));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ninguna factura seleccionada",
					"Selecciona la factura que quieras cargar");
		}
	}

}
