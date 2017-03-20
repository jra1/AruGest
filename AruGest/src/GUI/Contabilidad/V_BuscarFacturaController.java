package GUI.Contabilidad;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.guigarage.responsive.ResponsiveHandler;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.FacturaClienteVehiculo;
import Modelo.GestorVentana;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Clase controlador de la ventana BuscarFactura, que se utiliza para buscar en
 * la base de datos las facturas que coincidan con los parámetros que introduce
 * el usuario
 * 
 * @author Joseba
 *
 */
public class V_BuscarFacturaController {

	@FXML
	private CheckBox chckbxFacturas;
	@FXML
	private TextField txtNumfactura;
	@FXML
	private CheckBox chckbxPresupuestos;
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
	private TextField txtNombreCliente;
	@FXML
	private TextField txtFijo;
	@FXML
	private TextField txtMovil;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtMatricula;
	@FXML
	private DatePicker txtFechaDesde;
	@FXML
	private DatePicker txtFechaHasta;
	@FXML
	private ComboBox<String> comboEstado;

	@FXML
	private TableView<FacturaClienteVehiculo> tableFacturas;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaNombre;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaVehiculo;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaMatricula;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaFecha;
	@FXML
	private TableColumn<FacturaClienteVehiculo, Number> columnaImporte;
	@FXML
	private TableColumn<FacturaClienteVehiculo, String> columnaEstado;

	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();

	private Inicio main;
	private ScrollPane sp;
	private AnchorPane ap;
	private GestorVentana gv;
	private String nombre = "";
	private String estado = "T";

	public Button boton1;
	public Button boton2;
	public Button boton3;

	public void setMainAPP(Inicio p) {
		main = p;
	}

	public void setScrollPane(ScrollPane root) {
		this.sp = root;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Inicializar los checkbox marcados
		chckbxFacturas.setSelected(true);
		chckbxPresupuestos.setSelected(true);
		chckbxOrdenDeReparacion.setSelected(true);
		chckbxResguardoDeposito.setSelected(true);
		txtFechaHasta.setValue(LocalDate.now());
		txtFechaDesde.setValue(txtFechaHasta.getValue().minusMonths(1));

		comboEstado.getItems().addAll("Todos", "Pendiente", "Cobrado");
		comboEstado.setValue("Todos");
		comboEstado.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboEstado(newValue));

		// Para abrir factura con doble click
		tableFacturas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						int selectedIndex = tableFacturas.getSelectionModel().getSelectedIndex();
						if (selectedIndex >= 0) {
							cargarFactura();
						}
					}
				}
			}
		});
		tableFacturas.setTooltip(new Tooltip("Doble click para abrir la factura"));
	}

	/**
	 * Se comprueba el valor elegido en el combo de estado
	 * 
	 * @param valor
	 */
	private void comprobarComboEstado(String valor) {
		if (valor.equalsIgnoreCase("Todos")) {
			estado = "T";
		} else if (valor.equalsIgnoreCase("Pendiente")) {
			estado = "P";
		} else {
			estado = "C";
		}
	}

	/**
	 * Busca las facturas, presupuestos, órdenes o resguardos que coincidan con
	 * los parámetros de búsqueda y pone las encontradas en la tabla
	 */
	@FXML
	private void buscarFacturas() {
		listaFacturas.clear();
		tableFacturas.getItems().clear();
		String numFactura = "";
		String numPresupuesto = "";
		String numOrden = "";
		String numResguardo = "";
		String matricula = txtMatricula.getText();
		matricula.replaceAll("-", "");
		matricula.replaceAll(" ", "");
		matricula.replaceAll("/", "");
		if (!txtNumfactura.getText().isEmpty()) {
			numFactura = txtNumfactura.getText();
		}
		if (!txtNumPresupuesto.getText().isEmpty()) {
			numPresupuesto = txtNumPresupuesto.getText();
		}
		if (!txtNumOrden.getText().isEmpty()) {
			numOrden = txtNumOrden.getText();
		}
		if (!txtNumResguardo.getText().isEmpty()) {
			numResguardo = txtNumResguardo.getText();
		}
		ArrayList<FacturaClienteVehiculo> lista = Inicio.CONEXION.buscarFacturas(chckbxFacturas.isSelected(),
				chckbxPresupuestos.isSelected(), chckbxOrdenDeReparacion.isSelected(),
				chckbxResguardoDeposito.isSelected(), numFactura, numPresupuesto, numOrden, numResguardo,
				txtNombreCliente.getText(), txtModelo.getText(), matricula, txtFijo.getText(), txtMovil.getText(),
				txtFechaDesde.getValue(), txtFechaHasta.getValue(), estado);
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No encontrado",
					"No hay facturas con los parámetros de búsqueda introducidos.");
		} else {
			for (FacturaClienteVehiculo fce : lista) {
				listaFacturas.add(fce);
				columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaVehiculo
						.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
				columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
				columnaFecha.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaPropertyFormat());
				columnaImporte.setCellValueFactory(cellData -> cellData.getValue().getFactura().importeTotalProperty());
				columnaEstado.setCellValueFactory(cellData -> cellData.getValue().getFactura().cobradoLetra());
				tableFacturas.setItems(listaFacturas);
			}
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
