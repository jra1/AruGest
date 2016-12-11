package Logica;

public class Hilos extends Thread {

	// private String nombrePackage = "";
	// private String nombreFuncion = "";

	/**
	 * Constructor
	 * 
	 * @param str
	 *            nombre del hilo
	 */
	public Hilos(String str) {
		super(str);
	}

	/**
	 * Lo que se va a ejecutar en el hilo
	 */
	public void run() {
		for (int i = 0; i < 10; i++)
			System.out.println(i + " " + getName() + " - " + getState());
		System.out.println("Termina thread " + getName());
	}

	/**
	 * Para utilizar los hilos
	 */
	// new Hilos("Pepe").start();
	// new Hilos("Juan").start();
	// System.out.println("Termina thread main");

}
