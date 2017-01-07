package Modelo;

public class ProveedorCompaniaDireccion extends ProveedorCompania {
	// private ProveedorCompania pc;
	private Direccion direccion;

	public ProveedorCompaniaDireccion(boolean esCia) {
		super(esCia);
		// setPc(new ProveedorCompania(0, "", "", 0, "", "", null, false,
		// esCia));
		setDireccion(new Direccion("", 0, "", "", 0, "", ""));
	}

	public ProveedorCompaniaDireccion(ProveedorCompania pc, Direccion d) {
		super(pc.getIdprovecompa(), pc.getCif(), pc.getNombre(), pc.getDireccionID(), pc.getTelf1(), pc.getTelf2(),
				pc.getLogo(), pc.isEsdesguace(), pc.isEscompania());
		// setPc(pc);
		setDireccion(d);
	}

	// public ProveedorCompania getPc() {
	// return pc;
	// }
	//
	// public void setPc(ProveedorCompania pc) {
	// this.pc = pc;
	// }

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion d) {
		this.direccion = d;
	}
}
