package Logica;

import java.util.function.Consumer;

public class Hilo extends Thread {

	// private String nombrePackage = "";
	// private String nombreFuncion = "";

	/**
	 * Constructor
	 * 
	 * @param str
	 *            nombre del hilo
	 */
	public Hilo(String str) {
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
	// new Hilo("Pepe").start();
	// new Hilo("Juan").start();
	// System.out.println("Termina thread main");

	public static void ejecutaHilo(String nombre, Consumer<Void> method) {
		new Hilo(nombre).start();

		new Thread(new Runnable() {
			public void run() {
				// System.out.println("Comienza thread " + nombre);
				method.accept(null);
				// System.out.println("Termina thread " + nombre);
			}
		}).start();
	}
}
