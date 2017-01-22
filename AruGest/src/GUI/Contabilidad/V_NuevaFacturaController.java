package GUI.Contabilidad;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;

import Logica.Hilo;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Cliente;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.ClienteParticularEmpresaDireccionVehiculo;
import Modelo.Direccion;
import Modelo.ElementosGolpes;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.FacturaClienteVehiculo;
import Modelo.Material;
import Modelo.Particular;
import Modelo.Servicio;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Joseba
 *
 */
public class V_NuevaFacturaController {

	@FXML
	private Pane paneDatosFactura;

	// Datos cliente y vehículo
	@FXML
	private TextField txtDni;
	@FXML
	private Label lblNombre;
	@FXML
	private TextField txtMatricula;
	@FXML
	private Label lblMarcaModelo;

	// Datos Factura
	@FXML
	private CheckBox chckbxFactura;
	@FXML
	private TextField txtNumfactura;

	@FXML
	private CheckBox chckbxPresupuesto;
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
	private DatePicker txtFecha;

	// // Datos cliente
	// @FXML
	// private TextField txtNombre;
	// @FXML
	// private Label lblApellidos;
	// @FXML
	// private TextField txtApellidos;
	// @FXML
	// private TextField txtCalle;
	// @FXML
	// private TextField txtNumero;
	// @FXML
	// private TextField txtPiso;
	// @FXML
	// private TextField txtLetra;
	// @FXML
	// private TextField txtPoblacion;
	// @FXML
	// private TextField txtTel1;
	// // @FXML
	// // private TextField txtMovil;
	//
	// // Datos Vehiculo
	// @FXML
	// private ComboBox<String> comboTipoVehiculo;
	// @FXML
	// private TextField txtMarca;
	// @FXML
	// private TextField txtModelo;
	// @FXML
	// private TextField txtVersion;
	//
	// @FXML
	// private TextField txtKms;
	// @FXML
	// private ComboBox<String> comboTipoCliente;

	// Servicios y materiales
	@FXML
	private ComboBox<String> comboTipo;
	@FXML
	private TextField txtConcepto;
	@FXML
	private Label lblCantidad;
	@FXML
	private TextField txtCantidad;
	@FXML
	private Label lblValor;
	@FXML
	private TextField txtValor;
	@FXML
	private Button btnAdd;

	@FXML
	private TableView<Servicio> tableServicio;
	@FXML
	private TableColumn<Servicio, String> columnaConceptoServ;
	@FXML
	private TableColumn<Servicio, String> columnaHorasServ;
	@FXML
	private Button btnQuitarServ;

	@FXML
	private TableView<Material> tableMaterial;
	@FXML
	private TableColumn<Material, String> columnaConceptoMat;
	@FXML
	private TableColumn<Material, Number> columnaCantidadMat;
	@FXML
	private TableColumn<Material, String> columnaPrecioMat;
	@FXML
	private Button btnQuitarMat;

	@FXML
	private TextField txtManoObra;
	@FXML
	private TextField txtMateriales;
	@FXML
	private TextField txtOtros;
	@FXML
	private TextField txtSubtotal;
	@FXML
	private Label lblIva;
	@FXML
	private TextField txtIva;
	@FXML
	private TextField txtTotal;
	@FXML
	private Button btnActualizarPrecio;
	@FXML
	private CheckBox chckbxModificable;
	@FXML
	private Button btnGenerarFactura;

	@FXML
	private CheckBox chckbxRepararDefOcultos;
	@FXML
	private TextField txtPorcentajeDefOcultos;
	@FXML
	private CheckBox chckbxPermisoPruebas;
	@FXML
	private CheckBox chckbxNoPiezas;
	@FXML
	private DatePicker txtFechaEntrega;

	/**
	 * ObservableLists para los servicios y materiales.
	 */
	private ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
	private ObservableList<Material> listaMaterial = FXCollections.observableArrayList();

	private Inicio main;
	public Button boton1;
	public Button boton2;
	public Button boton3;

	private boolean estaGuardada = false;
	private boolean esServicio = false; // Controla si es servicio o material
	// private int tipoCliente = 1; // 1-Particular, 2-Empresa

	private Servicio servicio;
	private Material material;

	// private int tipoVehiculo = 1;

