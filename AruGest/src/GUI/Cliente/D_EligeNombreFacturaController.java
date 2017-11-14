package GUI.Cliente;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ProveedorCompaniaDireccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a person.
 * 
 * @author Joseba Ruiz
 */
public class D_EligeNombreFacturaController {

	@FXML
	private RadioButton radioCliente;
	@FXML
	private RadioButton radioCia;
	@FXML
	private TextField txtNombre;
	@FXML
	private Button btnBuscar;
	@FXML
	private TableView<ProveedorCompaniaDireccion> tableCias;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaNombre;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaCif;
	@FXML
	private TableColumn<ProveedorCompaniaDireccion, String> columnaTlf;
	@FXML
	private Pane paneCia;

	private Stage dialogStage;
	private int idCia = -1;
	private ObservableList<ProveedorCompaniaDireccion> listaCias = FXCollections.observableArrayList();
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		radioCliente.setSelected(true);
		paneCia.setVisible(false);

		radioCia.selectedProperty().addListener((observable, oldValue, newValue) -> comprobarRadioCia(newValue));
		
		txtNombre.textProperty().addListener((observable, oldValue, newValue) -> buscaCia(newValue));
		buscaCia("");
		
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public int returnIdCia() {
		return idCia;
	}

	/**
	 * Función que busca la cía escrita el textField
	 */
	private void buscaCia(String nombre) {
		listaCias = Inicio.CONEXION.buscarCias(true, false, nombre,"", "", "");
		columnaNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
		columnaCif.setCellValueFactory(cellData -> cellData.getValue().cifProperty());
		columnaTlf.setCellValueFactory(cellData -> cellData.getValue().telf1Property());
		tableCias.setItems(listaCias);
	}
	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			if(radioCliente.isSelected()) {
				idCia = -1;
			} else {
				idCia = tableCias.getSelectionModel().getSelectedItem().getIdprovecompa();
			}

			dialogStage.close();
		}

	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validaciones de los campos introducidos por el cliente
	 * 
	 * @return true si los campos son correctos
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		
		if(radioCia.isSelected() && tableCias.getSelectionModel().getSelectedIndex() < 0) {
			errorMessage = "Si quiere hacer la factura / presupuesto a nombre de una compañía, debe seleccionar una.";
		}
		
		if (errorMessage.length() == 0) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inváidos", "Por favor corrige los campos",
					errorMessage);
			return false;
		}
	}

	private void comprobarRadioCia(boolean newValue) {
		paneCia.setVisible(newValue);
	}
	
	/**
	 * Limpia el cuadro de búsqueda de una compañía
	 */
	@FXML
	private void limpiar() {
		txtNombre.clear();
		txtNombre.requestFocus();
	}
}
