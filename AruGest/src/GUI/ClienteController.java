package GUI;

import java.util.Optional;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ClienteController {
	// Datos cliente
	@FXML
	private ComboBox<String> comboTipoCliente;
	@FXML
	private TextField txtNombre;
	@FXML
	private Label lblApellidos;
	@FXML
	private TextField txtApellidos;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtTelf1;
	@FXML
	private TextField txtTelf2;
	@FXML
	private TextField txtTelf3;
	@FXML
	private TextField txtCalle;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtPiso;
	@FXML
	private TextField txtLetra;
	@FXML
	private TextField txtCPostal;
	@FXML
	private TextField txtPoblacion;
	@FXML
	private TextField txtProvincia;
	@FXML
	private Button btnGuardar;
	@FXML
	private Button btnEliminarCliente;

	// Datos Vehiculo
	@FXML
	private TableView<Vehiculo> tableVehiculo;
	@FXML
	private TableColumn<Vehiculo, String> columnaMarca;
	@FXML
	private TableColumn<Vehiculo, String> columnaMatricula;

	@FXML
	private Label lblTipoVehiculo;
	@FXML
	private Label lblMatricula;
	@FXML
	private Label lblMarca;
	@FXML
	private Label lblModelo;
	@FXML
	private Label lblVersion;
	@FXML
	private Label lblAnio;
	@FXML
	private Label lblBastidor;
	@FXML
	private Label lblLetrasMotor;
	@FXML
	private Label lblColor;
	@FXML
	private Label lblCodRadio;

	private Inicio main;

	private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	/**
	 * Carga una factura
	 */
	public void cargaCliente(ClienteParticularEmpresaDireccion cped) {
		btnGuardar.setVisible(false);
		Inicio.CLIENTE_ID = cped.getCliente().getIdcliente();

		// Cargar datos cliente
		if (cped.getParticular() != null) {
			comboTipoCliente.setValue("Particular");
			txtDni.setText(cped.getParticular().getNif());
			txtNombre.setText(cped.getParticular().getNombre());
			lblApellidos.setVisible(true);
			txtApellidos.setVisible(true);
			txtApellidos.setText(cped.getParticular().getApellidos());
		} else {
			if (cped.getEmpresa() != null) {
				comboTipoCliente.setValue("Empresa");
				txtDni.setText(cped.getEmpresa().getCif());
				txtNombre.setText(cped.getEmpresa().getNombre());
				lblApellidos.setVisible(false);
				txtApellidos.setVisible(false);
			}
		}
		if (cped.getCliente().getDireccionID() != 0) {
			txtCalle.setText(cped.getDireccion().getCalle());
			txtNumero.setText("" + cped.getDireccion().getNumero());
			txtPiso.setText(cped.getDireccion().getPiso());
			txtLetra.setText(cped.getDireccion().getLetra());
			txtCPostal.setText("" + cped.getDireccion().getCpostal());
			txtPoblacion.setText(cped.getDireccion().getLocalidad());
			txtProvincia.setText(cped.getDireccion().getProvincia());
		}
		txtTelf1.setText(cped.getCliente().getTelf1());
		txtTelf2.setText(cped.getCliente().getTelf2());
		txtTelf3.setText(cped.getCliente().getTelf3());

		// CARGAR TABLA DE VEHICULOS
		listaVehiculos = Inicio.CONEXION.buscarVehiculosPorClienteID(Inicio.CLIENTE_ID);
		columnaMarca.setCellValueFactory(cellData -> cellData.getValue().marcaModeloProperty());
		columnaMatricula.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
		tableVehiculo.setItems(listaVehiculos);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		btnGuardar.setVisible(true);
		comboTipoCliente.getItems().addAll("Particular", "Empresa");
		comboTipoCliente.setValue("Particular");

		tableVehiculo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarDetallesVehiculo(newValue));

		/*
		 * comboTipoCliente.getSelectionModel().selectedItemProperty()
		 * .addListener((observable, oldValue, newValue) ->
		 * comprobarComboTipoCliente(newValue));
		 * 
		 * comboTipoVehiculo.getSelectionModel().selectedItemProperty()
		 * .addListener((observable, oldValue, newValue) ->
		 * comprobarComboTipoVehiculo(newValue));
		 */
	}

	/**
	 * Muestra los detalles del vehículo seleccionado Si el vehiculo es null, se
	 * limpian los campos.
	 * 
	 * @param vehiculo
	 *            o null
	 */
	private void mostrarDetallesVehiculo(Vehiculo v) {
		if (v != null) {
			lblTipoVehiculo.setText(Utilidades.tipoIDtoString(v.getTipoID()));
			lblMatricula.setText(v.getMatricula());
			lblMarca.setText(v.getMarca());
			lblModelo.setText(v.getModelo());
			lblVersion.setText(v.getVersion());
			lblAnio.setText("" + v.getAnio());
			lblBastidor.setText(v.getBastidor());
			lblLetrasMotor.setText(v.getLetrasmotor());
			lblColor.setText(v.getColor());
			lblCodRadio.setText(v.getCodradio());
		} else {
			lblTipoVehiculo.setText("Selecciona un vehículo");
			lblMatricula.setText("");
			lblMarca.setText("");
			lblModelo.setText("");
			lblVersion.setText("");
			lblAnio.setText("");
			lblBastidor.setText("");
			lblLetrasMotor.setText("");
			lblColor.setText("");
			lblCodRadio.setText("");
		}
	}

	/**
	 * Se llama cuando el usuario pulsa en Añadir vehículo
	 */
	@FXML
	private void nuevoVehiculo() {
		Vehiculo v = new Vehiculo(Inicio.CLIENTE_ID);
		boolean okClicked = Inicio.mostrarEditorVehiculo(v);
		if (okClicked) {
			//Cuando llega aqui son correctos los datos introducidos
			if(Inicio.CONEXION.guardarVehiculo(v)){
				listaVehiculos.add(v);
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error al guardar el vehículo");
				alert.setContentText("Ocurrió un error al guardar el vehículo en la base de datos.");

				alert.showAndWait();
			}
		}
	}
	
	/**
     * Se llama cuando el usuario pulsa en Editar vehículo
     */
    @FXML
    private void editarVehiculo() {
        Vehiculo v = tableVehiculo.getSelectionModel().getSelectedItem();
        if (v != null) {
            boolean okClicked = Inicio.mostrarEditorVehiculo(v);
            if (okClicked) {
            	//EDITAR EN LA BD
                mostrarDetallesVehiculo(v);
            }

        } else {
            // Si no ha seleccionado un vehículo de la tabla
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Atención");
			alert.setHeaderText("Ningún vehículo seleccionado");
			alert.setContentText("Selecciona el vehículo que quieras editar.");

        	alert.showAndWait();
        }
    }
    
    /**
     * Se llama cuando el usuario pulsa en Eliminar vehículo
     */
    @FXML
    private void eliminarVehiculo() {
        int selectedIndex = tableVehiculo.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            //BORRAR DE LA BASE DE DATOS
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Eliminar vehículo");
        	alert.setHeaderText("¿Estás seguro que quieres eliminar este vehículo?");
        	//alert.setContentText("Are you ok with this?");

        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        		if(Inicio.CONEXION.eliminarVehiculo(tableVehiculo.getSelectionModel().getSelectedItem().getIdvehiculo())){
        			tableVehiculo.getItems().remove(selectedIndex);
        		}else{
        			alert = new Alert(AlertType.ERROR);
        			alert.setTitle("Error");
        			alert.setHeaderText("Error al eliminar el vehículo");
        			alert.setContentText("Ocurrió un error al eliminar el vehículo de la base de datos.");
        			
        			alert.showAndWait();
        		}
        	} else {
        	    // ... user chose CANCEL or closed the dialog
        	}
        } else {
            // Nothing selected.
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Atención");
			alert.setHeaderText("Ningún vehículo seleccionado");
			alert.setContentText("Selecciona el vehículo que quieras eliminar.");

        	alert.showAndWait();
        }
    }
}
