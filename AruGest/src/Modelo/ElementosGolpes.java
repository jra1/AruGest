package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ElementosGolpes {
	private final IntegerProperty idelemento;
	private final StringProperty nombreElemento;
	private final StringProperty tipo;
	private final IntegerProperty golpeID;

	public ElementosGolpes(IntegerProperty idelemento, StringProperty nombreElemento, StringProperty tipo,
			IntegerProperty golpeID) {
		super();
		this.idelemento = idelemento;
		this.nombreElemento = nombreElemento;
		this.tipo = tipo;
		this.golpeID = golpeID;
	}

	public ElementosGolpes(Integer idelemento, String nombreElemento, String tipo, Integer golpeID) {
		super();
		this.idelemento = new SimpleIntegerProperty(idelemento);
		this.nombreElemento = new SimpleStringProperty(nombreElemento);
		this.tipo = new SimpleStringProperty(tipo);
		this.golpeID = new SimpleIntegerProperty(golpeID);

	}

	public IntegerProperty idelementoProperty() {
		return this.idelemento;
	}

	public int getIdelemento() {
		return this.idelementoProperty().get();
	}

	public void setIdelemento(final int idelemento) {
		this.idelementoProperty().set(idelemento);
	}

	public StringProperty nombreElementoProperty() {
		return this.nombreElemento;
	}

	public String getNombreElemento() {
		return this.nombreElementoProperty().get();
	}

	public void setNombreElemento(final String nombreElemento) {
		this.nombreElementoProperty().set(nombreElemento);
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

	public IntegerProperty golpeIDProperty() {
		return this.golpeID;
	}

	public int getGolpeID() {
		return this.golpeIDProperty().get();
	}

	public void setGolpeID(final int golpeID) {
		this.golpeIDProperty().set(golpeID);
	}
}
