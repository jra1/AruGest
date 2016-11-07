package Modelo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Empresa {
	private final IntegerProperty idempresa;
	private final IntegerProperty clienteID;
	private final StringProperty nombre;
    private final StringProperty cif;
    private final BooleanProperty esProveedor;
	
    public Empresa(Integer idempresa, Integer clienteID, String nombre, String cif,
			Boolean esProveedor) {
		super();
		this.idempresa = new SimpleIntegerProperty(idempresa);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.nombre = new SimpleStringProperty(nombre);
		this.cif = new SimpleStringProperty(cif);
		this.esProveedor = new SimpleBooleanProperty(esProveedor);
	}
    
    public Empresa(String nombre, String cif){
    	this.idempresa = null;
		this.clienteID = null;
		this.nombre = new SimpleStringProperty(nombre);
		this.cif = new SimpleStringProperty(cif);
		this.esProveedor = null;
    }
	
    public IntegerProperty idempresaProperty() {
		return this.idempresa;
	}
	
	public int getIdempresa() {
		return this.idempresaProperty().get();
	}
	
	public void setIdempresa(final int idempresa) {
		this.idempresaProperty().set(idempresa);
	}
	
	public IntegerProperty clienteIDProperty() {
		return this.clienteID;
	}
	
	public int getClienteID() {
		return this.clienteIDProperty().get();
	}
	
	public void setClienteID(final int clienteID) {
		this.clienteIDProperty().set(clienteID);
	}
	
	public StringProperty nombreProperty() {
		return this.nombre;
	}
	
	public String getNombre() {
		return this.nombreProperty().get();
	}
	
	public void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}
	
	public StringProperty cifProperty() {
		return this.cif;
	}
	
	public String getCif() {
		return this.cifProperty().get();
	}
	
	public void setCif(final String cif) {
		this.cifProperty().set(cif);
	}
	
	public BooleanProperty esProveedorProperty() {
		return this.esProveedor;
	}
	
	public boolean isEsProveedor() {
		return this.esProveedorProperty().get();
	}
	
	public void setEsProveedor(final boolean esProveedor) {
		this.esProveedorProperty().set(esProveedor);
	}
	
    
}
