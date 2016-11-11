package Modelo;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vehiculo {
	private final IntegerProperty idvehiculo;
	private final IntegerProperty clienteID;
	private final StringProperty marca;
	private final StringProperty modelo;
	private final StringProperty version;
	private final StringProperty matricula;
	private final IntegerProperty anio;
	private final StringProperty bastidor;
	private final StringProperty letrasmotor;
	private final StringProperty color;
	private final StringProperty codradio;
	private final IntegerProperty tipoID;

	public Vehiculo(Integer clienteID) {
		//this(null,null,null,null,null,null,null);
		this.idvehiculo = new SimpleIntegerProperty(0);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.marca = new SimpleStringProperty("");
		this.modelo = new SimpleStringProperty("");
		this.version = new SimpleStringProperty("");
		this.matricula = new SimpleStringProperty("");
		this.anio = new SimpleIntegerProperty(0);
		this.bastidor = new SimpleStringProperty("");
		this.letrasmotor = new SimpleStringProperty("");
		this.color = new SimpleStringProperty("");
		this.codradio = new SimpleStringProperty("");
		this.tipoID = new SimpleIntegerProperty(0);
	}

	// Constructor para la funcion buscarVehiculoPorMatricula
	public Vehiculo(Integer idvehiculo, Integer clienteID, String matricula) {
		this.idvehiculo = new SimpleIntegerProperty(idvehiculo);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.marca = null;
		this.modelo = null;
		this.version = null;
		this.matricula = new SimpleStringProperty(matricula);
		this.anio = null;
		this.bastidor = null;
		this.letrasmotor = null;
		this.color = null;
		this.codradio = null;
		this.tipoID = null;
	}

	// Constructor para factura
	public Vehiculo(Integer idvehiculo, Integer clienteID, String marca, String modelo, String version,
			String matricula, Integer tipoID) {
		this.idvehiculo = new SimpleIntegerProperty(idvehiculo);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.marca = new SimpleStringProperty(marca);
		this.modelo = new SimpleStringProperty(modelo);
		this.version = new SimpleStringProperty(version);
		this.matricula = new SimpleStringProperty(matricula);
		this.anio = null;
		this.bastidor = null;
		this.letrasmotor = null;
		this.color = null;
		this.codradio = null;
		this.tipoID = new SimpleIntegerProperty(tipoID);
	}

	public Vehiculo(String marca, String modelo, String version, String matricula) {
		this.idvehiculo = null;
		this.clienteID = null;
		this.marca = new SimpleStringProperty(marca);
		this.modelo = new SimpleStringProperty(modelo);
		this.version = new SimpleStringProperty(version);
		this.matricula = new SimpleStringProperty(matricula);
		this.anio = null;
		this.bastidor = null;
		this.letrasmotor = null;
		this.color = null;
		this.codradio = null;
		this.tipoID = null;
	}

	public Vehiculo(Integer idvehiculo, Integer clienteID, String marca, String modelo, String version,
			String matricula, Integer anio, String bastidor, String letrasmotor, String color, String codradio,
			Integer tipoID) {
		this.idvehiculo = new SimpleIntegerProperty(idvehiculo);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.marca = new SimpleStringProperty(marca);
		this.modelo = new SimpleStringProperty(modelo);
		this.version = new SimpleStringProperty(version);
		this.matricula = new SimpleStringProperty(matricula);
		this.anio = new SimpleIntegerProperty(anio);
		this.bastidor = new SimpleStringProperty(bastidor);
		this.letrasmotor = new SimpleStringProperty(letrasmotor);
		this.color = new SimpleStringProperty(color);
		this.codradio = new SimpleStringProperty(codradio);
		this.tipoID = new SimpleIntegerProperty(tipoID);
	}

	public Vehiculo(IntegerProperty idvehiculo, IntegerProperty clienteID, StringProperty marca, StringProperty modelo,
			StringProperty version, StringProperty matricula, IntegerProperty anio, StringProperty bastidor,
			StringProperty letrasmotor, StringProperty color, StringProperty codradio, IntegerProperty tipoID) {
		super();
		this.idvehiculo = idvehiculo;
		this.clienteID = clienteID;
		this.marca = marca;
		this.modelo = modelo;
		this.version = version;
		this.matricula = matricula;
		this.anio = anio;
		this.bastidor = bastidor;
		this.letrasmotor = letrasmotor;
		this.color = color;
		this.codradio = codradio;
		this.tipoID = tipoID;
	}

	public IntegerProperty idvehiculoProperty() {
		return this.idvehiculo;
	}

	public int getIdvehiculo() {
		return this.idvehiculoProperty().get();
	}

	public void setIdvehiculo(final int idvehiculo) {
		this.idvehiculoProperty().set(idvehiculo);
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

	public StringProperty marcaProperty() {
		return this.marca;
	}

	public String getMarca() {
		return this.marcaProperty().get();
	}

	public void setMarca(final String marca) {
		this.marcaProperty().set(marca);
	}

	public StringProperty modeloProperty() {
		return this.modelo;
	}

	public String getModelo() {
		return this.modeloProperty().get();
	}

	public void setModelo(final String modelo) {
		this.modeloProperty().set(modelo);
	}

	public StringProperty versionProperty() {
		return this.version;
	}

	public String getVersion() {
		return this.versionProperty().get();
	}

	public void setVersion(final String version) {
		this.versionProperty().set(version);
	}

	public StringProperty matriculaProperty() {
		return this.matricula;
	}

	public String getMatricula() {
		return this.matriculaProperty().get();
	}

	public void setMatricula(final String matricula) {
		this.matriculaProperty().set(matricula);
	}

	public IntegerProperty anioProperty() {
		return this.anio;
	}

	public int getAnio() {
		return this.anioProperty().get();
	}

	public void setAnio(final int anio) {
		this.anioProperty().set(anio);
	}

	public StringProperty bastidorProperty() {
		return this.bastidor;
	}

	public String getBastidor() {
		return this.bastidorProperty().get();
	}

	public void setBastidor(final String bastidor) {
		this.bastidorProperty().set(bastidor);
	}

	public StringProperty letrasmotorProperty() {
		return this.letrasmotor;
	}

	public String getLetrasmotor() {
		return this.letrasmotorProperty().get();
	}

	public void setLetrasmotor(final String letrasmotor) {
		this.letrasmotorProperty().set(letrasmotor);
	}

	public StringProperty colorProperty() {
		return this.color;
	}

	public String getColor() {
		return this.colorProperty().get();
	}

	public void setColor(final String color) {
		this.colorProperty().set(color);
	}

	public StringProperty codradioProperty() {
		return this.codradio;
	}

	public String getCodradio() {
		return this.codradioProperty().get();
	}

	public void setCodradio(final String codradio) {
		this.codradioProperty().set(codradio);
	}

	public IntegerProperty tipoIDProperty() {
		return this.tipoID;
	}

	public int getTipoID() {
		return this.tipoIDProperty().get();
	}

	public void setTipoID(final int tipoID) {
		this.tipoIDProperty().set(tipoID);
	}

	public StringProperty marcaModeloProperty() {
		return new SimpleStringProperty(getMarca() + " " + getModelo() + " " + getVersion());
	}

}
