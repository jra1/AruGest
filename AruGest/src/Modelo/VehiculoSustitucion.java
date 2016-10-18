package Modelo;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

public class VehiculoSustitucion {
	private final IntegerProperty idvehiculosusti;
	private final ObjectProperty<LocalDate> fechacoge;
	private final ObjectProperty<LocalDate> fechadevuelve;
	private final IntegerProperty clienteID;
	private final IntegerProperty vehiculoID;
	
	public VehiculoSustitucion(IntegerProperty idvehiculosusti, ObjectProperty<LocalDate> fechacoge,
			ObjectProperty<LocalDate> fechadevuelve, IntegerProperty clienteID, IntegerProperty vehiculoID) {
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
	

	public ObjectProperty<LocalDate> fechacogeProperty() {
		return this.fechacoge;
	}
	

	public LocalDate getFechacoge() {
		return this.fechacogeProperty().get();
	}
	

	public void setFechacoge(final LocalDate fechacoge) {
		this.fechacogeProperty().set(fechacoge);
	}
	

	public ObjectProperty<LocalDate> fechadevuelveProperty() {
		return this.fechadevuelve;
	}
	

	public LocalDate getFechadevuelve() {
		return this.fechadevuelveProperty().get();
	}
	

	public void setFechadevuelve(final LocalDate fechadevuelve) {
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
