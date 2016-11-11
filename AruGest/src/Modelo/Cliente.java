package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente {
	private final IntegerProperty idcliente;
	private final StringProperty nombre;
    private final StringProperty telf1;
    private final StringProperty telf2;
    private final StringProperty telf3;
    private final IntegerProperty direccionID;
	
    public Cliente() {
    	this.idcliente = null;
		this.nombre = null;
		this.telf1 = null;
		this.telf2 = null;
		this.telf3 = null;
		this.direccionID = null;
	}

	public Cliente(IntegerProperty idcliente, StringProperty nombre, StringProperty telf1, StringProperty telf2,
			StringProperty telf3, IntegerProperty direccionID) {
		super();
		this.idcliente = idcliente;
		this.nombre = nombre;
		this.telf1 = telf1;
		this.telf2 = telf2;
		this.telf3 = telf3;
		this.direccionID = direccionID;
	}
	
	public Cliente(Integer idcliente, String nombre, String telf1, String telf2){
		this.idcliente = new SimpleIntegerProperty(idcliente);
		this.nombre = new SimpleStringProperty(nombre);
		this.telf1 = new SimpleStringProperty(telf1);
		this.telf2 = new SimpleStringProperty(telf2);
		this.telf3 = null;
		this.direccionID = null;
	}
	
	public Cliente(Integer idcliente, String nombre, String telf1, String telf2, String telf3, Integer direccionID){
		this.idcliente = new SimpleIntegerProperty(idcliente);
		this.nombre = new SimpleStringProperty(nombre);
		this.telf1 = new SimpleStringProperty(telf1);
		this.telf2 = new SimpleStringProperty(telf2);
		this.telf3 = new SimpleStringProperty(telf3);
		this.direccionID = new SimpleIntegerProperty(direccionID);
	}
	
	public Cliente(String nombre, String telf1, String telf2){
		this.idcliente = null;
		this.nombre = new SimpleStringProperty(nombre);
		this.telf1 = new SimpleStringProperty(telf1);
		this.telf2 = new SimpleStringProperty(telf2);
		this.telf3 = null;
		this.direccionID = null;
	}
	
	public Cliente(String nombre){
		this.idcliente = null;
		this.nombre = new SimpleStringProperty(nombre);
		this.telf1 = null;
		this.telf2 = null;
		this.telf3 = null;
		this.direccionID = null;
	}
    
    public IntegerProperty idclienteProperty() {
		return this.idcliente;
	}
	
	public int getIdcliente() {
		return this.idclienteProperty().get();
	}
	
	public void setIdcliente(final int idcliente) {
		this.idclienteProperty().set(idcliente);
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
	
	public StringProperty telf1Property() {
		return this.telf1;
	}
	
	public String getTelf1() {
		return this.telf1Property().get();
	}
	
	public void setTelf1(final String telf1) {
		this.telf1Property().set(telf1);
	}
	
	public StringProperty telf2Property() {
		return this.telf2;
	}
	
	public String getTelf2() {
		return this.telf2Property().get();
	}
	
	public void setTelf2(final String telf2) {
		this.telf2Property().set(telf2);
	}
	
	public StringProperty telf3Property() {
		return this.telf3;
	}
	
	public String getTelf3() {
		return this.telf3Property().get();
	}
	
	public void setTelf3(final String telf3) {
		this.telf3Property().set(telf3);
	}
	
	public IntegerProperty direccionIDProperty() {
		return this.direccionID;
	}
	
	public int getDireccionID() {
		return this.direccionIDProperty().get();
	}
	
	public void setDireccionID(final int direccionID) {
		this.direccionIDProperty().set(direccionID);
	}
	
    
    
}
