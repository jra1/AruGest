package Logica;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import Modelo.Factura;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

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

	public static void hilo_GeneraPDF(Factura f) {
		try {
			// Alert alert = new Alert(AlertType.INFORMATION);
			// alert.setTitle("Generando factura");
			// alert.setHeaderText("Generando pdf de factura...");
			// alert.setContentText("");
			// DialogPane dialogPane = alert.getDialogPane();
			// dialogPane.getStylesheets().add(Inicio.class.getResource("../GUI/EstiloRoot.css").toExternalForm());
			// dialogPane.getStyleClass().add("my-dialog");
			// alert.show();

			FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					try {
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("idfactura", 2);
						parameters.put("numfactura", f.getNumfactura());
						parameters.put("numpresupuesto", f.getNumpresupuesto());
						parameters.put("numorden", f.getNumordenrep());
						parameters.put("numresguardo", f.getNumresguardo());
						parameters.put("autor", "Joseba Ruiz Arana");
						parameters.put("direccion", "Calle Plaza de la Cooperativa 7, 1ºB");
						parameters.put("poblacion", "Vitoria-Gasteiz");
						parameters.put("dni", "72755519X");
						parameters.put("telefono", "670574316 / 945266015");
						parameters.put("tipovehiculo", "Turismo");
						parameters.put("marcamodelo", "Renault Megane 1.5 dci");
						parameters.put("matricula", "1947-FFY");
						parameters.put("kms", "187.779");
						parameters.put("manoobra", "713,00");
						parameters.put("materiales", "640,80");
						parameters.put("grua", "0,00");
						parameters.put("suma", "1353,80");
						parameters.put("iva", "21");
						parameters.put("sumaiva", "216,61");
						parameters.put("total", "1570,41");
						parameters.put("fecha", f.getFecha());
						parameters.put("fechaentrega", f.getFechaentrega());

						// Esta linea es aparte del resto
						JasperReport jr = JasperCompileManager.compileReport("reporteFactura.jrxml");
						JasperPrint jpr = JasperFillManager.fillReport(jr, parameters,
								Inicio.CONEXION.getCon()/* fds */);
						// Se puede cambiar el fds por
						// new JRBeanCollectionDataSource(listaMaterial)
						// y así no es necesario crear la clase
						// FacturaDataSource
						// **** PERO NO FUNCIONA CON ESTO *****

						JasperExportManager.exportReportToPdfFile(jpr, "reporteFacturaPDF_AruGest.pdf");
					} catch (Exception e) {
						Utilidades.mostrarError(e);
					}
					return 1;
				}
			});
			new Thread(task).start();

			Integer result = task.get();
			if (result == 1) {
				// alert.setHeaderText("¡Factura generada!");
				// alert.close();
			}
		} catch (Exception e) {

		}

		// new Thread(new Runnable() {
		// public void run() {
		//
		// }
		// }).start();

	}

}
