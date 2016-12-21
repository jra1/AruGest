package GUI.ProveedorCompania;

import java.sql.Blob;
import java.util.ArrayList;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ProveedorCompaniaDireccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
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
	private TableView<ProveedorCompaniaDireccion> tableCompania;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaNombreCompa;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaCifCompa;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaTelfCompa;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaDireccionCompa;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, Blob> columnaLogoCompa;

	// Variables de proveedores
	@FXML
	private TextField txtNombreProve;
	@FXML
	private TextField txtLocalidadProve;
	@FXML
	private TextField txtProvinciaProve;
	@FXML
	private TextField txtTelfProve;
	@FXML
	private CheckBox esDesguace;
	@FXML
	private TableView<ProveedorCompaniaDireccion> tableProveedor;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaNombreProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaCifProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaTelfProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaLocalidadProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaProvinciaProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaDireccionProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, Blob> columnaLogoProve;

	// Resto de variables
	private Inicio main;

	private ObservableList<ProveedorCompaniaDireccion> listaCias = FXCollections.observableArrayList();
	private ObservableList<ProveedorCompaniaDireccion> listaProveedores = FXCollections.observableArrayList();
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
	 * Busca las cías que coincidan con los parámetros introducidos
	 */
	@FXML
	private void buscarCompania() {
		listaCias.clear();
		tableCompania.getItems().clear();
		ArrayList<ProveedorCompaniaDireccion> lista = Inicio.CONEXION.buscarCias(true, false, txtNombreCompa.getText(),
				txtTelfCompa.getText());
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No encontrado",
					"No hay compañías con los parámetros de búsqueda introducidos.");
		} else {
			for (ProveedorCompaniaDireccion pcd : lista) {
				listaCias.add(pcd);
				columnaNombreCompa.setCellValueFactory(cellData -> cellData.getValue().getPc().nombreProperty());
				columnaCifCompa.setCellValueFactory(cellData -> cellData.getValue().getPc().cifProperty());
				columnaTelfCompa.setCellValueFactory(cellData -> cellData.getValue().getPc().telf1Property());
				if (pcd.getDireccion().getIddireccion() == 0) {
					columnaDireccionCompa
							.setCellValueFactory(cellData -> cellData.getValue().getDireccion().calleProperty());
				} else {
					columnaDireccionCompa.setCellValueFactory(
							cellData -> cellData.getValue().getDireccion().direccionCompletaProperty());
				}
				columnaLogoCompa.setCellValueFactory(cellData -> cellData.getValue().getPc().logoProperty());
				tableCompania.setItems(listaCias);
			}
		}
	}

	/**
	 * Busca los proveedores que coincidan con los parámetros introducidos
	 */
	@FXML
	private void buscarProve() {
		listaProveedores.clear();
		tableProveedor.getItems().clear();
		ArrayList<ProveedorCompaniaDireccion> lista = Inicio.CONEXION.buscarCias(false, esDesguace.isSelected(),
				txtNombreProve.getText(), txtTelfProve.getText());
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No encontrado",
					"No hay proveedores/desguaces con los parámetros de búsqueda introducidos.");
		} else {
			for (ProveedorCompaniaDireccion pcd : lista) {
				listaProveedores.add(pcd);
				columnaNombreProve.setCellValueFactory(cellData -> cellData.getValue().getPc().nombreProperty());
				columnaCifProve.setCellValueFactory(cellData -> cellData.getValue().getPc().cifProperty());
				columnaTelfProve.setCellValueFactory(cellData -> cellData.getValue().getPc().telf1Property());
				columnaLocalidadProve
						.setCellValueFactory(cellData -> cellData.getValue().getDireccion().localidadProperty());
				columnaProvinciaProve
						.setCellValueFactory(cellData -> cellData.getValue().getDireccion().provinciaProperty());
				if (pcd.getDireccion().getIddireccion() == 0) {
					columnaDireccionProve
							.setCellValueFactory(cellData -> cellData.getValue().getDireccion().calleProperty());
				} else {
					columnaDireccionProve.setCellValueFactory(
							cellData -> cellData.getValue().getDireccion().direccionCompletaProperty());
				}
				columnaLogoProve.setCellValueFactory(cellData -> cellData.getValue().getPc().logoProperty());
				tableProveedor.setItems(listaProveedores);
			}
		}
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
