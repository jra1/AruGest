package Logica;

import java.io.File;
import java.io.IOException;
//import java.security.Principal;
import java.util.ArrayList;

import com.guigarage.responsive.ResponsiveHandler;

import GUI.D_LoginController;
import GUI.D_OpcionesController;
import GUI.V_RootController;
import GUI.Cliente.D_AgregaDocumentoController;
import GUI.Cliente.D_EditClienteController;
import GUI.Contabilidad.D_SelectorClienteVehiculoController;
import GUI.Contabilidad.D_SelectorGolpesController;
import GUI.ProveedorCompania.D_EditCiaController;
import GUI.Vehiculo.D_EditVehiculoController;
import GUI.Vehiculo.D_SustitucionDevolucionController;
import GUI.Vehiculo.D_SustitucionEntregaController;
import Logica.BD.Conexion;
import Modelo.BotonVentana;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.ClienteParticularEmpresaDireccionVehiculo;
import Modelo.Documento;
import Modelo.GestorVentana;
import Modelo.ProveedorCompaniaDireccion;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Inicio extends Application {

	private static Stage escenario; // Donde se cargan las escenas (interfaces)
	public Scene scene;
	private BorderPane root;

	// Variables globales para todo el proyecto
	private final static String DBFILENAME = "AruGestDB";
	public static Conexion CONEXION = new Conexion();
	public static String OPCION_NUEVA = ""; // Para saber si se ha pulsado en
											// Nueva Factura o Nuevo Presupuesto
	public static int CLIENTE_ID;
	public static int VEHICULO_ID;
	public static int FACTURA_ID;
	public static float PRECIO_HORA;
	public static float PRECIO_IVA;
	public static int NUM_PRESUPUESTO;
	public static int NUM_FACTURA;
	public static String USUARIO;
	public static String PASS;
	public static boolean AUTOLOGIN;
	public static String RUTA_FACTURAS;
	public static double ANCHO_PANTALLA;
	public static double ALTO_PANTALLA;
	public static ArrayList<GestorVentana> LISTA_VENTANAS = new ArrayList<GestorVentana>();
	public static BotonVentana BOTON1 = new BotonVentana(0, false, "");
	public static BotonVentana BOTON2 = new BotonVentana(0, false, "");
	public static BotonVentana BOTON3 = new BotonVentana(0, false, "");
	public static boolean CAMBIAR_RESOLUCION = false; // Se pondrá a true cuando
														// sea necesario
														// sea necesario
														// cambiar
	// la resolucion porque
	// la pantalla es más
	// pequeña de 1680*1010
	// public static boolean ESINICIO = true;

	public void init() throws Exception {
		// comprobacionesIniciales();
	}

	/**
	 * Se comprueba si existe la BD y si no existe se crea
	 */
	public void comprobacionesIniciales() {
		// Se obtiene la ruta de AruGest que es:
		// C:/AruGest/AruGestDB.h2.db
		File f = new File(".");
		String ruta = f.getAbsolutePath();
		f.delete();
		int index = ruta.lastIndexOf(':');
		ruta = ruta.replace(ruta.substring(index + 1), "");

		String spath = ruta + "\\AruGest";
		// Se comprueba si existe la base de datos
		// Esto devuelve true o false si existe ese archivo en esa ruta
		boolean exists = new File(spath, DBFILENAME + ".h2.db").exists();
		if (!exists) {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Bienvenido a AruGest", "Enhorabuena por utilizar AruGest!",
					"No se ha detectado ninguna base de datos así que cuando cierres este mensaje se creará una nueva. Espero que AruGest cumpla tus espectativas! =D");
			spath += "\\" + DBFILENAME;
			String urlDB = "jdbc:h2:file:" + spath.replaceAll("\\\\", "/");
			// System.out.println(urlDB);
			Hilo.hilo_CreaBD(urlDB);
		} else {
			// System.out.println("La Base de Datos ya existe");
		}
	}

	@Override
	public void start(Stage primaryStage) {
		escenario = primaryStage;

		// Comprobar si existe la BD y sino crearla
		comprobacionesIniciales();

		// Se crea la conexión con la BD
		CONEXION.crearConexion();

		// Obtener las opciones
		Inicio.CONEXION.getPrecioHoraIva();

		// Se obtienen los datos de la pantalla
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		ANCHO_PANTALLA = primaryScreenBounds.getWidth();
		ALTO_PANTALLA = primaryScreenBounds.getHeight();
		CAMBIAR_RESOLUCION = true;

		// Abre login
		if (abreLogin()) {
			// Abre la ventana principal de la aplicación
			abreVentanaPrincipal();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	public BorderPane getRoot() {
		return root;
	}

	public static String getOpcionNueva() {
		return OPCION_NUEVA;
	}

	public static void setOpcionNueva(String opcionNueva) {
		Inicio.OPCION_NUEVA = opcionNueva;
	}

	/**
	 * Abre el diálogo del login
	 * 
	 * @return true si OK, false en los demás casos.
	 */
	public static boolean abreLogin() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/D_Login.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entrar");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_LoginController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			Utilidades.mostrarError(e);
			return false;
		}
	}

	/**
	 * Abre la ventana principal de la aplicación
	 */
	public void abreVentanaPrincipal() {
		escenario.setTitle("AruGest Software");

		// Ponemos nuestro propio icono de la aplicación
		escenario.getIcons().add(new Image("file:images/logo_coche.png"));

		FXMLLoader loader = new FXMLLoader(Inicio.class.getResource("/GUI/V_Root.fxml"));
		try {
			// 1.- Crear la escena desde el AnchorPane
			root = (BorderPane) loader.load();
			scene = new Scene(root, ANCHO_PANTALLA, ALTO_PANTALLA);
			if (CAMBIAR_RESOLUCION) {
				Utilidades.ajustarResolucionEscenario(escenario, ANCHO_PANTALLA, ALTO_PANTALLA);
			}
			// System.out.println("Anchura: " + primaryScreenBounds.getWidth() +
			// " ; Altura: " + primaryScreenBounds.getHeight());
			// 2.- Ponerla y mostrarla
			escenario.setScene(scene);
			ResponsiveHandler.addResponsiveToWindow(escenario);

			// Maximizado
			escenario.setMaximized(true);

			// Mostrar
			escenario.show();

			// 3.- Poner el controlador de la escena
			V_RootController controlador = loader.getController();
			controlador.setMainAPP(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Abre un diálogo para añadir un nuevo vehículo o editar uno
	 * 
	 * @param vehiculo
	 * @return true si el usuario a pulsado OK, false en los demás casos.
	 */
	public static boolean mostrarEditorVehiculo(Vehiculo v) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/D_EditVehiculo.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Vehículo");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_EditVehiculoController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			// if(v != null){
			controller.setVehiculo(v);
			// }

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre un diálogo para añadir un nuevo cliente o editar uno
	 * 
	 * @param cliente
	 *            a ser editado
	 * @return true si el usuario a pulsado OK, false en los demás casos.
	 */
	public static boolean mostrarEditorCliente(ClienteParticularEmpresaDireccion cped) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Cliente/D_EditCliente.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cliente");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_EditClienteController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			// if(v != null){
			controller.setCliente(cped);
			// }

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean mostrarEditorCia(ProveedorCompaniaDireccion pcd, int tipo) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/ProveedorCompania/D_EditCia.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			switch (tipo) {
			case 0: // 0 = Cia
				dialogStage.setTitle("Compañía");
				break;
			case 1: // 1 = Proveedor
				dialogStage.setTitle("Proveedor");
				break;
			case 2: // 2 = Desguace
				dialogStage.setTitle("Desguace");
				break;
			}
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_EditCiaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCia(pcd);
			if (tipo == 0) {
				controller.setEsDesguace(false);
				controller.setEsCia(true);
			} else if (tipo == 1) {
				controller.setEsDesguace(false);
				controller.setEsCia(false);
			} else {
				controller.setEsDesguace(true);
				controller.setEsCia(false);
			}

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Muestra el diálogo para preguntar la fecha y las observaciones de un
	 * vehículo de sustitución
	 * 
	 * @param
	 * @return
	 */
	public static boolean mostrarD_SustitucionDevolucion(VehiculoSustitucionClienteVehiculo vscv) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/D_SustitucionDevolucion.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Vehículo sustitución");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_SustitucionDevolucionController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setVehiculoSustitucion(vscv);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Muestra el diálogo para la entrega de un vehículo de sustitución
	 * 
	 * @param vscv
	 * @return
	 */
	public static boolean mostrarD_SustitucionEntrega(VehiculoSustitucionClienteVehiculo vscv) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/D_SustitucionEntrega.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Vehículo sustitución");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			D_SustitucionEntregaController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setVehiculoSustitucion(vscv);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre un diálogo para las opciones
	 * 
	 * @param
	 * @return true si el usuario a pulsado OK, false en los demás casos.
	 */
	public static boolean mostrarDialogoOpciones() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/D_Opciones.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Opciones");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_OpcionesController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el selector de cliente y vehículo desde una factura
	 * 
	 * @return
	 */
	public static boolean abrirSelectorFactura(ClienteParticularEmpresaDireccionVehiculo cpedv) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/D_SelectorClienteVehiculo.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Datos cliente / vehículo");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_SelectorClienteVehiculoController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			// if(v != null){
			controller.setClienteVehiculo(cpedv);
			// }

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			Utilidades.mostrarError(e);
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el selector de golpes predefinidos
	 * 
	 * @param cpedv
	 * @return
	 */
	public static int abrirSelectorGolpes() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/D_SelectorGolpes.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Seleccionar golpe predefinido");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			D_SelectorGolpesController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.cargarGolpes();

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.getId();
		} catch (IOException e) {
			Utilidades.mostrarError(e);
			// e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Abre el diálogo para agregar un documento
	 * 
	 * @return Documento a añadir
	 */
	public static Documento abrirAgregaDocumento() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Cliente/D_AgregaDocumento.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Agregar documento");
			dialogStage.initModality(Modality.NONE);
			dialogStage.initOwner(escenario);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Poner el controlador.
			D_AgregaDocumentoController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			// controller.cargarGolpes();

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.getDocumento();
		} catch (IOException e) {
			Utilidades.mostrarError(e);
			return null;
		}
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
