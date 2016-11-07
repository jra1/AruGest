package GUI;

import java.util.ArrayList;

import Logica.Inicio;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.FacturaClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class BuscarClienteController {

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
	private int tipoCliente = 1; //1=Particular, 2=Empresa
	
	private Inicio main;
	
	public void setMainAPP(Inicio p){
	     main=p;
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize(){
		comboTipoCliente.getItems().addAll("Particular", "Empresa");
		comboTipoCliente.setValue("Particular");
		comboTipoCliente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> comprobarComboTipoCliente(newValue));
	}
	
	/**
	 * Se comprueba el valor elegido en el combo de tipo cliente
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
	 * Busca los clientes que coincidan con
	 * los parámetros de búsqueda y pone los encontrados en la tabla
	 */
	@FXML
	private void buscarCliente() {
		listaClientes.clear();
		tableClientes.getItems().clear();
		ArrayList<ClienteParticularEmpresaDireccion> lista = Inicio.CONEXION.buscarClientes(tipoCliente, txtNombre.getText(), txtModelo.getText(), txtMatricula.getText(),
				txtDni.getText(), txtFijo.getText(), txtMovil.getText(), txtDomicilio.getText(), txtPoblacion.getText());
		if (lista.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("No encontrado");
			alert.setContentText("No hay clientes con los parámetros de búsqueda introducidos.");

			alert.showAndWait();
		} else {
			/*Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Encontrado!");
			alert.setContentText("SE HA ENCONTRADO ALGO!");
			alert.showAndWait();
			*/
			for(ClienteParticularEmpresaDireccion cpe : lista){
				
				listaClientes.add(cpe);
				columnaNombre.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				columnaDni.setCellValueFactory(cellData -> cellData.getValue().getParticular().nifProperty());
				columnaDomicilio.setCellValueFactory(cellData -> cellData.getValue().getDireccion().calleProperty());
				columnaPoblacion.setCellValueFactory(cellData -> cellData.getValue().getDireccion().localidadProperty());
				columnaTelf.setCellValueFactory(cellData -> cellData.getValue().getCliente().telf1Property());
				tableClientes.setItems(listaClientes);
			}
		}
	}
}
