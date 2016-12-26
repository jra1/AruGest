package GUI.ProveedorCompania;

import java.util.ArrayList;
import java.util.Optional;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ProveedorCompaniaDireccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class V_BuscarProveedorCompaniaController {

	// Variables de compañías
	@FXML
	private ImageView logoCia;
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
	private Label lblNombreCia;
	@FXML
	private Label lblCifCia;
	@FXML
	private Label lblTelf1Cia;
	@FXML
	private Label lblTelf2Cia;
	@FXML
	private Label lblDireccionCia;
	@FXML
	private Label lblCPostalCia;
	@FXML
	private Label lblLocalidadCia;
	@FXML
	private Label lblProvinciaCia;
	@FXML
	private Button btnEliminarCia;

	// Variables de proveedores
	@FXML
	private ImageView logoProve;
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
	private TableColumn<ProveedorCompaniaDireccion, String> columnaTelfProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaLocalidadProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaProvinciaProve;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaDireccionProve;
	@FXML
	private Label lblNombreProve;
	@FXML
	private Label lblCifProve;
	@FXML
	private Label lblTelf1Prove;
	@FXML
	private Label lblTelf2Prove;
	@FXML
	private Label lblDireccionProve;
	@FXML
	private Label lblCPostalProve;
	@FXML
	private Label lblLocalidadProve;
	@FXML
	private Label lblProvinciaProve;
	@FXML
	private Button btnEliminarProve;

	// Resto de variables
	private Inicio main;
	public Button boton1;
	public Button boton2;
	public Button boton3;

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
		tableCompania.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarDetallesCia(newValue));

		tableProveedor.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarDetallesProve(newValue));

		// Añadir un listener a los botones de eliminar cía y eliminar proveedor
		btnEliminarCia.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				eliminarCia(0);
			}
		});

		btnEliminarProve.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				eliminarCia(1);
			}
		});
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
				tableProveedor.setItems(listaProveedores);
			}
		}
	}

	/**
	 * Muestra los detalles de la cía seleccionada
	 * 
	 * @param pcd
	 */
	private void mostrarDetallesCia(ProveedorCompaniaDireccion pcd) {
		if (pcd != null) {
			lblNombreCia.setText(pcd.getPc().getNombre());
			lblCifCia.setText(pcd.getPc().getCif());
			lblTelf1Cia.setText(pcd.getPc().getTelf1());
			lblTelf2Cia.setText(pcd.getPc().getTelf2());
			lblDireccionCia.setText(pcd.getDireccion().getDireccionCompleta());
			lblCPostalCia.setText("" + pcd.getDireccion().getCpostal());
			lblLocalidadCia.setText(pcd.getDireccion().getLocalidad());
			lblProvinciaCia.setText(pcd.getDireccion().getProvincia());
			try {
				Image i = Inicio.CONEXION.cargarLogo(pcd.getPc().getIdprovecompa());
				if (i != null) {
					logoCia.setImage(i);
					logoCia.setVisible(true);
				} else {
					logoCia.setVisible(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Utilidades.mostrarAlerta(AlertType.WARNING, "Error", "Error al cargar el logo", "");
			}
		} else {
			lblNombreCia.setText("Selecciona una compañía");
			lblCifCia.setText("-");
			lblTelf1Cia.setText("-");
			lblTelf2Cia.setText("-");
			lblDireccionCia.setText("-");
			lblCPostalCia.setText("-");
			lblLocalidadCia.setText("-");
			lblProvinciaCia.setText("-");
		}
	}

	/**
	 * Muestra los detalles del proveedor seleccionado
	 * 
	 * @param pcd
	 */
	private void mostrarDetallesProve(ProveedorCompaniaDireccion pcd) {
		if (pcd != null) {
			lblNombreProve.setText(pcd.getPc().getNombre());
			lblCifProve.setText(pcd.getPc().getCif());
			lblTelf1Prove.setText(pcd.getPc().getTelf1());
			lblTelf2Prove.setText(pcd.getPc().getTelf2());
			lblDireccionProve.setText(pcd.getDireccion().getDireccionCompleta());
			lblCPostalProve.setText("" + pcd.getDireccion().getCpostal());
			lblLocalidadProve.setText(pcd.getDireccion().getLocalidad());
			lblProvinciaProve.setText(pcd.getDireccion().getProvincia());
			try {
				Image i = Inicio.CONEXION.cargarLogo(pcd.getPc().getIdprovecompa());
				if (i != null) {
					logoProve.setImage(i);
					logoProve.setVisible(true);
				} else {
					logoProve.setVisible(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Utilidades.mostrarAlerta(AlertType.WARNING, "Error", "Error al cargar el logo", e.getMessage());
			}
		} else {
			lblNombreProve.setText("Selecciona un proveedor/desguace");
			lblCifProve.setText("-");
			lblTelf1Prove.setText("-");
			lblTelf2Prove.setText("-");
			lblDireccionProve.setText("-");
			lblCPostalProve.setText("-");
			lblLocalidadProve.setText("-");
			lblProvinciaProve.setText("-");
		}
	}

	/**
	 * Se llama al ventanuco de editar compañía
	 */
	@FXML
	private void editarCia() {
		ProveedorCompaniaDireccion pcd = tableCompania.getSelectionModel().getSelectedItem();
		if (pcd != null) {
			boolean okClicked = Inicio.mostrarEditorCia(pcd, 0);
			if (okClicked) {
				if (Inicio.CONEXION.editarCia(pcd)) {
					Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Atención", "Compañía modificada con éxito", "");
					mostrarDetallesCia(pcd);
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al modificar la compañía",
							"Ocurrió un error al modificar la compañía en la base de datos.");
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ninguna compañía seleccionada",
					"Selecciona la compañía que quieras editar.");
		}
	}

	/**
	 * Se llama al ventanuco de editar proveedor
	 */
	@FXML
	private void editarProve() {
		ProveedorCompaniaDireccion pcd = tableProveedor.getSelectionModel().getSelectedItem();
		if (pcd != null) {
			int tipo = 1; // 1-Proveedor, 2-Desguace
			if (pcd.getPc().isEsdesguace()) {
				tipo = 2;
			} else {
				tipo = 1;
			}
			boolean okClicked = Inicio.mostrarEditorCia(pcd, tipo);
			if (okClicked) {
				if (Inicio.CONEXION.editarCia(pcd)) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Compañía modificada con éxito", "");
					mostrarDetallesProve(pcd);
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al modificar la compañía",
							"Ocurrió un error al modificar la compañía en la base de datos.");
				}

			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún proveedor/desguace seleccionado",
					"Selecciona el proveedor/desguace que quieras editar.");
		}
	}

	private void eliminarCia(int tipo) { // 0-cía, 1-proveedor/desguace
		ProveedorCompaniaDireccion pcd = null;
		String nombre = "";
		int selectedIndex = -1;
		if (tipo == 0) {
			selectedIndex = tableCompania.getSelectionModel().getSelectedIndex();
			pcd = tableCompania.getSelectionModel().getSelectedItem();
			nombre = "compañía";
		} else if (tipo == 1) {
			selectedIndex = tableProveedor.getSelectionModel().getSelectedIndex();
			pcd = tableProveedor.getSelectionModel().getSelectedItem();
			nombre = "proveedor/desguace";
		}
		if (selectedIndex >= 0) {
			Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Eliminar " + nombre,
					"¿Está seguro que quiere eliminar " + pcd.getPc().getNombre() + "?", "");
			if (result.get() == ButtonType.OK) {
				if (Inicio.CONEXION.eliminarCia(pcd.getPc().getIdprovecompa(), pcd.getDireccion().getIddireccion())) {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Compañía eliminada", "");
					if (tipo == 0) {
						tableCompania.getItems().remove(selectedIndex);
						tableCompania.getSelectionModel().clearSelection();
						logoCia.setVisible(false);
					} else if (tipo == 1) {
						tableProveedor.getItems().remove(selectedIndex);
						tableProveedor.getSelectionModel().clearSelection();
						logoProve.setVisible(false);
					}
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar la compañía",
							"Ocurrió un error al eliminar la compañía de la base de datos.");
				}
			}
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ninguna compañía seleccionada",
					"Selecciona la compañía que quieras eliminar.");
		}
	}

	@FXML
	private void cargarLogoCia() {
		// Image i = Inicio.CONEXION.cargarLogo();
		// if (i != null) {
		// logoCia.setImage(i);
		// } else {
		// Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al cargar
		// la
		// imagen", "");
		// }
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
