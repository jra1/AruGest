package Modelo;

public class VehiculoSustitucionClienteVehiculo {
	private VehiculoSustitucion vs;
	private Cliente cliente;
	private Vehiculo vehiculo;
	
	public VehiculoSustitucionClienteVehiculo(VehiculoSustitucion vs, Cliente cliente, Vehiculo vehiculo) {
		super();
		this.vs = vs;
		this.cliente = cliente;
		this.vehiculo = vehiculo;
	}
	
	public VehiculoSustitucion getVehiculoSustitucion() {
		return vs;
	}
	public void setFactura(VehiculoSustitucion vs) {
		this.vs = vs;
	}
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
