package GUI.Cliente;

import java.io.IOException;
import java.util.ArrayList;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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

	private Inicio main;

	public void setMainAPP(Inicio p) {
		main = p;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		comboTipoCliente.getItems().addAll("Particular", "Empresa");
		comboTipoCliente.setValue("Particular");
		comboTipoCliente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipoCliente(newValue));
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
				if(cpe.getParticular() != null){
					columnaDni.setCellValueFactory(cellData -> cellData.getValue().getParticular().nifProperty());					
				}else if(cpe.getEmpresa() != null){
					columnaDni.setCellValueFactory(cellData -> cellData.getValue().getEmpresa().cifProperty());
				}
				columnaDomicilio.setCellValueFactory(cellData -> cellData.getValue().getDireccion().calleProperty());
				columnaPoblacion
						.setCellValueFactory(cellData -> cellData.getValue().getDireccion().localidadProperty());
				columnaTelf.setCellValueFactory(cellData -> cellData.getValue().getCliente().telf1Property());
				tableClientes.setItems(listaClientes);
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
				main.getRoot().setCenter(cliente);

				// Poner el controlador de la nueva vista.
				V_ClienteController controller = loader.getController();
				controller.setMainAPP(main);
				controller.cargaCliente(listaClientes.get(selectedIndex));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Nada seleccionado.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Ningún cliente seleccionado");
			alert.setContentText("Selecciona el cliente que quieras cargar.");

			alert.showAndWait();
		}
	}
}
