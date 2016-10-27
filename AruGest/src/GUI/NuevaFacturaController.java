package GUI;

import java.text.DecimalFormat;
import java.time.LocalDate;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Cliente;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.Material;
import Modelo.Particular;
import Modelo.Servicio;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Joseba
 *
 */
public class NuevaFacturaController {

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

	// Datos cliente
	@FXML
	private TextField txtNombre;
	@FXML
	private Label lblApellidos;
	@FXML
	private TextField txtApellidos;
	@FXML
	private TextField txtCalle;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtPiso;
	@FXML
	private TextField txtLetra;
	@FXML
	private TextField txtPoblacion;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtFijo;
	@FXML
	private TextField txtMovil;

	// Datos Vehiculo
	@FXML
	private ComboBox<String> comboTipoVehiculo;
	@FXML
	private TextField txtMarca;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtVersion;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtKms;
	@FXML
	private ComboBox<String> comboTipoCliente;

	// Servicios y materiales
	@FXML
	private ComboBox<String> comboTipo;
	@FXML
	private TextArea txtConcepto;
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

	private boolean esServicio = false; // Variable para controlar si es un
										// servicio o un material
	private Servicio servicio;
	private Material material;

	private int tipoVehiculo = 1;

	public void setMainAPP(Inicio p) {
		main = p;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		tableServicio.setEditable(true);
		tableMaterial.setEditable(true);

		// Inicializar los valores de los combos, la fecha y marcar el checkbox
		// de presupuesto o factura
		txtFecha.setValue(LocalDate.now());
		comboTipoCliente.getItems().addAll("Particular", "Empresa");
		comboTipoCliente.setValue("Particular");
		comboTipoVehiculo.getItems().addAll("Turismo", "Furgoneta", "Cami�n", "Autob�s", "Autocaravana", "Moto",
				"Remolque");
		comboTipoVehiculo.setValue("Turismo");
		comboTipo.getItems().addAll("Material", "Chapa", "Pintura", "Electr�nica / mec�nica");
		comboTipo.setValue("Material");

		// A�adir un listener al combo
		comboTipo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarCombo(newValue));

