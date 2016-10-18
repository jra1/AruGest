package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TipoVehiculo {
	private final IntegerProperty idtipovehiculo;
	private final StringProperty tipo;
	
	public TipoVehiculo(IntegerProperty idtipovehiculo, StringProperty tipo) {
		super();
		this.idtipovehiculo = idtipovehiculo;
		this.tipo = tipo;
	}

	public IntegerProperty idtipovehiculoProperty() {
		return this.idtipovehiculo;
	}
	

	public int getIdtipovehiculo() {
		return this.idtipovehiculoProperty().get();
	}
	

	public void setIdtipovehiculo(final int idtipovehiculo) {
		this.idtipovehiculoProperty().set(idtipovehiculo);
	}
	

	public StringProperty tipoProperty() {
		return this.tipo;
	}
	

	public String getTipo() {
		return this.tipoProperty().get();
	}
	

	public void setTipo(final String tipo) {
		this.tipoProperty().set(tipo);
	}
	
	
}
