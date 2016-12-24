package Modelo;

public class ProveedorCompaniaDireccion {
	private ProveedorCompania pc;
	private Direccion direccion;

	public ProveedorCompaniaDireccion(boolean esCia) {
		setPc(new ProveedorCompania(0, "", "", 0, "", "", null, false, esCia));
		setDireccion(new Direccion("", 0, "", "", 0, "", ""));
	}

	public ProveedorCompaniaDireccion(ProveedorCompania pc, Direccion d) {
		setPc(pc);
		setDireccion(d);
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
