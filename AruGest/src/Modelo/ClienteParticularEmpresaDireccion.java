package Modelo;

public class ClienteParticularEmpresaDireccion {
	private Cliente cliente;
	private Particular particular;
	private Empresa empresa;
	private Direccion direccion;

	public ClienteParticularEmpresaDireccion(Cliente cliente, Particular particular, Empresa empresa,
			Direccion direccion) {
		super();
		this.cliente = cliente;
		this.particular = particular;
		this.empresa = empresa;
		this.direccion = direccion;
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
	
}