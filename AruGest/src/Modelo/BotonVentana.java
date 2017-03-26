package Modelo;

public class BotonVentana {
	private int num;
	private boolean visible;
	private String nombre;

	public BotonVentana(int num, boolean visible, String nombre) {
		this.num = num;
		this.visible = visible;
		this.nombre = nombre;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
