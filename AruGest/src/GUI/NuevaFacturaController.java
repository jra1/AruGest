package GUI;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import Logica.Inicio;
import Modelo.Material;
import Modelo.Servicio;
import javafx.beans.property.FloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Joseba
 *
 */
public class NuevaFacturaController {

	// Datos Factura
	@FXML
	private CheckBox chckbxFactura;
	@FXML
	private TextField txtNumfactura;

	@FXML
	private CheckBox chckbxPresupuesto;
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
	private DatePicker txtFecha;

	// Datos cliente
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtDireccion;
	@FXML
	private TextField txtPoblacion;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtFijo;
	@FXML
	private TextField txtMovil;

	// Datos Vehiculo
	@FXML
	private TextField txtTipo;
	@FXML
	private TextField txtMarcaModelo;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtKms;

	// Servicios y materiales
	@FXML
	private ComboBox<String> comboTipo;
	@FXML
	private TextArea txtConcepto;
	@FXML
	private Label lblCantidad;
	@FXML
	private TextField txtCantidad;
	@FXML
	private Label lblValor;
	@FXML
	private TextField txtValor;
	@FXML
	private Button btnAdd;

	@FXML
	private TableView<Servicio> tableServicio;
	@FXML
	private TableColumn<Servicio, String> columnaConceptoServ;
	@FXML
	private TableColumn<Servicio, Number> columnaHorasServ;
	@FXML
	private Button btnQuitarServ;

	@FXML
	private TableView<Material> tableMaterial;
	@FXML
	private TableColumn<Material, String> columnaConceptoMat;
	@FXML
	private TableColumn<Material, Integer> columnaCantidadMat;
	@FXML
	private TableColumn<Material, Float> columnaPrecioMat;
	@FXML
	private Button btnQuitarMat;

	@FXML
	private TextField txtManoObra;
	@FXML
	private TextField txtMateriales;
	@FXML
	private TextField txtOtros;
	@FXML
	private TextField txtSubtotal;
	@FXML
	private Label lblIva;
	@FXML
	private TextField txtIva;
	@FXML
	private TextField txtTotal;
	@FXML
	private Button btnActualizarPrecio;
	@FXML
	private Button btnGenerarFactura;

	@FXML
	private CheckBox chckbxRepararDefOcultos;
	@FXML
	private TextField txtPorcentajeDefOcultos;
	@FXML
	private CheckBox chckbxPermisoPruebas;
	@FXML
	private CheckBox chckbxNoPiezas;
	@FXML
	private DatePicker txtFechaEntrega;

	/**
	 * ObservableLists para los servicios y materiales.
	 */
	private ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
	private ObservableList<Material> listaMaterial = FXCollections.observableArrayList();
	/*
	 * private ArrayList<Servicio> listaServicios = new ArrayList<Servicio>();
	 * private ArrayList<Material> listaMaterial = new ArrayList<Material>();
	 */
	private Inicio main;

	private boolean esServicio = false; // Variable para controlar si es un
										// servicio o un material

	private Servicio servicio;
	private Material material;

	public void setMainAPP(Inicio p) {
		main = p;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		tableServicio.setEditable(true);
		// Inicializar los valores del combo, la fecha y marcar el checkbox de presupuesto
		comboTipo.getItems().addAll("Material", "Chapa", "Pintura", "Electrónica / mecánica");
		comboTipo.setValue("Material");
		// Añadir un listener al combo
		comboTipo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarCombo(newValue));

		// Marcar algunos checkbox que son habituales
		chckbxPresupuesto.setSelected(true);
		chckbxPermisoPruebas.setSelected(true);
		chckbxNoPiezas.setSelected(true);
		
		
		columnaHorasServ.setCellFactory(TextFieldTableCell.<Servicio, Number>forTableColumn(new NumberStringConverter()));
		columnaHorasServ.setOnEditCommit(
            (CellEditEvent<Servicio, Number> t) -> {
            	JOptionPane.showMessageDialog(null, t.getNewValue().doubleValue());
                //long l = (long) t.getNewValue().floatValue();
            	//float f = l;
            	((Servicio) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setHoras(t.getNewValue().floatValue());
            	//valueStr = valueStr.replace(',', '.');
            	//return new Float(valueStr);
        });
		
		//columnaHorasServ.setCellFactory(TextFieldTableCell.<Servicio, Number>forTableColumn(new NumberStringConverter()));
		
		/*columnaHorasServ.setCellFactory(TextFieldTableCell.forTableColumn());
		columnaHorasServ.setOnEditCommit(
		    new EventHandler<CellEditEvent<Servicio, Float>>() {
		        @Override
		        public void handle(CellEditEvent<Servicio, Float> t) {
		            ((Servicio) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setHoras(t.getNewValue());
		        }
		    }
		);*/
	}

