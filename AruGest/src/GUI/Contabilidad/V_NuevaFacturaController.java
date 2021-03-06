package GUI.Contabilidad;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import Logica.Hilo;
import Logica.Inicio;
import Logica.StringUtils;
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

	// Datos cliente y veh�culo
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
	private CheckBox chckbxCobrado;
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

	private boolean modificar = false;
	private boolean esServicio = false; // Controla si es servicio o material
	// private int tipoCliente = 1; // 1-Particular, 2-Empresa

	private Servicio servicio;
	private Material material;

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
		modificar = true;
		// Cargar datos factura
		if (!fce.getFactura().getNumfactura().equalsIgnoreCase("0")
				&& !fce.getFactura().getNumfactura().equalsIgnoreCase("")) {
			chckbxFactura.setSelected(true);
			txtNumfactura.setText("" + fce.getFactura().getNumfactura());
		} else {
			chckbxFactura.setSelected(false);
		}
		if (!fce.getFactura().getNumpresupuesto().equalsIgnoreCase("0")
				&& !fce.getFactura().getNumpresupuesto().equalsIgnoreCase("")) {
			chckbxPresupuesto.setSelected(true);
			txtNumPresupuesto.setText("" + fce.getFactura().getNumpresupuesto());
		} else {
			chckbxPresupuesto.setSelected(false);
		}
		if (!fce.getFactura().getNumordenrep().equalsIgnoreCase("0")
				&& !fce.getFactura().getNumordenrep().equalsIgnoreCase("")) {
			chckbxOrdenDeReparacion.setSelected(true);
			txtNumOrden.setText("" + fce.getFactura().getNumordenrep());
		} else {
			chckbxOrdenDeReparacion.setSelected(false);
		}
		if (!fce.getFactura().getNumresguardo().equalsIgnoreCase("0")
				&& !fce.getFactura().getNumresguardo().equalsIgnoreCase("")) {
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
		columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().tipoConServicioProperty());
		columnaHorasServ.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
		tableServicio.setItems(listaServicios);

		// Cargar materiales
		listaMaterial = Inicio.CONEXION.buscarMaterialesPorFacturaID(Inicio.FACTURA_ID);
		columnaConceptoMat.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
		columnaCantidadMat.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
		columnaPrecioMat.setCellValueFactory(cellData -> cellData.getValue().preciounitProperty());
		tableMaterial.setItems(listaMaterial);

		// Recoger piezas, modificable, fechaentrega...
		chckbxCobrado.setSelected(fce.getFactura().isCobrado());
		chckbxNoPiezas.setSelected(fce.getFactura().isNopiezas());
		chckbxPermisoPruebas.setSelected(fce.getFactura().isPermisopruebas());
		chckbxRepararDefOcultos.setSelected(fce.getFactura().isRdefocultos());
		txtPorcentajeDefOcultos.setText(Float.toString(fce.getFactura().getPorcentajedefocul()));
		txtFechaEntrega.setValue(Utilidades.DateALocalDate(fce.getFactura().getFechaentrega()));

		actualizarPrecio();
	}

	/**
	 * Carga en la factura los datos del cliente y del veh�culo
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
		// Para que el bot�n de a�adir funcione con el Enter
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
		comboTipo.getItems().addAll("Material", "Chapa", "Pintura", "Electr�nica / mec�nica");
		comboTipo.setValue("Material");

		// A�adir un listener al combo
		comboTipo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarCombo(newValue));

		// Marcar algunos checkbox que son habituales
		if (Inicio.OPCION_NUEVA.equalsIgnoreCase("P")) {
			chckbxPresupuesto.setSelected(true);
			txtNumPresupuesto.setText("" + Utilidades.formateaNumFactura(Inicio.NUM_PRESUPUESTO));
			chckbxFactura.setSelected(false);
		} else if (Inicio.OPCION_NUEVA.equalsIgnoreCase("F")) {
			chckbxPresupuesto.setSelected(false);
			chckbxFactura.setSelected(true);
			txtNumfactura.setText("" + Utilidades.formateaNumFactura(Inicio.NUM_FACTURA));
		} else if (Inicio.OPCION_NUEVA.equalsIgnoreCase("A")) { // A se ponen
																// los dos
			chckbxFactura.setSelected(true);
			txtNumfactura.setText("" + Utilidades.formateaNumFactura(Inicio.NUM_FACTURA));

			chckbxPresupuesto.setSelected(true);
			txtNumPresupuesto.setText("" + Utilidades.formateaNumFactura(Inicio.NUM_PRESUPUESTO));
		}
		chckbxPermisoPruebas.setSelected(true);
		chckbxNoPiezas.setSelected(true);
		chckbxCobrado.setSelected(false);

		// Editar columna Concepto de la tabla Servicios
		columnaConceptoServ.setCellFactory(TextFieldTableCell.<Servicio> forTableColumn());
		columnaConceptoServ.setOnEditCommit((CellEditEvent<Servicio, String> t) -> {
			(t.getTableView().getItems().get(t.getTablePosition().getRow())).setServicio(t.getNewValue());
		});

		// Editar columna Horas de la tabla Servicios
		columnaHorasServ.setCellFactory(TextFieldTableCell.<Servicio> forTableColumn());
		columnaHorasServ.setOnEditCommit((CellEditEvent<Servicio, String> t) -> {
			(t.getTableView().getItems().get(t.getTablePosition().getRow())).setHoras(t.getNewValue());
			// Se actualizan los valores del precio
			actualizarPrecio();
		});

		// Editar columna Concepto de la tabla Materiales
		columnaConceptoMat.setCellFactory(TextFieldTableCell.<Material> forTableColumn());
		columnaConceptoMat.setOnEditCommit((CellEditEvent<Material, String> t) -> {
			(t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
		});

		// Editar columna Cantidad de la tabla Materiales
		columnaCantidadMat
				.setCellFactory(TextFieldTableCell.<Material, Number> forTableColumn(new NumberStringConverter()));
		columnaCantidadMat.setOnEditCommit((CellEditEvent<Material, Number> t) -> {
			(t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setCantidad(t.getNewValue().intValue());
			// Se actualizan los valores del precio
			actualizarPrecio();
		});

		// Editar columna Horas de la tabla Servicios
		columnaPrecioMat.setCellFactory(TextFieldTableCell.<Material> forTableColumn());
		columnaPrecioMat.setOnEditCommit((CellEditEvent<Material, String> t) -> {
			(t.getTableView().getItems().get(t.getTablePosition().getRow())).setPreciounit(t.getNewValue());
			// Se actualizan los valores del precio
			actualizarPrecio();
		});

		chckbxFactura.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
			if (isSelected) {
				txtNumfactura.setText("" + Utilidades.formateaNumFactura(Inicio.NUM_FACTURA));
			} else {
				txtNumfactura.setText("");
			}
		});

		chckbxPresupuesto.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
			if (isSelected) {
				txtNumPresupuesto.setText("" + Utilidades.formateaNumFactura(Inicio.NUM_PRESUPUESTO));
			} else {
				txtNumPresupuesto.setText("");
			}
		});
	}

	public void setFocus() {
		txtNumPresupuesto.requestFocus();
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
	 * Se a�ade el servicio o material a la tabla correspondiente
	 */
	@FXML
	private void add() {
		if (esServicio) {
			if (txtConcepto.getText().equals("") || txtCantidad.getText().equals("")) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, StringUtils.ATENCION,
						"Introduce todos los datos del servicio a a�adir",
						"Indica el concepto y el n�mero de horas del servicio.");
			} else {
				String horasComa = txtCantidad.getText();
				String horasPunto = horasComa.replace(",", ".");
				try {
					Float.parseFloat(horasPunto);
					servicio = new Servicio(0, txtConcepto.getText(), horasPunto, 0, comboTipo.getValue());
					listaServicios.add(servicio);
					columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().tipoConServicioProperty());
					columnaHorasServ.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
					tableServicio.setItems(listaServicios);
					// Se actualizan los valores del precio
					actualizarPrecio();
					txtConcepto.setText("");
					txtCantidad.setText("");
					comboTipo.requestFocus();

				} catch (NumberFormatException e) {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Valores incorrectos", "El valor de horas es incorrecto",
							"El valor de horas debe contener �nicamente n�meros, separados por coma o punto, sin letras u otros s�mbolos");
				}
			}

		} else {
			if (txtConcepto.getText().equals("") || txtCantidad.getText().equals("") || txtValor.getText().equals("")) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n",
						"Introduce todos los datos del material a a�adir",
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
							"El valor de la cantidad � el precio es incorrecto",
							"Tanto el valor de cantidad como el de precio unitario deben contener �nicamente n�meros, separados por coma o punto, sin letras u otros s�mbolos");
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
		if (!txtOtros.getText().isEmpty()) {
			try {
				valorOtros = Float.parseFloat(txtOtros.getText().replace(",", "."));
			} catch (NumberFormatException e) {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Valor incorrecto",
						"El valor de \"Otros cargos / Grua\" es incorrecto", "");
			}
		} else {
			valorOtros = 0;
		}
		float valorSubtotal = 0;
		float valorIva = 0;
		float valorTotal = 0;

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		DecimalFormat dt = new DecimalFormat("##.##", simbolos);
		dt.setMinimumFractionDigits(2);

		if (!listaServicios.isEmpty()) {
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

		if (!listaMaterial.isEmpty()) {
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
	 * Funci�n que elimina el servicio seleccionado
	 */
	@FXML
	private void eliminarServicio() {
		int selectedIndex = tableServicio.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			tableServicio.getItems().remove(selectedIndex);
			actualizarPrecio();
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "Ning�n servicio seleccionado",
					"Selecciona el servicio que quieras eliminar de la tabla.");
		}
	}

	/**
	 * Funci�n que elimina el material seleccionado
	 */
	@FXML
	private void eliminarMaterial() {
		int selectedIndex = tableMaterial.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			tableMaterial.getItems().remove(selectedIndex);
			actualizarPrecio();
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "Ning�n material seleccionado",
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
		String error = validarDatos();
		if (error.equals("")) {
			if (modificar) {
				// 4� Modificar la factura
				Factura fa = crearFactura();
				Inicio.CONEXION.modificarFactura(Inicio.FACTURA_ID, fa, listaServicios, listaMaterial);
				if (origen == 1) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "Factura modificada",
							"La factura ha sido modificada en la base de datos");
				}
				return Inicio.FACTURA_ID;
			} else {
				// 2� Comprobar si existe ese cliente en la BD (DNI) y guardarlo si no lo est�
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
					if (cpedv.getDireccion() != null) {
						if (!cpedv.getDireccion().getDireccion().equalsIgnoreCase("")
								|| !cpedv.getDireccion().getLocalidad().equalsIgnoreCase("")) {
							d = cpedv.getDireccion();
						}
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
					// Si est� el cliente pero no tiene direccion, la guardo
					if (c.getDireccionID() == 0) {
						if (cpedv.getDireccion() != null) {
							if (!cpedv.getDireccion().getDireccion().equalsIgnoreCase("")
									|| !cpedv.getDireccion().getLocalidad().equalsIgnoreCase("")) {
								Direccion d = cpedv.getDireccion();
								int id = (int) Inicio.CONEXION.guardarDireccion(d);
								Inicio.CONEXION.actualizarIDDireccionCliente(c.getIdcliente(), id);
							}
						}
					}
				}
				Inicio.CLIENTE_ID = c.getIdcliente();

				// 3� Comprobar si existe ese vehiculo en la BD (Matricula) y guardarlo si no lo est�
				Vehiculo v = null;
				v = Inicio.CONEXION.buscarVehiculoPorMatricula(cpedv.getVehiculo().getMatricula());
				if (v == null) {
					cpedv.getVehiculo().setClienteID(Inicio.CLIENTE_ID);
					if (Inicio.CONEXION.guardarVehiculo(cpedv.getVehiculo())) {
						v = Inicio.CONEXION.buscarVehiculoPorMatricula(cpedv.getVehiculo().getMatricula());
						Inicio.VEHICULO_ID = v.getIdvehiculo();
					} else {
						Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el veh�culo",
								"Ocurri� un error al guardar el veh�culo en la base de datos.");
					}
				}else {
					Inicio.VEHICULO_ID = v.getIdvehiculo();
				}

				// 4� Guardar la factura
				Factura f = crearFactura();
				Inicio.FACTURA_ID = Inicio.CONEXION.guardarFactura(f, listaServicios, listaMaterial);
				if (Inicio.FACTURA_ID != 0) {
					modificar = true;
					if (origen == 1) {
						Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "Factura guardada",
								"La factura ha sido guardada en la base de datos");
					}
					return Inicio.FACTURA_ID;
				} else {
					modificar = false;
					return 0;
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "Faltan datos", error);
			return 0;
		}
	}

	/**
	 * Llama a la funci�n para abrir el di�logo para introducir los datos del
	 * cliente y veh�culo
	 */
	@FXML
	private void abrirSelectorCliente() {
		if (Inicio.abrirSelectorFactura(cpedv)) {
			colocarDatos(cpedv);
		}
	}

	/**
	 * Coloca los datos del cliente y el veh�culo en la ventana de NuevaFactura
	 * 
	 * @param pDatos
	 */
	private void colocarDatos(ClienteParticularEmpresaDireccionVehiculo pDatos) {
		if (pDatos.getParticular() != null || pDatos.getEmpresa() != null) {
			if (pDatos.getParticular().getNif() != "") {
				txtDni.setText(pDatos.getParticular().getNif());
			} else if (pDatos.getEmpresa().getCif() != "") {
				txtDni.setText(pDatos.getEmpresa().getCif());
			} else {
				txtDni.setText("");
			}
		} else {
			txtDni.setText("");
		}

		if (pDatos.getCliente() != null) {
			if (!pDatos.getCliente().getNombre().equalsIgnoreCase("")) {
				lblNombre.setText(pDatos.getCliente().getNombre());
			} else {
				lblNombre.setText("Pulse para introducir cliente");
			}
		} else {
			lblNombre.setText("Pulse para introducir cliente");
		}

		if (pDatos.getVehiculo() != null) {
			txtMatricula.setText(pDatos.getVehiculo().getMatricula());
			if (!pDatos.getVehiculo().getMarca().equalsIgnoreCase("")
					|| !pDatos.getVehiculo().getModelo().equalsIgnoreCase("")) {
				lblMarcaModelo.setText(pDatos.getVehiculo().getMarcaModelo());
			} else {
				lblMarcaModelo.setText("Pulse para introducir veh�culo");
			}
		} else {
			lblMarcaModelo.setText("Pulse para introducir veh�culo");
		}
	}

	public ClienteParticularEmpresaDireccionVehiculo getCpedv() {
		return cpedv;
	}

	public void setCpedv(ClienteParticularEmpresaDireccionVehiculo cpedv) {
		this.cpedv = cpedv;
	}

	/**
	 * Llama a la funci�n para abrir el di�logo para seleccionar un golpe
	 * predefinido
	 */
	@FXML
	private void abrirSelectorGolpes() {
		int idGolpe = Inicio.abrirSelectorGolpes();
		if (idGolpe != 0) {
			anadirGolpe(idGolpe);
		}
	}

	/**
	 * A�ade el golpe seleccionado a la factura / presupuesto
	 * 
	 * @param idGolpe
	 *            seleccionado
	 */
	private void anadirGolpe(int idGolpe) {
		ArrayList<ElementosGolpes> listaElementos = Inicio.CONEXION.buscarElementosPorGolpeID(idGolpe);
		if (!listaElementos.isEmpty()) {
			for (ElementosGolpes e : listaElementos) {
				if (e.getTipo().equalsIgnoreCase("Material")) {
					material = new Material(0, e.getNombreElemento(), "", 0, 0, 0f);
					listaMaterial.add(material);
					columnaConceptoMat.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
					columnaCantidadMat.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
					columnaPrecioMat.setCellValueFactory(cellData -> cellData.getValue().preciounitProperty());
					tableMaterial.setItems(listaMaterial);
				} else {
					servicio = new Servicio(0, e.getNombreElemento(), "", 0, e.getTipo());
					listaServicios.add(servicio);
					columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().tipoConServicioProperty());
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
			if (modificar) {
				idf = Inicio.FACTURA_ID;
			} else {
				idf = guardarFactura(2);
			}
			Factura f = crearFactura();
			f.setIdfactura(idf);
			Hilo.hilo_GeneraPDF(f);
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Error de validaci�n",
					"Algunos de los datos introducidos son incorrectos", error);
		}
	}

	/**
	 * Comprueba que los datos introducidos en la factura son correctos
	 * 
	 * @return cadena vac�a si los datos son v�lidos o mensaje de error en caso
	 *         contrario
	 */
	private String validarDatos() {
		String mensaje = "";
		// Comprobar datos cliente
		if (cpedv.getCliente().getNombre().equalsIgnoreCase("") || txtDni.getText().isEmpty()
				|| cpedv.getCliente().getTelf1().equalsIgnoreCase("")) {
			mensaje = "Debes indicar por lo menos el nombre, DNI y un tel�fono del cliente.";
		}
		// Datos del vehiculo
		if (cpedv.getVehiculo().getMatricula().equalsIgnoreCase("")
				|| cpedv.getVehiculo().getMarca().equalsIgnoreCase("")
				|| cpedv.getVehiculo().getModelo().equalsIgnoreCase("")) {
			mensaje = "Debes indicar la marca, modelo y matr�cula del veh�culo.";
		}
		// Comprobar datos factura
		if ((!chckbxFactura.isSelected() && !chckbxPresupuesto.isSelected() && !chckbxOrdenDeReparacion.isSelected()
				&& !chckbxResguardoDeposito.isSelected())
				|| (txtNumfactura.getText().equals("") && txtNumPresupuesto.getText().equals("")
						&& txtNumOrden.getText().equals("") && txtNumResguardo.getText().equals(""))) {
			mensaje = "Debes marcar si es factura, presupuesto, orden de reparaci�n o resguardo de dep�sito e indicar su n�mero.";
		}
		if (listaServicios.isEmpty() && listaMaterial.isEmpty()) {
			mensaje = "Debes a�adir alg�n servicio o material.";
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
			mensaje = "Los valores num�ricos no son correctos, rev�selos.";
		}

		return mensaje;
	}

	/**
	 * Crea el objeto de tipo Factura con los datos que hay en este momento
	 * 
	 * @return factura
	 */
	private Factura crearFactura() {
		String numFactura = "";
		String numPresupuesto = "";
		String numOrden = "";
		String numResguardo = "";
		if (!txtNumfactura.getText().isEmpty()) {
			numFactura = Utilidades.formateaNumFactura(txtNumfactura.getText());
		} else {
			numFactura = "0";
		}
		if (!txtNumPresupuesto.getText().isEmpty()) {
			numPresupuesto = Utilidades.formateaNumFactura(txtNumPresupuesto.getText());
		} else {
			numPresupuesto = "0";
		}
		if (!txtNumOrden.getText().isEmpty()) {
			numOrden = Utilidades.formateaNumFactura(txtNumOrden.getText());
		} else {
			numOrden = "0";
		}
		if (!txtNumResguardo.getText().isEmpty()) {
			numResguardo = Utilidades.formateaNumFactura(txtNumResguardo.getText());
		} else {
			numResguardo = "0";
		}

		Float manoObra = 0f;
		Float materiales = 0f;
		Float otros = 0f;
		Float suma = 0f;
		Float sumaIva = 0f;
		Float total = 0f;
		Float porcentajeOcultos = 0f;
		Date fechaEntrega;
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

		if (txtFechaEntrega.getValue() != null) {
			fechaEntrega = Utilidades.LocalDateADate(txtFechaEntrega.getValue());
		} else {
			fechaEntrega = null;
		}
		return new Factura(1, Inicio.CLIENTE_ID, Inicio.VEHICULO_ID, cpedv.getKms(), numFactura, numPresupuesto,
				numOrden, numResguardo, Utilidades.LocalDateADate(txtFecha.getValue()), fechaEntrega, manoObra,
				materiales, otros, suma, sumaIva, chckbxRepararDefOcultos.isSelected(), porcentajeOcultos,
				chckbxPermisoPruebas.isSelected(), chckbxNoPiezas.isSelected(), chckbxCobrado.isSelected(), total);
	}
	
	@FXML
	private void upperFirstLetter() {
		int pos = txtConcepto.getCaretPosition();
		txtConcepto.setText(Utilidades.upperFirstLetter(txtConcepto.getText()));
		txtConcepto.positionCaret(pos);
	}

}