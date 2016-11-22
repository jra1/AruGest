package Modelo;

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
	private final IntegerProperty numfactura;
	private final IntegerProperty numpresupuesto;
	private final IntegerProperty numordenrep;
	private final IntegerProperty numresguardo;
	private final ObjectProperty<Date> fecha;
	private final ObjectProperty<Date> fechaentrega;
	private final FloatProperty manoobra;
	private final FloatProperty materiales;
	private final FloatProperty grua;
	private final FloatProperty importeTotal;
	private final StringProperty estado;
	private final FloatProperty porcentajedefocul;
    private final BooleanProperty rdefocultos;
    private final BooleanProperty permisopruebas;
    private final BooleanProperty nopiezas;
    private final BooleanProperty modificable;
    private final ObjectProperty documento;
    
    public Factura(Date fecha, Float importeTotal){
    	this.idfactura = null;
		this.clienteID = null;
		this.vehiculoID = null;
		this.numfactura = null;
		this.numpresupuesto = null;
		this.numordenrep = null;
		this.numresguardo = null;
		this.fecha = new SimpleObjectProperty<Date>(fecha);
		this.fechaentrega = null;
		this.manoobra = null;
		this.materiales = null;
		this.grua = null;
		this.estado = null;
		this.porcentajedefocul = null;
		this.rdefocultos = null;
		this.permisopruebas = null;
		this.nopiezas = null;
		this.modificable = null;
		this.documento = null;
		this.importeTotal = new SimpleFloatProperty(importeTotal);
    }
    public Factura(int idfactura, int clienteID, int vehiculoID, int numfactura, int numpresupuesto, int numordenrep,
    		int numresguardo, Date fecha, Date fechaentrega,
			float manoobra, float materiales, float grua, String estado,
			boolean rdefocultos, float porcentajedefocul, boolean permisopruebas,
			boolean nopiezas, boolean modificable, float importeTotal){
    	this.idfactura = new SimpleIntegerProperty(idfactura);
		this.clienteID = new SimpleIntegerProperty(clienteID);
		this.vehiculoID = new SimpleIntegerProperty(vehiculoID);
		this.numfactura = new SimpleIntegerProperty(numfactura);
		this.numpresupuesto = new SimpleIntegerProperty(numpresupuesto);
		this.numordenrep = new SimpleIntegerProperty(numordenrep); 
		this.numresguardo = new SimpleIntegerProperty(numresguardo);
		this.fecha = new SimpleObjectProperty<Date>(fecha);
		this.fechaentrega = new SimpleObjectProperty<Date>(fechaentrega);
		this.manoobra = new SimpleFloatProperty(manoobra); 
		this.materiales = new SimpleFloatProperty(materiales);
		this.grua = new SimpleFloatProperty(grua);
		this.estado = new SimpleStringProperty(estado);
		this.porcentajedefocul = new SimpleFloatProperty(porcentajedefocul);
		this.rdefocultos = new SimpleBooleanProperty(rdefocultos);
		this.permisopruebas = new SimpleBooleanProperty(permisopruebas);
		this.nopiezas = new SimpleBooleanProperty(nopiezas);
		this.modificable = new SimpleBooleanProperty(modificable);
		this.documento = null;
		this.importeTotal = new SimpleFloatProperty(importeTotal);
    }
    
	public Factura(IntegerProperty idfactura, IntegerProperty clienteID, IntegerProperty vehiculoID,
			IntegerProperty numfactura, IntegerProperty numpresupuesto, IntegerProperty numordenrep,
			IntegerProperty numresguardo, ObjectProperty<Date> fecha, ObjectProperty<Date> fechaentrega,
			FloatProperty manoobra, FloatProperty materiales, FloatProperty grua, StringProperty estado,
			FloatProperty porcentajedefocul, BooleanProperty rdefocultos, BooleanProperty permisopruebas,
			BooleanProperty nopiezas, BooleanProperty modificable, ObjectProperty documento, FloatProperty importeTotal) {
		super();
		this.idfactura = idfactura;
		this.clienteID = clienteID;
		this.vehiculoID = vehiculoID;
		this.numfactura = numfactura;
		this.numpresupuesto = numpresupuesto;
		this.numordenrep = numordenrep;
		this.numresguardo = numresguardo;
		this.fecha = fecha;
		this.fechaentrega = fechaentrega;
		this.manoobra = manoobra;
		this.materiales = materiales;
		this.grua = grua;
		this.estado = estado;
		this.porcentajedefocul = porcentajedefocul;
		this.rdefocultos = rdefocultos;
		this.permisopruebas = permisopruebas;
		this.nopiezas = nopiezas;
		this.modificable = modificable;
		this.documento = documento;
		this.importeTotal = importeTotal;
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
	

	public IntegerProperty numfacturaProperty() {
		return this.numfactura;
	}
	

	public int getNumfactura() {
		return this.numfacturaProperty().get();
	}
	

	public void setNumfactura(final int numfactura) {
		this.numfacturaProperty().set(numfactura);
	}
	

	public IntegerProperty numpresupuestoProperty() {
		return this.numpresupuesto;
	}
	

	public int getNumpresupuesto() {
		return this.numpresupuestoProperty().get();
	}
	

	public void setNumpresupuesto(final int numpresupuesto) {
		this.numpresupuestoProperty().set(numpresupuesto);
	}
	

	public IntegerProperty numordenrepProperty() {
		return this.numordenrep;
	}
	

	public int getNumordenrep() {
		return this.numordenrepProperty().get();
	}
	

	public void setNumordenrep(final int numordenrep) {
		this.numordenrepProperty().set(numordenrep);
	}
	

	public IntegerProperty numresguardoProperty() {
		return this.numresguardo;
	}
	

	public int getNumresguardo() {
		return this.numresguardoProperty().get();
	}
	

	public void setNumresguardo(final int numresguardo) {
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
	

	public BooleanProperty modificableProperty() {
		return this.modificable;
	}
	

	public boolean isModificable() {
		return this.modificableProperty().get();
	}
	

	public void setModificable(final boolean modificable) {
		this.modificableProperty().set(modificable);
	}
	

	public ObjectProperty documentoProperty() {
		return this.documento;
	}
	

	public Object getDocumento() {
		return this.documentoProperty().get();
	}
	

	public void setDocumento(final Object documento) {
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
	
	
}
