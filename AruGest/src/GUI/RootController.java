package GUI;

import java.io.IOException;

import Logica.Inicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class RootController {
	
	@FXML
	private Accordion acor;

	@FXML
	private TitledPane tPane;
	
	private Inicio main;
	
	public void setMainAPP(Inicio p){
	     main=p;
	     
	     //acor.setExpandedPane(tPane); //Para que al iniciar se expanda el panel de contabilidad (Se puede poner el que quiera)
	}
	
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
