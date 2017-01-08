package Modelo;

import java.sql.Blob;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Documento {
	private final IntegerProperty iddocumento;
	private final IntegerProperty clienteID;
	private final IntegerProperty vehiculoID;
	private final StringProperty titulo;
	private final ObjectProperty<Blob> documento;
	private final StringProperty extension;

	public Documento(IntegerProperty iddocumento, IntegerProperty clienteID, IntegerProperty vehiculoID,
			StringProperty titulo, ObjectProperty<Blob> documento, StringProperty extension) {
		super();
		this.iddocumento = iddocumento;
		this.clienteID = clienteID;
		this.vehiculoID = vehiculoID;
		this.titulo = titulo;
		this.documento = documento;
		this.extension = extension;
	}

	public Documento(Integer iddocumento, Integer clienteID, Integer vehiculoID, String titulo, Blob documento,
			String extension) {
		this.iddocumento = new SimpleIntegerProperty(iddocumento);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.vehiculoID = new SimpleIntegerProperty(vehiculoID);
		this.titulo = new SimpleStringProperty(titulo);
		this.documento = new SimpleObjectProperty<Blob>(documento);
		this.extension = new SimpleStringProperty(extension);

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

	public StringProperty extensionProperty() {
		return this.extension;
	}

	public String getExtension() {
		return this.extensionProperty().get();
	}

	public void setExtension(final String extension) {
		this.extensionProperty().set(extension);
	}

}
