package GUI;

import java.io.IOException;

import com.guigarage.responsive.ResponsiveHandler;

import GUI.Cliente.V_BuscarClienteController;
import GUI.Cliente.V_ClienteController;
import GUI.Contabilidad.V_BuscarFacturaController;
import GUI.Contabilidad.V_NuevaFacturaController;
import GUI.Documento.V_BuscarDocumentoController;
import GUI.ProveedorCompania.V_BuscarProveedorCompaniaController;
import GUI.Vehiculo.V_BuscarVehiculoController;
import GUI.Vehiculo.V_GolpesPredefinidosController;
import GUI.Vehiculo.V_VehiculosSustitucionController;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Cliente;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.GestorVentana;
import Modelo.ProveedorCompaniaDireccion;
import Modelo.Vehiculo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class V_RootController {

	// Variables de la vista
	@FXML
	public Button btnPantalla1;
	@FXML
	public Button btnPantalla2;
	@FXML
	public Button btnPantalla3;

	@FXML
	private Menu btnMenuInicio;
	@FXML
	private Menu btnMenuVerUltimas;

	@FXML
	private ImageView imgClientes;
	@FXML
	private ImageView imgVehi;
	@FXML
	private ImageView imgConta;
	@FXML
	private ImageView imgCias;
	@FXML
	private ImageView imgDocu;
	@FXML
	private Accordion acor;
	@FXML
	private TitledPane tPane;
	@FXML
	private Button btnNuevaFactura;
	@FXML
	private Button btnNuevoPresupuesto;

	@FXML
	private ScrollPane sp;

	private Inicio main;

	GestorVentana gv;
	private AnchorPane ap;
	private String nombre;

	public void setMainAPP(Inicio p) {
		main = p;

		acor.setExpandedPane(tPane); // Para que al iniciar se expanda el
		// panel de contabilidad (Se puede poner el que quiera)
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		if (Inicio.CAMBIAR_RESOLUCION) {
			sp.setPrefWidth(Inicio.ANCHO_PANTALLA - 260);
			sp.setPrefHeight(Inicio.ALTO_PANTALLA - 45);
		}
		// Añadir un listener a los botones de Nuevo Presupuesto y Nueva Factura
		btnNuevaFactura.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Inicio.setOpcionNueva("F");
				nuevoPresupuesto();
			}
		});
		btnNuevoPresupuesto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Inicio.setOpcionNueva("P");
				nuevoPresupuesto();
			}
		});
		imgClientes.setImage(new Image("recursos/images/clients.png"));
		imgVehi.setImage(new Image("recursos/images/vehiculo.png"));
		imgConta.setImage(new Image("recursos/images/conta.png"));
		imgCias.setImage(new Image("recursos/images/agency.png"));
		imgDocu.setImage(new Image("recursos/images/docus.png"));

		final MenuItem menuItem = new MenuItem();
		btnMenuInicio.getItems().add(menuItem);
		btnMenuInicio.addEventHandler(Menu.ON_SHOWN, event -> btnMenuInicio.hide());
		btnMenuInicio.addEventHandler(Menu.ON_SHOWING, event -> btnMenuInicio.fire());

		final MenuItem menuItem2 = new MenuItem();
		btnMenuVerUltimas.getItems().add(menuItem2);
		btnMenuVerUltimas.addEventHandler(Menu.ON_SHOWN, event -> btnMenuVerUltimas.hide());
		btnMenuVerUltimas.addEventHandler(Menu.ON_SHOWING, event -> btnMenuVerUltimas.fire());
	}

	/**
	 * Coloca la ventana de buscar vehículo
	 */
	@FXML
	public void buscarVehiculo() {
		try {
			// Cargar la vista de buscar vehiculo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/V_BuscarVehiculo.fxml"));
			AnchorPane buscarVehiculo = (AnchorPane) loader.load();
			nombre = "Buscar Vehículo";
			// Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(buscarVehiculo);
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(buscarVehiculo, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(buscarVehiculo);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_BuscarVehiculoController controller = loader.getController();
			controller.setMainAPP(main);
			controller.setScrollPane(sp);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			// controller.setFocus();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Se llama cuando el usuario pulsa en Añadir vehículo de sustitución
	 */
	@FXML
	private void nuevoVehiculoSustitucion() {
		Vehiculo v = new Vehiculo(0);
		boolean okClicked = Inicio.mostrarEditorVehiculo(v, true);
		if (okClicked) {
			// Cuando llega aqui son correctos los datos introducidos
			if (Inicio.CONEXION.guardarVehiculo(v)) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Vehículo de sustitución guardado",
						"Se ha guardado el vehículo en la base de datos.");
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el vehículo",
						"Ocurrió un error al guardar el vehículo en la base de datos.");
			}
		}
	}

	/**
	 * Abre la ventana de nuevo Presupuesto / Factura
	 */
	@FXML
	private void nuevoPresupuesto() {
		try {
			// Cargar la vista de nueva factura
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_NuevaFactura.fxml"));
			AnchorPane nuevaFactura = (AnchorPane) loader.load();

			nombre = "Nueva Factura";

			// Poner la nueva vista en el centro del root
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(nuevaFactura, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(nuevaFactura);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_NuevaFacturaController controller = loader.getController();
			controller.setMainAPP(main);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			// controller.setFocus();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Coloca la ventana de buscar presupuesto / factura
	 */
	@FXML
	private void buscarPresupuesto() {
		try {
			// Cargar la vista de nueva factura
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_BuscarFactura.fxml"));
			AnchorPane nuevaFactura = (AnchorPane) loader.load();
			nombre = "Buscar Factura";
			// Poner la nueva vista en el centro del root
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(nuevaFactura, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(nuevaFactura);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_BuscarFacturaController controller = loader.getController();
			controller.setMainAPP(main);
			controller.setScrollPane(sp);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Coloca la ventana de buscar proveedor / compañía
	 */
	@FXML
	private void buscarCompania() {
		try {
			// Cargar la vista de nueva factura
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/ProveedorCompania/V_BuscarProveedorCompania.fxml"));
			AnchorPane buscar = (AnchorPane) loader.load();
			nombre = "Buscar proveedor / compañía";
			// Poner la nueva vista en el centro del root
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(buscar, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(buscar);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_BuscarProveedorCompaniaController controller = loader.getController();
			controller.setMainAPP(main);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			/*
			 * controller.setScrollPane(sp); controller.boton1 = btnPantalla1;
			 * controller.boton2 = btnPantalla2; controller.boton3 =
			 * btnPantalla3;
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Abre el ventanuco de nuevo Cliente
	 */
	@FXML
	private void nuevoCliente() {
		ClienteParticularEmpresaDireccion cped = new ClienteParticularEmpresaDireccion();
		boolean okClicked = Inicio.mostrarEditorCliente(cped);
		if (okClicked) {
			// Cuando llega aqui son correctos los datos introducidos
			if (Inicio.CONEXION.guardarCliente(cped)) {
				try {
					// Cargar la vista de Cliente
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Inicio.class.getResource("/GUI/Cliente/V_Cliente.fxml"));
					AnchorPane cliente = (AnchorPane) loader.load();

					// Poner la nueva vista en el centro del root
					Utilidades.ajustarResolucionAnchorPane(cliente, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
					sp.setContent(cliente);
					ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());

					// Poner el controlador de la nueva vista.
					V_ClienteController controller = loader.getController();
					controller.setMainAPP(main);
					controller.setScrollPane(sp);
					controller.boton1 = btnPantalla1;
					controller.boton2 = btnPantalla2;
					controller.boton3 = btnPantalla3;
					Cliente c = null;
					if (cped.getParticular() != null) {
						c = Inicio.CONEXION.buscarClientePorDni(cped.getParticular().getNif(), 1);
						cped.setParticular(Inicio.CONEXION.buscarParticularPorClienteID(c.getIdcliente()));
					} else if (cped.getEmpresa() != null) {
						c = Inicio.CONEXION.buscarClientePorDni(cped.getEmpresa().getCif(), 2);
						cped.setEmpresa(Inicio.CONEXION.buscarEmpresaPorClienteID(c.getIdcliente()));
					}
					Inicio.CLIENTE_ID = c.getIdcliente();
					cped.setCliente(c);

					nombre = "Cliente: " + c.getNombre();
					ap = (AnchorPane) sp.getContent();
					gv = new GestorVentana(ap, nombre);
					Utilidades.gestionarPantallas(gv);
					btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
					btnPantalla1.setText(Inicio.BOTON1.getNombre());
					btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
					btnPantalla2.setText(Inicio.BOTON2.getNombre());
					btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
					btnPantalla3.setText(Inicio.BOTON3.getNombre());

					controller.cargaCliente(cped);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el cliente",
						"Ocurrió un error al guardar el cliente en la base de datos.");
			}
		}
	}

	/**
	 * Abre el diálogo de nueva Cía
	 */
	@FXML
	private void nuevaCia() {
		ProveedorCompaniaDireccion pcd = new ProveedorCompaniaDireccion(true);
		boolean okClicked = Inicio.mostrarEditorCia(pcd, 0);
		if (okClicked) {
			// Cuando llega aqui son correctos los datos introducidos
			if (Inicio.CONEXION.guardarCia(pcd)) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito", "Compañía guardada en la base de datos", "");
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar la compañía",
						"Ocurrió un error al guardar la compañía en la base de datos.");
			}
		}
	}

	/**
	 * Abre el diálogo de nuevo proveedor
	 */
	@FXML
	private void nuevoProve() {
		ProveedorCompaniaDireccion pcd = new ProveedorCompaniaDireccion(false);
		boolean okClicked = Inicio.mostrarEditorCia(pcd, 1);
		if (okClicked) {
			// Cuando llega aqui son correctos los datos introducidos
			if (Inicio.CONEXION.guardarCia(pcd)) {
				Utilidades.mostrarAlerta(AlertType.INFORMATION, "Éxito",
						"Proveedor/desguace guardado en la base de datos", "");
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el proveedor/desguace",
						"Ocurrió un error al guardar el proveedor/desguace en la base de datos.");
			}
		}
	}

	/**
	 * Coloca la ventana de buscar cliente
	 */
	@FXML
	public void buscarCliente() {
		try {
			// Cargar la vista de buscar vehiculo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Cliente/V_BuscarCliente.fxml"));
			AnchorPane buscarCliente = (AnchorPane) loader.load();
			nombre = "Buscar Cliente";
			// Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(buscarCliente);

			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(buscarCliente, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(buscarCliente);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_BuscarClienteController controller = loader.getController();
			controller.setMainAPP(main);
			controller.setScrollPane(sp);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			controller.setFocus();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Coloca la ventana de vehículos de sustitución
	 */
	@FXML
	public void vehiculosSustitucion() {
		try {
			// Cargar la vista de buscar vehiculo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/V_VehiculosSustitucion.fxml"));
			AnchorPane vs = (AnchorPane) loader.load();
			nombre = "Vehiculos Sustitucion";
			// Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(vs);
			sp.setVisible(false);
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(vs, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(vs);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			sp.setVisible(true);
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_VehiculosSustitucionController controller = loader.getController();
			controller.setMainAPP(main);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			controller.setFocus();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Coloca la ventana de golpes predefinidos
	 */
	@FXML
	public void golpesPredefinidos() {
		try {
			// Cargar la vista de buscar vehiculo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/V_GolpesPredefinidos.fxml"));
			AnchorPane vs = (AnchorPane) loader.load();
			nombre = "Golpes predefinidos";
			// Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(vs);
			sp.setVisible(false);
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(vs, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(vs);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			sp.setVisible(true);
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_GolpesPredefinidosController controller = loader.getController();
			controller.setMainAPP(main);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			controller.setFocus();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Coloca la ventana de buscar documento
	 */
	@FXML
	public void buscarDocumento() {
		try {
			// Cargar la vista de buscar documento
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Documento/V_BuscarDocumento.fxml"));
			AnchorPane vs = (AnchorPane) loader.load();
			nombre = "Buscar documento";
			// Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(vs);
			sp.setVisible(false);
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(vs, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(vs);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			sp.setVisible(true);
			ap = (AnchorPane) sp.getContent();// main.getRoot().getCenter();
			gv = new GestorVentana(ap, nombre);
			Utilidades.gestionarPantallas(gv);
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

			// Poner el controlador de la nueva vista.
			V_BuscarDocumentoController controller = loader.getController();
			controller.setMainAPP(main);
			controller.setScrollPane(sp);
			controller.boton1 = btnPantalla1;
			controller.boton2 = btnPantalla2;
			controller.boton3 = btnPantalla3;
			controller.setFocus();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Llama al método para abrir el diálogo de opciones
	 */
	@FXML
	private void mostrarOpciones() {
		Inicio.mostrarDialogoOpciones();
	}

	/**
	 * Llama al método para abrir el diálogo de opciones
	 */
	@FXML
	private void menuInicio() {
		try {
			// Cargar la vista de buscar documento
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Documento/V_BuscarDocumento.fxml"));
			AnchorPane vs = new AnchorPane();
			Label l = new Label("AruGest Software");
			l.setId("lblInicio");
			vs.getChildren().add(l);
			// Poner la nueva vista en el centro del root
			sp.setVisible(false);
			// **************************************************************************************************
			Utilidades.ajustarResolucionAnchorPane(vs, Inicio.ANCHO_PANTALLA, Inicio.ALTO_PANTALLA);
			// **************************************************************************************************
			sp.setContent(vs);
			// Esta línea es para que se ejecute la pseudoclase del CSS ya
			ResponsiveHandler.addResponsiveToWindow(main.getScene().getWindow());
			sp.setVisible(true);
			Utilidades.eliminaBotones();
			btnPantalla1.setVisible(Inicio.BOTON1.isVisible());
			btnPantalla1.setText(Inicio.BOTON1.getNombre());
			btnPantalla2.setVisible(Inicio.BOTON2.isVisible());
			btnPantalla2.setText(Inicio.BOTON2.getNombre());
			btnPantalla3.setVisible(Inicio.BOTON3.isVisible());
			btnPantalla3.setText(Inicio.BOTON3.getNombre());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Muestra la ventana de últimas facturas
	 */
	@FXML
	private void menuVerUltimas() {
		Inicio.mostrarD_UltimasFacturas();
	}

	/**
	 * Crea una copia de seguridad
	 */
	@FXML
	private void crearCopiaSeguridad() {
		Inicio.mostrarDialogoOpcionesBackup();
	}

	/**
	 * Muestra la ayuda
	 */
	@FXML
	private void ayuda() {
		Inicio.mostrarD_Ayuda();
	}

	/**
	 * Va a la página 1
	 */
	@FXML
	private void atras1() {
		try {
			// Poner la nueva vista en el centro del root
			sp.setContent(Inicio.LISTA_VENTANAS.get(0).getAp());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Va a la página 2
	 */
	@FXML
	private void atras2() {
		try {
			// Poner la nueva vista en el centro del root
			sp.setContent(Inicio.LISTA_VENTANAS.get(1).getAp());
			// main.getRoot().setCenter(lista.get(1).getAp());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Va a la página 3
	 */
	@FXML
	private void atras3() {
		try {
			// Poner la nueva vista en el centro del root
			sp.setContent(Inicio.LISTA_VENTANAS.get(2).getAp());
			// main.getRoot().setCenter(lista.get(2).getAp());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ocultarBotonesGestor() {
		getBtnPantalla1().setVisible(false);
		getBtnPantalla2().setVisible(false);
		getBtnPantalla3().setVisible(false);
	}

	public ScrollPane getScrollPane() {
		return sp;
	}

	public Button getBtnPantalla1() {
		return btnPantalla1;
	}

	public void setBtnPantalla1(Button btnPantalla1) {
		this.btnPantalla1 = btnPantalla1;
	}

	public Button getBtnPantalla2() {
		return btnPantalla2;
	}

	public void setBtnPantalla2(Button btnPantalla2) {
		this.btnPantalla2 = btnPantalla2;
	}

	public Button getBtnPantalla3() {
		return btnPantalla3;
	}

	public void setBtnPantalla3(Button btnPantalla3) {
		this.btnPantalla3 = btnPantalla3;
	}
}
