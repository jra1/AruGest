package Logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
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
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressBar;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRStyledText;

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
			dialogPane.getStylesheets().add(Inicio.class.getResource("/GUI/EstiloRoot.css").toExternalForm());
			dialogPane.getStyleClass().add("my-dialog");
			alert.show();

			FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					if (Inicio.RUTA_FACTURAS.equalsIgnoreCase("")) {
						Platform.runLater(new Runnable() {
						    @Override
						    public void run() {
						    	alert.setAlertType(AlertType.WARNING);
						    	alert.setHeaderText("Debe introducir una carpeta donde guardar las facturas");
						    	alert.setContentText(
						    			"Vaya a 'Opciones' y seleccione la ruta donde se van a guardar las facturas generadas.");
						    }
						});
						return 0;
					}

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
						if (!f.getNumfactura().equalsIgnoreCase("") && !f.getNumfactura().equalsIgnoreCase("0")) {
							parameters.put("cboxfactura", "recursos/images/selec.png");
							parameters.put("numfactura", f.getNumfactura());
						} else {
							parameters.put("cboxfactura", "recursos/images/selecNO.png");
							parameters.put("numfactura", "");
						}
						if (!f.getNumpresupuesto().equalsIgnoreCase("")
								&& !f.getNumpresupuesto().equalsIgnoreCase("0")) {
							parameters.put("cboxpresupuesto", "recursos/images/selec.png");
							parameters.put("numpresupuesto", f.getNumpresupuesto());
						} else {
							parameters.put("cboxpresupuesto", "recursos/images/selecNO.png");
							parameters.put("numpresupuesto", "");
						}
						if (!f.getNumordenrep().equalsIgnoreCase("") && !f.getNumordenrep().equalsIgnoreCase("0")) {
							parameters.put("cboxorden", "recursos/images/selec.png");
							parameters.put("numorden", f.getNumordenrep());
						} else {
							parameters.put("cboxorden", "recursos/images/selecNO.png");
							parameters.put("numorden", "");
						}

						if (!f.getNumresguardo().equalsIgnoreCase("") && !f.getNumresguardo().equalsIgnoreCase("0")) {
							parameters.put("cboxresguardo", "recursos/images/selec.png");
							parameters.put("numresguardo", f.getNumresguardo());
						} else {
							parameters.put("cboxresguardo", "recursos/images/selecNO.png");
							parameters.put("numresguardo", "");
						}
						parameters.put("autor", c.getNombre());
						if (d.getIddireccion() != 0) {
							parameters.put("direccion", d.getDireccion());
						} else {
							parameters.put("direccion", "");
						}
						parameters.put("poblacion", d.getLocalidad());
						if (c.getTipo().equalsIgnoreCase("P")) {
							parameters.put("dni", p.getNif());
						} else {
							parameters.put("dni", e.getCif());
						}
						parameters.put("telefono", c.getTelf1() + " " + c.getTelf2());
						parameters.put("mostrarTlf", f.isMostrarTlf());
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
						parameters.put("logoAdeada", "recursos/images/Logo_Adeada.png");
						if (f.isRdefocultos()) {
							parameters.put("cboxdef", "recursos/images/selec.png");
						} else {
							parameters.put("cboxdef", "recursos/images/selecNO.png");
						}
						if (f.isPermisopruebas()) {
							parameters.put("cboxpermiso", "recursos/images/selec.png");
						} else {
							parameters.put("cboxpermiso", "recursos/images/selecNO.png");
						}
						if (f.isNopiezas()) {
							parameters.put("cboxpiezas", "recursos/images/selec.png");
						} else {
							parameters.put("cboxpiezas", "recursos/images/selecNO.png");
						}
						parameters.put("porcentaje", f.getPorcentajedefocul());

						String nombreFactura = "";
						if (!f.getNumfactura().equalsIgnoreCase("0")) {
							nombreFactura += "Factura " + f.getNumfactura();
						} else if (!f.getNumpresupuesto().equalsIgnoreCase("0")) {
							nombreFactura += "Presupuesto " + f.getNumpresupuesto();
						} else if (!f.getNumordenrep().equalsIgnoreCase("0")) {
							nombreFactura += "Orden rep " + f.getNumordenrep();
						} else if (!f.getNumresguardo().equalsIgnoreCase("0")) {
							nombreFactura += "Resguardo dep " + f.getNumresguardo();
						} else {
							nombreFactura += v.getMarcaModelo();
						}
						nombreFactura += "-" + c.getNombre() + ".pdf";
						String ruta = Utilidades.dameRuta(f.getFecha(), nombreFactura);
						InputStream is = Inicio.class.getResourceAsStream("/recursos/ReporteFactura.jrxml");
						JasperReport jr = JasperCompileManager.compileReport(is);
						jr.setProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, "true");
						JasperPrint jpr = JasperFillManager.fillReport(jr, parameters, Inicio.CONEXION.getCon());
						JasperExportManager.exportReportToPdfFile(jpr, ruta);
					} catch (Exception ex) {
						ex.printStackTrace();
						return -1;
					}
					return 1;
				}
			});

			new Thread(task).start();
			Integer result = task.get();

			if (result == 1) {
				alert.setHeaderText("¡Factura generada!");
			} else if (result == 0) {

			} else {
				alert.close();
				Utilidades.mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error al generar el pdf de la factura",
						"");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