	private ClienteParticularEmpresaDireccionVehiculo cpedv = new ClienteParticularEmpresaDireccionVehiculo();

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
	public void cargaFactura(FacturaClienteVehiculo fce) {
		Inicio.CLIENTE_ID = fce.getCliente().getIdcliente();
		Inicio.VEHICULO_ID = fce.getVehiculo().getIdvehiculo();
		Inicio.FACTURA_ID = fce.getFactura().getIdfactura();
		// Cargar datos factura
		if (fce.getFactura().getNumfactura() != 0) {
			chckbxFactura.setSelected(true);
			txtNumfactura.setText("" + fce.getFactura().getNumfactura());
		} else {
			chckbxFactura.setSelected(false);
		}
		if (fce.getFactura().getNumpresupuesto() != 0) {
			chckbxPresupuesto.setSelected(true);
			txtNumPresupuesto.setText("" + fce.getFactura().getNumpresupuesto());
		} else {
			chckbxPresupuesto.setSelected(false);
		}
		if (fce.getFactura().getNumordenrep() != 0) {
			chckbxOrdenDeReparacion.setSelected(true);
			txtNumOrden.setText("" + fce.getFactura().getNumordenrep());
		} else {
			chckbxOrdenDeReparacion.setSelected(false);
		}
		if (fce.getFactura().getNumresguardo() != 0) {
			chckbxResguardoDeposito.setSelected(true);
			txtNumResguardo.setText("" + fce.getFactura().getNumresguardo());
		} else {
			chckbxResguardoDeposito.setSelected(false);
		}
		txtFecha.setValue(Utilidades.DateALocalDate(fce.getFactura().getFecha()));

		// Cargar datos cliente y vehiculo
		cargarDatosClienteVehiculo(fce.getCliente(), fce.getVehiculo());

		// Cargar servicios
		listaServicios = Inicio.CONEXION.buscarServiciosPorFacturaID(Inicio.FACTURA_ID);
		columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().servicioProperty());
		columnaHorasServ.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
		tableServicio.setItems(listaServicios);

		// Cargar materiales
		listaMaterial = Inicio.CONEXION.buscarMaterialesPorFacturaID(Inicio.FACTURA_ID);
		columnaConceptoMat.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
		columnaCantidadMat.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
		columnaPrecioMat.setCellValueFactory(cellData -> cellData.getValue().preciounitProperty());
		tableMaterial.setItems(listaMaterial);

		// Recoger piezas, modificable, fechaentrega...
		chckbxModificable.setSelected(fce.getFactura().isModificable());
		if (!fce.getFactura().isModificable()) {
			facturaNoModificable();
		}
		chckbxNoPiezas.setSelected(fce.getFactura().isNopiezas());
		chckbxPermisoPruebas.setSelected(fce.getFactura().isPermisopruebas());
		chckbxRepararDefOcultos.setSelected(fce.getFactura().isRdefocultos());
		txtPorcentajeDefOcultos.setText("" + fce.getFactura().getPorcentajedefocul());
		txtFechaEntrega.setValue(Utilidades.DateALocalDate(fce.getFactura().getFechaentrega()));

