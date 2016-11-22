package Modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehiculoSustitucion {
	private final IntegerProperty idvehiculosusti;
	private final ObjectProperty<Date> fechacoge;
	private final ObjectProperty<Date> fechadevuelve;
	private final IntegerProperty clienteID;
	private final IntegerProperty vehiculoID;
	
	public VehiculoSustitucion(int idvehiculosusti, Date fechacoge,
			Date fechadevuelve, int clienteID, int vehiculoID) {
		super();
		this.idvehiculosusti = new SimpleIntegerProperty(idvehiculosusti);
		this.fechacoge = new SimpleObjectProperty<Date>(fechacoge);
		this.fechadevuelve = new SimpleObjectProperty<Date>(fechadevuelve);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.vehiculoID = new SimpleIntegerProperty(vehiculoID);
	}
	
	public VehiculoSustitucion(IntegerProperty idvehiculosusti, ObjectProperty<Date> fechacoge,
			ObjectProperty<Date> fechadevuelve, IntegerProperty clienteID, IntegerProperty vehiculoID) {
		super();
		this.idvehiculosusti = idvehiculosusti;
		this.fechacoge = fechacoge;
		this.fechadevuelve = fechadevuelve;
		this.clienteID = clienteID;
		this.vehiculoID = vehiculoID;
	}

	public IntegerProperty idvehiculosustiProperty() {
		return this.idvehiculosusti;
	}
	

	public int getIdvehiculosusti() {
		return this.idvehiculosustiProperty().get();
	}
	

	public void setIdvehiculosusti(final int idvehiculosusti) {
		this.idvehiculosustiProperty().set(idvehiculosusti);
	}
	

	public ObjectProperty<Date> fechacogeProperty() {
		return this.fechacoge;
	}
	
	public StringProperty fechacogePropertyFormat() {
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return new SimpleStringProperty(formateador.format(getFechacoge()));
	}
	

	public Date getFechacoge() {
		return this.fechacogeProperty().get();
	}
	

	public void setFechacoge(final Date fechacoge) {
		this.fechacogeProperty().set(fechacoge);
	}
	

	public ObjectProperty<Date> fechadevuelveProperty() {
		return this.fechadevuelve;
	}
	
	public StringProperty fechadevuelvePropertyFormat() {
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return new SimpleStringProperty(formateador.format(getFechadevuelve()));
	}
	

	public Date getFechadevuelve() {
		return this.fechadevuelveProperty().get();
	}
	

	public void setFechadevuelve(final Date fechadevuelve) {
		this.fechadevuelveProperty().set(fechadevuelve);
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
	
}
