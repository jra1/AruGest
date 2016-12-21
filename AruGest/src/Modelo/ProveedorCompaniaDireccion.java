package Modelo;

public class ProveedorCompaniaDireccion {
	private ProveedorCompania pc;
	private Direccion direccion;

	public ProveedorCompaniaDireccion() {
		setPc(new ProveedorCompania(0, "", "", 0, "", "", null, false, false));
		setDireccion(new Direccion("", 0, "", "", 0, "", ""));
	}

	public ProveedorCompania getPc() {
		return pc;
	}

	public void setPc(ProveedorCompania pc) {
		this.pc = pc;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion d) {
		this.direccion = d;
	}
}
