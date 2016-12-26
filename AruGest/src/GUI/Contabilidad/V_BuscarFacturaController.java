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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Clase controlador de la ventana BuscarFactura, que se utiliza para buscar en
 * la base de datos las facturas que coincidan con los par�metros que introduce
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

	private ObservableList<FacturaClienteVehiculo> listaFacturas = FXCollections.observableArrayList();

	private Inicio main;
	private ScrollPane sp;
	private AnchorPane ap;
	private GestorVentana gv;
	private String nombre = "";

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
		txtFechaDesde.setValue(txtFechaHasta.getValue().minusDays(7));
	}

	/**
	 * Busca las facturas, presupuestos, �rdenes o resguardos que coincidan con
	 * los par�metros de b�squeda y pone las encontradas en la tabla
	 */
	@FXML
	private void buscarFacturas() {
		listaFacturas.clear();
		tableFacturas.getItems().clear();
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
		ArrayList<FacturaClienteVehiculo> lista = Inicio.CONEXION.buscarFacturas(numFactura, numPresupuesto, numOrden,
				numResguardo, txtNombreCliente.getText(), txtModelo.getText(), txtMatricula.getText(),
				txtFijo.getText(), txtMovil.getText(), txtFechaDesde.getValue(), txtFechaHasta.getValue());
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "No encontrado",
					"No hay facturas con los par�metros de b�squeda introducidos.");
		} else {
			for (FacturaClienteVehiculo fce : lista) {
				listaFacturas.add(fce);
				columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaVehiculo
						.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().marcaModeloProperty());
				columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().getVehiculo().matriculaProperty());
				columnaFecha.setCellValueFactory(cellData -> cellData.getValue().getFactura().fechaPropertyFormat());
				columnaImporte.setCellValueFactory(cellData -> cellData.getValue().getFactura().importeTotalProperty());
				tableFacturas.setItems(listaFacturas);
			}
		}
	}

	/**
	 * Funci�n para cargar la factura seleccionada
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
				// Esta l�nea es para que se ejecute la pseudoclase del CSS ya
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
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atenci�n", "Ninguna factura seleccionada",
					"Selecciona la factura que quieras cargar");
		}
	}
}
