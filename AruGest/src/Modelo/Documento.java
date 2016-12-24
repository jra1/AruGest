package Modelo;

import java.sql.Blob;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class Documento {
	private final IntegerProperty iddocumento;
	private final IntegerProperty clienteID;
	private final IntegerProperty vehiculoID;
	private final StringProperty titulo;
	private final ObjectProperty<Blob> documento;

	public Documento(IntegerProperty iddocumento, IntegerProperty clienteID, IntegerProperty vehiculoID,
			StringProperty titulo, ObjectProperty<Blob> documento) {
		super();
		this.iddocumento = iddocumento;
		this.clienteID = clienteID;
		this.vehiculoID = vehiculoID;
		this.titulo = titulo;
		this.documento = documento;
	}

	public IntegerProperty iddocumentoProperty() {
		return this.iddocumento;
	}

	public int getIddocumento() {
		return this.iddocumentoProperty().get();
	}

	public void setIddocumento(final int iddocumento) {
		this.iddocumentoProperty().set(iddocumento);
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

	public IntegerProperty vehiculoIDProperty() {
		return this.vehiculoID;
	}

	public int getVehiculoID() {
		return this.vehiculoIDProperty().get();
	}

	public void setVehiculoID(final int vehiculoID) {
		this.vehiculoIDProperty().set(vehiculoID);
	}

	public StringProperty tituloProperty() {
		return this.titulo;
	}

	public String getTitulo() {
		return this.tituloProperty().get();
	}

	public void setTitulo(final String titulo) {
		this.tituloProperty().set(titulo);
	}

	public ObjectProperty<Blob> documentoProperty() {
		return this.documento;
	}

	public Object getDocumento() {
		return this.documentoProperty().get();
	}

	public void setDocumento(final Blob documento) {
		this.documentoProperty().set(documento);
	}

}
