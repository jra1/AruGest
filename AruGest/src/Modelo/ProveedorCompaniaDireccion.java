package Modelo;

public class ProveedorCompaniaDireccion extends ProveedorCompania {
	private Direccion direccion;

	public ProveedorCompaniaDireccion(boolean esCia) {
		super(esCia);
		setDireccion(new Direccion(0, "", 0, "", ""));
	}

	public ProveedorCompaniaDireccion(ProveedorCompania pc, Direccion d) {
		super(pc.getIdprovecompa(), pc.getCif(), pc.getNombre(), pc.getDireccionID(), pc.getTelf1(), pc.getTelf2(),
				pc.getLogo(), pc.isEsdesguace(), pc.isEscompania(), pc.getPersonaContacto());
		setDireccion(d);
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion d) {
		this.direccion = d;
	}
}
