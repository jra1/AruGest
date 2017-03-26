package Modelo;

public class ClienteParticularEmpresaDireccionVehiculo extends ClienteParticularEmpresaDireccion {
	/*
	 * private Cliente cliente; private Particular particular; private Empresa
	 * empresa; private Direccion direccion;
	 */
	private Vehiculo vehiculo;
	private int kms;

	public ClienteParticularEmpresaDireccionVehiculo() {
		/*
		 * this.cliente = new Cliente(0, "", "", "", "", 0); this.particular =
		 * new Particular(0, 0, "", "", ""); this.empresa = new Empresa(0, 0,
		 * "", "", false); this.direccion = new Direccion("", 0, "", "", 0, "",
		 * "");
		 */
		super();
		this.vehiculo = new Vehiculo(0, 0, "", "", "", "", 1);
		this.kms = 0;
	}

	public ClienteParticularEmpresaDireccionVehiculo(Cliente cliente, Particular particular, Empresa empresa,
			Direccion direccion, Vehiculo vehiculo, int kms) {
		// setCliente(cliente);
		// setParticular(particular);
		// setEmpresa(empresa);
		// setDireccion(direccion);
		super(cliente, particular, empresa, direccion);
		setVehiculo(vehiculo);
		setKms(kms);
	}

	// public Cliente getCliente() {
	// return cliente;
	// }
	//
	// public void setCliente(Cliente cliente) {
	// this.cliente = cliente;
	// }
	//
	// public Particular getParticular() {
	// return particular;
	// }
	//
	// public void setParticular(Particular particular) {
	// this.particular = particular;
	// }
	//
	// public Empresa getEmpresa() {
	// return empresa;
	// }
	//
	// public void setEmpresa(Empresa empresa) {
	// this.empresa = empresa;
	// }
	//
	// public Direccion getDireccion() {
	// return direccion;
	// }
	//
	// public void setDireccion(Direccion direccion) {
	// this.direccion = direccion;
	// }

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public int getKms() {
		return kms;
	}

	public void setKms(int kms) {
		this.kms = kms;
	}

}
