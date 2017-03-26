package Modelo;

public class FacturaClienteVehiculo {
	private Factura factura;
	private Cliente cliente;
	private Vehiculo vehiculo;
	
	public FacturaClienteVehiculo(Factura factura, Cliente cliente, Vehiculo vehiculo) {
		super();
		this.factura = factura;
		this.cliente = cliente;
		this.vehiculo = vehiculo;
	}
	
	public Factura getFactura() {
		return factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
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
