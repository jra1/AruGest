package GUI.ProveedorCompania;

import java.sql.Blob;

import Logica.Inicio;
import Modelo.ProveedorCompania;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class V_BuscarProveedorCompaniaController {

	// Variables de compañías
	@FXML
	private TextField txtNombreCompa;
	@FXML
	private TextField txtTelfCompa;
	@FXML
	private TableView<ProveedorCompania> tableCompania;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaNombreCompa;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaCifCompa;
	@FXML
	private TableColumn<ProveedorCompania, Number> columnaTelfCompa;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaDireccionCompa;
	@FXML
	private TableColumn<ProveedorCompania, Blob> columnaLogoCompa;

	// Variables de compañías
	@FXML
	private TextField txtNombreProve;
	@FXML
	private TextField txtLocalidadProve;
	@FXML
	private TextField txtProvinciaProve;
	@FXML
	private TextField txtTelfProve;
	@FXML
	private TableView<ProveedorCompania> tableProveedor;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaNombreProve;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaCifProve;
	@FXML
	private TableColumn<ProveedorCompania, Number> columnaTelfProve;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaLocalidadProve;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaProvinciaProve;
	@FXML
	private TableColumn<ProveedorCompania, String> columnaDireccionProve;
	@FXML
	private TableColumn<ProveedorCompania, Blob> columnaLogoProve;

	// Resto de variables
	private Inicio main;
	/*
	 * private ScrollPane sp; private AnchorPane ap; private GestorVentana gv;
	 * private String nombre = "";
	 * 
	 * public Button boton1; public Button boton2; public Button boton3;
	 */

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/*
	 * public void setScrollPane(ScrollPane root) { this.sp = root; }
	 */

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * Funciones que se llamen desde la vista, con @FXML delante
	 */
	@FXML
	private void buscarCompania() {
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