		actualizarPrecio();
	}

	/**
	 * Carga en la factura los datos del cliente y del vehículo
	 * 
	 * @param c:
	 *            cliente a cargar los datos
	 * @param v:
	 *            vehiculo a cargar los datos
	 */
	public void cargarDatosClienteVehiculo(Cliente c, Vehiculo v) {
		cpedv.setCliente(c);
		cpedv.setVehiculo(v);
		Particular p = Inicio.CONEXION.buscarParticularPorClienteID(Inicio.CLIENTE_ID);
		if (p != null) {
			cpedv.setParticular(p);
		} else {
			Empresa e = Inicio.CONEXION.buscarEmpresaPorClienteID(Inicio.CLIENTE_ID);
			if (e != null) {
				cpedv.setEmpresa(e);
			}
		}
		if (c.getDireccionID() != 0) {
			Direccion d = Inicio.CONEXION.leerDireccionPorID(c.getDireccionID());
			cpedv.setDireccion(d);
		}

		// Cargar datos
		colocarDatos(cpedv);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// txtNumfactura.setId("toolbar");
		// paneDatosFactura.getChildren().add(txtNumfactura);

		// Para que el botón de añadir funcione con el Enter
		btnAdd.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.F12 || ke.getCode() == KeyCode.ENTER) {
					add();
				}
			}
		});

		lblIva.setText("I.V.A. " + Inicio.PRECIO_IVA + "%");

		tableServicio.setEditable(true);
		tableMaterial.setEditable(true);

		// Inicializar los valores de los combos, la fecha y marcar el checkbox
		// de presupuesto o factura
		txtFecha.setValue(LocalDate.now());
		txtFechaEntrega.setValue(txtFecha.getValue().plusDays(7));
		// comboTipoCliente.getItems().addAll("Particular", "Empresa");
		// comboTipoCliente.setValue("Particular");
		// comboTipoVehiculo.getItems().addAll("Turismo", "Furgoneta", "Camión",
		// "Autobús", "Autocaravana", "Moto",
		// "Remolque");
		// comboTipoVehiculo.setValue("Turismo");
		comboTipo.getItems().addAll("Material", "Chapa", "Pintura", "Electrónica / mecánica");
		comboTipo.setValue("Material");

		// Añadir un listener al combo
		comboTipo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarCombo(newValue));

		// comboTipoCliente.getSelectionModel().selectedItemProperty()
		// .addListener((observable, oldValue, newValue) ->
		// comprobarComboTipoCliente(newValue));
		//
		// comboTipoVehiculo.getSelectionModel().selectedItemProperty()
		// .addListener((observable, oldValue, newValue) ->
		// comprobarComboTipoVehiculo(newValue));

		// Marcar algunos checkbox que son habituales
		if (Inicio.OPCION_NUEVA.equalsIgnoreCase("P")) {
			chckbxPresupuesto.setSelected(true);
			txtNumPresupuesto.setText("" + Inicio.NUM_PRESUPUESTO);
			chckbxFactura.setSelected(false);
		} else if (Inicio.OPCION_NUEVA.equalsIgnoreCase("F")) {
			chckbxPresupuesto.setSelected(false);
			chckbxFactura.setSelected(true);
			txtNumfactura.setText("" + Inicio.NUM_FACTURA);
		} else if (Inicio.OPCION_NUEVA.equalsIgnoreCase("A")) { // A se ponen
																// los dos
			chckbxFactura.setSelected(true);
			txtNumfactura.setText("" + Inicio.NUM_FACTURA);

			chckbxPresupuesto.setSelected(true);
			txtNumPresupuesto.setText("" + Inicio.NUM_PRESUPUESTO);
		}
		chckbxPermisoPruebas.setSelected(true);
		chckbxNoPiezas.setSelected(true);
		chckbxModificable.setSelected(true);

		// Editar columna Concepto de la tabla Servicios
		columnaConceptoServ.setCellFactory(TextFieldTableCell.<Servicio> forTableColumn());
		columnaConceptoServ.setOnEditCommit((CellEditEvent<Servicio, String> t) -> {
			((Servicio) t.getTableView().getItems().get(t.getTablePosition().getRow())).setServicio(t.getNewValue());
		});

		// Editar columna Horas de la tabla Servicios
		columnaHorasServ.setCellFactory(TextFieldTableCell.<Servicio> forTableColumn());
		columnaHorasServ.setOnEditCommit((CellEditEvent<Servicio, String> t) -> {
			((Servicio) t.getTableView().getItems().get(t.getTablePosition().getRow())).setHoras(t.getNewValue());
			// Se actualizan los valores del precio
			actualizarPrecio();
		});

		// Editar columna Concepto de la tabla Materiales
		columnaConceptoMat.setCellFactory(TextFieldTableCell.<Material> forTableColumn());
		columnaConceptoMat.setOnEditCommit((CellEditEvent<Material, String> t) -> {
			((Material) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
		});

		// Editar columna Cantidad de la tabla Materiales
		columnaCantidadMat
				.setCellFactory(TextFieldTableCell.<Material, Number> forTableColumn(new NumberStringConverter()));
		columnaCantidadMat.setOnEditCommit((CellEditEvent<Material, Number> t) -> {
			((Material) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCantidad(t.getNewValue().intValue());
			// Se actualizan los valores del precio
			actualizarPrecio();
		});

		// Editar columna Horas de la tabla Servicios
		columnaPrecioMat.setCellFactory(TextFieldTableCell.<Material> forTableColumn());
		columnaPrecioMat.setOnEditCommit((CellEditEvent<Material, String> t) -> {
			((Material) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPreciounit(t.getNewValue());
			// Se actualizan los valores del precio
			actualizarPrecio();
		});
	}

	public void setFocus() {
		txtNumPresupuesto.requestFocus();
	}

	/**
	 * Función que pone los campos de la factura deshabilitados para que no se
	 * pueda modificar
	 */
	private void facturaNoModificable() {
		chckbxFactura.setDisable(true);
		chckbxModificable.setDisable(true);
		chckbxNoPiezas.setDisable(true);
		chckbxOrdenDeReparacion.setDisable(true);
		chckbxPermisoPruebas.setDisable(true);
		chckbxPresupuesto.setDisable(true);
		chckbxRepararDefOcultos.setDisable(true);
		chckbxResguardoDeposito.setDisable(true);
		btnAdd.setDisable(true);
		btnQuitarMat.setDisable(true);
		btnQuitarServ.setDisable(true);
		txtNumfactura.setDisable(true);
		txtNumPresupuesto.setDisable(true);
		txtNumOrden.setDisable(true);
		txtNumResguardo.setDisable(true);
		txtFecha.setDisable(true);
		// txtNombre.setDisable(true);
		// txtApellidos.setDisable(true);
		// txtCalle.setDisable(true);
		// txtNumero.setDisable(true);
		// txtPiso.setDisable(true);
		// txtLetra.setDisable(true);
		// txtPoblacion.setDisable(true);
		txtDni.setDisable(true);
		// txtTel1.setDisable(true);
		// txtMovil.setDisable(true);
		// txtMarca.setDisable(true);
		// txtModelo.setDisable(true);
		// txtVersion.setDisable(true);
		txtMatricula.setDisable(true);
		// txtKms.setDisable(true);
		txtConcepto.setDisable(true);
		txtCantidad.setDisable(true);
		txtValor.setDisable(true);
		txtManoObra.setDisable(true);
		txtMateriales.setDisable(true);
		txtOtros.setDisable(true);
		txtSubtotal.setDisable(true);
		txtIva.setDisable(true);
		txtTotal.setDisable(true);
		txtPorcentajeDefOcultos.setDisable(true);
		txtFechaEntrega.setDisable(true);
	}

	/**
	 * Se comprueba el valor elegido en el combo para ocultar o no el TextField
	 * de "Cantidad"
	 * 
	 * @param valor
	 */
	private void comprobarCombo(String valor) {
		if (valor.equalsIgnoreCase("Material")) {
			lblValor.setVisible(true);
			txtValor.setVisible(true);
			lblCantidad.setText("Cantidad");
			txtCantidad.setPromptText("Uds");
			esServicio = false;
		} else {
			lblValor.setVisible(false);
			txtValor.setVisible(false);
			lblCantidad.setText("Horas");
			txtCantidad.setPromptText("Horas");
			esServicio = true;
		}
	}

	/**
	 * Se comprueba el valor elegido en el combo de tipo cliente para ocultar o
	 * no el TextField de "Apellidos"
	 * 
	 * @param valor
	 */
	// private void comprobarComboTipoCliente(String valor) {
	// if (valor.equalsIgnoreCase("Empresa")) {
	// lblApellidos.setVisible(false);
	// txtApellidos.setVisible(false);
	// txtNombre.setPrefWidth(300);
	// tipoCliente = 2;
	// } else {
	// lblApellidos.setVisible(true);
	// txtApellidos.setVisible(true);
	// txtNombre.setPrefWidth(100);
	// tipoCliente = 1;
	// }
	// }
	//
	// private void comprobarComboTipoVehiculo(String valor) {
	// tipoVehiculo = Utilidades.StringToTipoID(valor);
	// }

	/**
	 * Se añade el servicio o material a la tabla correspondiente
	 */
	@FXML
	private void add() {
		if (esServicio) {
			if (txtConcepto.getText().equals("") || txtCantidad.getText().equals("")) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
						"Introduce todos los datos del servicio a añadir",
						"Indica el concepto y el número de horas del servicio.");
			} else {
				String horasComa = txtCantidad.getText();
				String horasPunto = horasComa.replace(",", ".");
				try {
					Float.parseFloat(horasPunto);
					servicio = new Servicio(0, comboTipo.getValue() + ": " + txtConcepto.getText(), horasPunto, 0,
							comboTipo.getValue());
					listaServicios.add(servicio);
					columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().servicioProperty());
					columnaHorasServ.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
					tableServicio.setItems(listaServicios);
					// Se actualizan los valores del precio
					actualizarPrecio();
					txtConcepto.setText("");
					txtCantidad.setText("");
					comboTipo.requestFocus();
				} catch (NumberFormatException e) {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Valores incorrectos", "El valor de horas es incorrecto",
							"El valor de horas debe contener únicamente números, separados por coma o punto, sin letras u otros símbolos");
				}
			}

		} else {
			if (txtConcepto.getText().equals("") || txtCantidad.getText().equals("") || txtValor.getText().equals("")) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
						"Introduce todos los datos del material a añadir",
						"Indica el concepto, la cantidad y el precio unitario del material.");
			} else {
				String precioComa = txtValor.getText();
				String precioPunto = precioComa.replace(",", ".");
				float precioTotal;
				try {
					precioTotal = Float.parseFloat(precioPunto) * Integer.parseInt(txtCantidad.getText());
					material = new Material(0, txtConcepto.getText(), precioPunto, 0,
							Integer.parseInt(txtCantidad.getText()), precioTotal);
					listaMaterial.add(material);
					columnaConceptoMat.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
					columnaCantidadMat.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
					columnaPrecioMat.setCellValueFactory(cellData -> cellData.getValue().preciounitProperty());
					tableMaterial.setItems(listaMaterial);
					// Se actualizan los valores del precio
					actualizarPrecio();
					txtConcepto.setText("");
					txtCantidad.setText("");
					txtValor.setText("");
					comboTipo.requestFocus();
				} catch (NumberFormatException e) {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Valores incorrectos",
							"El valor de la cantidad ó el precio es incorrecto",
							"Tanto el valor de cantidad como el de precio unitario deben contener únicamente números, separados por coma o punto, sin letras u otros símbolos");
				}
			}
		}
	}

	/**
	 * Funcion que actualiza los TextField de los precios
	 */
	@FXML
	private void actualizarPrecio() {
		float valorMaterial = 0;
		float valorServicio = 0;
		float valorOtros = 0;
		try {
			valorOtros = Float.parseFloat(txtOtros.getText().replace(",", "."));
		} catch (NumberFormatException e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Valor incorrecto",
					"El valor de \"Otros cargos / Grua\" es incorrecto", "");
		}
		float valorSubtotal = 0;
		float valorIva = 0;
		float valorTotal = 0;

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		DecimalFormat dt = new DecimalFormat("##.##", simbolos);
		dt.setMinimumFractionDigits(2);

		if (listaServicios.size() > 0) {
			for (Servicio serv : listaServicios) {
				if (!serv.getHoras().equalsIgnoreCase("")) {
					String horasComa = serv.getHoras();
					String horasPunto = horasComa.replace(",", ".");
					valorServicio += Float.parseFloat(horasPunto) * Inicio.PRECIO_HORA;
				}
			}
			txtManoObra.setText("" + dt.format(valorServicio));
		} else {
			txtManoObra.setText("" + dt.format(0));
		}

		if (listaMaterial.size() > 0) {
			for (Material mat : listaMaterial) {
				if (!mat.getPreciounit().equalsIgnoreCase("")) {
					String precioUnitPunto = mat.getPreciounit().replace(",", ".");
					float precioTotal = Float.parseFloat(precioUnitPunto) * mat.getCantidad();
					valorMaterial += precioTotal;
				}
			}
			txtMateriales.setText("" + dt.format(valorMaterial));
		} else {
			txtMateriales.setText("" + dt.format(0));
		}

		txtOtros.setText("" + dt.format(valorOtros));

		valorSubtotal = valorMaterial + valorServicio + valorOtros;
		txtSubtotal.setText("" + dt.format(valorSubtotal));

		valorIva = (valorSubtotal * Inicio.PRECIO_IVA) / 100;
		txtIva.setText("" + dt.format(valorIva));

		valorTotal = valorSubtotal + valorIva;
		txtTotal.setText("" + dt.format(valorTotal));
	}

	/**
	 * Función que elimina el servicio seleccionado
	 */
	@FXML
	private void eliminarServicio() {
		int selectedIndex = tableServicio.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			tableServicio.getItems().remove(selectedIndex);
			actualizarPrecio();
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún servicio seleccionado",
					"Selecciona el servicio que quieras eliminar de la tabla.");
		}
	}

	/**
	 * Función que elimina el material seleccionado
	 */
	@FXML
	private void eliminarMaterial() {
		int selectedIndex = tableMaterial.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			tableMaterial.getItems().remove(selectedIndex);
			actualizarPrecio();
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún material seleccionado",
					"Selecciona el material que quieras eliminar de la tabla.");
		}
	}

	/**
	 * Guarda en la base de datos el presupuesto / factura
	 */
	@FXML
	private void llamaGuardarFactura() {
		guardarFactura(1);
	}

	/**
	 * Guarda en la BD el presupuesto / factura
	 * 
	 * @param origen
	 *            1-app, 2-pdf
	 * @return idfactura generado
	 */
	private int guardarFactura(int origen) {
		// Hilo.ejecutaHilo("Hilo 1", funcion -> {
		String error = validarDatos();
		if (error.equals("")) {
			// 2º Comprobar si existe ese cliente en la BD (DNI) y guardarlo
			// si
			// no lo está
			Cliente c = null;
			if (!cpedv.getParticular().getNif().equalsIgnoreCase("")) {
				c = Inicio.CONEXION.buscarClientePorDni(cpedv.getParticular().getNif(), 1);
			} else if (!cpedv.getEmpresa().getCif().equalsIgnoreCase("")) {
				c = Inicio.CONEXION.buscarClientePorDni(cpedv.getEmpresa().getCif(), 2);
			}
			if (c == null) {
				Direccion d = null;
				c = cpedv.getCliente();
				Particular p = null;
				Empresa e = null;
				if (!cpedv.getDireccion().getCalle().equalsIgnoreCase("")
						|| !cpedv.getDireccion().getLocalidad().equalsIgnoreCase("")) {
					d = cpedv.getDireccion();
				}
				if (!cpedv.getParticular().getNombre().equalsIgnoreCase("")
						|| !cpedv.getParticular().getNif().equalsIgnoreCase("")) {
					p = cpedv.getParticular();
				} else if (!cpedv.getEmpresa().getNombre().equalsIgnoreCase("")
						|| !cpedv.getEmpresa().getCif().equalsIgnoreCase("")) {
					e = cpedv.getEmpresa();
				}
				ClienteParticularEmpresaDireccion cped = new ClienteParticularEmpresaDireccion(c, p, e, d);
				Inicio.CONEXION.guardarCliente(cped);
				if (!cpedv.getParticular().getNif().equalsIgnoreCase("")) {
					c = Inicio.CONEXION.buscarClientePorDni(cpedv.getParticular().getNif(), 1);
				} else if (!cpedv.getEmpresa().getCif().equalsIgnoreCase("")) {
					c = Inicio.CONEXION.buscarClientePorDni(cpedv.getEmpresa().getCif(), 2);
				}
			} else {
				// Si está el cliente pero no tiene direccion, la guardo
				if (c.getDireccionID() == 0) {
					if (cpedv.getDireccion() != null) {
						if (!cpedv.getDireccion().getCalle().equalsIgnoreCase("")
								|| !cpedv.getDireccion().getLocalidad().equalsIgnoreCase("")) {
							Direccion d = cpedv.getDireccion();
							int id = (int) Inicio.CONEXION.guardarDireccion(d);
							Inicio.CONEXION.actualizarIDDireccionCliente(c.getIdcliente(), id);
						}
					}
				}
			}
			Inicio.CLIENTE_ID = c.getIdcliente();

			// 3º Comprobar si existe ese vehiculo en la BD (Matricula) y
			// guardarlo si no lo está
			Vehiculo v = null;
			v = Inicio.CONEXION.buscarVehiculoPorMatricula(cpedv.getVehiculo().getMatricula());
			if (v == null) {
				cpedv.getVehiculo().setClienteID(Inicio.CLIENTE_ID);
				// v = new Vehiculo(1, Inicio.CLIENTE_ID,
				// txtMarca.getText(),
				// txtModelo.getText(), txtVersion.getText(),
				// txtMatricula.getText(), 0, "", "", "", "", tipoVehiculo,
				// false);
				if (Inicio.CONEXION.guardarVehiculo(cpedv.getVehiculo())) {
					v = Inicio.CONEXION.buscarVehiculoPorMatricula(cpedv.getVehiculo().getMatricula());
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el vehículo",
							"Ocurrió un error al guardar el vehículo en la base de datos.");
				}
			}
			Inicio.VEHICULO_ID = v.getIdvehiculo();
			// 4º Guardar la factura
			Factura f = crearFactura();
			Inicio.FACTURA_ID = Inicio.CONEXION.guardarFactura(f, listaServicios, listaMaterial);
			if (Inicio.FACTURA_ID != 0) {
				estaGuardada = true;
				if (origen == 1) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Factura guardada",
							"La factura ha sido guardada en la base de datos");
				}
				return Inicio.FACTURA_ID;
			} else {
				estaGuardada = false;
				return 0;
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Faltan datos", error);
			estaGuardada = false;
			return 0;
		}

		// });
	}

	/**
	 * Llama a la función para abrir el diálogo para introducir los datos del
	 * cliente y vehículo
	 */
	@FXML
	private void abrirSelectorCliente() {
		if (Inicio.abrirSelectorFactura(cpedv)) {
			// Utilidades.mostrarAlerta(AlertType.INFORMATION, "Info", "",
			// cpedv.getCliente().getNombre());
			colocarDatos(cpedv);
		}
	}

	/**
	 * Coloca los datos del cliente y el vehículo en la ventana de NuevaFactura
	 * 
	 * @param pDatos
	 */
	private void colocarDatos(ClienteParticularEmpresaDireccionVehiculo pDatos) {
		if (pDatos.getParticular().getNif() != "") {
			txtDni.setText(pDatos.getParticular().getNif());
		} else if (pDatos.getEmpresa().getCif() != "") {
			txtDni.setText(pDatos.getEmpresa().getCif());
		} else {
			txtDni.setText("");
		}
		if (!pDatos.getCliente().getNombre().equalsIgnoreCase("")) {
			lblNombre.setText(pDatos.getCliente().getNombre());
		} else {
			lblNombre.setText("Pulse para introducir cliente");
		}

		txtMatricula.setText(pDatos.getVehiculo().getMatricula());
		if (!pDatos.getVehiculo().getMarca().equalsIgnoreCase("")
				|| !pDatos.getVehiculo().getModelo().equalsIgnoreCase("")) {
			lblMarcaModelo.setText(pDatos.getVehiculo().getMarcaModelo());
		} else {
			lblMarcaModelo.setText("Pulse para introducir vehículo");
		}
	}

	public ClienteParticularEmpresaDireccionVehiculo getCpedv() {
		return cpedv;
	}

	public void setCpedv(ClienteParticularEmpresaDireccionVehiculo cpedv) {
		this.cpedv = cpedv;
	}

	/**
	 * Llama a la función para abrir el diálogo para seleccionar un golpe
	 * predefinido
	 */
	@FXML
	private void abrirSelectorGolpes() {
		int idGolpe = Inicio.abrirSelectorGolpes();
		if (idGolpe != 0) {
			// Utilidades.mostrarAlerta(AlertType.ERROR, "", "" + idGolpe,"");
			anadirGolpe(idGolpe);
		}
	}

	/**
	 * Añade el golpe seleccionado a la factura / presupuesto
	 * 
	 * @param idGolpe
	 *            seleccionado
	 */
	private void anadirGolpe(int idGolpe) {
		ArrayList<ElementosGolpes> listaElementos = Inicio.CONEXION.buscarElementosPorGolpeID(idGolpe);
		if (listaElementos.size() > 0) {
			for (ElementosGolpes e : listaElementos) {
				if (e.getTipo().equalsIgnoreCase("Material")) {
					material = new Material(0, e.getNombreElemento(), "", 0, 0, 0f);
					listaMaterial.add(material);
					columnaConceptoMat.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
					columnaCantidadMat.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
					columnaPrecioMat.setCellValueFactory(cellData -> cellData.getValue().preciounitProperty());
					tableMaterial.setItems(listaMaterial);
				} else {
					servicio = new Servicio(0, e.getNombreElemento(), "", 0, "");
					listaServicios.add(servicio);
					columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().servicioProperty());
					columnaHorasServ.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
					tableServicio.setItems(listaServicios);
				}
			}
		}
	}

	/**
	 * Llama al hilo para generar el pdf de la factura/presupuesto
	 */
	@FXML
	private void generarPDF() {
		String error = validarDatos();
		if (error.equalsIgnoreCase("")) {
			int idf = 0;
			if (estaGuardada) {
				idf = Inicio.FACTURA_ID;
			} else {
				idf = guardarFactura(2);
			}
			Factura f = crearFactura();
			f.setIdfactura(idf);
			Hilo.hilo_GeneraPDF(f);
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Error de validación",
					"Algunos de los datos introducidos son incorrectos", error);
		}
		// FacturaDataSource fds = new FacturaDataSource(f, listaServicios,
		// listaMaterial);
		/*
		 * try { // JasperReport reporte = (JasperReport) //
		 * JRLoader.loadObjectFromFile("reporte1.jasper"); // JasperPrint
		 * jasperPrint = JasperFillManager.fillReport(reporte, // null,
		 * Inicio.CONEXION.getCon()); // JRExporter exporter = new
		 * JRPdfExporter(); //
		 * exporter.setParameter(JRExporterParameter.JASPER_PRINT, //
		 * jasperPrint); //
		 * exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new //
		 * java.io.File("reportePDF.pdf")); // exporter.exportReport();
		 * 
		 * // DataBeanList DataBeanList = new DataBeanList(); //
		 * ArrayList<DataBean> dataList = DataBeanList.getDataBeanList(); //
		 * JRBeanCollectionDataSource beanColDataSource = new //
		 * JRBeanCollectionDataSource(dataList); // String sourceFileName =
		 * "reporte1.jasper"; // String printFileName = null; Map<String,
		 * Object> parameters = new HashMap<String, Object>(); // Esta linea es
		 * aparte del resto JasperReport jr =
		 * JasperCompileManager.compileReport("reporte1.jrxml");
		 * 
		 * // printFileName = //
		 * JasperFillManager.fillReportToFile(sourceFileName, parameters, //
		 * beanColDataSource); JasperPrint jpr =
		 * JasperFillManager.fillReport(jr, parameters,
		 * Inicio.CONEXION.getCon());
		 * JasperExportManager.exportReportToPdfFile(jpr, "reportePDF.pdf"); }
		 * catch (Exception e) { Utilidades.mostrarError(e); }
		 */
	}

	/**
	 * Comprueba que los datos introducidos en la factura son correctos
	 * 
	 * @return cadena vacía si los datos son válidos o mensaje de error en caso
	 *         contrario
	 */
	private String validarDatos() {
		String mensaje = "";
		// Comprobar datos cliente
		if (cpedv.getCliente().getNombre().equalsIgnoreCase("") || txtDni.getText().isEmpty()
				|| cpedv.getCliente().getTelf1().equalsIgnoreCase("")) {
			mensaje = "Debes indicar por lo menos el nombre, DNI y un teléfono del cliente.";
		}
		// Datos del vehiculo
		if (cpedv.getVehiculo().getMatricula().equalsIgnoreCase("")
				|| cpedv.getVehiculo().getMarca().equalsIgnoreCase("")
				|| cpedv.getVehiculo().getModelo().equalsIgnoreCase("")) {
			mensaje = "Debes indicar la marca, modelo y matrícula del vehículo.";
		}
		// Comprobar datos factura
		if ((!chckbxFactura.isSelected() && !chckbxPresupuesto.isSelected() && !chckbxOrdenDeReparacion.isSelected()
				&& !chckbxResguardoDeposito.isSelected())
				|| (txtNumfactura.getText().equals("") && txtNumPresupuesto.getText().equals("")
						&& txtNumOrden.getText().equals("") && txtNumResguardo.getText().equals(""))) {
			mensaje = "Debes marcar si es factura, presupuesto, orden de reparación o resguardo de depósito e indicar su número.";
		}
		if (listaServicios.isEmpty() && listaMaterial.isEmpty()) {
			mensaje = "Debes añadir algún servicio o material.";
		}
		try {
			if (!txtManoObra.getText().isEmpty()) {
				Float.parseFloat(txtManoObra.getText().replace(",", "."));
			}
			if (!txtMateriales.getText().isEmpty()) {
				Float.parseFloat(txtMateriales.getText().replace(",", "."));
			}
			if (!txtOtros.getText().isEmpty()) {
				Float.parseFloat(txtOtros.getText().replace(",", "."));
			}
			if (!txtSubtotal.getText().isEmpty()) {
				Float.parseFloat(txtSubtotal.getText().replace(",", "."));
			}
			if (!txtIva.getText().isEmpty()) {
				Float.parseFloat(txtIva.getText().replace(",", "."));
			}
			if (!txtTotal.getText().isEmpty()) {
				Float.parseFloat(txtTotal.getText().replace(",", "."));
			}
			if (!txtPorcentajeDefOcultos.getText().isEmpty()) {
				Float.parseFloat(txtPorcentajeDefOcultos.getText().replace(",", "."));
			}
		} catch (Exception e) {
			mensaje = "Los valores numéricos no son correctos, revíselos.";
		}

		return mensaje;
	}

	/**
	 * Crea el objeto de tipo Factura con los datos que hay en este momento
	 * 
	 * @return factura
	 */
	private Factura crearFactura() {
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

		Float manoObra = 0f;
		Float materiales = 0f;
		Float otros = 0f;
		Float suma = 0f;
		Float sumaIva = 0f;
		Float total = 0f;
		Float porcentajeOcultos = 0f;
		if (!txtManoObra.getText().isEmpty()) {
			manoObra = Float.parseFloat(txtManoObra.getText().replace(",", "."));
		}
		if (!txtMateriales.getText().isEmpty()) {
			materiales = Float.parseFloat(txtMateriales.getText().replace(",", "."));
		}
		if (!txtOtros.getText().isEmpty()) {
			otros = Float.parseFloat(txtOtros.getText().replace(",", "."));
		}
		if (!txtSubtotal.getText().isEmpty()) {
			suma = Float.parseFloat(txtSubtotal.getText().replace(",", "."));
		}
		if (!txtIva.getText().isEmpty()) {
			sumaIva = Float.parseFloat(txtIva.getText().replace(",", "."));
		}
		if (!txtTotal.getText().isEmpty()) {
			total = Float.parseFloat(txtTotal.getText().replace(",", "."));
		}

		if (!txtPorcentajeDefOcultos.getText().isEmpty()) {
			porcentajeOcultos = Float.parseFloat(txtPorcentajeDefOcultos.getText().replace(",", "."));
		}
		Factura f = new Factura(1, Inicio.CLIENTE_ID, Inicio.VEHICULO_ID, cpedv.getKms(), numFactura, numPresupuesto,
				numOrden, numResguardo, Utilidades.LocalDateADate(txtFecha.getValue()),
				Utilidades.LocalDateADate(txtFechaEntrega.getValue()), manoObra, materiales, otros, suma, sumaIva,
				"ESTADO", chckbxRepararDefOcultos.isSelected(), porcentajeOcultos, chckbxPermisoPruebas.isSelected(),
				chckbxNoPiezas.isSelected(), chckbxModificable.isSelected(), total);
		return f;
	}

}