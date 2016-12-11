package GUI;

import java.io.IOException;

import GUI.Cliente.V_BuscarClienteController;
import GUI.Cliente.V_ClienteController;
import GUI.Contabilidad.V_BuscarFacturaController;
import GUI.Contabilidad.V_NuevaFacturaController;
import GUI.ProveedorCompania.V_BuscarProveedorCompaniaController;
import GUI.Vehiculo.V_BuscarVehiculoController;
import GUI.Vehiculo.V_VehiculosSustitucionController;
import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Cliente;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.GestorVentana;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
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
	private Accordion acor;
	@FXML
	private TitledPane tPane;
	@FXML
	private Button btnNuevaFactura;
	@FXML
	private Button btnNuevoPresupuesto;

	private Inicio main;
	@FXML
	private ScrollPane sp;

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
		// A�adir un listener a los botones de Nuevo Presupuesto y Nueva Factura
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
	}

	/**
	 * Coloca la ventana de buscar veh�culo
	 */
	@FXML
	public void buscarVehiculo() {
		try {
			// Cargar la vista de buscar vehiculo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Vehiculo/V_BuscarVehiculo.fxml"));
			AnchorPane buscarVehiculo = (AnchorPane) loader.load();
			nombre = "Buscar Veh�culo";
			// Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(buscarVehiculo);
			sp.setContent(buscarVehiculo);
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
	 * Abre la ventana de nuevo Presupuesto / Factura
	 */
	@FXML
	private void nuevoPresupuesto() {
		try {
			// Cargar la vista de nueva factura
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/Contabilidad/V_NuevaFactura.fxml"));
			AnchorPane nuevaFactura = (AnchorPane) loader.load();
			// if (Inicio.CAMBIAR_RESOLUCION) {
			// nuevaFactura.setPrefWidth(Inicio.ANCHO_PANTALLA - 260);
			// nuevaFactura.setPrefHeight(Inicio.ALTO_PANTALLA - 40);
			// nuevaFactura.getStylesheets().add("GUI/EstiloPequenio.css");
			// }

			nombre = "Nueva Factura";

			// Poner la nueva vista en el centro del root
			sp.setContent(nuevaFactura);
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
			sp.setContent(nuevaFactura);
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
	 * Coloca la ventana de buscar proveedor / compa��a
	 */
	@FXML
	private void buscarCompania() {
		try {
			// Cargar la vista de nueva factura
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Inicio.class.getResource("/GUI/ProveedorCompania/V_BuscarProveedorCompania.fxml"));
			AnchorPane buscar = (AnchorPane) loader.load();
			nombre = "Buscar proveedor / compa��a";
			// Poner la nueva vista en el centro del root
			sp.setContent(buscar);
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
					sp.setContent(cliente);
					// main.getRoot().setCenter(cliente);

					// Poner el controlador de la nueva vista.
					V_ClienteController controller = loader.getController();
					controller.setMainAPP(main);
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
					controller.cargaCliente(cped);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar el cliente",
						"Ocurri� un error al guardar el cliente en la base de datos.");
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
			sp.setContent(buscarCliente);
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

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Coloca la ventana de veh�culos de sustituci�n
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
			sp.setContent(vs);
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

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Llama al m�todo para abrir el di�logo de opciones
	 */
	@FXML
	private void mostrarOpciones() {
		Inicio.mostrarDialogoOpciones();
	}

	/**
	 * Va a la p�gina 1
	 */
	@FXML
	private void atras1() {
		try {
			// Poner la nueva vista en el centro del root
			sp.setContent(Inicio.LISTA_VENTANAS.get(0).getAp());
			// main.getRoot().setCenter(lista.get(0).getAp());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Va a la p�gina 2
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
	 * Va a la p�gina 3
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
