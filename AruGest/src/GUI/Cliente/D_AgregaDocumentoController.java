package GUI.Cliente;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Blob;
import java.util.ResourceBundle;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Documento;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

public class D_AgregaDocumentoController implements Initializable {
	// Variables de la vista
	@FXML
	private ListView<String> lista;
	@FXML
	private Label textoSoltar;
	@FXML
	private TextField txtNombre;

	// Resto de variables
	private Stage dialogStage;
	// private boolean okClicked = false;

	private String path = "";
	private Documento docu = null;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		lista.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				/* data is dragged over the target */
				// System.out.println("onDragOver");

				/*
				 * accept it only if it is not dragged from the same node and if
				 * it has a File data
				 * 
				 */
				if (event.getGestureSource() != lista && event.getDragboard().hasFiles()) {
					/*
					 * allow for both copying and moving, whatever user chooses
					 */
					event.acceptTransferModes(TransferMode.COPY);
				}
				event.consume();
			}
		});

		lista.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				/* the drag-and-drop gesture entered the target */
				// System.out.println("onDragEntered");
				/* show to the user that it is an actual gesture target */
				if (event.getGestureSource() != lista && event.getDragboard().hasFiles()) {
					// archivo.setFill(Color.GREEN);
					textoSoltar.setVisible(true);
				}
				event.consume();
			}
		});

		lista.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				/* mouse moved away, remove the graphical cues */
				textoSoltar.setVisible(false);
				event.consume();
			}
		});

		lista.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				/* data dropped */
				// System.out.println("onDragDropped");
				/* if there is a File data on dragboard, read it and use it */
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					// System.out.println(db.getFiles().get(0).getAbsolutePath());
					// System.out.println(db.getFiles().get(0).getName());
					lista.getItems().add(db.getFiles().get(0).getName());
					if (txtNombre.getText().isEmpty()) {
						txtNombre.setText(db.getFiles().get(0).getName());
					}
					path = db.getFiles().get(0).getAbsolutePath();

					// archivoTextView.setText(db.getFiles().get(0).getPath());
					// try {
					// // Copy file to the local App directory /
					// // copyFile(db.getFiles().get(0), new File("" +
					// // db.getFiles().get(0).getName()));
					// } catch (Exception ex) {
					// //
					// Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,
					// // null, ex);
					// }

					success = true;
				}
				/*
				 * let the source know whether the File was successfully
				 * transferred and used
				 */
				event.setDropCompleted(success);

				event.consume();
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
	 * Devuelve el documento seleccionado
	 * 
	 * @return
	 */
	public Documento getDocumento() {
		return docu;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		try {
			File file = new File(path);
			Blob b1 = Inicio.CONEXION.getCon().createBlob();
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[(int) file.length()];
			while ((nRead = fis.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			byte[] bytes = buffer.toByteArray();
			buffer.close();
			fis.close();
			b1.setBytes(1, bytes);

			this.docu = new Documento(0, Inicio.CLIENTE_ID, 6, txtNombre.getText(), b1, Utilidades.getExtension(path)); // Cambiar
			// el
			// 6
			// *********************

			// Esto funciona, abre el archivo
			// Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
			// + path);

		} catch (Exception e) {
			Utilidades.mostrarError(e);
			docu = null;
		}

		dialogStage.close();
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
