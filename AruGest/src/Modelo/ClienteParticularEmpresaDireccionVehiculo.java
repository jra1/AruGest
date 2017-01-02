package Modelo;

public class ClienteParticularEmpresaDireccionVehiculo {
	private Cliente cliente;
	private Particular particular;
	private Empresa empresa;
	private Direccion direccion;
	private Vehiculo vehiculo;

	// public ClienteParticularEmpresaDireccionVehiculo() {
	// this.cliente = new Cliente(0, "", "", "", "", 0);
	// this.particular = null;// new Particular(0, 0, "","","");
	// this.empresa = null;// new Empresa(0, 0, "","", false);
	// this.direccion = null;// new Direccion("", 0, "", "", 0, "", "");
	// }

	public ClienteParticularEmpresaDireccionVehiculo(Cliente cliente, Particular particular, Empresa empresa,
			Direccion direccion, Vehiculo vehiculo) {
		// super();
		setCliente(cliente);
		setParticular(particular);
		setEmpresa(empresa);
		setDireccion(direccion);
		setVehiculo(vehiculo);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Particular getParticular() {
		return particular;
	}

	public void setParticular(Particular particular) {
		this.particular = particular;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

}
