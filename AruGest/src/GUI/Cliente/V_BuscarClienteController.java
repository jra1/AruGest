package GUI.Cliente;

import java.io.IOException;
import java.util.ArrayList;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.GestorVentana;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class V_BuscarClienteController {
	@FXML
	private ComboBox<String> comboTipoCliente;
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtFijo;
	@FXML
	private TextField txtMovil;
	@FXML
	private TextField txtDomicilio;
	@FXML
	private TextField txtPoblacion;
	@FXML
	private Button btnBuscar;

	@FXML
	private TableView<ClienteParticularEmpresaDireccion> tableClientes;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccion, String> columnaNombre;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccion, String> columnaDni;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccion, String> columnaDomicilio;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccion, String> columnaPoblacion;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccion, String> columnaTelf;

	@FXML
	private Button btnVerCliente;
	@FXML
	private Button btnHacerFactura;

	private ObservableList<ClienteParticularEmpresaDireccion> listaClientes = FXCollections.observableArrayList();
	private int tipoCliente = 1; // 1=Particular, 2=Empresa

	public Button boton1;
	public Button boton2;
	public Button boton3;

	private Inicio main;
	private ScrollPane sp;
	private AnchorPane ap;
	private GestorVentana gv;
	private String nombre = "";

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
		// btnBuscar.defaultButtonProperty().bind(btnBuscar.focusedProperty());
		btnBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.F12 || ke.getCode() == KeyCode.ENTER) {
					buscarCliente();
				}
			}
		});
		comboTipoCliente.getItems().addAll("Particular", "Empresa");
		comboTipoCliente.setValue("Particular");
		comboTipoCliente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipoCliente(newValue));
	}

	public void setFocus() {
		txtModelo.requestFocus();
	}

	/**
	 * Se comprueba el valor elegido en el combo de tipo cliente
	 * 
	 * @param valor
	 */
	private void comprobarComboTipoCliente(String valor) {
		if (valor.equalsIgnoreCase("Empresa")) {
			tipoCliente = 2;
		} else {
			tipoCliente = 1;
		}
	}

	/**
	 * Busca los clientes que coincidan con los parámetros de búsqueda y pone
	 * los encontrados en la tabla
	 */
	@FXML
	private void buscarCliente() {
		listaClientes.clear();
		tableClientes.getItems().clear();
		ArrayList<ClienteParticularEmpresaDireccion> lista = Inicio.CONEXION.buscarClientes(tipoCliente,
				txtNombre.getText(), txtModelo.getText(), txtMatricula.getText(), txtDni.getText(), txtFijo.getText(),
				txtMovil.getText(), txtDomicilio.getText(), txtPoblacion.getText());
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No encontrado",
					"No hay clientes con los parámetros de búsqueda introducidos.");
		} else {
			for (ClienteParticularEmpresaDireccion cpe : lista) {
				listaClientes.add(cpe);
				columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				if (cpe.getParticular() != null) {
					columnaDni.setCellValueFactory(cellData -> cellData.getValue().getParticular().nifProperty());
				} else if (cpe.getEmpresa() != null) {
					columnaDni.setCellValueFactory(cellData -> cellData.getValue().getEmpresa().cifProperty());
				}
				columnaDomicilio.setCellValueFactory(cellData -> cellData.getValue().getDireccion().calleProperty());
				columnaPoblacion
						.setCellValueFactory(cellData -> cellData.getValue().getDireccion().localidadProperty());
				columnaTelf.setCellValueFactory(cellData -> cellData.getValue().getCliente().telf1Property());
				tableClientes.setItems(listaClientes);
				if (listaClientes.size() > 0) {
					btnVerCliente.setDisable(false);
				}
			}
		}
	}

	/**
	 * Función para cargar el cliente seleccionado
	 */
	@FXML
	private void cargarCliente() {
		int selectedIndex = tableClientes.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Inicio.CLIENTE_ID = listaClientes.get(selectedIndex).getCliente().getIdcliente();
			try {
				// Cargar la vista de Cliente
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Inicio.class.getResource("/GUI/Cliente/V_Cliente.fxml"));
				AnchorPane cliente = (AnchorPane) loader.load();

				// Poner la nueva vista en el centro del root
				Utilidades.ajustarResolucionAnchorPane(cliente, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
				sp.setContent(cliente);
				nombre = "Cliente: " + listaClientes.get(selectedIndex).getCliente().getNombre();
				ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
				gv = new GestorVentana(ap, nombre);
				Utilidades.gestionarPantallas(gv);
				boton1.setVisible(Inicio.BOTON1.isVisible());
				boton1.setText(Inicio.BOTON1.getNombre());
				boton2.setVisible(Inicio.BOTON2.isVisible());
				boton2.setText(Inicio.BOTON2.getNombre());
				boton3.setVisible(Inicio.BOTON3.isVisible());
				boton3.setText(Inicio.BOTON3.getNombre());

				// main.getRoot().setCenter(cliente);

				// Poner el controlador de la nueva vista.
				V_ClienteController controller = loader.getController();
				controller.setMainAPP(main);
				controller.setScrollPane(sp);
				controller.boton1 = boton1;
				controller.boton2 = boton2;
				controller.boton3 = boton3;
				controller.cargaCliente(listaClientes.get(selectedIndex));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún cliente seleccionado",
					"Selecciona el cliente que quieras cargar.");
		}
	}
}
