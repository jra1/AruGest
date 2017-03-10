package Modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Servicio {
	private final IntegerProperty idservicio;
	private final StringProperty servicio;
	private final StringProperty horas;
	private final IntegerProperty facturaID;
	private final StringProperty tiposervicio;

	/**
	 * Default constructor.
	 */
	// public Servicio() {
	// this(0, null, null, null, null);
	// }

	public Servicio(Integer idservicio, String servicio, String horas, Integer facturaID, String tiposervicio) {
		super();
		this.idservicio = new SimpleIntegerProperty(idservicio);
		this.servicio = new SimpleStringProperty(servicio);
		this.horas = new SimpleStringProperty(horas);
		this.facturaID = new SimpleIntegerProperty(facturaID);
		this.tiposervicio = new SimpleStringProperty(tiposervicio);
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

	public StringProperty tipoConServicioProperty() {
		StringProperty sp;
		// Lo hago así porque sino duplica el tipoServicio
		if (getServicio().lastIndexOf("Chapa:") == -1 && getServicio().lastIndexOf("Pintura:") == -1
				&& getServicio().lastIndexOf("Electrónica / mecánica:") == -1) {
			sp = new SimpleStringProperty(getTiposervicio() + ": " + getServicio());
		} else {
			sp = servicioProperty();
		}
		return sp;
	}

	public String getServicio() {
		return this.servicioProperty().get();
	}

	public void setServicio(final String servicio) {
		this.servicioProperty().set(servicio);
	}

	public StringProperty horasProperty() {
		return this.horas;
	}

	public String getHoras() {
		return this.horasProperty().get();
	}

	public void setHoras(final String horas) {
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