	/**
	 * Se comprueba el valor elegido en el combo para ocultar o no el TextField
	 * de "Cantidad"
	 * 
	 * @param valor
	 */
	private void comprobarCombo(String valor) {
		if (valor.equalsIgnoreCase("Material")) {
			lblValor.setVisible(true);
			txtValor.setVisible(true);
			lblCantidad.setText("Cantidad");
			esServicio = false;
		} else {
			lblValor.setVisible(false);
			txtValor.setVisible(false);
			lblCantidad.setText("Horas");
			txtCantidad.setPromptText("Horas");
			esServicio = true;
		}
	}

	/**
	 * Se añade el servicio o material a la tabla correspondiente
	 */
	@FXML
	private void add() {
		if (esServicio) {
			servicio = new Servicio(0, comboTipo.getValue() + ": " + txtConcepto.getText(), Float.parseFloat(txtCantidad.getText()), 0, comboTipo.getValue());
			listaServicios.add(servicio);
			columnaConceptoServ.setCellValueFactory(cellData -> cellData.getValue().servicioProperty());
			columnaHorasServ.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
			tableServicio.setItems(listaServicios);
			//Se actualizan los valores del precio
			actualizarPrecio();
			
		} else {
			float precioTotal = Float.parseFloat(txtValor.getText()) * Integer.parseInt(txtCantidad.getText());
			material = new Material(0, txtConcepto.getText(), Float.parseFloat(txtValor.getText()), 0, Integer.parseInt(txtCantidad.getText()), precioTotal);
			listaMaterial.add(material);
			columnaConceptoMat.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
			columnaCantidadMat.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
			columnaPrecioMat.setCellValueFactory(cellData -> cellData.getValue().preciounitProperty().asObject());
			tableMaterial.setItems(listaMaterial);
			//Se actualizan los valores del precio
			actualizarPrecio();
		}
	}
	
	/**
	 * Funcion que actualiza los TextField de los precios
	 */
	@FXML
	private void actualizarPrecio(){
		float valorMaterial = 0;
		float valorServicio = 0;
		float valorOtros = Float.parseFloat(txtOtros.getText());
		float valorSubtotal = 0;
		float valorIva = 0; //LEER EL VALOR DEL IVA !!!!!!!!!!!!!!!!
		float valorTotal = 0;

		DecimalFormat dt = new DecimalFormat("##.##");

		if(listaServicios.size() > 0){
			for (Servicio serv : listaServicios) {
				valorServicio += serv.getHoras()*46; // LEER EL PRECIO DE HORA Y CAMBIARLO POR EL 46!!!!!!!!!!!!!!!!!!!!!
				txtManoObra.setText(""+dt.format(valorServicio));
			}			
		}else{
			txtManoObra.setText(""+dt.format(0));
		}
		
		for (Material mat : listaMaterial) {
			valorMaterial += mat.getPreciototal();
			txtMateriales.setText(""+dt.format(valorMaterial));
		}
		
		valorSubtotal = valorMaterial + valorServicio + valorOtros;
		txtSubtotal.setText("" + dt.format(valorSubtotal));
		
		valorIva = (valorSubtotal * 16) / 100; //CAMBIAR EL 21 POR EL VALOR DEL IVA!!!!!!!!!!!!!!!!!!!
		txtIva.setText("" + dt.format(valorIva));
		
		valorTotal = valorSubtotal + valorIva;
		txtTotal.setText("" + dt.format(valorTotal));
	}
	
	/**
	 * Función que elimina el servicio seleccionado
	 */
	@FXML
	private void eliminarServicio(){
		int selectedIndex = tableServicio.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	tableServicio.getItems().remove(selectedIndex);
        	actualizarPrecio();
        } else {
            // Nothing selected.
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Sin selección");
        	alert.setHeaderText("Ningún servicio seleccionado");
        	alert.setContentText("Selecciona el servicio que quieras eliminar de la tabla.");

        	alert.showAndWait();
        }
	}
}