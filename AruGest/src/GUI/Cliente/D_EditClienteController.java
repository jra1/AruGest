package GUI.Cliente;

import Logica.StringUtils;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Particular;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a person.
 * 
 * @author Joseba Ruiz
 */
public class D_EditClienteController {

	@FXML
	private ComboBox<String> comboTipo;
	@FXML
	private TextField txtNif;
	@FXML
	private TextField txtNombre;
	@FXML
	private CheckBox chckboxEsProveedor;
	@FXML
	private Label lblApellidos;
	@FXML
	private TextField txtApellidos;
	@FXML
	private TextField txtTel1;
	@FXML
	private TextField txtTel2;
	@FXML
	private TextField txtTel3;
	@FXML
	private TextField txtDireccion;
	@FXML
	private TextField txtCodPostal;
	@FXML
	private TextField txtLocalidad;
	@FXML
	private TextField txtProvincia;

	private Stage dialogStage;
	private ClienteParticularEmpresaDireccion cped = null;
	private boolean esEmpresa = false;
	private boolean okClicked = false;
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		comboTipo.getItems().addAll(StringUtils.TIPO_PARTICULAR, StringUtils.TIPO_EMPRESA);
		comboTipo.setValue(StringUtils.TIPO_PARTICULAR);

		comboTipo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipo(newValue));
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
		this.cped = cped;

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
		}
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
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			// Particular / Empresa
			if (!esEmpresa) {
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
			// Direcci�n
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
									"El c�digo postal s�lo puede contener d�gitos", "Se pondr� 0 hasta que lo cambie.");
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
							Utilidades.mostrarAlerta(AlertType.WARNING, "Atenci�n",
									"El c�digo postal s�lo puede contener d�gitos", "Se pondr� 0 hasta que lo cambie.");
							cped.getDireccion().setCpostal(0);
						}
					} else {
						cped.getDireccion().setCpostal(0);
					}
					cped.getDireccion().setLocalidad(txtLocalidad.getText());
					cped.getDireccion().setProvincia(txtProvincia.getText());
				}
			} else {
				// Calle, cpostal, localidad o provincia vac�a
				if (cped.getDireccion() == null) {
					cped.setDireccion(new Direccion());
					cped.getCliente().setDireccionID(0);
				} else {
					cped.getDireccion().setDireccion("");
					cped.getDireccion().setCpostal(0);
					cped.getDireccion().setLocalidad("");
					cped.getDireccion().setProvincia("");
				}
			}
			cped.getCliente().setNombre(txtNombre.getText() + " " + txtApellidos.getText());
			cped.getCliente().setTelf1(txtTel1.getText());
			cped.getCliente().setTelf2(txtTel2.getText());
			cped.getCliente().setTelf3(txtTel3.getText());

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

		if (txtNif.getText().length() == 0) {
			errorMessage += "Introduce el NIF/CIF del cliente.\n";
		}
		if (!Utilidades.validaDni(txtNif.getText())) {
			errorMessage += txtNif.getText() + " no es un NIF/CIF correcto.\n";
		}
		if (txtNombre.getText().length() == 0) {
			errorMessage += "Introduce el nombre del cliente.";
		}
		if (txtCodPostal.getText().length() > 0 && Utilidades.validaNumero(txtCodPostal.getText()) == -1) {
			errorMessage = "C�digo postal no v�lido.\n Introduce �nicamente n�meros.";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inv�idos", "Por favor corrige los campos",
					errorMessage);
			return false;
		}
	}

	private void comprobarComboTipo(String newValue) {
		if (newValue.equalsIgnoreCase(StringUtils.TIPO_PARTICULAR)) {
			lblApellidos.setVisible(true);
			txtApellidos.setVisible(true);
			chckboxEsProveedor.setVisible(false);
			esEmpresa = false;
		} else {
			lblApellidos.setVisible(false);
			txtApellidos.setVisible(false);
			chckboxEsProveedor.setVisible(true);
			esEmpresa = true;
		}

	}
}
