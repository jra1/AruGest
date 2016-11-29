package GUI;

import java.io.IOException;
import java.util.ArrayList;

import GUI.Cliente.V_BuscarClienteController;
import GUI.Cliente.V_ClienteController;
import GUI.Contabilidad.V_BuscarFacturaController;
import GUI.Contabilidad.V_NuevaFacturaController;
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
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class V_RootController {

	// Variables de la vista
	@FXML
	private Button btnPantalla1;
	@FXML
	private Button btnPantalla2;
	@FXML
	private Button btnPantalla3;

	@FXML
	private Accordion acor;
	@FXML
	private TitledPane tPane;
	@FXML
	private Button btnNuevaFactura;
	@FXML
	private Button btnNuevoPresupuesto;

	private Inicio main;

	ArrayList<GestorVentana> lista = new ArrayList<GestorVentana>();
	GestorVentana gv;
	private AnchorPane ap;
	private String nombre;

	public void setMainAPP(Inicio p) {
		main = p;

		// acor.setExpandedPane(tPane); //Para que al iniciar se expanda el
		// panel de contabilidad (Se puede poner el que quiera)
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
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
			// if(Inicio.ESINICIO){
			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(buscarVehiculo);
			ap = (AnchorPane) main.getRoot().getCenter();
			// Inicio.ESINICIO = false;
			// }else{
			// ap = (AnchorPane) main.getRoot().getCenter();
			// main.getRoot().setCenter(buscarVehiculo);
			// }
			gv = new GestorVentana(ap, nombre);
			gestionarPantallas();

			// Poner el controlador de la nueva vista.
			V_BuscarVehiculoController controller = loader.getController();
			controller.setMainAPP(main);

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
			nombre = "Nueva Factura";
			// if(Inicio.ESINICIO){
			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(nuevaFactura);
			ap = (AnchorPane) main.getRoot().getCenter();
			// Inicio.ESINICIO = false;
			// }else{
			// ap = (AnchorPane) main.getRoot().getCenter();
			// // Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(nuevaFactura);
			// }
			gv = new GestorVentana(ap, nombre);
			gestionarPantallas();

			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(nuevaFactura);

			// Poner el controlador de la nueva vista.
			V_NuevaFacturaController controller = loader.getController();
			controller.setMainAPP(main);

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
			// if(Inicio.ESINICIO){
			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(nuevaFactura);
			ap = (AnchorPane) main.getRoot().getCenter();
			// Inicio.ESINICIO = false;
			// }else{
			// ap = (AnchorPane) main.getRoot().getCenter();
			// // Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(nuevaFactura);
			// }
			gv = new GestorVentana(ap, nombre);
			gestionarPantallas();

			// Poner el controlador de la nueva vista.
			V_BuscarFacturaController controller = loader.getController();
			controller.setMainAPP(main);

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
					main.getRoot().setCenter(cliente);

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
						"Ocurrió un error al guardar el cliente en la base de datos.");
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
			// if(Inicio.ESINICIO){
			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(buscarCliente);
			ap = (AnchorPane) main.getRoot().getCenter();
			// Inicio.ESINICIO = false;
			// }else{
			// ap = (AnchorPane) main.getRoot().getCenter();
			// // Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(buscarCliente);
			// }
			gv = new GestorVentana(ap, nombre);
			gestionarPantallas();

			// Poner el controlador de la nueva vista.
			V_BuscarClienteController controller = loader.getController();
			controller.setMainAPP(main);
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
			// if(Inicio.ESINICIO){
			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(vs);
			ap = (AnchorPane) main.getRoot().getCenter();
			// Inicio.ESINICIO = false;
			// }else{
			// ap = (AnchorPane) main.getRoot().getCenter();
			// // Poner la nueva vista en el centro del root
			// main.getRoot().setCenter(vs);
			// }
			gv = new GestorVentana(ap, nombre);
			gestionarPantallas();

			// Poner el controlador de la nueva vista.
			V_VehiculosSustitucionController controller = loader.getController();
			controller.setMainAPP(main);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void gestionarPantallas() {
		boolean esta = false;
		switch (lista.size()) {
		case 0:
			lista.add(0, gv);
			break;
		case 1:
			for (GestorVentana gv : lista) {
				if (gv.getNombre().equalsIgnoreCase(nombre)) {
					esta = true;
				}
			}
			if (!esta) {
				lista.add(0, gv);
			}
			break;
		case 2:
			for (GestorVentana gv : lista) {
				if (gv.getNombre().equalsIgnoreCase(nombre)) {
					esta = true;
				}
			}
			if (!esta) {
				lista.add(0, gv);
			}
			break;
		case 3:
			for (GestorVentana gv : lista) {
				if (gv.getNombre().equalsIgnoreCase(nombre)) {
					esta = true;
				}
			}
			if (!esta) {
				lista.remove(2);
				lista.add(0, gv);
			}
			break;
		default:
			break;
		}

		switch (lista.size()) {
		case 0:
			btnPantalla1.setVisible(false);
			btnPantalla2.setVisible(false);
			btnPantalla3.setVisible(false);
			break;
		case 1:
			btnPantalla1.setText(lista.get(0).getNombre());
			btnPantalla1.setVisible(true);
			btnPantalla2.setVisible(false);
			btnPantalla3.setVisible(false);
			break;
		case 2:
			btnPantalla1.setText(lista.get(0).getNombre());
			btnPantalla1.setVisible(true);
			btnPantalla2.setText(lista.get(1).getNombre());
			btnPantalla2.setVisible(true);
			btnPantalla3.setVisible(false);
			break;
		case 3:
			btnPantalla1.setText(lista.get(0).getNombre());
			btnPantalla1.setVisible(true);
			btnPantalla2.setText(lista.get(1).getNombre());
			btnPantalla2.setVisible(true);
			btnPantalla3.setText(lista.get(2).getNombre());
			btnPantalla3.setVisible(true);
			break;
		default:
			break;
		}

	}

	/**
	 * Va a la página 1
	 */
	@FXML
	private void atras1() {
		try {
			// Poner la nueva vista en el centro del root
			main.getRoot().setCenter(lista.get(0).getAp());

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
			main.getRoot().setCenter(lista.get(1).getAp());
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
			main.getRoot().setCenter(lista.get(2).getAp());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
