package GUI.Cliente;

import java.io.IOException;
import java.util.Optional;

import com.guigarage.responsive.ResponsiveHandler;

import GUI.Contabilidad.V_NuevaFacturaController;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
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
	@FXML
	private Label lblSI;

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
	@FXML
	private Label lblSIP;

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
	@FXML
	private Label lblSIF;

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
	@FXML
	private Label lblSIVS;

	// Para saber si pulsa eliminar presupuesto o factura
	private boolean esFactura = false;

	private Inicio main;
	private ScrollPane sp;

	public Button boton1;
	public Button boton2;
	public Button boton3;

	private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();
	private ClienteParticularEmpresaDireccion cped;
	private ObservableList<VehiculoSustitucionClienteVehiculo> listaSustitucion = FXCollections.observableArrayList();
	private ObservableList<FacturaClienteVehiculo> listaPresupuestos = FXCollections.observableArrayList();
	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();

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
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al modificar el cliente",
						"Ocurrió un error al modificar el cliente en la base de datos.");
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
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar cliente",
					"Se eliminará todo lo asociado a este cliente (facturas, vehículos, documentos... )\n¿Estás seguro que quieres eliminar este cliente?",
					"");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION.eliminarCliente(cped)) {
					try {
						// Cargar la vista de buscar vehiculo
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
						if (tableFacturas.getItems().isEmpty()) {
							tableFacturas.setVisible(false);
							lblSIF.setVisible(true);
						} else {
							tableFacturas.setVisible(true);
							lblSIF.setVisible(false);
						}
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
						if (tablePresupuestos.getItems().isEmpty()) {
							tablePresupuestos.setVisible(false);
							lblSIP.setVisible(true);
						} else {
							tablePresupuestos.setVisible(true);
							lblSIP.setVisible(false);
						}
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
				vscv.getVehiculoSustitucion().getFechadevuelve().toString();
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
		if (tableVehiculo.getItems().isEmpty()) {
			tableVehiculo.setVisible(false);
			lblSI.setVisible(true);
		} else {
			tableVehiculo.setVisible(true);
			lblSI.setVisible(false);
		}
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
		if (tablePresupuestos.getItems().isEmpty()) {
			tablePresupuestos.setVisible(false);
			btnEliminarPresupuesto.setVisible(false);
			lblSIP.setVisible(true);
		} else {
			tablePresupuestos.setVisible(true);
			btnEliminarPresupuesto.setVisible(true);
			lblSIP.setVisible(false);
		}
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
		if (tableFacturas.getItems().isEmpty()) {
			tableFacturas.setVisible(false);
			btnEliminarFactura.setVisible(false);
			lblSIF.setVisible(true);
		} else {
			tableFacturas.setVisible(true);
			btnEliminarFactura.setVisible(true);
			lblSIF.setVisible(false);
		}
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
		columnaFEntregaSustitucion.setCellValueFactory(
				cellData -> cellData.getValue().getVehiculoSustitucion().fechacogePropertyFormat());
		columnaFDevolucionSustitucion.setCellValueFactory(
				cellData -> cellData.getValue().getVehiculoSustitucion().fechadevuelvePropertyFormat());
		columnaObservacionesSustitucion
				.setCellValueFactory(cellData -> cellData.getValue().getVehiculoSustitucion().observacionesProperty());
		tableSustitucion.setItems(listaSustitucion);
		if (tableSustitucion.getItems().isEmpty()) {
			tableSustitucion.setVisible(false);
			lblSIVS.setVisible(true);
		} else {
			tableSustitucion.setVisible(true);
			lblSIVS.setVisible(false);
		}
	}

	@FXML
	private void mensajeNoModificar() {
		Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
				"Para modificar los datos del cliente utilice el botón 'Modificar'", "");
	}

}
