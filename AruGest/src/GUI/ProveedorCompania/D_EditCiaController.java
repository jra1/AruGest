package GUI.ProveedorCompania;

import Logica.Utilidades;
import Modelo.Direccion;
import Modelo.ProveedorCompaniaDireccion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a cia
 * 
 * @author Joseba Ruiz
 */
public class D_EditCiaController {

	@FXML
	private TextField txtCif;
	@FXML
	private TextField txtNombre;
	@FXML
	private CheckBox chckboxEsDesguace;
	@FXML
	private TextField txtTel1;
	@FXML
	private TextField txtTel2;
	@FXML
	private TextField txtCalle;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtPiso;
	@FXML
	private TextField txtLetra;
	@FXML
	private TextField txtCodPostal;
	@FXML
	private TextField txtLocalidad;
	@FXML
	private TextField txtProvincia;

	private Stage dialogStage;
	private ProveedorCompaniaDireccion pcd = null;
	private boolean esDesguace = false;
	private boolean esCia = false;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
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
	 * Coloca la cía/proveedor a ser editado
	 * 
	 * @param cia/proveedor
	 *            a ser editado
	 */
	public void setCia(ProveedorCompaniaDireccion pcd) {
		this.pcd = pcd;
		txtCif.setText(pcd.getPc().getCif());
		txtNombre.setText(pcd.getPc().getNombre());
		txtTel1.setText(pcd.getPc().getTelf1());
		txtTel2.setText(pcd.getPc().getTelf2());
		txtCalle.setText(pcd.getDireccion().getCalle());
		txtNumero.setText("" + pcd.getDireccion().getNumero());
		txtPiso.setText(pcd.getDireccion().getPiso());
		txtLetra.setText(pcd.getDireccion().getLetra());
		txtCodPostal.setText("" + pcd.getDireccion().getCpostal());
		txtLocalidad.setText(pcd.getDireccion().getLocalidad());
		txtProvincia.setText(pcd.getDireccion().getProvincia());
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
			if (pcd.getDireccion() != null) {
				if (!txtCalle.getText().isEmpty() || !txtCodPostal.getText().isEmpty()
						|| !txtLocalidad.getText().isEmpty() || !txtProvincia.getText().isEmpty()) {
					pcd.getDireccion().setCalle(txtCalle.getText());
					pcd.getDireccion().setNumero(Integer.parseInt(txtNumero.getText()));
					pcd.getDireccion().setPiso(txtPiso.getText());
					pcd.getDireccion().setLetra(txtLetra.getText());
					pcd.getDireccion().setCpostal(Integer.parseInt(txtCodPostal.getText()));
					pcd.getDireccion().setLocalidad(txtLocalidad.getText());
					pcd.getDireccion().setProvincia(txtProvincia.getText());
				}
			} else {
				Direccion d = new Direccion(txtCalle.getText(), Integer.parseInt(txtNumero.getText()),
						txtPiso.getText(), txtLetra.getText(), Integer.parseInt(txtCodPostal.getText()),
						txtLocalidad.getText(), txtProvincia.getText());
				pcd.setDireccion(d);
			}

			pcd.getPc().setNombre(txtNombre.getText());
			pcd.getPc().setTelf2(txtCif.getText());
			pcd.getPc().setTelf1(txtTel1.getText());
			pcd.getPc().setTelf2(txtTel2.getText());
			pcd.getPc().setEsdesguace(esDesguace);
			pcd.getPc().setEscompania(esCia);

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

		if (txtCif.getText().length() == 0) {
			errorMessage += "Introduce el CIF del proveedor/compañía\n";
		}
		if (txtNombre.getText().length() == 0) {
			errorMessage += "Introduce el nombre del proveedor/compañía";
		}
		if (txtNumero.getText().length() > 0) {
			try {
				Integer.parseInt(txtNumero.getText());
			} catch (NumberFormatException e) {
				errorMessage = "Número no válido.\n Introduce únicamente números";
			}
		}
		if (txtCodPostal.getText().length() > 0) {
			try {
				Integer.parseInt(txtCodPostal.getText());
			} catch (NumberFormatException e) {
				errorMessage = "Código postal no válido.\n Introduce únicamente números";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inváidos", "Por favor corrige los campos",
					errorMessage);
			return false;
		}
	}

	public boolean isEsDesguace() {
		return esDesguace;
	}

	public void setEsDesguace(boolean esDesguace) {
		this.esDesguace = esDesguace;
	}

	public boolean isEsCia() {
		return esCia;
	}

	public void setEsCia(boolean esCia) {
		this.esCia = esCia;
	}
}
