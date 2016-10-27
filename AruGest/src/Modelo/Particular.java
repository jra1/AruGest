package Modelo;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Particular {
	private final IntegerProperty idparticular;
	private final IntegerProperty clienteID;
	private final StringProperty nombre;
	private final StringProperty apellidos;
	private final StringProperty nif;
	
	public Particular(IntegerProperty idparticular, IntegerProperty clienteID, StringProperty nombre,
			StringProperty apellidos, StringProperty nif) {
		super();
		this.idparticular = idparticular;
		this.clienteID = clienteID;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.nif = nif;
	}
	
	public Particular(String nombre, String apellidos, String nif){
		this.idparticular = null;
		this.clienteID = null;
		this.nombre = new SimpleStringProperty(nombre);;
		this.apellidos = new SimpleStringProperty(apellidos);
		this.nif = new SimpleStringProperty(nif);
	}

	public IntegerProperty idparticularProperty() {
		return this.idparticular;
	}
	

	public int getIdparticular() {
		return this.idparticularProperty().get();
	}
	

	public void setIdparticular(final int idparticular) {
		this.idparticularProperty().set(idparticular);
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
	

	public StringProperty apellidosProperty() {
		return this.apellidos;
	}
	

	public String getApellidos() {
		return this.apellidosProperty().get();
	}
	

	public void setApellidos(final String apellidos) {
		this.apellidosProperty().set(apellidos);
	}
	

	public StringProperty nifProperty() {
		return this.nif;
	}
	

	public String getNif() {
		return this.nifProperty().get();
	}
	

	public void setNif(final String nif) {
		this.nifProperty().set(nif);
	}
	
}