		comboTipoCliente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipoCliente(newValue));

		comboTipoVehiculo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipoVehiculo(newValue));

		// Marcar algunos checkbox que son habituales
		if (Inicio.getOpcionNueva().equalsIgnoreCase("P")) {
			chckbxPresupuesto.setSelected(true);
			chckbxFactura.setSelected(false);
		} else if (Inicio.getOpcionNueva().equalsIgnoreCase("F")) {
			chckbxPresupuesto.setSelected(false);
			chckbxFactura.setSelected(true);
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
	private void comprobarComboTipoCliente(String valor) {
		if (valor.equalsIgnoreCase("Empresa")) {
			lblApellidos.setVisible(false);
			txtApellidos.setVisible(false);
			txtNombre.setPrefWidth(300);
		} else {
			lblApellidos.setVisible(true);
			txtApellidos.setVisible(true);
			txtNombre.setPrefWidth(100);
		}
	}

	/**
	 * Se comprueba el valor elegido en el combo de tipo vehiculo para definir
	 * el valor de la variable tipoVehiculo
	 * 
	 * @param valor
	 */
	private void comprobarComboTipoVehiculo(String valor) {
		switch (valor) {
		case "Turismo":
			tipoVehiculo = 1;
			break;
		case "Furgoneta":
			tipoVehiculo = 2;
			break;
		case "Cami�n":
			tipoVehiculo = 3;
			break;
		case "Autob�s":
			tipoVehiculo = 4;
			break;
		case "Autocaravana":
			tipoVehiculo = 5;
			break;
		case "Moto":
			tipoVehiculo = 6;
			break;
		case "Remolque":
			tipoVehiculo = 7;
			break;
		}
	}

	/**
	 * Se a�ade el servicio o material a la tabla correspondiente
	 */
	@FXML
	private void add() {
		if (esServicio) {
			if (txtConcepto.getText().equals("") || txtCantidad.getText().equals("")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Atenci�n");
				alert.setHeaderText("Introduce todos los datos del servicio a a�adir");
				alert.setContentText("Indica el concepto y el n�mero de horas del servicio realizado.");
				alert.showAndWait();
			} else {
				String horasComa = txtCantidad.getText();
				String horasPunto = horasComa.replace(",", ".");
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
			}

		} else {
			if (txtConcepto.getText().equals("") || txtCantidad.getText().equals("") || txtValor.getText().equals("")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Atenci�n");
				alert.setHeaderText("Introduce todos los datos del material a a�adir");
				alert.setContentText("Indica el concepto, la cantidad y el precio unitario del material.");
				alert.showAndWait();
			} else {
				String precioComa = txtValor.getText();
				String precioPunto = precioComa.replace(",", ".");
				float precioTotal = Float.parseFloat(precioPunto) * Integer.parseInt(txtCantidad.getText());
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
		float valorOtros = Float.parseFloat(txtOtros.getText());
		float valorSubtotal = 0;
		float valorIva = 0; // LEER EL VALOR DEL IVA !!!!!!!!!!!!!!!!
		float valorTotal = 0;

		DecimalFormat dt = new DecimalFormat("##.##");

		if (listaServicios.size() > 0) {
			for (Servicio serv : listaServicios) {
				String horasComa = serv.getHoras();
				String horasPunto = horasComa.replace(",", ".");
				valorServicio += Float.parseFloat(horasPunto) * 46; // LEER EL
																	// PRECIO DE
																	// HORA Y
																	// CAMBIARLO
																	// POR EL
																	// 46!!!!!!!!!!!!!!!!!!!!!
				txtManoObra.setText("" + dt.format(valorServicio));
			}
		} else {
			txtManoObra.setText("" + dt.format(0));
		}

		if (listaMaterial.size() > 0) {
			for (Material mat : listaMaterial) {
				float precioTotal = Float.parseFloat(mat.getPreciounit()) * mat.getCantidad();
				valorMaterial += precioTotal;
				txtMateriales.setText("" + dt.format(valorMaterial));
			}
		} else {
			txtMateriales.setText("" + dt.format(0));
		}

		valorSubtotal = valorMaterial + valorServicio + valorOtros;
		txtSubtotal.setText("" + dt.format(valorSubtotal));

		valorIva = (valorSubtotal * 16) / 100; // CAMBIAR EL 21 POR EL VALOR DEL
												// IVA!!!!!!!!!!!!!!!!!!!
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
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atenci�n");
			alert.setHeaderText("Ning�n servicio seleccionado");
			alert.setContentText("Selecciona el servicio que quieras eliminar de la tabla.");

			alert.showAndWait();
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
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atenci�n");
			alert.setHeaderText("Ning�n material seleccionado");
			alert.setContentText("Selecciona el material que quieras eliminar de la tabla.");

			alert.showAndWait();
		}
	}

	/**
	 * Guarda en la base de datos el presupuesto / factura
	 */
	@FXML
	private void guardarFactura() {
		String mensaje = "";
		// 1� Comprobar entradas de datos son correctas
		// Comprobar datos factura
		if ((!chckbxFactura.isSelected() && !chckbxPresupuesto.isSelected() && !chckbxOrdenDeReparacion.isSelected()
				&& !chckbxResguardoDeposito.isSelected())
				|| (txtNumfactura.getText().equals("") && txtNumPresupuesto.getText().equals("")
						&& txtNumOrden.getText().equals("") && txtNumResguardo.getText().equals(""))) {
			mensaje = "Debes marcar si es factura, presupuesto, orden de reparaci�n o resguardo de dep�sito e indicar su n�mero.";
		}
		// Comprobar datos cliente
		if (txtNombre.getText().isEmpty() || txtDni.getText().isEmpty()
				|| (txtFijo.getText().isEmpty() && txtMovil.getText().isEmpty())) {
			mensaje = "Debes indicar por lo menos el nombre, DNI y un tel�fono del cliente.";
		}
		// Datos del vehiculo
		if (txtMatricula.getText().isEmpty() || txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty()) {
			mensaje = "Debes indicar la marca, modelo y matr�cula del veh�culo.";
		}
		if (listaServicios.isEmpty() && listaMaterial.isEmpty()) {
			mensaje = "Debes a�adir alg�n servicio o material.";
		}
		if (mensaje.equals("")) {
			// 2� Comprobar si existe ese cliente en la BD (DNI) y guardarlo si
			// no lo est�
			Cliente c = null;
			c = Inicio.CONEXION.buscarClientePorDni(txtDni.getText());
			if (c == null) {
				Direccion d = null;
				c = new Cliente(txtNombre.getText() + " " + txtApellidos.getText(), txtFijo.getText(),
						txtMovil.getText());
				Particular p = null;
				Empresa e = null;
				if (!txtCalle.getText().isEmpty()) {
					d = new Direccion(txtCalle.getText(), Integer.parseInt(txtNumero.getText()), txtPiso.getText(),
							txtLetra.getText(), txtPoblacion.getText());
				}
				if (comboTipoCliente.getValue().equalsIgnoreCase("Particular")) {
					p = new Particular(txtNombre.getText(), txtApellidos.getText(), txtDni.getText());
				} else {
					e = new Empresa(txtNombre.getText(), txtDni.getText());
				}
				Inicio.CONEXION.guardarCliente(d, c, p, e);
				c = Inicio.CONEXION.buscarClientePorDni(txtDni.getText());
			}
			Inicio.CLIENTE_ID = c.getIdcliente();

			// 3� Comprobar si existe ese vehiculo en la BD (Matricula) y
			// guardarlo si no lo est�
			Vehiculo v = null;
			v = Inicio.CONEXION.buscarVehiculoPorMatricula(txtMatricula.getText());
			if (v == null) {
				int clienteID = Inicio.CONEXION.buscarClientePorDni(txtDni.getText()).getIdcliente();
				v = new Vehiculo(clienteID, txtMarca.getText(), txtModelo.getText(), txtVersion.getText(),
						txtMatricula.getText(), tipoVehiculo);
				Inicio.CONEXION.guardarVehiculo(v);
				v = Inicio.CONEXION.buscarVehiculoPorMatricula(txtMatricula.getText());
			}
			Inicio.VEHICULO_ID = v.getIdvehiculo();
			// 4� Guardar la factura -> 1� Factura 2� Servicios 3� Materiales
			Factura f = new Factura(Inicio.CLIENTE_ID, Inicio.VEHICULO_ID, Integer.parseInt(txtNumfactura.getText()),
					Integer.parseInt(txtNumPresupuesto.getText()), Integer.parseInt(txtNumOrden.getText()), Integer.parseInt(txtNumResguardo.getText()),
					Utilidades.LocalDateADate(txtFecha.getValue()),
					Utilidades.LocalDateADate(txtFechaEntrega.getValue()), Float.parseFloat(txtManoObra.getText()),
					Float.parseFloat(txtMateriales.getText()), Float.parseFloat(txtOtros.getText()), "ESTADO",
					chckbxRepararDefOcultos.isSelected(), Float.parseFloat(txtPorcentajeDefOcultos.getText()),
					chckbxPermisoPruebas.isSelected(), chckbxNoPiezas.isSelected(), chckbxModificable.isSelected());
			Inicio.CONEXION.guardarFactura(f);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atenci�n");
			alert.setHeaderText("Faltan datos");
			alert.setContentText(mensaje);

			alert.showAndWait();
		}
	}
}