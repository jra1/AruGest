package GUI;

import java.io.IOException;

import Logica.Inicio;
import Modelo.ClienteParticularEmpresaDireccion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class RootController {
	
	//Variables de la vista
	@FXML
	private Accordion acor;
	@FXML
	private TitledPane tPane;
	@FXML
	private Button btnNuevaFactura;
	@FXML
	private Button btnNuevoPresupuesto;
	
	private Inicio main;
	
	private AnchorPane ap;
	
	public void setMainAPP(Inicio p){
	     main=p;
	     
	     //acor.setExpandedPane(tPane); //Para que al iniciar se expanda el panel de contabilidad (Se puede poner el que quiera)
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
	 * Abre la ventana de nuevo Presupuesto / Factura
	 */
	@FXML
	private void nuevoPresupuesto(){
		try {
            // Cargar la vista de nueva factura
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Inicio.class.getResource("/GUI/NuevaFactura.fxml"));
            AnchorPane nuevaFactura = (AnchorPane) loader.load();
        	ap = nuevaFactura;
            // Poner la nueva vista en el centro del root
            main.getRoot().setCenter(nuevaFactura);
            
            // Poner el controlador de la nueva vista.
            NuevaFacturaController controller = loader.getController();
            controller.setMainAPP(main);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Coloca la ventana de buscar presupuesto / factura
	 */
	@FXML
	private void buscarPresupuesto(){
		try {
            // Cargar la vista de nueva factura
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Inicio.class.getResource("/GUI/BuscarFactura.fxml"));
            AnchorPane nuevaFactura = (AnchorPane) loader.load();
        	ap=nuevaFactura;
            // Poner la nueva vista en el centro del root
            main.getRoot().setCenter(nuevaFactura);
            
            // Poner el controlador de la nueva vista.
            BuscarFacturaController controller = loader.getController();
            controller.setMainAPP(main);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Abre el ventanuco de nuevo Cliente
	 */
	@FXML
	private void nuevoCliente(){
		ClienteParticularEmpresaDireccion cped = new ClienteParticularEmpresaDireccion();
		boolean okClicked = Inicio.mostrarEditorCliente(cped);
		if (okClicked) {
			//Cuando llega aqui son correctos los datos introducidos
			
			/*
			if(Inicio.CONEXION.guardarVehiculo(v)){
				listaVehiculos.add(v);
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error al guardar el cliente");
				alert.setContentText("Ocurrió un error al guardar el cliente en la base de datos.");

				alert.showAndWait();
			}
			*/
		}
	}
	
	/**
	 * Coloca la ventana de buscar cliente
	 */
	@FXML
	private void buscarCliente(){
		try {
            // Cargar la vista de nueva factura
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Inicio.class.getResource("/GUI/BuscarCliente.fxml"));
            AnchorPane buscarCliente = (AnchorPane) loader.load();
        	ap=buscarCliente;
            // Poner la nueva vista en el centro del root
            main.getRoot().setCenter(buscarCliente);
            
            // Poner el controlador de la nueva vista.
            BuscarClienteController controller = loader.getController();
            controller.setMainAPP(main);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Va a la página anterior
	 */
	@FXML
	private void atras(){
		try {
            // Cargar la vista de nueva factura
            //FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(Inicio.class.getResource("/GUI/BuscarCliente.fxml"));
            //AnchorPane nuevaFactura = (AnchorPane) loader.load();
        	
            // Poner la nueva vista en el centro del root
            main.getRoot().setCenter(ap);
            
            // Poner el controlador de la nueva vista.
           // BuscarClienteController controller = loader.getController();
            //controller.setMainAPP(main);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
