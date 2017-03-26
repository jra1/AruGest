package Modelo;

import javafx.scene.layout.AnchorPane;

public class GestorVentana {
	private AnchorPane ap;
	private String nombre;
	
	
	public GestorVentana(AnchorPane ap, String nombre) {
		super();
		this.ap = ap;
		this.nombre = nombre;
	}
	
	public AnchorPane getAp() {
		return ap;
	}
	public void setAp(AnchorPane ap) {
		this.ap = ap;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
