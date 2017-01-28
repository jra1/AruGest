package Logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import Modelo.Cliente;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.Particular;
import Modelo.Vehiculo;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressBar;
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
	 * @param nombre
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

			// Mostrar mensaje
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Generando factura");
			alert.setHeaderText("Generando pdf de factura...");
			alert.setContentText("");
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(Inicio.class.getResource("../GUI/EstiloRoot.css").toExternalForm());
			dialogPane.getStyleClass().add("my-dialog");
			alert.show();

			FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					// Leer los parámetros desde la factura
					Cliente c = Inicio.CONEXION.leerClientePorID(f.getClienteID());
					Direccion d = Inicio.CONEXION.leerDireccionPorID(c.getDireccionID());
					Vehiculo v = Inicio.CONEXION.leerVehiculoPorID(f.getVehiculoID());
					Particular p = null;
					Empresa e = null;
					if (c.getTipo().equalsIgnoreCase("P")) {
						p = Inicio.CONEXION.buscarParticularPorClienteID(c.getIdcliente());
					} else {
						e = Inicio.CONEXION.buscarEmpresaPorClienteID(c.getIdcliente());
					}

					try {
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("idfactura", f.getIdfactura());
						if (f.getNumfactura() > 0) {
							parameters.put("cboxfactura", "file:images/selec.png");
						} else {
							parameters.put("cboxfactura", "file:images/selecNO.png");
						}
						parameters.put("numfactura", f.getNumfactura());
						if (f.getNumpresupuesto() > 0) {
							parameters.put("cboxpresupuesto", "file:images/selec.png");
						} else {
							parameters.put("cboxpresupuesto", "file:images/selecNO.png");
						}
						parameters.put("numpresupuesto", f.getNumpresupuesto());
						if (f.getNumordenrep() > 0) {
							parameters.put("cboxorden", "file:images/selec.png");
						} else {
							parameters.put("cboxorden", "file:images/selecNO.png");
						}
						parameters.put("numorden", f.getNumordenrep());
						if (f.getNumresguardo() > 0) {
							parameters.put("cboxresguardo", "file:images/selec.png");
						} else {
							parameters.put("cboxresguardo", "file:images/selecNO.png");
						}
						parameters.put("numresguardo", f.getNumresguardo());
						parameters.put("autor", c.getNombre());
						parameters.put("direccion", d.getDireccionCompleta());
						parameters.put("poblacion", d.getLocalidad());
						if (c.getTipo().equalsIgnoreCase("P")) {
							parameters.put("dni", p.getNif());
						} else {
							parameters.put("dni", e.getCif());
						}
						parameters.put("telefono", c.getTelf1() + " " + c.getTelf2());
						parameters.put("tipovehiculo", Utilidades.tipoIDtoString(v.getTipoID()));
						parameters.put("marcamodelo", v.getMarcaModelo());
						parameters.put("matricula", v.getMatricula());
						parameters.put("kms", f.getKms());
						parameters.put("manoobra", f.getManoobra());
						parameters.put("materiales", f.getMateriales());
						parameters.put("grua", f.getGrua());
						parameters.put("suma", f.getSuma());
						parameters.put("iva", Inicio.PRECIO_IVA);
						parameters.put("sumaiva", f.getSumaIva());
						parameters.put("total", f.getImporteTotal());
						parameters.put("fecha", f.getFecha());
						parameters.put("fechaentrega", f.getFechaentrega());
						parameters.put("logoAdeada", "file:images/Logo_Adeada.png");
						if (f.isRdefocultos()) {
							parameters.put("cboxdef", "file:images/selec.png");
						} else {
							parameters.put("cboxdef", "file:images/selecNO.png");
						}
						if (f.isPermisopruebas()) {
							parameters.put("cboxpermiso", "file:images/selec.png");
						} else {
							parameters.put("cboxpermiso", "file:images/selecNO.png");
						}
						if (f.isNopiezas()) {
							parameters.put("cboxpiezas", "file:images/selec.png");
						} else {
							parameters.put("cboxpiezas", "file:images/selecNO.png");
						}

						String nombreFactura = c.getNombre() + "-" + v.getMarcaModelo() + ".pdf";
						String ruta = Inicio.RUTA_FACTURAS + "\\" + nombreFactura;

						// Esta linea es aparte del resto
						JasperReport jr = JasperCompileManager.compileReport("reporteFactura.jrxml");
						JasperPrint jpr = JasperFillManager.fillReport(jr, parameters, Inicio.CONEXION.getCon());
						JasperExportManager.exportReportToPdfFile(jpr, ruta);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
						return 0;
					}
					return 1;
				}
			});
			new Thread(task).start();

			Integer result = task.get();
			if (result == 1) {
				alert.setHeaderText("¡Factura generada!");
				// alert.close();
				// Runtime.getRuntime().exec("rundll32url.dll,FileProtocolHandler
				// " + "reporteFacturaPDF_AruGest.pdf");
			} else {
				alert.close();
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error al generar el pdf de la factura",
						"");
			}
		} catch (Exception e) {

		}

		// new Thread(new Runnable() {
		// public void run() {
		// try {
		// // Leer los parámetros desde la factura
		// Cliente c = Inicio.CONEXION.leerClientePorID(f.getClienteID());
		// Direccion d = Inicio.CONEXION.leerDireccionPorID(c.getDireccionID());
		// Vehiculo v = Inicio.CONEXION.leerVehiculoPorID(f.getVehiculoID());
		// Particular p = null;
		// Empresa e = null;
		// if (c.getTipo().equalsIgnoreCase("P")) {
		// p = Inicio.CONEXION.buscarParticularPorClienteID(c.getIdcliente());
		// } else {
		// e = Inicio.CONEXION.buscarEmpresaPorClienteID(c.getIdcliente());
		// }
		//
		// Map<String, Object> parameters = new HashMap<String, Object>();
		// parameters.put("idfactura", f.getIdfactura());
		// parameters.put("numfactura", f.getNumfactura());
		// parameters.put("numpresupuesto", f.getNumpresupuesto());
		// parameters.put("numorden", f.getNumordenrep());
		// parameters.put("numresguardo", f.getNumresguardo());
		// parameters.put("autor", c.getNombre());
		// parameters.put("direccion", d.getDireccionCompleta());
		// parameters.put("poblacion", d.getLocalidad());
		// if (c.getTipo().equalsIgnoreCase("P")) {
		// parameters.put("dni", p.getNif());
		// } else {
		// parameters.put("dni", e.getCif());
		// }
		// parameters.put("telefono", c.getTelf1() + " " + c.getTelf2());
		// parameters.put("tipovehiculo",
		// Utilidades.tipoIDtoString(v.getTipoID()));
		// parameters.put("marcamodelo", v.getMarcaModelo());
		// parameters.put("matricula", v.getMatricula());
		// parameters.put("kms", "-");
		// parameters.put("manoobra", f.getManoobra());
		// parameters.put("materiales", f.getMateriales());
		// parameters.put("grua", f.getGrua());
		// parameters.put("suma", f.getSuma());
		// parameters.put("iva", Inicio.PRECIO_IVA);
		// parameters.put("sumaiva", f.getSumaIva());
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
		// System.out.println("ERROR: " + e.getMessage());
		// // Utilidades.mostrarError(e);
		// }
		// }
		// }).start();

	}

	/**
	 * Hilo para crear la BD desde el script
	 */
	public static boolean hilo_CreaBD(ProgressBar pb) {
		try {
			// Mostrar mensaje
			// Alert alert = new Alert(AlertType.INFORMATION);
			// alert.setTitle("Creando Base de datos");
			// alert.setHeaderText("Creando base de datos...");
			// alert.setContentText("");
			// DialogPane dialogPane = alert.getDialogPane();
			// dialogPane.getStylesheets().add(Inicio.class.getResource("../GUI/EstiloRoot.css").toExternalForm());
			// dialogPane.getStyleClass().add("my-dialog");
			// alert.show();

			FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					System.out.println("Empieza");
					BufferedReader br = new BufferedReader(new FileReader("ScriptSQL"));
					System.out.println("Empezando a leer fichero...");
					Connection connection = null;
					try {
						System.out.println(Inicio.DBURL);
						connection = openConnection(Inicio.DBURL);
						String line = br.readLine();
						StringBuilder statement = new StringBuilder();
						int contador = 0;
						while (line != null) {
							contador++;
							line = line.trim();
							pb.setProgress(contador / 10);
							Thread.sleep(100);
							if (!line.startsWith("--") && !line.startsWith("#") && !line.startsWith("//")) {
								statement.append(line);
								if (line.endsWith(";")) {
									executeLine(connection, statement.toString());
									statement = new StringBuilder();
								}
							}
							line = br.readLine();
						}
						if (statement.length() > 0) {
							executeLine(connection, statement.toString());
						}
					} finally {
						try {
							br.close();
						} catch (Exception e) {
							;
						}
						try {
							if (connection != null)
								connection.close();
						} catch (Exception e) {
							;
						}
					}
					return 1;
				}
			});

			new Thread(task).start();
			Integer result = task.get();
			if (result == 1) {
				System.out.println("Base de datos creada");
				return true;
				// alert.close();
				// Inicio.abreVentanaPrincipal();
			} else {
				System.out.println("Ocurrió un error al crear la base de datos");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private static void executeLine(Connection connection, String statement) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(statement);
		pstmt.execute();
		pstmt.close();
		System.out.println("Ejecutando " + statement);
	}

	public static Connection openConnection(String pUrl) throws SQLException {
		return DriverManager.getConnection(pUrl, "sa", "");
	}

}
