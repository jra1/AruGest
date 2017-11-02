package GUI.Cliente;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
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
public class D_EligeNombreFactura {

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
	private boolean okClicked = false;
	private ObservableList<ProveedorCompaniaDireccion> listaCias = FXCollections.observableArrayList();
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		radioCliente.setSelected(true);
		paneCia.setVisible(false);

		radioCia.selectedProperty()
				.addListener((observable, oldValue, newValue) -> comprobarRadioCia(newValue));
		
		txtNombre.textProperty().addListener((observable, oldValue, newValue) -> buscaCia(newValue));
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
	 * Coloca el cliente a ser editado
	 * 
	 * @param cliente
	 *            a ser editado
	 */
	public void setCliente(ClienteParticularEmpresaDireccion cped) {
		/*this.cped = cped;

		if (cped.getParticular() != null) {
			lblApellidos.setVisible(true);
			txtApellidos.setVisible(true);
			comboTipo.setValue(StringUtils.TIPO_PARTICULAR);
			txtNif.setText(cped.getParticular().getNif());
			txtNombre.setText(cped.getParticular().getNombre());
			txtApellidos.setText(cped.getParticular().getApellidos());
		} else if (cped.getEmpresa() != null) {
			lblApellidos.setVisible(false);
			txtApellidos.setVisible(false);
			comboTipo.setValue(StringUtils.TIPO_EMPRESA);
			txtNif.setText(cped.getEmpresa().getCif());
			txtNombre.setText(cped.getEmpresa().getNombre());
		}
		txtTel1.setText(cped.getCliente().getTelf1());
		txtTel2.setText(cped.getCliente().getTelf2());
		txtTel3.setText(cped.getCliente().getTelf3());
		if (cped.getDireccion() != null && cped.getDireccion().getIddireccion() != 0) {
			txtDireccion.setText(cped.getDireccion().getDireccion());
			txtCodPostal.setText(Integer.toString(cped.getDireccion().getCpostal()));
			txtLocalidad.setText(cped.getDireccion().getLocalidad());
			txtProvincia.setText(cped.getDireccion().getProvincia());
		}*/
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
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
			// Particular / Empresa
			/*if (!esEmpresa) {
				Particular p;
				if (cped.getParticular() != null) {
					p = new Particular(cped.getParticular().getIdparticular(), cped.getParticular().getClienteID(),
							txtNombre.getText(), txtApellidos.getText(), txtNif.getText());
				} else {
					p = new Particular(txtNombre.getText(), txtApellidos.getText(), txtNif.getText());
				}
				cped.setParticular(p);
				cped.getCliente().setTipo("P");
			} else {
				Empresa e;
				if (cped.getEmpresa() != null) {
					e = new Empresa(cped.getEmpresa().getIdempresa(), cped.getEmpresa().getClienteID(),
							txtNombre.getText(), txtNif.getText(), chckboxEsProveedor.isSelected());
				} else {
					e = new Empresa(txtNombre.getText(), txtNif.getText(), chckboxEsProveedor.isSelected());
				}
				cped.setEmpresa(e);
				cped.getCliente().setTipo("E");
			}
			// Dirección
			if (!txtDireccion.getText().isEmpty()
					|| (!txtCodPostal.getText().isEmpty() && Integer.parseInt(txtCodPostal.getText()) != 0)
					|| !txtLocalidad.getText().isEmpty() || !txtProvincia.getText().isEmpty()) {
				if (cped.getDireccion() == null || cped.getDireccion().getIddireccion() == 0) {
					int cpostal = 0;
					if (!txtCodPostal.getText().isEmpty()) {
						if (Utilidades.validaNumero(txtCodPostal.getText()) != -1) {
							cpostal = Utilidades.validaNumero(txtCodPostal.getText());
						} else {
							Utilidades.mostrarAlerta(AlertType.WARNING, StringUtils.ATENCION,
									"El código postal sólo puede contener dígitos", "Se pondrá 0 hasta que lo cambie.");
							cpostal = 0;
						}
					}
					cped.setDireccion(new Direccion(0, txtDireccion.getText(), cpostal, txtLocalidad.getText(), txtProvincia.getText()));
				} else {
					cped.getDireccion().setDireccion(txtDireccion.getText());
					if (!txtCodPostal.getText().isEmpty()) {
						if (Utilidades.validaNumero(txtCodPostal.getText()) != -1) {
							cped.getDireccion().setCpostal(Utilidades.validaNumero(txtCodPostal.getText()));
						} else {
							Utilidades.mostrarAlerta(AlertType.WARNING, "Atención",
									"El código postal sólo puede contener dígitos", "Se pondrá 0 hasta que lo cambie.");
							cped.getDireccion().setCpostal(0);
						}
					} else {
						cped.getDireccion().setCpostal(0);
					}
					cped.getDireccion().setLocalidad(txtLocalidad.getText());
					cped.getDireccion().setProvincia(txtProvincia.getText());
				}
			} else {
			}*/

			okClicked = true;
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
}
