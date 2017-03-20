package Modelo;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Factura {
	private final IntegerProperty idfactura;
	private final IntegerProperty clienteID;
	private final IntegerProperty vehiculoID;
	private final IntegerProperty kms;
	private final StringProperty numfactura;
	private final StringProperty numpresupuesto;
	private final StringProperty numordenrep;
	private final StringProperty numresguardo;
	private final ObjectProperty<Date> fecha;
	private final ObjectProperty<Date> fechaentrega;
	private final FloatProperty manoobra;
	private final FloatProperty materiales;
	private final FloatProperty grua;
	private final FloatProperty suma;
	private final FloatProperty sumaIva;
	private final FloatProperty importeTotal;
	private final StringProperty estado;
	private final FloatProperty porcentajedefocul;
	private final BooleanProperty rdefocultos;
	private final BooleanProperty permisopruebas;
	private final BooleanProperty nopiezas;
	private final BooleanProperty cobrado;
	private final ObjectProperty<Blob> documento;

	public Factura(Date fecha, Float importeTotal) {
		this.idfactura = new SimpleIntegerProperty(0);
		this.clienteID = new SimpleIntegerProperty(0);
		this.vehiculoID = new SimpleIntegerProperty(0);
		this.kms = new SimpleIntegerProperty(0);
		this.numfactura = new SimpleStringProperty("0");
		this.numpresupuesto = new SimpleStringProperty("0");
		this.numordenrep = new SimpleStringProperty("0");
		this.numresguardo = new SimpleStringProperty("0");
		this.fecha = new SimpleObjectProperty<Date>(fecha);
		this.fechaentrega = new SimpleObjectProperty<Date>(null);
		this.manoobra = new SimpleFloatProperty(0);
		this.materiales = new SimpleFloatProperty(0);
		this.grua = new SimpleFloatProperty(0);
		this.suma = new SimpleFloatProperty(0);
		this.sumaIva = new SimpleFloatProperty(0);
		this.importeTotal = new SimpleFloatProperty(importeTotal);
		this.estado = new SimpleStringProperty("");
		this.porcentajedefocul = new SimpleFloatProperty(0);
		this.rdefocultos = new SimpleBooleanProperty(false);
		this.permisopruebas = new SimpleBooleanProperty(false);
		this.nopiezas = new SimpleBooleanProperty(false);
		this.cobrado = new SimpleBooleanProperty(false);
		this.documento = new SimpleObjectProperty<Blob>(null);
	}

	public Factura(int idfactura, int clienteID, int vehiculoID, int kms, String numfactura, String numpresupuesto,
			String numordenrep, String numresguardo, Date fecha, Date fechaentrega, float manoobra, float materiales,
			float grua, float suma, float sumaIva, String estado, boolean rdefocultos, float porcentajedefocul,
			boolean permisopruebas, boolean nopiezas, boolean cobrado, float importeTotal) {
		this.idfactura = new SimpleIntegerProperty(idfactura);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.vehiculoID = new SimpleIntegerProperty(vehiculoID);
		this.kms = new SimpleIntegerProperty(kms);
		this.numfactura = new SimpleStringProperty(numfactura);
		this.numpresupuesto = new SimpleStringProperty(numpresupuesto);
		this.numordenrep = new SimpleStringProperty(numordenrep);
		this.numresguardo = new SimpleStringProperty(numresguardo);
		this.fecha = new SimpleObjectProperty<Date>(fecha);
		this.fechaentrega = new SimpleObjectProperty<Date>(fechaentrega);
		this.manoobra = new SimpleFloatProperty(manoobra);
		this.materiales = new SimpleFloatProperty(materiales);
		this.grua = new SimpleFloatProperty(grua);
		this.suma = new SimpleFloatProperty(suma);
		this.sumaIva = new SimpleFloatProperty(sumaIva);
		this.importeTotal = new SimpleFloatProperty(importeTotal);
		this.estado = new SimpleStringProperty(estado);
		this.porcentajedefocul = new SimpleFloatProperty(porcentajedefocul);
		this.rdefocultos = new SimpleBooleanProperty(rdefocultos);
		this.permisopruebas = new SimpleBooleanProperty(permisopruebas);
		this.nopiezas = new SimpleBooleanProperty(nopiezas);
		this.cobrado = new SimpleBooleanProperty(cobrado);
		this.documento = null;
	}

	public Factura(IntegerProperty idfactura, IntegerProperty clienteID, IntegerProperty vehiculoID,
			IntegerProperty kms, StringProperty numfactura, StringProperty numpresupuesto, StringProperty numordenrep,
			StringProperty numresguardo, ObjectProperty<Date> fecha, ObjectProperty<Date> fechaentrega,
			FloatProperty manoobra, FloatProperty materiales, FloatProperty grua, FloatProperty suma,
			FloatProperty sumaIva, StringProperty estado, FloatProperty porcentajedefocul, BooleanProperty rdefocultos,
			BooleanProperty permisopruebas, BooleanProperty nopiezas, BooleanProperty cobrado,
			ObjectProperty<Blob> documento, FloatProperty importeTotal) {
		super();
		this.idfactura = idfactura;
		this.clienteID = clienteID;
		this.vehiculoID = vehiculoID;
		this.kms = kms;
		this.numfactura = numfactura;
		this.numpresupuesto = numpresupuesto;
		this.numordenrep = numordenrep;
		this.numresguardo = numresguardo;
		this.fecha = fecha;
		this.fechaentrega = fechaentrega;
		this.manoobra = manoobra;
		this.materiales = materiales;
		this.grua = grua;
		this.suma = suma;
		this.sumaIva = sumaIva;
		this.importeTotal = importeTotal;
		this.estado = estado;
		this.porcentajedefocul = porcentajedefocul;
		this.rdefocultos = rdefocultos;
		this.permisopruebas = permisopruebas;
		this.nopiezas = nopiezas;
		this.cobrado = cobrado;
		this.documento = documento;
	}

	public IntegerProperty idfacturaProperty() {
		return this.idfactura;
	}

	public int getIdfactura() {
		return this.idfacturaProperty().get();
	}

	public void setIdfactura(final int idfactura) {
		this.idfacturaProperty().set(idfactura);
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

	public StringProperty numfacturaProperty() {
		return this.numfactura;
	}

	public String getNumfactura() {
		return this.numfacturaProperty().get();
	}

	public void setNumfactura(final String numfactura) {
		this.numfacturaProperty().set(numfactura);
	}

	public StringProperty numpresupuestoProperty() {
		return this.numpresupuesto;
	}

	public String getNumpresupuesto() {
		return this.numpresupuestoProperty().get();
	}

	public void setNumpresupuesto(final String numpresupuesto) {
		this.numpresupuestoProperty().set(numpresupuesto);
	}

	public StringProperty numordenrepProperty() {
		return this.numordenrep;
	}

	public String getNumordenrep() {
		return this.numordenrepProperty().get();
	}

	public void setNumordenrep(final String numordenrep) {
		this.numordenrepProperty().set(numordenrep);
	}

	public StringProperty numresguardoProperty() {
		return this.numresguardo;
	}

	public String getNumresguardo() {
		return this.numresguardoProperty().get();
	}

	public void setNumresguardo(final String numresguardo) {
		this.numresguardoProperty().set(numresguardo);
	}

	public ObjectProperty<Date> fechaProperty() {
		return this.fecha;
	}

	public StringProperty fechaPropertyFormat() {
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return new SimpleStringProperty(formateador.format(getFecha()));
	}

	public Date getFecha() {
		return this.fechaProperty().get();
	}

	public void setFecha(final Date fecha) {
		this.fechaProperty().set(fecha);
	}

	public ObjectProperty<Date> fechaentregaProperty() {
		return this.fechaentrega;
	}

	public Date getFechaentrega() {
		return this.fechaentregaProperty().get();
	}

	public void setFechaentrega(final Date fechaentrega) {
		this.fechaentregaProperty().set(fechaentrega);
	}

	public FloatProperty manoobraProperty() {
		return this.manoobra;
	}

	public float getManoobra() {
		return this.manoobraProperty().get();
	}

	public void setManoobra(final float manoobra) {
		this.manoobraProperty().set(manoobra);
	}

	public FloatProperty materialesProperty() {
		return this.materiales;
	}

	public float getMateriales() {
		return this.materialesProperty().get();
	}

	public void setMateriales(final float materiales) {
		this.materialesProperty().set(materiales);
	}

	public FloatProperty gruaProperty() {
		return this.grua;
	}

	public float getGrua() {
		return this.gruaProperty().get();
	}

	public void setGrua(final float grua) {
		this.gruaProperty().set(grua);
	}

	public StringProperty estadoProperty() {
		return this.estado;
	}

	public String getEstado() {
		return this.estadoProperty().get();
	}

	public void setEstado(final String estado) {
		this.estadoProperty().set(estado);
	}

	public FloatProperty porcentajedefoculProperty() {
		return this.porcentajedefocul;
	}

	public float getPorcentajedefocul() {
		return this.porcentajedefoculProperty().get();
	}

	public void setPorcentajedefocul(final float porcentajedefocul) {
		this.porcentajedefoculProperty().set(porcentajedefocul);
	}

	public BooleanProperty rdefocultosProperty() {
		return this.rdefocultos;
	}

	public boolean isRdefocultos() {
		return this.rdefocultosProperty().get();
	}

	public void setRdefocultos(final boolean rdefocultos) {
		this.rdefocultosProperty().set(rdefocultos);
	}

	public BooleanProperty permisopruebasProperty() {
		return this.permisopruebas;
	}

	public boolean isPermisopruebas() {
		return this.permisopruebasProperty().get();
	}

	public void setPermisopruebas(final boolean permisopruebas) {
		this.permisopruebasProperty().set(permisopruebas);
	}

	public BooleanProperty nopiezasProperty() {
		return this.nopiezas;
	}

	public boolean isNopiezas() {
		return this.nopiezasProperty().get();
	}

	public void setNopiezas(final boolean nopiezas) {
		this.nopiezasProperty().set(nopiezas);
	}

	public BooleanProperty cobradoProperty() {
		return this.cobrado;
	}

	public StringProperty cobradoLetra() {
		SimpleStringProperty res;
		if (isCobrado()) {
			res = new SimpleStringProperty("COBRADO");
		} else {
			res = new SimpleStringProperty("PENDIENTE");
		}
		return res;
	}

	public boolean isCobrado() {
		return this.cobradoProperty().get();
	}

	public void setCobrado(final boolean cobrado) {
		this.cobradoProperty().set(cobrado);
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

	public FloatProperty importeTotalProperty() {
		return this.importeTotal;
	}

	public float getImporteTotal() {
		return this.importeTotalProperty().get();
	}

	public void setImporteTotal(final float importeTotal) {
		this.importeTotalProperty().set(importeTotal);
	}

	public IntegerProperty kmsProperty() {
		return this.kms;
	}

	public int getKms() {
		return this.kmsProperty().get();
	}

	public void setKms(final int kms) {
		this.kmsProperty().set(kms);
	}

	public FloatProperty sumaProperty() {
		return this.suma;
	}

	public float getSuma() {
		return this.sumaProperty().get();
	}

	public void setSuma(final float suma) {
		this.sumaProperty().set(suma);
	}

	public FloatProperty sumaIvaProperty() {
		return this.sumaIva;
	}

	public float getSumaIva() {
		return this.sumaIvaProperty().get();
	}

	public void setSumaIva(final float sumaIva) {
		this.sumaIvaProperty().set(sumaIva);
	}

}
