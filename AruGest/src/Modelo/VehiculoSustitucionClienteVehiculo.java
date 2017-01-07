package Modelo;

public class VehiculoSustitucionClienteVehiculo extends VehiculoSustitucion {
	// private VehiculoSustitucion vs;
	private Cliente cliente;
	private Vehiculo vehiculo;

	public VehiculoSustitucionClienteVehiculo() {
		super();
	}

	public VehiculoSustitucionClienteVehiculo(VehiculoSustitucion vs, Cliente cliente, Vehiculo vehiculo) {
		super(vs.getIdvehiculosusti(), vs.getFechacoge(), vs.getFechadevuelve(), vs.getClienteID(), vs.getVehiculoID(),
				vs.getObservaciones());
		// this.vs = vs;
		this.cliente = cliente;
		this.vehiculo = vehiculo;
	}

	// public VehiculoSustitucion getVehiculoSustitucion() {
	// return vs;
	// }
	// public void setVehiculoSustitucion(VehiculoSustitucion vs) {
	// this.vs = vs;
	// }
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

}
