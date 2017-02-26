package GUI;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

import Logica.Inicio;
import Logica.Utilidades;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class D_OpcionesBackupController {
	@FXML
	private TextField txtRuta;

	private String ruta;

	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Cargar los datos
		ruta = Inicio.RUTA_BACKUP;
		txtRuta.setText(ruta);
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
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Abre el diálogo para que seleccione la carpeta donde se guardarán las
	 * copias de seguridad
	 */
	@FXML
	private void abrirSelectorRuta() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Seleccionar carpeta");

		// Obtener la imagen seleccionada
		File f = chooser.showDialog(dialogStage);
		if (f != null) {
			ruta = f.getAbsolutePath();
			txtRuta.setText(ruta);
		}
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			try {
				// Crear backup
				String rutaNombre = ruta;
				rutaNombre += "\\AruGestDB_";
				rutaNombre += LocalDate.now();
				rutaNombre += ".zip";
				Inicio.CONEXION.getCon().prepareStatement("BACKUP TO '" + rutaNombre + "'").executeUpdate();
				// Guardar ruta de backup
				Inicio.RUTA_BACKUP = ruta;
				Inicio.CONEXION.setRutaBackup(ruta);
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Creada la copia de seguridad", "");
			} catch (SQLException e) {
				System.out.println("Error al crear copia de seguridad");
				e.printStackTrace();
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
		String mensaje = "";
		if (txtRuta.getText().isEmpty()) {
			mensaje = "La ruta para guardar las copias de seguridad no puede estar vacía.";
		}
		if (mensaje.equalsIgnoreCase("")) {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Datos incorrectos", mensaje);
			return false;
		}
	}
}
