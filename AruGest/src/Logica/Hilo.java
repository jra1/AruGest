package Logica;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import Modelo.Cliente;
import Modelo.Direccion;
import Modelo.Factura;
import Modelo.Vehiculo;
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
		// try {
		// // Mostrar mensaje
		// Alert alert = new Alert(AlertType.INFORMATION);
		// alert.setTitle("Generando factura");
		// alert.setHeaderText("Generando pdf de factura...");
		// alert.setContentText("");
		// DialogPane dialogPane = alert.getDialogPane();
		// dialogPane.getStylesheets().add(Inicio.class.getResource("../GUI/EstiloRoot.css").toExternalForm());
		// dialogPane.getStyleClass().add("my-dialog");
		// alert.show();
		//
		// // Leer los parámetros desde la factura
		// Cliente c = Inicio.CONEXION.leerClientePorID(f.getClienteID());
		// Direccion d = Inicio.CONEXION.leerDireccionPorID(c.getDireccionID());
		// Vehiculo v = Inicio.CONEXION.leerVehiculoPorID(f.getVehiculoID());
		//
		// FutureTask<Integer> task = new FutureTask<Integer>(new
		// Callable<Integer>() {
		// @Override
		// public Integer call() throws Exception {
		// try {
		// Map<String, Object> parameters = new HashMap<String, Object>();
		// parameters.put("idfactura", 2);
		// parameters.put("numfactura", f.getNumfactura());
		// parameters.put("numpresupuesto", f.getNumpresupuesto());
		// parameters.put("numorden", f.getNumordenrep());
		// parameters.put("numresguardo", f.getNumresguardo());
		// parameters.put("autor", c.getNombre());
		// parameters.put("direccion", d.getDireccionCompleta());
		// parameters.put("poblacion", d.getLocalidad());
		// parameters.put("dni", "72755519X");
		// parameters.put("telefono", c.getTelf1() + " " + c.getTelf2());
		// parameters.put("tipovehiculo",
		// Utilidades.tipoIDtoString(v.getTipoID()));
		// parameters.put("marcamodelo", v.getMarcaModelo());
		// parameters.put("matricula", v.getMatricula());
		// parameters.put("kms", "-");
		// parameters.put("manoobra", f.getManoobra());
		// parameters.put("materiales", f.getMateriales());
		// parameters.put("grua", f.getGrua());
		// parameters.put("suma", "Valor suma");
		// parameters.put("iva", Inicio.PRECIO_IVA);
		// parameters.put("sumaiva", "Valor suma IVA");
		// parameters.put("total", f.getImporteTotal());
		// parameters.put("fecha", f.getFecha());
		// parameters.put("fechaentrega", f.getFechaentrega());
		//
		// // Esta linea es aparte del resto
		// JasperReport jr =
		// JasperCompileManager.compileReport("reporteFactura.jrxml");
		// JasperPrint jpr = JasperFillManager.fillReport(jr, parameters,
		// Inicio.CONEXION.getCon()/* fds */);
		// // Se puede cambiar el fds por
		// // new JRBeanCollectionDataSource(listaMaterial)
		// // y así no es necesario crear la clase
		// // FacturaDataSource
		// // **** PERO NO FUNCIONA CON ESTO *****
		//
		// JasperExportManager.exportReportToPdfFile(jpr,
		// "reporteFacturaPDF_AruGest.pdf");
		// } catch (Exception e) {
		// Utilidades.mostrarError(e);
		// }
		// return 1;
		// }
		// });
		// new Thread(task).start();
		//
		// Integer result = task.get();
		// if (result == 1) {
		// alert.setHeaderText("¡Factura generada!");
		// alert.close();
		// // Runtime.getRuntime().exec("rundll32
		// // url.dll,FileProtocolHandler " +
		// // "reporteFacturaPDF_AruGest.pdf");
		// }
		// } catch (Exception e) {
		//
		// }

		new Thread(new Runnable() {
			public void run() {
				try {
					// Leer los parámetros desde la factura
					Cliente c = Inicio.CONEXION.leerClientePorID(f.getClienteID());
					Direccion d = Inicio.CONEXION.leerDireccionPorID(c.getDireccionID());
					Vehiculo v = Inicio.CONEXION.leerVehiculoPorID(f.getVehiculoID());

					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("idfactura", 2);
					parameters.put("numfactura", f.getNumfactura());
					parameters.put("numpresupuesto", f.getNumpresupuesto());
					parameters.put("numorden", f.getNumordenrep());
					parameters.put("numresguardo", f.getNumresguardo());
					parameters.put("autor", c.getNombre());
					parameters.put("direccion", d.getDireccionCompleta());
					parameters.put("poblacion", d.getLocalidad());
					parameters.put("dni", "72755519X");
					parameters.put("telefono", c.getTelf1() + " " + c.getTelf2());
					parameters.put("tipovehiculo", Utilidades.tipoIDtoString(v.getTipoID()));
					parameters.put("marcamodelo", v.getMarcaModelo());
					parameters.put("matricula", v.getMatricula());
					parameters.put("kms", "-");
					parameters.put("manoobra", f.getManoobra());
					parameters.put("materiales", f.getMateriales());
					parameters.put("grua", f.getGrua());
					parameters.put("suma", "Valor suma");
					parameters.put("iva", Inicio.PRECIO_IVA);
					parameters.put("sumaiva", "Valor suma IVA");
					parameters.put("total", f.getImporteTotal());
					parameters.put("fecha", f.getFecha());
					parameters.put("fechaentrega", f.getFechaentrega());

					// Esta linea es aparte del resto
					JasperReport jr = JasperCompileManager.compileReport("reporteFactura.jrxml");
					JasperPrint jpr = JasperFillManager.fillReport(jr, parameters, Inicio.CONEXION.getCon()/* fds */);
					// Se puede cambiar el fds por
					// new JRBeanCollectionDataSource(listaMaterial)
					// y así no es necesario crear la clase
					// FacturaDataSource
					// **** PERO NO FUNCIONA CON ESTO *****

					JasperExportManager.exportReportToPdfFile(jpr, "reporteFacturaPDF_AruGest.pdf");
				} catch (Exception e) {
					System.out.println("ERROR: " + e.getMessage());
					// Utilidades.mostrarError(e);
				}
			}
		}).start();

	}

}
