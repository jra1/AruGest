package Modelo;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Servicio {
	private final IntegerProperty idservicio;
	private final StringProperty servicio;
	private final FloatProperty horas;
	private final IntegerProperty facturaID;
	private final StringProperty tiposervicio;
	
	public Servicio(IntegerProperty idservicio, StringProperty servicio, FloatProperty horas, IntegerProperty facturaID,
			StringProperty tiposervicio) {
		super();
		this.idservicio = idservicio;
		this.servicio = servicio;
		this.horas = horas;
		this.facturaID = facturaID;
		this.tiposervicio = tiposervicio;
	}

	public IntegerProperty idservicioProperty() {
		return this.idservicio;
	}
	

	public int getIdservicio() {
		return this.idservicioProperty().get();
	}
	

	public void setIdservicio(final int idservicio) {
		this.idservicioProperty().set(idservicio);
	}
	

	public StringProperty servicioProperty() {
		return this.servicio;
	}
	

	public String getServicio() {
		return this.servicioProperty().get();
	}
	

	public void setServicio(final String servicio) {
		this.servicioProperty().set(servicio);
	}
	

	public FloatProperty horasProperty() {
		return this.horas;
	}
	

	public float getHoras() {
		return this.horasProperty().get();
	}
	

	public void setHoras(final float horas) {
		this.horasProperty().set(horas);
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
	

	public StringProperty tiposervicioProperty() {
		return this.tiposervicio;
	}
	

	public String getTiposervicio() {
		return this.tiposervicioProperty().get();
	}
	

	public void setTiposervicio(final String tiposervicio) {
		this.tiposervicioProperty().set(tiposervicio);
	}
	
}
