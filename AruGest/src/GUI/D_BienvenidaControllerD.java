package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import Logica.Inicio;
import Logica.Utilidades;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class D_BienvenidaControllerD extends Thread {
	// Variables de la vista
	@FXML
	private Label lblBienvenida;
	@FXML
	public ProgressBar pbar;
	@FXML
	private Label lblCargando;
	@FXML
	private Label lblCreaUsuario;
	@FXML
	private Label lblUsuario;
	@FXML
	private Label lblPass;
	@FXML
	private Label lblPass2;
	@FXML
	private TextField txtUsuario;
	@FXML
	private PasswordField txtPass;
	@FXML
	private PasswordField txtPass2;

	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;

	// Resto de variables
	private Stage dialogStage;
	private boolean okClicked = false;
	String line;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		lblBienvenida.setVisible(true);
		pbar.setVisible(false);
		lblCargando.setVisible(false);
		lblCreaUsuario.setVisible(false);
		lblUsuario.setVisible(false);
		lblPass.setVisible(false);
		lblPass2.setVisible(false);
		txtUsuario.setVisible(false);
		txtPass.setVisible(false);
		txtPass2.setVisible(false);

		// ProgressBar
		// ProgressBar pbar = new ProgressBar(0);
		pbar.indeterminateProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				if (t1) {
					lblCargando.setText("Recopilando información...");
				} else {
					lblCargando.setText("Creando base de datos...");

				}
			}
		});

		pbar.progressProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {

				if (t1.doubleValue() == 1) {
					lblCargando.setText("Base de datos creada");
					// txtState.setFill(Color.GREEN);
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
	 * 
	 * @throws InterruptedException
	 */
	@FXML
	private void handleOk() throws InterruptedException {
		if (lblBienvenida.isVisible()) {
			empiezaCrearBD();
		} else if (lblCargando.isVisible()) {
			pideUserPass();
		} else {
			if (!txtUsuario.getText().isEmpty() && !txtPass.getText().isEmpty() && !txtPass2.getText().isEmpty()) {
				if (txtPass.getText().length() >= 5) {
					if (txtPass.getText().equals(txtPass2.getText())) {
						Inicio.CONEXION.guardarUserPass(txtUsuario.getText(), txtPass.getText());
						okClicked = true;
						dialogStage.close();
					} else {
						Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "La contraseña no coincide",
								"Debe introducir dos veces la contraseña para asegurarse que la ha escrito bien");
					}
				} else {
					Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención",
							"La contraseña debe contener al menos 5 caracteres",
							"La contraseña que introduzca debe contener al menos 5 caracteres. Es recomendable utilizar minúsculas, "
									+ "mayúscula y números.");
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Debe introducir un usuario y una contraseña",
						"");
			}
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		Optional<ButtonType> result = Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "¿Cancelar proceso?",
				"¿Cancelar proceso?",
				"Esto cancelará el proceso y eliminará la base de datos creada. ¿Confirma que quiere cancelar el proceso?");
		if (result.get() == ButtonType.OK) {
			line = null;
			// File fichero = new File(Inicio.DBPATHNAME + ".h2.db");
			// fichero.delete();

			File file = new File("C:\\AruGest");
			File[] contents = file.listFiles();
			if (contents != null) {
				for (File f : contents) {
					f.delete();
				}
			}
			file.delete();

			Platform.exit();
			System.exit(0);
		}
	}

	/**
	 * Llama al hilo para crear la BD
	 */
	private void empiezaCrearBD() throws InterruptedException {
		System.out.println("Se comienza a crear la BD");
		lblBienvenida.setVisible(false);
		pbar.setVisible(true);
		lblCargando.setVisible(true);
		btnAceptar.setDisable(true);
		lblCreaUsuario.setVisible(false);
		lblUsuario.setVisible(false);
		lblPass.setVisible(false);
		lblPass2.setVisible(false);
		txtUsuario.setVisible(false);
		txtPass.setVisible(false);
		txtPass2.setVisible(false);

		// Hilo crea BD
		Task<Object> task = taskCreator();
		pbar.progressProperty().unbind();
		pbar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		System.out.println("Después del hilo");

	}

	// Create a New Task
	private Task<Object> taskCreator() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				BufferedReader br = null;
				InputStream is = Inicio.class.getResourceAsStream("/recursos/ScriptSQL.txt");
				br = new BufferedReader(new InputStreamReader(is));

				Connection connection = null;
				try {
					connection = Inicio.CONEXION.getCon();
					line = br.readLine();
					StringBuilder statement = new StringBuilder();
					int contador = 0;
					while (line != null) {
						contador++;
						line = line.trim();
						updateProgress(contador + 1, 100); // Script 108 lineas
						if (!line.startsWith("--") && !line.startsWith("#") && !line.startsWith("//")) {
							statement.append(line);
							if (line.endsWith(";")) {
								executeLine(connection, statement.toString());
								statement = new StringBuilder();
							}
						}
						line = br.readLine();
					}
					if (statement.length() > 0) {
						executeLine(connection, statement.toString());
					}
				} finally {
					try {
						br.close();
						btnAceptar.setDisable(false);
					} catch (Exception e) {
						;
					}
					try {
						if (connection != null)
							connection.close();
					} catch (Exception e) {
					}
				}
				return true;
			}
		};
	}

	private static void executeLine(Connection connection, String statement) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(statement);
		pstmt.execute();
		pstmt.close();
		System.out.println("Ejecutando " + statement);
	}

	public static Connection openConnection(String pUrl) throws SQLException {
		return DriverManager.getConnection(pUrl, "sa", "");
	}

	private void pideUserPass() {
		lblBienvenida.setVisible(false);
		pbar.setVisible(false);
		lblCargando.setVisible(false);
		lblCreaUsuario.setVisible(true);
		lblUsuario.setVisible(true);
		lblPass.setVisible(true);
		lblPass2.setVisible(true);
		txtUsuario.setVisible(true);
		txtPass.setVisible(true);
		txtPass2.setVisible(true);
	}
}
