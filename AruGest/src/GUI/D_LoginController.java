package GUI;

import org.apache.commons.codec.digest.DigestUtils;

import Logica.Inicio;
import Logica.Utilidades;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class D_LoginController {
	// Variables de la vista
	@FXML
	private TextField txtUsuario;
	@FXML
	private PasswordField txtPass;
	@FXML
	private CheckBox chkAutologin;
	@FXML
	private Button btnAceptar;

	// Resto de variables
	private Stage dialogStage;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		chkAutologin.setSelected(Inicio.AUTOLOGIN);
		if (Inicio.AUTOLOGIN) {
			txtUsuario.setText(Inicio.USUARIO);
			// Pongo la sal en el campo del pass
			// Da igual porque si est� el autologin
			// No se va a comprobar el pass de aqu�
			txtPass.setText(Inicio.SAL);
		}

		btnAceptar.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.F12 || ke.getCode() == KeyCode.ENTER) {
					handleOk();
				}
			}
		});
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
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			Inicio.CONEXION.setAutologin(chkAutologin.isSelected());
			Inicio.AUTOLOGIN = chkAutologin.isSelected();
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
		if (Inicio.AUTOLOGIN) {
			return true;
		} else {
			if (Inicio.USUARIO.equals(txtUsuario.getText())) {
				// Encriptar el pass que ha introducido el usuario
				String passIntroducido = txtPass.getText();
				passIntroducido += Inicio.SAL;
				passIntroducido = DigestUtils.sha512Hex(passIntroducido);

				if (Inicio.PASS.equals(passIntroducido)) {
					return true;
				} else {
					Utilidades.mostrarAlerta(AlertType.ERROR, "Error de autenticaci�n", "Contrase�a incorrecta",
							"Comprueba que has introducido bien la contrase�a");
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error de autenticaci�n", "Usuario incorrecto",
						"Comprueba que has introducido bien el usuario");
			}
		}
		return false;
	}

	/**
	 * Se pone el foco donde interesa
	 */
	public void setFocus() {
		txtUsuario.requestFocus();
	}
}
