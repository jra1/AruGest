package Logica;

import java.io.IOException;
//import java.security.Principal;

import GUI.D_OpcionesController;
import GUI.V_RootController;
import GUI.Cliente.D_EditClienteController;
import GUI.Vehiculo.D_EditVehiculoController;
import GUI.Vehiculo.D_SustitucionDevolucionController;
import GUI.Vehiculo.D_SustitucionEntregaController;
import Logica.BD.Conexion;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Inicio extends Application {

	private static Stage escenario; // Donde se cargan las escenas (interfaces)
	private BorderPane root;

	// Variables globales para todo el proyecto
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
	public static double ANCHO_PANTALLA;
	public static double ALTO_PANTALLA;
	public static boolean CAMBIAR_RESOLUCION = false; // Se pondrá a true cuando
														// sea necesario cambiar
														// la resolucion porque
														// la pantalla es más
														// pequeña de 1680*1010
	// public static boolean ESINICIO = true;

	public void init() throws Exception {
		CONEXION.crearConexion();
		// Obtener los precios de Hora e IVA
		Inicio.CONEXION.getPrecioHoraIva();
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		if (primaryScreenBounds.getWidth() < 1680 || primaryScreenBounds.getHeight() < 1010) {
			ANCHO_PANTALLA = primaryScreenBounds.getWidth();
			ALTO_PANTALLA = primaryScreenBounds.getHeight();
			CAMBIAR_RESOLUCION = true;
			// Utilidades.ajustarEscena(escenario,
			// primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
		}
	}

	@Override
	public void start(Stage primaryStage) {
		escenario = primaryStage;
		escenario.setTitle("AruGest Software");

		// Ponemos nuestro propio icono de la aplicación
		escenario.getIcons().add(new Image("file:images/logo_coche.png"));

		// Maximizado
		escenario.setMaximized(true);

		FXMLLoader loader = new FXMLLoader(Inicio.class.getResource("/GUI/V_Root.fxml"));
		try {
			// 1.- Crear la escena desde el AnchorPane
			root = (BorderPane) loader.load();
			Scene escena = new Scene(root, ANCHO_PANTALLA, ALTO_PANTALLA);
			if (CAMBIAR_RESOLUCION) {
				Utilidades.ajustarResolucion(escenario, ANCHO_PANTALLA, ALTO_PANTALLA);
			}
			// System.out.println("Anchura: " + primaryScreenBounds.getWidth() +
			// " ; Altura: " + primaryScreenBounds.getHeight());
			// 2.- Ponerla y mostrarla
			escenario.setScene(escena);
			escenario.show();
			// 3.- Poner el controlador de la escena
			V_RootController controlador = loader.getController();
			controlador.setMainAPP(this);

		} catch (IOException e) {
			e.printStackTrace();
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
	 * Abre un diálogo para añadir un nuevo vehículo o editar uno
	 * 
	 * @param vehiculo
	 *            a ser editado
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

}
