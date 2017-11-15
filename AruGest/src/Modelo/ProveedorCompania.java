package Modelo;

import java.sql.Blob;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProveedorCompania {
	private final IntegerProperty idprovecompa;
	private final StringProperty cif;
	private final StringProperty nombre;
	private final IntegerProperty direccionID;
	private final StringProperty telf1;
	private final StringProperty telf2;
	private final ObjectProperty<Blob> logo;
	private final BooleanProperty esdesguace;
	private final BooleanProperty escompania;
	private final StringProperty personaContacto;

	public ProveedorCompania(boolean esCia) {
		this.idprovecompa = new SimpleIntegerProperty(0);
		this.cif = new SimpleStringProperty("");
		this.nombre = new SimpleStringProperty("");
		this.direccionID = new SimpleIntegerProperty(0);
		this.telf1 = new SimpleStringProperty("");
		this.telf2 = new SimpleStringProperty("");
		this.logo = new SimpleObjectProperty<Blob>(null);
		this.esdesguace = new SimpleBooleanProperty(false);
		this.escompania = new SimpleBooleanProperty(esCia);
		this.personaContacto = new SimpleStringProperty("");
	}

	public ProveedorCompania(Integer idprovecompa, String cif, String nombre, Integer direccionID, String telf1,
			String telf2, Blob logo, Boolean esdesguace, Boolean escompania, String personaContacto) {
		this.idprovecompa = new SimpleIntegerProperty(idprovecompa);
		this.cif = new SimpleStringProperty(cif);
		this.nombre = new SimpleStringProperty(nombre);
		this.direccionID = new SimpleIntegerProperty(direccionID);
		this.telf1 = new SimpleStringProperty(telf1);
		this.telf2 = new SimpleStringProperty(telf2);
		this.logo = new SimpleObjectProperty<Blob>(logo);
		this.esdesguace = new SimpleBooleanProperty(esdesguace);
		this.escompania = new SimpleBooleanProperty(escompania);
		this.personaContacto = new SimpleStringProperty(personaContacto);
	}

	public ProveedorCompania(IntegerProperty idprovecompa, StringProperty cif, StringProperty nombre,
			IntegerProperty direccionID, StringProperty telf1, StringProperty telf2, ObjectProperty<Blob> logo,
			BooleanProperty esdesguace, BooleanProperty escompania, StringProperty personaContacto) {
		super();
		this.idprovecompa = idprovecompa;
		this.cif = cif;
		this.nombre = nombre;
		this.direccionID = direccionID;
		this.telf1 = telf1;
		this.telf2 = telf2;
		this.logo = logo;
		this.esdesguace = esdesguace;
		this.escompania = escompania;
		this.personaContacto = personaContacto;
	}

	public IntegerProperty idprovecompaProperty() {
		return this.idprovecompa;
	}

	public int getIdprovecompa() {
		return this.idprovecompaProperty().get();
	}

	public void setIdprovecompa(final int idprovecompa) {
		this.idprovecompaProperty().set(idprovecompa);
	}

	public StringProperty cifProperty() {
		return this.cif;
	}

	public String getCif() {
		return this.cifProperty().get();
	}

	public void setCif(final String cif) {
		this.cifProperty().set(cif);
	}

	public StringProperty nombreProperty() {
		return this.nombre;
	}

	public String getNombre() {
		return this.nombreProperty().get();
	}

	public void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public IntegerProperty direccionIDProperty() {
		return this.direccionID;
	}

	public int getDireccionID() {
		return this.direccionIDProperty().get();
	}

	public void setDireccionID(final int direccionID) {
		this.direccionIDProperty().set(direccionID);
	}

	public StringProperty telf1Property() {
		return this.telf1;
	}

	public String getTelf1() {
		return this.telf1Property().get();
	}

	public void setTelf1(final String telf1) {
		this.telf1Property().set(telf1);
	}

	public StringProperty telf2Property() {
		return this.telf2;
	}

	public String getTelf2() {
		return this.telf2Property().get();
	}

	public void setTelf2(final String telf2) {
		this.telf2Property().set(telf2);
	}

	public ObjectProperty<Blob> logoProperty() {
		return this.logo;
	}

	public Blob getLogo() {
		return this.logoProperty().get();
	}

	public void setLogo(final Blob logo) {
		this.logoProperty().set(logo);
	}

	public BooleanProperty esdesguaceProperty() {
		return this.esdesguace;
	}

	public boolean isEsdesguace() {
		return this.esdesguaceProperty().get();
	}

	public void setEsdesguace(final boolean esdesguace) {
		this.esdesguaceProperty().set(esdesguace);
	}

	public BooleanProperty escompaniaProperty() {
		return this.escompania;
	}

	public boolean isEscompania() {
		return this.escompaniaProperty().get();
	}

	public void setEscompania(final boolean escompania) {
		this.escompaniaProperty().set(escompania);
	}

	public StringProperty personaContactoProperty() {
		return this.personaContacto;
	}
	

	public String getPersonaContacto() {
		return this.personaContactoProperty().get();
	}
	

	public void setPersonaContacto(final String personaContacto) {
		this.personaContactoProperty().set(personaContacto);
	}
	

}
