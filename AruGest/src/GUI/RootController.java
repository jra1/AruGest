package GUI;

import java.io.IOException;

import javax.swing.JOptionPane;

import Logica.Inicio;
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
	 * Abre la ventana de nuevo Cliente
	 */
	@FXML
	private void nuevoCliente(){
		try {
            // Cargar la vista de nueva factura
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Inicio.class.getResource("/GUI/Cliente.fxml"));
            AnchorPane nuevoCliente = (AnchorPane) loader.load();
        	
            // Poner la nueva vista en el centro del root
            main.getRoot().setCenter(nuevoCliente);
            
            // Poner el controlador de la nueva vista.
            ClienteController controller = loader.getController();
            controller.setMainAPP(main);

        } catch (IOException e) {
            e.printStackTrace();
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
            AnchorPane nuevaFactura = (AnchorPane) loader.load();
        	
            // Poner la nueva vista en el centro del root
            main.getRoot().setCenter(nuevaFactura);
            
            // Poner el controlador de la nueva vista.
            BuscarClienteController controller = loader.getController();
            controller.setMainAPP(main);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
