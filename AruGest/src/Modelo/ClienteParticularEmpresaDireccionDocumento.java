package Modelo;

public class ClienteParticularEmpresaDireccionDocumento extends ClienteParticularEmpresaDireccion {

	private Documento documento;

	public ClienteParticularEmpresaDireccionDocumento() {
		super();
		this.documento = new Documento(0, 0, 0, "", null, "");
	}

	public ClienteParticularEmpresaDireccionDocumento(Cliente cliente, Particular particular, Empresa empresa,
			Direccion direccion, Documento documento) {
		super(cliente, particular, empresa, direccion);
		setDocumento(documento);
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

}
