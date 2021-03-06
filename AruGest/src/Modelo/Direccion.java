package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Direccion {
	private final IntegerProperty iddireccion;
	private final StringProperty direccion;
	private final IntegerProperty cpostal;
	private final StringProperty localidad;
	private final StringProperty provincia;

	public Direccion() {
		this.direccion = new SimpleStringProperty("");
		this.localidad = new SimpleStringProperty("");
		this.iddireccion = new SimpleIntegerProperty(0);
		this.provincia = new SimpleStringProperty("");
		this.cpostal = new SimpleIntegerProperty(0);
	}

	public Direccion(String direccion, String localidad) {
		this.direccion = new SimpleStringProperty(direccion);
		this.localidad = new SimpleStringProperty(localidad);
		this.iddireccion = new SimpleIntegerProperty(0);
		this.provincia = new SimpleStringProperty("");
		this.cpostal = new SimpleIntegerProperty(0);
	}

	public Direccion(Integer iddireccion, String direccion, Integer cpostal,
			String localidad, String provincia) {
		this.iddireccion = new SimpleIntegerProperty(iddireccion);
		this.direccion = new SimpleStringProperty(direccion);
		this.cpostal = new SimpleIntegerProperty(cpostal);
		this.localidad = new SimpleStringProperty(localidad);
		this.provincia = new SimpleStringProperty(provincia);
	}

	public Direccion(IntegerProperty iddireccion, StringProperty direccion, IntegerProperty cpostal, StringProperty localidad, StringProperty provincia) {
		super();
		this.iddireccion = iddireccion;
		this.direccion = direccion;
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

	public StringProperty direccionProperty() {
		return this.direccion;
	}

	public StringProperty direccionCompletaProperty() {
		String respuesta = "";
		if (!this.getDireccion().equalsIgnoreCase("")) {
			respuesta = this.getDireccion() + Integer.toString(this.getCpostal()) + " " + this.getLocalidad() + ", " + this.getProvincia();
		} else {
			respuesta = this.getLocalidad() + " - " + this.getProvincia();
		}
		return new SimpleStringProperty(respuesta);
	}

	public String getDireccion() {
		return this.direccionProperty().get();
	}

	public void setDireccion(final String direccion) {
		this.direccionProperty().set(direccion);
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
