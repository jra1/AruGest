package GUI.ProveedorCompania;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import Logica.Inicio;
import Logica.StringUtils;
import Logica.Utilidades;
import Modelo.Direccion;
import Modelo.ProveedorCompaniaDireccion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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
	private TextField txtDireccion;
	@FXML
	private TextField txtCodPostal;
	@FXML
	private TextField txtLocalidad;
	@FXML
	private TextField txtProvincia;
	@FXML
	private ImageView logo;

	private Stage dialogStage;
	private ProveedorCompaniaDireccion pcd = null;
	private boolean esDesguace = false;
	private boolean esCia = false;
	private boolean okClicked = false;
	File imgFile;

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
	 * Coloca la c�a/proveedor a ser editado
	 * 
	 * @param cia/proveedor
	 *            a ser editado
	 */
	public void setCia(ProveedorCompaniaDireccion pcd) {
		this.pcd = pcd;
		txtCif.setText(pcd.getCif());
		txtNombre.setText(pcd.getNombre());
		txtTel1.setText(pcd.getTelf1());
		txtTel2.setText(pcd.getTelf2());
		if (pcd.getDireccion() != null && pcd.getDireccion().getIddireccion() != 0) {
			txtDireccion.setText(pcd.getDireccion().getDireccion());
			txtCodPostal.setText(Integer.toString(pcd.getDireccion().getCpostal()));
			txtLocalidad.setText(pcd.getDireccion().getLocalidad());
			txtProvincia.setText(pcd.getDireccion().getProvincia());
		}
		if (pcd.isEscompania()) {
			chckboxEsDesguace.setVisible(false);
		} else {
			chckboxEsDesguace.setVisible(true);
			chckboxEsDesguace.setSelected(pcd.isEsdesguace());
		}
		if (pcd.getLogo() != null) {
			try {
				InputStream is = pcd.getLogo().getBinaryStream();
				logo.setImage(new Image(is));
			} catch (SQLException e) {
				Utilidades.mostrarAlerta(AlertType.WARNING, StringUtils.ERROR, "Error al cargar el logo", "");
			}
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
			if (!txtDireccion.getText().isEmpty()
					|| (!txtCodPostal.getText().isEmpty() && Integer.parseInt(txtCodPostal.getText()) != 0)
					|| !txtLocalidad.getText().isEmpty() || !txtProvincia.getText().isEmpty()) {
				if (pcd.getDireccion() != null || pcd.getDireccion().getIddireccion() != 0) {
					pcd.getDireccion().setDireccion(txtDireccion.getText());
					pcd.getDireccion().setCpostal(Utilidades.validaNumero(txtCodPostal.getText()));
					pcd.getDireccion().setLocalidad(txtLocalidad.getText());
					pcd.getDireccion().setProvincia(txtProvincia.getText());

				} else {
					pcd.setDireccion(new Direccion(0, txtDireccion.getText(), Utilidades.validaNumero(txtCodPostal.getText()),
							txtLocalidad.getText(), txtProvincia.getText()));
				}
			} else {
				// Calle, localidad, etc vac�o
				pcd.setDireccion(new Direccion());
				pcd.setDireccionID(0);
			}

			pcd.setNombre(txtNombre.getText());
			pcd.setCif(txtCif.getText());
			pcd.setTelf1(txtTel1.getText());
			pcd.setTelf2(txtTel2.getText());
			pcd.setEsdesguace(chckboxEsDesguace.isSelected());
			pcd.setEscompania(esCia);
			if (imgFile != null) {
				// Se guarda el logo como logo del ProveedorCompania
				try {
					Blob b1 = Inicio.CONEXION.getCon().createBlob();
					FileInputStream fis = new FileInputStream(new File(imgFile.getAbsolutePath()));
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					int nRead;
					byte[] data = new byte[(int) imgFile.length()];
					while ((nRead = fis.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
					buffer.flush();
					byte[] bytes = buffer.toByteArray();
					buffer.close();
					fis.close();
					b1.setBytes(1, bytes);
					pcd.setLogo(b1);

					// File image = new
					// File("C:/Users/Joseba/git/AruGest/AruGest/images/docus.png");
					// fis = new FileInputStream(image);
					// stmt.setBinaryStream(4, fis, (int) image.length());
					// stmt.setBinar(4, fis);
				} catch (Exception e) {
					Utilidades.mostrarAlerta(AlertType.ERROR, StringUtils.ERROR, "Error al guardar el logo en el objeto",
							e.getMessage());
				}
			}

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
			errorMessage += "Introduce el CIF del proveedor/compa��a\n";
		}
		if (txtNombre.getText().length() == 0) {
			errorMessage += "Introduce el nombre del proveedor/compa��a";
		}
		if (txtCodPostal.getText().length() > 0 && Utilidades.validaNumero(txtCodPostal.getText()) == -1) {
			errorMessage = "C�digo postal no v�lido.\n Introduce �nicamente n�meros";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Campos inv�idos", "Por favor corrige los campos",
					errorMessage);
			return false;
		}
	}

	/**
	 * Abre un selector de archivos para que el usuario seleccione el logo de la
	 * compa��a/proveedor
	 */
	@FXML
	private void buscarLogo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Buscar logo compa��a/proveedor");

		// Agregar filtros para facilitar la busqueda
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Im�genes", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));

		// Obtener la imagen seleccionada
		imgFile = fileChooser.showOpenDialog(dialogStage);

		// Mostar la imagen
		if (imgFile != null) {
			try {
				// Se muestra el logo en el ImageView
				Image image = new Image("file:" + imgFile.getAbsolutePath());
				logo.setImage(image);
			} catch (Exception e) {
				Utilidades.mostrarError(e);
			}
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
