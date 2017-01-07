package GUI.ProveedorCompania;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import Logica.Inicio;
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
	 * Coloca la cía/proveedor a ser editado
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
		txtCalle.setText(pcd.getDireccion().getCalle());
		txtNumero.setText("" + pcd.getDireccion().getNumero());
		txtPiso.setText(pcd.getDireccion().getPiso());
		txtLetra.setText(pcd.getDireccion().getLetra());
		txtCodPostal.setText("" + pcd.getDireccion().getCpostal());
		txtLocalidad.setText(pcd.getDireccion().getLocalidad());
		txtProvincia.setText(pcd.getDireccion().getProvincia());
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
				e.printStackTrace();
				Utilidades.mostrarAlerta(AlertType.WARNING, "Error", "Error al cargar el logo", "");
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
			if (pcd.getDireccion() != null) {
				if (!txtCalle.getText().isEmpty()
						|| (!txtCodPostal.getText().isEmpty() && Integer.parseInt(txtCodPostal.getText()) != 0)
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
				if (!txtCalle.getText().isEmpty() || !txtCodPostal.getText().isEmpty()
						|| !txtLocalidad.getText().isEmpty() || !txtProvincia.getText().isEmpty()) {
					Direccion d = new Direccion(txtCalle.getText(), Integer.parseInt(txtNumero.getText()),
							txtPiso.getText(), txtLetra.getText(), Integer.parseInt(txtCodPostal.getText()),
							txtLocalidad.getText(), txtProvincia.getText());
					pcd.setDireccion(d);
				}
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
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el logo en el objeto",
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

	/**
	 * Abre un selector de archivos para que el usuario seleccione el logo de la
	 * compañía/proveedor
	 */
	@FXML
	private void buscarLogo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Buscar logo compañía/proveedor");

		// Agregar filtros para facilitar la busqueda
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.*"),
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
				e.printStackTrace();
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
