package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Golpe {
	private final IntegerProperty idgolpe;
	private final StringProperty nombreGolpe;

	public Golpe(IntegerProperty idgolpe, StringProperty nombreGolpe) {
		super();
		this.idgolpe = idgolpe;
		this.nombreGolpe = nombreGolpe;
	}

	public Golpe(int idgolpe, String nombreGolpe) {
		this.idgolpe = new SimpleIntegerProperty(idgolpe);
		this.nombreGolpe = new SimpleStringProperty(nombreGolpe);
	}

	public IntegerProperty idgolpeProperty() {
		return this.idgolpe;
	}

	public int getIdgolpe() {
		return this.idgolpeProperty().get();
	}

	public void setIdgolpe(final int idgolpe) {
		this.idgolpeProperty().set(idgolpe);
	}

	public StringProperty nombreGolpeProperty() {
		return this.nombreGolpe;
	}

	public String getNombreGolpe() {
		return this.nombreGolpeProperty().get();
	}

	public void setNombreGolpe(final String nombreGolpe) {
		this.nombreGolpeProperty().set(nombreGolpe);
	}

}
