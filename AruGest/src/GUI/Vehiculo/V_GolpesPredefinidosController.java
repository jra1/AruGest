package GUI.Vehiculo;

import java.util.ArrayList;
import java.util.Optional;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ElementosGolpes;
import Modelo.Golpe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class V_GolpesPredefinidosController {

	// Variables de la vista
	@FXML
	private TableView<Golpe> tableGolpes;
	@FXML
	private TableColumn<Golpe, Number> columnaIDGolpe;
	@FXML
	private TableColumn<Golpe, String> columnaNombreGolpe;

	@FXML
	private TextField txtNombreGolpe;
	@FXML
	private TextField txtNombreElemento;
	@FXML
	private ComboBox<String> comboTipoElemento;
	@FXML
	private Button btnAddElemento;
	@FXML
	private Button btnEditarGolpe;
	@FXML
	private Button btnNuevoGolpe;
	@FXML
	private Button btnGuardarGolpe;

	@FXML
	private TableView<ElementosGolpes> tableElementos;
	@FXML
	private TableColumn<ElementosGolpes, String> columnaNombreElemento;
	@FXML
	private TableColumn<ElementosGolpes, String> columnaTipoElemento;

	// Resto de variables
	private Inicio main;
	public Button boton1;
	public Button boton2;
	public Button boton3;

	private ObservableList<Golpe> listaGolpes = FXCollections.observableArrayList();
	private ObservableList<ElementosGolpes> listaElementos = FXCollections.observableArrayList();

	private String modo = ""; // Agregar ó editar

	private int GOLPEID = 0;

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Cargar golpes
		cargarGolpes();

		// Listener para cargar los elementos del golpe seleccionado
		tableGolpes.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarElementosGolpe(newValue));

		comboTipoElemento.getItems().addAll("Material", "Chapa", "Pintura", "Electrónica / mecánica");
		comboTipoElemento.setValue("Material");
	}

	/**
	 * Carga en la tabla de golpes los que hay en la BD
	 */
	private void cargarGolpes() {
		listaGolpes.clear();
		tableGolpes.getItems().clear();
		listaGolpes = Inicio.CONEXION.buscarGolpes();
		if (!listaGolpes.isEmpty()) {
			columnaIDGolpe.setCellValueFactory(cellData -> cellData.getValue().idgolpeProperty());
			columnaNombreGolpe.setCellValueFactory(cellData -> cellData.getValue().nombreGolpeProperty());
			tableGolpes.setItems(listaGolpes);
		}
	}

	/**
	 * Carga en la tabla correspondiente los elementos que contiene el golpe
	 * seleccionado
	 * 
	 * @param id
	 *            del golpe seleccionado
	 */
	private void mostrarElementosGolpe(Golpe g) {
		listaElementos.clear();
		tableElementos.getItems().clear();
		camposDeshabilitados(true);
		if (g != null) {
			setGOLPEID(g.getIdgolpe());
		}
		ArrayList<ElementosGolpes> lista = Inicio.CONEXION.buscarElementosPorGolpeID(g.getIdgolpe());
		if (!lista.isEmpty()) {
			for (ElementosGolpes e : lista) {
				listaElementos.add(e);
				columnaNombreElemento.setCellValueFactory(cellData -> cellData.getValue().nombreElementoProperty());
				columnaTipoElemento.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
				tableElementos.setItems(listaElementos);
			}
		}

		txtNombreGolpe.setText(g.getNombreGolpe());
	}

	/**
	 * Pone los campos editables y la variable correspondiente en EDITAR
	 */
	@FXML
	private void editarGolpe() {
		int selectedIndex = tableGolpes.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			modo = "EDITAR";
			camposDeshabilitados(false);
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún golpe seleccionado",
					"Selecciona en la tabla el golpe que editar.");
		}
	}

	/**
	 * Pone los campos editables y la variable correspondiente en AGREGAR
	 */
	@FXML
	private void nuevoGolpe() {
		modo = "AGREGAR";
		camposDeshabilitados(false);
		txtNombreGolpe.setText("");
		txtNombreElemento.setText("");
		listaElementos.clear();
		tableElementos.getItems().clear();
	}

	/**
	 * Pone los campos modificables
	 * 
	 * @param modificable
	 */
	private void camposDeshabilitados(boolean modificable) {
		txtNombreGolpe.setDisable(modificable);
		txtNombreElemento.setDisable(modificable);
		comboTipoElemento.setDisable(modificable);
		btnAddElemento.setDisable(modificable);
		btnGuardarGolpe.setDisable(modificable);
	}

	/**
	 * Añade un elemento a la tabla correspondiente
	 */
	@FXML
	private void addElemento() {
		ElementosGolpes e = null;
		if (txtNombreElemento.getText().isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Debe introducir un nombre de elemento",
					"Introduzca el nombre del elemento a añadir.");
		} else {
			if (modo.equalsIgnoreCase("AGREGAR")) {
				e = new ElementosGolpes(0, txtNombreElemento.getText(), comboTipoElemento.getValue(), 0);
			} else if (modo.equalsIgnoreCase("EDITAR")) {
				e = new ElementosGolpes(0, txtNombreElemento.getText(), comboTipoElemento.getValue(), getGOLPEID());
			}
			listaElementos.add(e);
			columnaNombreElemento.setCellValueFactory(cellData -> cellData.getValue().nombreElementoProperty());
			columnaTipoElemento.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
			tableElementos.setItems(listaElementos);

			txtNombreElemento.setText("");
			txtNombreElemento.requestFocus();
		}
	}

	/**
	 * Guarda o edita el golpe
	 */
	@FXML
	private void guardarGolpe() {
		if (modo.equalsIgnoreCase("AGREGAR")) {
			modo = "";
			if (txtNombreGolpe.getText().isEmpty()) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Debe introducir el nombre del golpe",
						"Introduzca el nombre del golpe a guardar.");
			} else {
				if (tableElementos.getItems().isEmpty()) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
							"Debe introducir algún elemento del golpe",
							"Introduzca al menos un elemento del golpe a guardar.");
				} else {
					Golpe g = new Golpe(0, txtNombreGolpe.getText());
					if (Inicio.CONEXION.guardarGolpe(g, tableElementos.getItems())) {
						Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Golpe guardado en la base de datos.",
								"");
						cargarGolpes();
					} else {
						Utilidades.mostrarAlerta(AlertType.ERROR, "Error",
								"Ocurrió un error al guardar el golpe en la base de datos.", "");
					}
				}
			}
		} else if (modo.equalsIgnoreCase("EDITAR")) {
			if (txtNombreGolpe.getText().isEmpty()) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Debe introducir el nombre del golpe",
						"Introduzca el nombre del golpe a guardar.");
			} else {
				if (tableElementos.getItems().isEmpty()) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
							"Debe introducir algún elemento del golpe",
							"Introduzca al menos un elemento del golpe a guardar.");
				} else {
					Golpe g = new Golpe(getGOLPEID(), txtNombreGolpe.getText());
					if (Inicio.CONEXION.editarGolpe(getGOLPEID(), g, tableElementos.getItems())) {
						Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Golpe guardado en la base de datos.",
								"");
						cargarGolpes();
					} else {
						Utilidades.mostrarAlerta(AlertType.ERROR, "Error",
								"Ocurrió un error al guardar el golpe en la base de datos.", "");
					}
				}
			}
		}

		camposDeshabilitados(true);
	}

	/**
	 * Elimina el golpe seleccionado
	 */
	@FXML
	private void eliminarGolpe() {
		int selectedIndex = tableGolpes.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar golpe",
					"¿Estáa seguro que quiere eliminar este golpe?", "");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION.eliminarGolpe(getGOLPEID())) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Golpe eliminado de la base de datos.",
							"");
					cargarGolpes();
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error",
							"Ocurrió un error al eliminar el golpe de la base de datos.", "");
				}
			}
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún golpe seleccionado",
					"Selecciona en la tabla el golpe que quieras eliminar.");
		}
	}

	public void setFocus() {
		tableGolpes.requestFocus();
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}

	public int getGOLPEID() {
		return GOLPEID;
	}

	public void setGOLPEID(int gOLPEID) {
		GOLPEID = gOLPEID;
	}
}
