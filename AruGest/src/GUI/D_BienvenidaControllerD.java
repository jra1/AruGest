package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class D_BienvenidaControllerD {
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
	private TextField txtUsuario;
	@FXML
	private PasswordField txtPass;

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
		txtUsuario.setVisible(false);
		txtPass.setVisible(false);

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
			if (!txtUsuario.getText().isEmpty() && !txtPass.getText().isEmpty()) {
				Inicio.CONEXION.guardarUserPass(txtUsuario.getText(), txtPass.getText());
				okClicked = true;
				dialogStage.close();
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
			File fichero = new File(Inicio.DBPATHNAME + ".h2.db");
			fichero.delete();
			Platform.exit();
			System.exit(0);
			// dialogStage.close();
		}
	}

	/**
	 * Llama al hilo para crear la BD
	 */
	private void empiezaCrearBD() throws InterruptedException {
		lblBienvenida.setVisible(false);
		pbar.setVisible(true);
		lblCargando.setVisible(true);
		btnAceptar.setDisable(true);
		lblCreaUsuario.setVisible(false);
		lblUsuario.setVisible(false);
		lblPass.setVisible(false);
		txtUsuario.setVisible(false);
		txtPass.setVisible(false);

		// Create new Task and Thread - Bind Progress Property to
		// Task Progress
		Task<?> task = taskCreator();
		pbar.progressProperty().unbind();
		pbar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();

		// llamaHilo();
	}

	// Create a New Task
	private Task<?> taskCreator() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				BufferedReader br = new BufferedReader(new FileReader("ScriptSQL"));
				System.out.println("Empezando a leer fichero...");
				Connection connection = null;
				try {
					System.out.println(Inicio.DBPATHNAME);
					connection = Inicio.CONEXION.getCon();// openConnection(Inicio.DBURL);
					line = br.readLine();
					StringBuilder statement = new StringBuilder();
					int contador = 0;
					while (line != null) {
						contador++;
						line = line.trim();
						// Thread.sleep(200);
						updateProgress(contador + 1, 46); // 46 lineas script
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
						;
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
		txtUsuario.setVisible(true);
		txtPass.setVisible(true);
	}
}
