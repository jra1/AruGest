package GUI.Documento;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import GUI.Cliente.V_ClienteController;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccionDocumento;
import Modelo.Documento;
import Modelo.GestorVentana;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class V_BuscarDocumentoController {

	// Variables de la vista
	@FXML
	private TextField txtNombreDocumento;
	@FXML
	private TextField txtNombreCliente;
	@FXML
	private TableView<ClienteParticularEmpresaDireccionDocumento> tableDocumentos;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccionDocumento, String> columnaNombreDocumento;
	@FXML
	private TableColumn<ClienteParticularEmpresaDireccionDocumento, String> columnaCliente;
	@FXML
	private ImageView imgPrevio;

	// Resto de variables
	private Inicio main;
	public Button boton1;
	public Button boton2;
	public Button boton3;
	private ScrollPane sp;
	private AnchorPane ap;
	private GestorVentana gv;
	private String nombre = "";

	private ObservableList<ClienteParticularEmpresaDireccionDocumento> listaDocumentos = FXCollections
			.observableArrayList();

	public void setMainAPP(Inicio p) {
		setMain(p);
	}

	public void setScrollPane(ScrollPane root) {
		this.sp = root;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		tableDocumentos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarPrevio(newValue));

		// Para abrir documento con doble click
		tableDocumentos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						int selectedIndex = tableDocumentos.getSelectionModel().getSelectedIndex();
						if (selectedIndex >= 0) {
							abrirDocumento();
						}
					}
				}
			}
		});
		tableDocumentos.setTooltip(new Tooltip("Doble click para abrir el documento"));
	}

	/**
	 * Funciones que se llamen desde la vista, con @FXML delante
	 */
	@FXML
	private void buscarDocumento() {
		listaDocumentos.clear();
		tableDocumentos.getItems().clear();
		ArrayList<ClienteParticularEmpresaDireccionDocumento> lista = Inicio.CONEXION
				.buscarDocumentos(txtNombreDocumento.getText(), txtNombreCliente.getText());
		if (lista.isEmpty()) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "No encontrado",
					"No hay documentos con los parámetros de búsqueda introducidos.");
		} else {
			for (ClienteParticularEmpresaDireccionDocumento cpedd : lista) {
				listaDocumentos.add(cpedd);
				columnaNombreDocumento
						.setCellValueFactory(cellData -> cellData.getValue().getDocumento().tituloProperty());
				columnaCliente.setCellValueFactory(cellData -> cellData.getValue().getCliente().nombreProperty());
				tableDocumentos.setItems(listaDocumentos);
			}
		}

	}

	/**
	 * Muestra la previsualización del documento seleccionado
	 * 
	 * @param documento
	 */
	private void mostrarPrevio(ClienteParticularEmpresaDireccionDocumento cpedd) {
		Image i;
		if (cpedd.getDocumento().getExtension().equalsIgnoreCase("JPG")
				|| cpedd.getDocumento().getExtension().equalsIgnoreCase("JPEG")
				|| cpedd.getDocumento().getExtension().equalsIgnoreCase("PNG")
				|| cpedd.getDocumento().getExtension().equalsIgnoreCase("BMP")) {
			try {
				File f = Inicio.CONEXION.leerDocumentoPorID(cpedd.getDocumento().getIddocumento());
				InputStream is = new FileInputStream(f);
				i = new Image(is);
				if (i != null) {
					imgPrevio.setImage(i);
					imgPrevio.setFitWidth(485);
					imgPrevio.setFitHeight(355);
				}
			} catch (Exception e) {
				Utilidades.mostrarError(e);
			}
		} else if (cpedd.getDocumento().getExtension().equalsIgnoreCase("DOC")
				|| cpedd.getDocumento().getExtension().equalsIgnoreCase("DOCX")) {
			i = new Image("recursos/images/doc_icon.png");
			imgPrevio.setImage(i);
			imgPrevio.setFitWidth(128);
			imgPrevio.setFitHeight(128);
		} else if (cpedd.getDocumento().getExtension().equalsIgnoreCase("PDF")) {
			i = new Image("recursos/images/pdf_icon.png");
			imgPrevio.setImage(i);
			imgPrevio.setFitWidth(128);
			imgPrevio.setFitHeight(128);
		} else if (cpedd.getDocumento().getExtension().equalsIgnoreCase("XLS")
				|| cpedd.getDocumento().getExtension().equalsIgnoreCase("XLSX")) {
			i = new Image("recursos/images/xls_icon.png");
			imgPrevio.setImage(i);
			imgPrevio.setFitWidth(128);
			imgPrevio.setFitHeight(128);
		} else {
			i = new Image("recursos/images/document_icon.png");
			imgPrevio.setImage(i);
			imgPrevio.setFitWidth(128);
			imgPrevio.setFitHeight(128);
		}
	}

	/**
	 * Abre el documento seleccionado en la tabla
	 */
	@FXML
	private void abrirDocumento() {
		int selectedIndex = tableDocumentos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Documento d = tableDocumentos.getSelectionModel().getSelectedItem().getDocumento();
			File f = Inicio.CONEXION.leerDocumentoPorID(d.getIddocumento());

			// Abrir el documento:
			try {
				// String ruta = f.getAbsolutePath();
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + f.getAbsolutePath());

			} catch (Exception e) {
				Utilidades.mostrarError(e);
			}

		} else {
			Utilidades.mostrarAlerta(AlertType.WARNING, "Atención", "Ningún documento seleccionado",
					"Selecciona el documento que quieras abrir.");
		}
	}

	/**
	 * Función para cargar el cliente seleccionado
	 */
	@FXML
	private void abrirCliente() {
		int selectedIndex = tableDocumentos.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Inicio.CLIENTE_ID = listaDocumentos.get(selectedIndex).getCliente().getIdcliente();
			try {
				// Cargar la vista de Cliente
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Inicio.class.getResource("/GUI/Cliente/V_Cliente.fxml"));
				AnchorPane cliente = (AnchorPane) loader.load();

				// Poner la nueva vista en el centro del root
				Utilidades.ajustarResolucionAnchorPane(cliente, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
				sp.setContent(cliente);
				nombre = "Cliente: " + listaDocumentos.get(selectedIndex).getCliente().getNombre();
				ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
				gv = new GestorVentana(ap, nombre);
				Utilidades.gestionarPantallas(gv);
				boton1.setVisible(Inicio.BOTON1.isVisible());
				boton1.setText(Inicio.BOTON1.getNombre());
				boton2.setVisible(Inicio.BOTON2.isVisible());
				boton2.setText(Inicio.BOTON2.getNombre());
				boton3.setVisible(Inicio.BOTON3.isVisible());
				boton3.setText(Inicio.BOTON3.getNombre());

				// main.getRoot().setCenter(cliente);

				// Poner el controlador de la nueva vista.
				V_ClienteController controller = loader.getController();
				controller.setMainAPP(main);
				controller.setScrollPane(sp);
				controller.boton1 = boton1;
				controller.boton2 = boton2;
				controller.boton3 = boton3;
				controller.cargaCliente(listaDocumentos.get(selectedIndex));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Nada seleccionado.
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Ningún cliente seleccionado",
					"Selecciona el cliente que quieras cargar.");
		}
	}

	public void setFocus() {
		txtNombreDocumento.requestFocus();
	}

	public Inicio getMain() {
		return main;
	}

	public void setMain(Inicio main) {
		this.main = main;
	}
}
