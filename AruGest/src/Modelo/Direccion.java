package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Direccion {
	private final IntegerProperty iddireccion;
	private final StringProperty calle;
    private final IntegerProperty numero;
    private final StringProperty piso;
    private final StringProperty letra;
    private final IntegerProperty cpostal;
    private final StringProperty localidad;
    private final StringProperty provincia;
    
    public Direccion(String calle, Integer numero, String piso, String letra,
			String localidad) {
		this.calle = new SimpleStringProperty(calle);
		this.numero = new SimpleIntegerProperty(numero);
		this.piso = new SimpleStringProperty(piso);
		this.letra = new SimpleStringProperty(letra);
		this.localidad = new SimpleStringProperty(localidad);
		this.iddireccion = new SimpleIntegerProperty(0);
		this.provincia = null;
		this.cpostal = null;
	}
    
    public Direccion(String calle, Integer numero, String piso, String letra, Integer cpostal,
			String localidad, String provincia) {
    	this.iddireccion = new SimpleIntegerProperty(0);
		this.calle = new SimpleStringProperty(calle);
		this.numero = new SimpleIntegerProperty(numero);
		this.piso = new SimpleStringProperty(piso);
		this.letra = new SimpleStringProperty(letra);
		this.cpostal = new SimpleIntegerProperty(cpostal);
		this.localidad = new SimpleStringProperty(localidad);
		this.provincia = new SimpleStringProperty(provincia);
	}

	public Direccion(IntegerProperty iddireccion, StringProperty calle, IntegerProperty numero, StringProperty piso,
			StringProperty letra, IntegerProperty cpostal, StringProperty localidad, StringProperty provincia) {
		super();
		this.iddireccion = iddireccion;
		this.calle = calle;
		this.numero = numero;
		this.piso = piso;
		this.letra = letra;
		this.cpostal = cpostal;
		this.localidad = localidad;
		this.provincia = provincia;
	}

	public IntegerProperty iddireccionProperty() {
		return this.iddireccion;
	}
	
	public int getIddireccion() {
		return this.iddireccionProperty().get();
	}
	
	public void setIddireccion(final int iddireccion) {
		this.iddireccionProperty().set(iddireccion);
	}
	
	public StringProperty calleProperty() {
		return this.calle;
	}
	
	public String getCalle() {
		return this.calleProperty().get();
	}
	
	public void setCalle(final String calle) {
		this.calleProperty().set(calle);
	}
	
	public IntegerProperty numeroProperty() {
		return this.numero;
	}
	
	public int getNumero() {
		return this.numeroProperty().get();
	}
	
	public void setNumero(final int numero) {
		this.numeroProperty().set(numero);
	}
	
	public StringProperty pisoProperty() {
		return this.piso;
	}
	
	public String getPiso() {
		return this.pisoProperty().get();
	}
	
	public void setPiso(final String piso) {
		this.pisoProperty().set(piso);
	}
	
	public StringProperty letraProperty() {
		return this.letra;
	}
	
	public String getLetra() {
		return this.letraProperty().get();
	}
	
	public void setLetra(final String letra) {
		this.letraProperty().set(letra);
	}
	
	public IntegerProperty cpostalProperty() {
		return this.cpostal;
	}
	
	public int getCpostal() {
		return this.cpostalProperty().get();
	}
	
	public void setCpostal(final int cpostal) {
		this.cpostalProperty().set(cpostal);
	}
	
	public StringProperty localidadProperty() {
		return this.localidad;
	}
	
	public String getLocalidad() {
		return this.localidadProperty().get();
	}
	
	public void setLocalidad(final String localidad) {
		this.localidadProperty().set(localidad);
	}
	
	public StringProperty provinciaProperty() {
		return this.provincia;
	}
	
	public String getProvincia() {
		return this.provinciaProperty().get();
	}
	
	public void setProvincia(final String provincia) {
		this.provinciaProperty().set(provincia);
	}
	
}
