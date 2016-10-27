package Modelo;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Material {
	private final IntegerProperty idmaterial;
	private final StringProperty nombre;
	private final StringProperty preciounit;
	private final IntegerProperty facturaID;
	private final IntegerProperty cantidad;
	private final FloatProperty preciototal;
	
	public Material(Integer idmaterial, String nombre, String preciounit,
			Integer facturaID, Integer cantidad, Float preciototal) {
		super();
		this.idmaterial = new SimpleIntegerProperty(idmaterial);
		this.nombre = new SimpleStringProperty(nombre);
		this.preciounit = new SimpleStringProperty(preciounit);
		this.facturaID = new SimpleIntegerProperty(facturaID);
		this.cantidad = new SimpleIntegerProperty(cantidad);
		this.preciototal = new SimpleFloatProperty(preciototal);
	}

	public IntegerProperty idmaterialProperty() {
		return this.idmaterial;
	}
	

	public int getIdmaterial() {
		return this.idmaterialProperty().get();
	}
	

	public void setIdmaterial(final int idmaterial) {
		this.idmaterialProperty().set(idmaterial);
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
	

	public StringProperty preciounitProperty() {
		return this.preciounit;
	}
	

	public String getPreciounit() {
		return this.preciounitProperty().get();
	}
	

	public void setPreciounit(final String preciounit) {
		this.preciounitProperty().set(preciounit);
	}
	

	public IntegerProperty facturaIDProperty() {
		return this.facturaID;
	}
	

	public int getFacturaID() {
		return this.facturaIDProperty().get();
	}
	

	public void setFacturaID(final int facturaID) {
		this.facturaIDProperty().set(facturaID);
	}
	

	public IntegerProperty cantidadProperty() {
		return this.cantidad;
	}
	

	public int getCantidad() {
		return this.cantidadProperty().get();
	}
	

	public void setCantidad(final int cantidad) {
		this.cantidadProperty().set(cantidad);
	}
	

	public FloatProperty preciototalProperty() {
		return this.preciototal;
	}
	

	public float getPreciototal() {
		return this.preciototalProperty().get();
	}
	

	public void setPreciototal(final float preciototal) {
		this.preciototalProperty().set(preciototal);
	}
	
	
	
}
