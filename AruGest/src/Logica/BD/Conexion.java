package Logica.BD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.h2.tools.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Logica.Inicio;
import Logica.Utilidades;
import Modelo.Cliente;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.ClienteParticularEmpresaDireccionDocumento;
import Modelo.Direccion;
import Modelo.Documento;
import Modelo.ElementosGolpes;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.FacturaClienteVehiculo;
import Modelo.Golpe;
import Modelo.Material;
import Modelo.Particular;
import Modelo.ProveedorCompania;
import Modelo.ProveedorCompaniaDireccion;
import Modelo.Servicio;
import Modelo.Vehiculo;
import Modelo.VehiculoSustitucion;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

/**
 * Esta clase se utilizará para todas las funciones que tengan que hacer accesos
 * a la base de datos, así como para crear y cerrar las conexiones.
 * 
 * @author Joseba
 *
 */
public class Conexion {

	private Connection con = null;

	public Conexion() {
	}

	/**
	 * Crea la conexion con la base de datos
	 * 
	 * @return - Connection a la base de datos
	 */
	public Connection crearConexion(String url) {

		try {
			// Con base de datos H2 (PRUEBA)
			// Usuario: sa
			// Pass: (No hay)
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection(url + ";AUTO_SERVER=TRUE;CIPHER=AES", "sa", "1234 1234");
			/**
			 * con = DriverManager.getConnection(
			 * "jdbc:h2:C:/AruGest/AruGestDB;AUTO_SERVER=TRUE", "sa", "");
			 */
			getCon().setAutoCommit(true);
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return con;
	}

	public void arrancarServidor(/* String args */) {
		try {
			Server server = Server.createTcpServer("-tcpAllowOthers");
			server.start();

			int seleccion = JOptionPane.showOptionDialog(null, "Servidor arrancado en: " + server.getStatus(),
					"Servidor arrancado", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, // null
																												// icono
																												// por
																												// defecto.
					new Object[] { "Aceptar", "Parar servidor" }, "Aceptar");

			if (seleccion == 1) {
				server.stop();
				server.shutdown();
			}

			// System.out.println(InetAddress.getLocalHost().getHostAddress());
			// System.out.println(InetAddress.getLocalHost().getHostName());
			// System.out.println(NetworkInterface.getNetworkInterfaces());
			// new Console().runTool(args);
			// directorio/ejecutable es el path del ejecutable y un nombre
			// Process p = Runtime.getRuntime().exec("C:/Program Files
			// (x86)/H2/bin/h2.bat");
			// Runtime.getRuntime().exec("C:/Program Files
			// (x86)/iTunes/iTunes.exe");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Hubo un error al arrancar el servidor: " + e.getMessage());
		}
	}

	/**
	 * Guarda en la base de datos la factura que se le pasa como parámetro
	 * 
	 * @param factura
	 *
	 * @return id de la factura guardada
	 */
	public int guardarFactura(Factura f, ObservableList<Servicio> servicios, ObservableList<Material> materiales) {
		int id = 0;
		try {
			// Para guardar factura -> 1º Factura 2º Servicios 3º Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO FACTURA (CLIENTEID, VEHICULOID, NUMFACTURA, NUMPRESUPUESTO, NUMORDENREP, NUMRESGUARDO, FECHA, "
							+ "FECHAENTREGA, MANOOBRA, MATERIALES, GRUA, RDEFOCULTOS, PORCENTAJEDEFOCUL, PERMISOPRUEBAS, "
							+ "NOPIEZAS, COBRADA, IMPORTETOTAL, SUMA, SUMAIVA, KMS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setInt(1, f.getClienteID());
			st.setInt(2, f.getVehiculoID());
			st.setString(3, f.getNumfactura());
			st.setString(4, f.getNumpresupuesto());
			st.setString(5, f.getNumordenrep());
			st.setString(6, f.getNumresguardo());
			st.setDate(7, new java.sql.Date(f.getFecha().getTime()));
			st.setDate(8, new java.sql.Date(f.getFechaentrega().getTime()));
			st.setFloat(9, f.getManoobra());
			st.setFloat(10, f.getMateriales());
			st.setFloat(11, f.getGrua());
			st.setBoolean(12, f.isRdefocultos());
			st.setFloat(13, f.getPorcentajedefocul());
			st.setBoolean(14, f.isPermisopruebas());
			st.setBoolean(15, f.isNopiezas());
			st.setBoolean(16, f.isCobrado());
			st.setFloat(17, f.getImporteTotal());
			st.setFloat(18, f.getSuma());
			st.setFloat(19, f.getSumaIva());
			st.setFloat(20, f.getKms());

			// Ejecutamos la sentencia
			int i = st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				// long idFactura =
				id = (int) rs.getLong(1);
				// Guardar servicios
				st = getCon().prepareStatement(
						"INSERT INTO SERVICIO (SERVICIO, HORAS, FACTURAID, TIPOSERVICIO) VALUES (?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				for (Servicio serv : servicios) {
					st.setString(1, serv.getServicio());
					st.setBigDecimal(2, new BigDecimal(serv.getHoras()));
					st.setInt(3, id);
					st.setString(4, serv.getTiposervicio());
					st.executeUpdate();
				}
				// Guardar materiales
				st = getCon().prepareStatement(
						"INSERT INTO MATERIAL (NOMBRE, PRECIOUNIT, FACTURAID, CANTIDAD, PRECIOTOTAL) VALUES (?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				for (Material mat : materiales) {
					st.setString(1, mat.getNombre());
					st.setBigDecimal(2, new BigDecimal(mat.getPreciounit()));
					st.setInt(3, id);
					st.setInt(4, mat.getCantidad());
					st.setBigDecimal(5, new BigDecimal(mat.getPreciototal()));
					st.executeUpdate();
				}
			}

			if (i > 0) {
				// Actualizar próximo número de presupuesto y factura
				actualizarNumPresuFactura();
				// Actualizar los valores de próximos presupuestos/facturas
				Inicio.CONEXION.getPrecioHoraIva();
			}

			// Se cierra la conexion
			getCon().close();

		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención", "Error al guardar factura",
					"Ocurrió un error al guardar la factura en la base de datos");
			e.printStackTrace();
			id = 0;
		}
		return id;
	}

	/**
	 * Modifica en la base de datos la factura que se le pasa como parámetro
	 * 
	 * @param factura
	 *
	 * @return id de la factura modificada
	 */
	public int modificarFactura(int idFactura, Factura f, ObservableList<Servicio> servicios,
			ObservableList<Material> materiales) {
		String sql = "";
		try {
			// Para guardar factura -> 1º Factura 2º Servicios 3º Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			sql = "UPDATE FACTURA SET CLIENTEID = ?, VEHICULOID = ?, NUMFACTURA = ?, NUMPRESUPUESTO = ?, NUMORDENREP = ?, NUMRESGUARDO = ?, FECHA = ?, "
					+ "FECHAENTREGA = ?, MANOOBRA = ?, MATERIALES = ?, GRUA = ?, RDEFOCULTOS = ?, PORCENTAJEDEFOCUL = ?, PERMISOPRUEBAS = ?, "
					+ "NOPIEZAS = ?, COBRADA = ?, IMPORTETOTAL = ?, SUMA = ?, SUMAIVA = ?, KMS = ? WHERE IDFACTURA = "
					+ idFactura;
			java.sql.PreparedStatement st = getCon().prepareStatement(sql);

			// Añadimos los parametros
			st.setInt(1, f.getClienteID());
			st.setInt(2, f.getVehiculoID());
			st.setString(3, f.getNumfactura());
			st.setString(4, f.getNumpresupuesto());
			st.setString(5, f.getNumordenrep());
			st.setString(6, f.getNumresguardo());
			st.setDate(7, new java.sql.Date(f.getFecha().getTime()));
			st.setDate(8, new java.sql.Date(f.getFechaentrega().getTime()));
			st.setFloat(9, f.getManoobra());
			st.setFloat(10, f.getMateriales());
			st.setFloat(11, f.getGrua());
			st.setBoolean(12, f.isRdefocultos());
			st.setFloat(13, f.getPorcentajedefocul());
			st.setBoolean(14, f.isPermisopruebas());
			st.setBoolean(15, f.isNopiezas());
			st.setBoolean(16, f.isCobrado());
			st.setFloat(17, f.getImporteTotal());
			st.setFloat(18, f.getSuma());
			st.setFloat(19, f.getSumaIva());
			st.setFloat(20, f.getKms());

			// Ejecutamos la sentencia
			st.executeUpdate();

			// Borrar servicios y guardar los nuevos
			sql = "DELETE FROM SERVICIO WHERE FACTURAID = " + idFactura;
			st = getCon().prepareStatement(sql);
			st.executeUpdate();

			sql = "INSERT INTO SERVICIO (SERVICIO, HORAS, FACTURAID, TIPOSERVICIO) VALUES (?,?,?,?)";
			st = getCon().prepareStatement(sql);
			for (Servicio serv : servicios) {
				st.setString(1, serv.getServicio());
				st.setBigDecimal(2, new BigDecimal(serv.getHoras()));
				st.setInt(3, idFactura);
				st.setString(4, serv.getTiposervicio());
				st.executeUpdate();
			}

			// Borrar materiales y guadar los nuevos
			sql = "DELETE FROM MATERIAL WHERE FACTURAID = " + idFactura;
			st = getCon().prepareStatement(sql);
			st.executeUpdate();

			sql = "INSERT INTO MATERIAL (NOMBRE, PRECIOUNIT, FACTURAID, CANTIDAD, PRECIOTOTAL) VALUES (?,?,?,?,?)";
			st = getCon().prepareStatement(sql);
			for (Material mat : materiales) {
				st.setString(1, mat.getNombre());
				st.setBigDecimal(2, new BigDecimal(mat.getPreciounit()));
				st.setInt(3, idFactura);
				st.setInt(4, mat.getCantidad());
				st.setBigDecimal(5, new BigDecimal(mat.getPreciototal()));
				st.executeUpdate();
			}

			// Actualizar próximo número de presupuesto y factura
			actualizarNumPresuFactura();
			// Actualizar los valores de próximos presupuestos/facturas
			Inicio.CONEXION.getPrecioHoraIva();

			// Se cierra la conexion
			getCon().close();

		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención", "Error al guardar factura",
					"Ocurrió un error al guardar la factura en la base de datos");
			e.printStackTrace();
		}
		return idFactura;
	}

	/**
	 * Se actualiza en la BD el nº siguiente de presupuesto/factura
	 */
	public void actualizarNumPresuFactura() {
		String sql = "";
		PreparedStatement st;
		ResultSet rs;
		try {
			// Actualizar nº presupuesto
			sql = "SELECT MAX(NUMPRESUPUESTO) FROM FACTURA";
			st = getCon().prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				if (!rs.getString("MAX(NUMPRESUPUESTO)").equalsIgnoreCase("")) {
					sql = "UPDATE AUXILIAR SET VALOR = ((SELECT MAX(NUMPRESUPUESTO) FROM FACTURA) + 1) WHERE CLAVE = 'PRESUPUESTO'";
				} else {
					sql = "UPDATE AUXILIAR SET VALOR = '" + Inicio.NUM_PRESUPUESTO + "' WHERE CLAVE = 'PRESUPUESTO'";
				}
				st = getCon().prepareStatement(sql);
				st.executeUpdate();
			}

			// Actualizar nº factura
			sql = "SELECT MAX(NUMFACTURA) FROM FACTURA";
			st = getCon().prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				if (!rs.getString("MAX(NUMFACTURA)").equalsIgnoreCase("")) {
					sql = "UPDATE AUXILIAR SET VALOR = ((SELECT MAX(NUMFACTURA) FROM FACTURA) + 1) WHERE CLAVE = 'FACTURA'";
				} else {
					sql = "UPDATE AUXILIAR SET VALOR = '" + Inicio.NUM_FACTURA + "' WHERE CLAVE = 'FACTURA'";
				}
				st = getCon().prepareStatement(sql);
				st.executeUpdate();
			}

		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención",
					"Error al actualizar el número de presupuesto/factura",
					"Ocurrió un error al actualizar el número de presupuesto/factura en la base de datos");
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza las opciones de la tabla auxiliar en la BD
	 * 
	 * @param precioHora
	 * @param iva
	 * @param numPresupuesto
	 * @param numFactura
	 * @param rutaFacturas
	 * @return
	 */
	public boolean actualizarOpciones(float precioHora, float iva, String numPresupuesto, String numFactura,
			String rutaFacturas) {
		String sql = "";
		PreparedStatement st;
		try {
			// Actualizar nº presupuesto
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'PRECIOHORA'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setFloat(1, precioHora);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Actualizar nº presupuesto
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'IVA'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setFloat(1, iva);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Actualizar nº presupuesto
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'PRESUPUESTO'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setString(1, numPresupuesto);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Actualizar nº presupuesto
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'FACTURA'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setString(1, numFactura);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Actualizar ruta
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'RUTAFACTURAS'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setString(1, rutaFacturas);
			// Ejecutamos la sentencia
			st.executeUpdate();
			return true;
		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención", "Error al guardar las opciones",
					"Ocurrió un error al actualizar las opciones en la base de datos");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Guarda en la BD el autologin como el parámetro que se pasa
	 */
	public boolean setAutologin(boolean login) {
		String sql = "";
		PreparedStatement st;
		try {
			// Actualizar autologin
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'AUTOLOGIN'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setBoolean(1, login);
			// Ejecutamos la sentencia
			st.executeUpdate();
			return true;
		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención", "Error al recordar",
					"Puede que la preferencia de recordar o no el usuario y contraseña no se haya guardado correctamente");
			return false;
		}
	}

	/**
	 * Guarda en la BD la ruta para las copias de seguridad
	 */
	public boolean setRutaBackup(String ruta) {
		String sql = "";
		PreparedStatement st;
		try {
			// Actualizar autologin
			sql = "UPDATE AUXILIAR SET VALOR = ? WHERE CLAVE = 'RUTABACKUP'";
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setString(1, ruta);
			// Ejecutamos la sentencia
			st.executeUpdate();
			return true;
		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención", "Error al guardar la ruta",
					"Ocurrió un problema al guardar la ruta para las copias de seguridad en la base de datos");
			return false;
		}
	}

	/**
	 * Guarda la direccion pasada como parámetro en la BD
	 * 
	 * @param d
	 * @return id generado en la BD o CERO si ocurrió algún error
	 */
	public long guardarDireccion(Direccion d) {
		PreparedStatement st;
		ResultSet rs;
		long idGenerado = 0;
		String sql = "";
		try {
			sql = "INSERT INTO DIRECCION (CALLE, NUMERO, PISO, LETRA, CPOSTAL, LOCALIDAD, PROVINCIA) VALUES (?,?,?,?,?,?,?)";
			st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// Añadimos los parametros
			try {
				st.setString(1, d.getCalle());
			} catch (NullPointerException e) {
				st.setString(1, "");
			}
			try {
				st.setInt(2, d.getNumero());
			} catch (NullPointerException e) {
				st.setInt(2, 0);
			}
			try {
				st.setString(3, d.getPiso());
			} catch (NullPointerException e) {
				st.setString(3, "");
			}
			try {
				st.setString(4, d.getLetra());
			} catch (NullPointerException e) {
				st.setString(4, "");
			}
			try {
				st.setInt(5, d.getCpostal());
			} catch (NullPointerException e) {
				st.setInt(5, 0);
			}
			try {
				st.setString(6, d.getLocalidad());
			} catch (NullPointerException e) {
				st.setString(6, "");
			}
			try {
				st.setString(7, d.getProvincia());
			} catch (NullPointerException e) {
				st.setString(7, "");
			}
			// Ejecutamos la sentencia
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				idGenerado = rs.getLong(1);
			}
		} catch (SQLException e) {
			idGenerado = 0;
		}
		return idGenerado;
	}

	/**
	 * Actualiza el DIRECCIONID de un cliente
	 * 
	 * @param idcliente
	 * @param iddireccion
	 * @return true si fue bien, false si hubo algún error
	 */
	public boolean actualizarIDDireccionCliente(int idcliente, int iddireccion) {
		String sql = "";
		boolean resul = true;
		PreparedStatement st;
		try {
			sql = "UPDATE CLIENTE SET DIRECCIONID = ? WHERE IDCLIENTE = " + idcliente;
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setInt(1, (int) iddireccion);
			// Ejecutamos la sentencia
			st.executeUpdate();
			resul = true;
		} catch (SQLException e) {
			resul = false;
		}
		return resul;
	}

	/**
	 * Actualiza el DIRECCIONID de un proveedor/cia
	 * 
	 * @param idproveedor
	 * @param iddireccion
	 * @return
	 */
	public boolean actualizarIDDireccionProveedor(int idproveedor, int iddireccion) {
		String sql = "";
		boolean resul = true;
		PreparedStatement st;
		try {
			sql = "UPDATE PROVEEDORCOMPANIA SET DIRECCIONID = ? WHERE IDPROVECOMPA = " + idproveedor;
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setInt(1, (int) iddireccion);
			// Ejecutamos la sentencia
			st.executeUpdate();
			resul = true;
		} catch (SQLException e) {
			resul = false;
		}
		return resul;
	}

	/**
	 * Guarda en la base de datos el cliente que se le pasa como parámetro
	 * 
	 * @param Direccion,
	 *            Cliente, Particular o Empresa (El que no sea, será null) a
	 *            guardar en la BD
	 */
	public boolean guardarCliente(ClienteParticularEmpresaDireccion cped) {
		boolean res = true;
		// 1º Guardar Direccion
		try {
			PreparedStatement st;
			ResultSet rs;
			long idGenerado = 0;
			String sql = "";
			// Se prepara la sentencia para introducir los datos de la direccion
			// SI NO ES NULL
			if (cped.getDireccion() != null
					&& (cped.getCliente().getDireccionID() != 0 || (cped.getCliente().getDireccionID() == 0
							&& !cped.getDireccion().getCalle().equalsIgnoreCase("No informado")))) {
				idGenerado = guardarDireccion(cped.getDireccion());
			}

			// 2º Guardar Cliente
			sql = "INSERT INTO CLIENTE (NOMBRE, TELF1, TELF2, TELF3, DIRECCIONID, TIPO) VALUES (?,?,?,?,?,?)";
			st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// Añadimos los parametros
			st.setString(1, cped.getCliente().getNombre());
			st.setString(2, cped.getCliente().getTelf1());
			st.setString(3, cped.getCliente().getTelf2());
			st.setString(4, cped.getCliente().getTelf3());
			if (idGenerado != 0) {
				st.setInt(5, (int) idGenerado);
			} else {
				st.setInt(5, 0);
			}
			st.setString(6, cped.getCliente().getTipo());
			// Ejecutamos la sentencia
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				idGenerado = rs.getLong(1);
				// 3º Guardar Particular / Empresa
				if (cped.getParticular() != null && !cped.getParticular().getNif().equalsIgnoreCase("")
						&& !cped.getParticular().getNombre().equalsIgnoreCase("")) {
					// Guardar Particular
					sql = "INSERT INTO PARTICULAR (CLIENTEID, NOMBRE, APELLIDOS, NIF) VALUES (?,?,?,?)";
					st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// Añadimos los parametros
					st.setInt(1, (int) idGenerado);
					st.setString(2, cped.getParticular().getNombre());
					st.setString(3, cped.getParticular().getApellidos());
					st.setString(4, cped.getParticular().getNif());
					// Ejecutamos la sentencia
					st.executeUpdate();
				} else if (cped.getEmpresa() != null && !cped.getEmpresa().getCif().equalsIgnoreCase("")
						&& !cped.getEmpresa().getNombre().equalsIgnoreCase("")) {
					// Guardar Empresa
					sql = "INSERT INTO EMPRESA (CLIENTEID, NOMBRE, CIF, ESPROVEEDOR) VALUES (?,?,?,?)";
					st.close();
					st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// Añadimos los parametros
					st.setInt(1, (int) idGenerado);
					st.setString(2, cped.getEmpresa().getNombre());
					st.setString(3, cped.getEmpresa().getCif());
					st.setBoolean(4, cped.getEmpresa().isEsProveedor());
					// Ejecutamos la sentencia
					st.executeUpdate();
				}
			}
			res = true;
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * 
	 * @param cped
	 * @return
	 */
	public boolean guardarCia(ProveedorCompaniaDireccion pcd) {
		boolean res = true;
		// 1º Guardar Direccion
		try {
			PreparedStatement st;
			long idGenerado = 0;
			String sql = "";
			// Se prepara la sentencia para introducir los datos de la direccion
			// SI NO ES NULL
			if (pcd.getDireccion() != null && pcd.getDireccionID() != 0) {
				idGenerado = guardarDireccion(pcd.getDireccion());
			}

			// 2º Guardar cía/proveedor
			sql = "INSERT INTO PROVEEDORCOMPANIA (CIF, NOMBRE, DIRECCIONID, TELF1, TELF2, LOGO, ESDESGUACE, ESCOMPANIA) VALUES (?,?,?,?,?,?,?,?)";
			st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// Añadimos los parametros
			st.setString(1, pcd.getCif());
			st.setString(2, pcd.getNombre());
			if (idGenerado != 0) {
				st.setInt(3, (int) idGenerado);
			} else {
				st.setInt(3, 0);
			}
			st.setString(4, pcd.getTelf1());
			st.setString(5, pcd.getTelf2());

			// File theFile = new File("sample_resume.pdf");
			// FileInputStream input = null;
			// input = new FileInputStream(theFile);
			// myStmt.setBinaryStream(1, input);

			// st.setBinaryStream(6, pcd.getPc().getLogo().getBinaryStream());
			st.setBlob(6, (Blob) pcd.getLogo());

			st.setBoolean(7, pcd.isEsdesguace());
			st.setBoolean(8, pcd.isEscompania());
			// Ejecutamos la sentencia
			st.executeUpdate();
			res = true;
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Guarda en la base de datos el vehículo que se le pasa como parámetro
	 * 
	 * @param vehiculo
	 *            a guardar en la BD, el ID del cliente dueño y el tipo de
	 *            vehiculo que es
	 * @return true si se guardo correctamente, false si no
	 */
	public boolean guardarVehiculo(Vehiculo v) {
		boolean res = true;
		String matricula = v.getMatricula();
		matricula = matricula.replaceAll("-", "");
		matricula = matricula.replaceAll(" ", "");
		matricula = matricula.replaceAll("/", "");
		try {
			// Para guardar factura -> 1º Factura 2º Servicios 3º Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO VEHICULO (CLIENTEID, MARCA, MODELO, VERSION, MATRICULA, ANIO, BASTIDOR, LETRASMOTOR, COLOR, CODRADIO, TIPOID, ESVEHICULOSUSTITUCION) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setInt(1, v.getClienteID());
			st.setString(2, v.getMarca());
			st.setString(3, v.getModelo());
			st.setString(4, v.getVersion());
			st.setString(5, matricula);
			st.setInt(6, v.getAnio());
			st.setString(7, v.getBastidor());
			st.setString(8, v.getLetrasmotor());
			st.setString(9, v.getColor());
			st.setString(10, v.getCodradio());
			st.setInt(11, v.getTipoID());
			st.setBoolean(12, v.isEsVehiculoSustitucion());

			// Ejecutamos la sentencia
			st.executeUpdate();
			res = true;
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina de la base de datos el vehículo cuyo id se le pasa como parámetro
	 * 
	 * @param id
	 *            del vehiculo a eliminar
	 * @return true si se eliminó correctamente, false si no
	 */
	public boolean eliminarVehiculo(int id) {
		/*
		 * 1º comprobar si tiene facturas: - si: coger id factura, eliminar
		 * servicios, eliminar materiales, eliminar factura. 2º comprobar si
		 * tiene documentos - si: eliminar documento 3º comprobar si es vehiculo
		 * sustitucion - si: eliminar vehiculo sustitucion
		 */
		String sql = "";
		boolean res = true;
		PreparedStatement pt;
		try {
			Statement st = getCon().createStatement();
			sql = "SELECT IDFACTURA FROM FACTURA WHERE VEHICULOID = " + id;

			ResultSet rs = st.executeQuery(sql);

			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					eliminarServiciosPorFacturaID(rs.getInt("IDFACTURA"));
					eliminarMaterialesPorFacturaID(rs.getInt("IDFACTURA"));
				}
				sql = "DELETE FROM FACTURA WHERE VEHICULOID = " + id;
				// Se prepara la sentencia
				pt = getCon().prepareStatement(sql);
				// Ejecutamos la sentencia
				pt.executeUpdate();
			}

			// 2º comprobar si tiene documentos
			/*
			 * sql = "SELECT IDDOCUMENTO FROM DOCUMENTO WHERE VEHICULOID = " +
			 * id; rs = st.executeQuery(sql); while (rs.next()) {
			 * eliminarDocumentosPorVehiculoID(id); }
			 */

			// 3º comprobar si es vehiculo de sustitucion
			sql = "SELECT IDVEHICULOSUSTI FROM VEHICULOSUSTITUCION WHERE VEHICULOID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarVehiculoSustiPorVehiculoID(id);
			}

			// 4º eliminar vehiculo
			sql = "DELETE FROM VEHICULO WHERE IDVEHICULO = " + id;
			pt = getCon().prepareStatement(sql);
			pt.executeUpdate();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Edita en la BD el vehículo pasado como parámetro
	 * 
	 * @param v
	 * @return true si fue bien, false si no
	 */
	public boolean editarVehiculo(Vehiculo v) {
		boolean res = true;
		String sql = "";
		try {
			sql = "UPDATE VEHICULO SET MARCA = ?, MODELO = ?, VERSION = ?, ANIO = ?,"
					+ "BASTIDOR = ?, LETRASMOTOR = ?, COLOR = ?, CODRADIO = ?, TIPOID = ?, ESVEHICULOSUSTITUCION = ? "
					+ "WHERE IDVEHICULO = " + v.getIdvehiculo();
			PreparedStatement st = getCon().prepareStatement(sql);
			st.setString(1, v.getMarca());
			st.setString(2, v.getModelo());
			st.setString(3, v.getVersion());
			st.setInt(4, v.getAnio());
			st.setString(5, v.getBastidor());
			st.setString(6, v.getLetrasmotor());
			st.setString(7, v.getColor());
			st.setString(8, v.getCodradio());
			st.setInt(9, v.getTipoID());
			st.setBoolean(10, v.isEsVehiculoSustitucion());
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();
			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Edita en la BD el cliente pasado como parámetro
	 * 
	 * @param cped
	 * @return true si fue bien, false si no
	 */
	public boolean editarCliente(ClienteParticularEmpresaDireccion cped) {
		boolean res = true;
		String sql = "";
		long idGenerado;
		try {
			PreparedStatement st;
			// 1º Direccion
			// Si ya tiene direccion, se actualiza
			// Si no tiene direccion (iddireccion = 0) Y SE HA INTRODUCIDO UNA
			// NUEVA, se crea
			if (cped.getCliente().getDireccionID() != 0) {
				sql = "UPDATE DIRECCION SET CALLE = ?, NUMERO = ?, PISO = ?, "
						+ "LETRA = ?, CPOSTAL = ?, LOCALIDAD = ?, PROVINCIA = ? " + "WHERE IDDIRECCION = "
						+ cped.getCliente().getDireccionID();
				st = getCon().prepareStatement(sql);
				// Añadimos los parametros
				st.setString(1, cped.getDireccion().getCalle());
				st.setInt(2, cped.getDireccion().getNumero());
				st.setString(3, cped.getDireccion().getPiso());
				st.setString(4, cped.getDireccion().getLetra());
				st.setInt(5, cped.getDireccion().getCpostal());
				st.setString(6, cped.getDireccion().getLocalidad());
				st.setString(7, cped.getDireccion().getProvincia());
				// Ejecutamos la sentencia
				st.executeUpdate();
			} else {
				// Si llega aquí: iddireccion = 0
				if (cped.getDireccion().getCalle() != "" || cped.getDireccion().getLocalidad() != ""
						|| cped.getDireccion().getProvincia() != "") {
					// Guardar direccion y asignar su id al cliente
					idGenerado = guardarDireccion(cped.getDireccion());
					if (idGenerado > 0) { // Si es 0 es que hubo un error al
						// guardar
						// la direccion
						res = actualizarIDDireccionCliente(cped.getCliente().getIdcliente(), (int) idGenerado);
						if (res == false) {
							return res;
						}
						cped.getCliente().setDireccionID((int) idGenerado);
						// Acabar aquí la funcion si res = false
					}
				}
			}
			// 2º Cliente
			sql = "UPDATE CLIENTE SET NOMBRE = ?, TELF1 = ?, TELF2 = ?, " + "TELF3 = ? " + "WHERE IDCLIENTE = "
					+ cped.getCliente().getIdcliente();
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setString(1, cped.getCliente().getNombre());
			st.setString(2, cped.getCliente().getTelf1());
			st.setString(3, cped.getCliente().getTelf2());
			st.setString(4, cped.getCliente().getTelf3());
			// Ejecutamos la sentencia
			st.executeUpdate();
			// 3º Particular / Empresa
			if (cped.getParticular() != null) {
				// Guardar Particular
				sql = "UPDATE PARTICULAR SET NOMBRE = ?, APELLIDOS = ?, NIF = ? " + "WHERE IDPARTICULAR = "
						+ cped.getParticular().getIdparticular();
				st = getCon().prepareStatement(sql);
				// Añadimos los parametros
				st.setString(1, cped.getParticular().getNombre());
				st.setString(2, cped.getParticular().getApellidos());
				st.setString(3, cped.getParticular().getNif());
			} else if (cped.getEmpresa() != null) {
				// Guardar Empresa
				sql = "UPDATE EMPRESA SET NOMBRE = ?, CIF = ?, ESPROVEEDOR = ? " + "WHERE IDEMPRESA = "
						+ cped.getEmpresa().getIdempresa();
				st.close();
				st = getCon().prepareStatement(sql);
				// Añadimos los parametros
				st.setString(1, cped.getEmpresa().getNombre());
				st.setString(2, cped.getEmpresa().getCif());
				st.setBoolean(3, cped.getEmpresa().isEsProveedor());
			}
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();
			res = true;
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Edita en la BD la cia/proveedor pasado como parámetro
	 * 
	 * @param pcd
	 * @return
	 */
	public boolean editarCia(ProveedorCompaniaDireccion pcd) {
		boolean res = true;
		String sql = "";
		long idGenerado;
		try {
			PreparedStatement st;
			// 1º Direccion
			// Si ya tiene direccion, se actualiza
			// Si no tiene direccion (iddireccion = 0), se crea
			if (pcd.getDireccionID() != 0) {
				sql = "UPDATE DIRECCION SET CALLE = ?, NUMERO = ?, PISO = ?, "
						+ "LETRA = ?, CPOSTAL = ?, LOCALIDAD = ?, PROVINCIA = ? " + " WHERE IDDIRECCION = "
						+ pcd.getDireccionID();
				st = getCon().prepareStatement(sql);
				// Añadimos los parametros
				st.setString(1, pcd.getDireccion().getCalle());
				st.setInt(2, pcd.getDireccion().getNumero());
				st.setString(3, pcd.getDireccion().getPiso());
				st.setString(4, pcd.getDireccion().getLetra());
				st.setInt(5, pcd.getDireccion().getCpostal());
				st.setString(6, pcd.getDireccion().getLocalidad());
				st.setString(7, pcd.getDireccion().getProvincia());
				// Ejecutamos la sentencia
				st.executeUpdate();
			} else {
				// Si llega aquí: iddireccion = 0
				if (pcd.getDireccion().getCalle() != "" || pcd.getDireccion().getLocalidad() != ""
						|| pcd.getDireccion().getProvincia() != "") {
					// Guardar direccion y asignarle su iddireccion al cliente
					idGenerado = guardarDireccion(pcd.getDireccion());
					if (idGenerado > 0) { // Si es 0 es que hubo un error al
						// guardar
						// la direccion
						res = actualizarIDDireccionProveedor(pcd.getIdprovecompa(), (int) idGenerado);
						// Acabar aquí la funcion si res = false
					}
				}
			}
			// 2º Cia
			sql = "UPDATE PROVEEDORCOMPANIA SET CIF = ?, NOMBRE = ?, TELF1 = ?, TELF2 = ?, LOGO = ?, ESDESGUACE = ?, ESCOMPANIA = ? WHERE IDPROVECOMPA = "
					+ pcd.getIdprovecompa();
			st = getCon().prepareStatement(sql);
			// Añadimos los parametros
			st.setString(1, pcd.getCif());
			st.setString(2, pcd.getNombre());
			st.setString(3, pcd.getTelf1());
			st.setString(4, pcd.getTelf2());
			st.setBlob(5, (Blob) pcd.getLogo());
			st.setBoolean(6, pcd.isEsdesguace());
			st.setBoolean(7, pcd.isEscompania());
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();
			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	public boolean eliminarCia(int idCia, int idDireccion) {
		String sql = "";
		boolean res = true;
		PreparedStatement pt;
		try {
			// Eliminar cía
			sql = "DELETE FROM PROVEEDORCOMPANIA WHERE PROVEEDORCOMPANIA.IDPROVECOMPA = " + idCia;
			pt = getCon().prepareStatement(sql);
			pt.executeUpdate();

			// Eliminar direccion
			if (idDireccion > 0) {
				res = eliminarDireccionPorID(idDireccion);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina el cliente pasado como parámetro de la BD con todo lo que tenga
	 * asociado (Facturas, vehiculos de sustitucion, documentos...)
	 * 
	 * @param cped
	 * @return
	 */
	public boolean eliminarCliente(ClienteParticularEmpresaDireccion cped) {
		/*
		 * Orden para eliminar un cliente: 1º Facturas asociadas 2º
		 * VehiculosSustitucion asociados 3º Documentos asociados 4º Vehiculos
		 * asociados 5º Particular / Empresa asociada 6º Cliente 7º Direccion
		 * asociada
		 */
		String sql = "";
		boolean res = true;
		PreparedStatement pt;
		int id = cped.getCliente().getIdcliente();
		int iddireccion = cped.getCliente().getDireccionID();
		try {
			// 1º Facturas
			Statement st = getCon().createStatement();
			sql = "SELECT IDFACTURA FROM FACTURA WHERE CLIENTEID = " + id;

			ResultSet rs = st.executeQuery(sql);

			if (rs.isBeforeFirst()) { // Si es false es que no hay filas
				while (rs.next()) {
					eliminarServiciosPorFacturaID(rs.getInt("IDFACTURA"));
					eliminarMaterialesPorFacturaID(rs.getInt("IDFACTURA"));
				}
				sql = "DELETE FROM FACTURA WHERE CLIENTEID = " + id;
				pt = getCon().prepareStatement(sql);
				pt.executeUpdate();

				// Actualizar próximo número de presupuesto y factura
				actualizarNumPresuFactura();
				// Actualizar los valores de próximos presupuestos/facturas
				Inicio.CONEXION.getPrecioHoraIva();
			}

			// 2º VehiculosSustitucion
			sql = "SELECT IDVEHICULOSUSTI FROM VEHICULOSUSTITUCION WHERE CLIENTEID = " + id;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				eliminarVehiculoSustiPorClienteID(id);
			}

			// 3º Documentos
			sql = "SELECT IDDOCUMENTO FROM DOCUMENTO WHERE CLIENTEID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarDocumentosPorClienteID(id);
			}

			// 4º Vehiculos
			sql = "SELECT IDVEHICULO FROM VEHICULO WHERE CLIENTEID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarVehiculo(rs.getInt("IDVEHICULO"));
			}

			// 5º Particular / Empresa
			if (cped.getParticular() != null) {
				sql = "SELECT IDPARTICULAR FROM PARTICULAR WHERE CLIENTEID = " + id;
				rs = st.executeQuery(sql);
				while (rs.next()) {
					eliminarParticularPorClienteID(id);
				}
			} else if (cped.getEmpresa() != null) {
				sql = "SELECT IDEMPRESA FROM EMPRESA WHERE CLIENTEID = " + id;
				rs.close();
				rs = st.executeQuery(sql);
				while (rs.next()) {
					eliminarEmpresaPorClienteID(id);
				}
			}

			// 6º Cliente
			sql = "DELETE FROM CLIENTE WHERE IDCLIENTE = " + id;
			pt = getCon().prepareStatement(sql);
			pt.executeUpdate();

			// 7º Direccion
			if (iddireccion > 0) {
				eliminarDireccionPorID(iddireccion);
			}

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina de la BD la factura cuyo ID se pasa como parámetro
	 * 
	 * @param idFactura
	 * @return
	 */
	public boolean eliminarFacturaPorID(int idFactura) {
		String sql = "";
		boolean res = true;
		PreparedStatement pt;
		try {
			// 1º Eliminar servicios y materiales y después la factura
			eliminarServiciosPorFacturaID(idFactura);
			eliminarMaterialesPorFacturaID(idFactura);

			sql = "DELETE FROM FACTURA WHERE IDFACTURA = " + idFactura;
			pt = getCon().prepareStatement(sql);
			pt.executeUpdate();

			// Actualizar próximo número de presupuesto y factura
			actualizarNumPresuFactura();
			// Actualizar los valores de próximos presupuestos/facturas
			Inicio.CONEXION.getPrecioHoraIva();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina los servicios asociados a la factura con id facturaID
	 * 
	 * @param facturaID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarServiciosPorFacturaID(int facturaID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM SERVICIO WHERE FACTURAID = " + facturaID + "";
			PreparedStatement st = getCon().prepareStatement(sql);

			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina los materiales asociados a la factura con id facturaID
	 * 
	 * @param facturaID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarMaterialesPorFacturaID(int facturaID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM MATERIAL WHERE FACTURAID = " + facturaID + "";
			PreparedStatement st = getCon().prepareStatement(sql);

			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina los documentos asociados al vehiculo con id vehiculoID
	 * 
	 * @param vehiculoID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarDocumentosPorVehiculoID(int vehiculoID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM DOCUMENTO WHERE VEHICULOID = " + vehiculoID + "";
			PreparedStatement st = getCon().prepareStatement(sql);

			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina los documentos asociados al cliente con id clienteID
	 * 
	 * @param clienteID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarDocumentosPorClienteID(int clienteID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM DOCUMENTO WHERE CLIENTEID = " + clienteID + "";
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();
			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina el Particular asociado al cliente con id clienteID
	 * 
	 * @param clienteID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarParticularPorClienteID(int clienteID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM PARTICULAR WHERE CLIENTEID = " + clienteID + "";
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();
			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina la Empresa asociada al cliente con id clienteID
	 * 
	 * @param clienteID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarEmpresaPorClienteID(int clienteID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM EMPRESA WHERE CLIENTEID = " + clienteID + "";
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();
			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina la dirección con id direccionID
	 * 
	 * @param direccionID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarDireccionPorID(int direccionID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM DIRECCION WHERE IDDIRECCION = " + direccionID + "";
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();
			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina los vehiculos de sustitucion asociados al vehiculo con id
	 * vehiculoID
	 * 
	 * @param vehiculoID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarVehiculoSustiPorVehiculoID(int vehiculoID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM VEHICULOSUSTITUCION WHERE VEHICULOID = " + vehiculoID + "";
			PreparedStatement st = getCon().prepareStatement(sql);

			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Elimina los vehiculos de sustitucion asociados al cliente con id
	 * clienteID
	 * 
	 * @param clienteID
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarVehiculoSustiPorClienteID(int clienteID) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM VEHICULOSUSTITUCION WHERE CLIENTEID = " + clienteID + "";
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();
			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Busca un cliente en la BD por su ID
	 * 
	 * @param id
	 * 
	 * @return el cliente encontrado o null si no existe ese ID
	 */
	public Cliente leerClientePorID(int id) {
		String sql = "";
		Cliente c = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM CLIENTE WHERE CLIENTE.IDCLIENTE = " + id;
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"), rs.getString("TIPO"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return c;
	}

	/**
	 * Busca un vehiculo en la BD por su ID
	 * 
	 * @param id
	 *            a buscar en la BD
	 * 
	 * @return el vehiculo encontrado o null si no existe ese ID
	 */
	public Vehiculo leerVehiculoPorID(int id) {
		String sql = "";
		Vehiculo v = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULO WHERE VEHICULO.IDVEHICULO = " + id;
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return v;
	}

	/**
	 * Busca un cliente en la BD por su DNI
	 * 
	 * @param dni
	 *            a buscar en la BD tipo de documento a buscar: 1-Dni, 2-Cif
	 * 
	 * @return el cliente encontrado o null si no existe ese DNI
	 */
	public Cliente buscarClientePorDni(String dni, int tipo) {
		String sql = "";
		Cliente c = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			if (tipo == 1) {
				sql = "SELECT * FROM PARTICULAR INNER JOIN CLIENTE ON PARTICULAR.CLIENTEID = CLIENTE.IDCLIENTE WHERE "
						+ "NIF = '" + dni + "'";
			} else if (tipo == 2) {
				sql = "SELECT * FROM EMPRESA INNER JOIN CLIENTE ON EMPRESA.CLIENTEID = CLIENTE.IDCLIENTE WHERE "
						+ "CIF LIKE '%" + dni + "%'";
			}
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"), rs.getString("TIPO"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return c;
	}

	/**
	 * Busca un vehiculo en la BD por su matricula
	 * 
	 * @param matricula
	 *            a buscar en la BD
	 * @return el vehiculo encontrado o null si no existe
	 */
	public Vehiculo buscarVehiculoPorMatricula(String matricula) {
		String sql = "";
		Vehiculo v = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULO  WHERE UPPER(MATRICULA) = UPPER('" + matricula + "')";// WHERE
																								// UPPER(COLUMNNAME)
																								// like
																								// UPPER('%valuetocompare%')

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MATRICULA"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return v;
	}

	/**
	 * Busca un Particular en la BD con el clienteID que se pasa como parámetro
	 * 
	 * @param clienteID
	 * @return el Particular encontrado o null si no existe
	 */
	public Particular buscarParticularPorClienteID(int clienteID) {
		String sql = "";
		Particular p = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM PARTICULAR WHERE CLIENTEID = " + clienteID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				p = new Particular(rs.getInt("IDPARTICULAR"), rs.getInt("CLIENTEID"), rs.getString("NOMBRE"),
						rs.getString("APELLIDOS"), rs.getString("NIF"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return p;
	}

	/**
	 * Busca una Empresa en la BD con el clienteID que se pasa como parámetro
	 * 
	 * @param clienteID
	 * @return la Empresa encontrada o null si no existe
	 */
	public Empresa buscarEmpresaPorClienteID(int clienteID) {
		String sql = "";
		Empresa e = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM EMPRESA WHERE CLIENTEID = " + clienteID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				e = new Empresa(rs.getInt("IDEMPRESA"), rs.getInt("CLIENTEID"), rs.getString("NOMBRE"),
						rs.getString("CIF"), rs.getBoolean("ESPROVEEDOR"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return e;
	}

	/**
	 * Busca una Direccion en la BD por su ID
	 * 
	 * @param ID
	 *            a buscar
	 * @return Direccion encontrada o null si no existe
	 */
	public Direccion leerDireccionPorID(int iddireccion) {
		String sql = "";
		Direccion d = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM DIRECCION WHERE IDDIRECCION = " + iddireccion + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				d = new Direccion(rs.getInt("IDDIRECCION"), rs.getString("CALLE"), rs.getInt("NUMERO"),
						rs.getString("PISO"), rs.getString("LETRA"), rs.getInt("CPOSTAL"), rs.getString("LOCALIDAD"),
						rs.getString("PROVINCIA"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return d;
	}

	public boolean guardarLogo() {
		String sql = "";
		FileInputStream fis = null;
		boolean res = true;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			sql = "INSERT INTO DOCUMENTO (CLIENTEID, VEHICULOID, TITULO, DOCUMENTO) VALUES (?, ?, ?, ?)";
			PreparedStatement stmt = getCon().prepareStatement(sql);
			stmt.setInt(1, 2); // Cliente JOSEBA RUIZ ARANA
			stmt.setInt(2, 6); // Vehiculo RENAULT MEGANE 1947FFY
			stmt.setString(3, "Prueba");

			File image = new File("C:/Users/Joseba/git/AruGest/AruGest/images/docus.png");
			fis = new FileInputStream(image);
			stmt.setBinaryStream(4, fis/* , (int) image.length() */);
			// stmt.setBlob(4, fis);

			// File theFile = new File("sample_resume.pdf");
			// FileInputStream input = null;
			// input = new FileInputStream(theFile);
			// myStmt.setBinaryStream(1, input);

			stmt.execute();
			// Se cierra la conexion
			getCon().close();
			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			res = false;
		}
		return res;
	}

	/**
	 * Carga y devuelve el logo de la cía / proveedor cuyo id se le pasa como
	 * parámetro
	 * 
	 * @param id
	 *            de la cía o proveedor
	 * @return logo o null
	 */
	public Image cargarLogo(int id) {
		String sql = "";
		InputStream is = null;
		Image i = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM PROVEEDORCOMPANIA WHERE PROVEEDORCOMPANIA.IDPROVECOMPA = " + id;
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				is = rs.getBinaryStream("LOGO");
				if (is != null) {
					i = new Image(is);
				}
			}
			// Se cierra la conexion
			getCon().close();
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}

	/**
	 * Busca en la BD los servicios de una factura
	 * 
	 * @param ID
	 *            de la factura
	 * @return ObservableList<Servicio> con los servicios
	 */
	public ObservableList<Servicio> buscarServiciosPorFacturaID(int facturaID) {
		String sql = "";
		Servicio s = null;
		ObservableList<Servicio> l = FXCollections.observableArrayList();
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM SERVICIO WHERE FACTURAID = " + facturaID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				s = new Servicio(rs.getInt("IDSERVICIO"), rs.getString("SERVICIO"),
						String.valueOf(rs.getFloat("HORAS")), rs.getInt("FACTURAID"), rs.getString("TIPOSERVICIO"));
				l.add(s);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

	/**
	 * Busca en la BD los materiales de una factura
	 * 
	 * @param ID
	 *            de la factura
	 * @return ObservableList<Material> con los materiales
	 */
	public ObservableList<Material> buscarMaterialesPorFacturaID(int facturaID) {
		String sql = "";
		Material m = null;
		ObservableList<Material> l = FXCollections.observableArrayList();
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM MATERIAL WHERE FACTURAID = " + facturaID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				m = new Material(rs.getInt("IDMATERIAL"), rs.getString("NOMBRE"),
						String.valueOf(rs.getFloat("PRECIOUNIT")), rs.getInt("FACTURAID"), rs.getInt("CANTIDAD"),
						rs.getFloat("PRECIOTOTAL"));
				l.add(m);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

	/**
	 * Busca las factura que coincidan con los parámetros introducidos
	 * 
	 * @param numFactura
	 * @param numPresu
	 * @param numOrden
	 * @param numResgu
	 * @param nombre
	 * @param modelo
	 * @param matricula
	 * @param fijo
	 * @param movil
	 * @param dni
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return ArrayList con las facturas encontradas
	 */
	public ArrayList<FacturaClienteVehiculo> buscarFacturas(boolean esFactura, boolean esPresu, boolean esOrden,
			boolean esResgu, String numFactura, String numPresu, String numOrden, String numResgu, String nombre,
			String modelo, String matricula, String fijo, String movil, LocalDate fechaDesde, LocalDate fechaHasta,
			String estado) {

		String sql = "";
		Factura f;
		Cliente c;
		Vehiculo v;
		FacturaClienteVehiculo fcv;
		ArrayList<FacturaClienteVehiculo> listaFacturas = new ArrayList<FacturaClienteVehiculo>();
		boolean esPrimero = true;
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM FACTURA INNER JOIN CLIENTE ON FACTURA.CLIENTEID = CLIENTE.IDCLIENTE INNER JOIN VEHICULO ON FACTURA.VEHICULOID = VEHICULO.IDVEHICULO ";
			if (esFactura) {
				if (esPrimero) {
					sql += "WHERE (FACTURA.NUMFACTURA > 0 ";
					esPrimero = false;
				} else {
					sql += "OR FACTURA.NUMFACTURA > 0 ";
				}
			}
			if (esPresu) {
				if (esPrimero) {
					sql += "WHERE (FACTURA.NUMPRESUPUESTO > 0 ";
					esPrimero = false;
				} else {
					sql += "OR FACTURA.NUMPRESUPUESTO > 0 ";
				}

			}
			if (esOrden) {
				if (esPrimero) {
					sql += "WHERE (FACTURA.NUMORDENREP > 0 ";
					esPrimero = false;
				} else {
					sql += "OR FACTURA.NUMORDENREP > 0 ";
				}

			}
			if (esResgu) {
				if (esPrimero) {
					sql += "WHERE (FACTURA.NUMRESGUARDO > 0 ";
					esPrimero = false;
				} else {
					sql += "OR FACTURA.NUMRESGUARDO > 0 ";
				}

			}
			if (!esPrimero) {
				sql += ") ";
			}
			if (!numFactura.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMFACTURA = '" + Utilidades.formateaNumFactura(numFactura) + "' ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMFACTURA = '" + Utilidades.formateaNumFactura(numFactura) + "' ";
				}
			}
			if (!numPresu.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMPRESUPUESTO = '" + Utilidades.formateaNumFactura(numPresu) + "' ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMPRESUPUESTO = '" + Utilidades.formateaNumFactura(numPresu) + "' ";
				}
			}
			if (!numOrden.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMORDENREP = '" + Utilidades.formateaNumFactura(numOrden) + "' ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMORDENREP = '" + Utilidades.formateaNumFactura(numOrden) + "' ";
				}
			}
			if (!numResgu.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (FACTURA.NUMRESGUARDO = '" + Utilidades.formateaNumFactura(numResgu) + "') ";
					esPrimero = false;
				} else {
					sql += "AND (FACTURA.NUMRESGUARDO = '" + Utilidades.formateaNumFactura(numResgu) + "') ";
				}
			}
			if (!nombre.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%')) ";
					esPrimero = false;
				} else {
					sql += "AND (UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%')) ";
				}
			}
			if (!modelo.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (UPPER(VEHICULO.MODELO) LIKE UPPER('%" + modelo + "%')) ";
					esPrimero = false;
				} else {
					sql += "AND (UPPER(VEHICULO.MODELO) LIKE UPPER('%" + modelo + "%')) ";
				}
			}
			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%')) ";
					esPrimero = false;
				} else {
					sql += "AND (UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%')) ";
				}
			}
			if (!fijo.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (CLIENTE.TELF1 = '" + fijo + "') ";
					esPrimero = false;
				} else {
					sql += "AND (CLIENTE.TELF1 = '" + fijo + "') ";
				}
			}
			if (!movil.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (CLIENTE.TELF2 = '" + movil + "') ";
					esPrimero = false;
				} else {
					sql += "AND (CLIENTE.TELF2 = '" + movil + "') ";
				}
			}
			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE (UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%')) ";
					esPrimero = false;
				} else {
					sql += "AND (UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%')) ";
				}
			}

			// Para comparar fechas, hay que ponerlas en el formato aaaa-mm-dd
			sql += "AND (FACTURA.FECHA BETWEEN '" + fechaDesde + "' AND '" + fechaHasta + "') ";

			if (!estado.equalsIgnoreCase("T")) {
				if (esPrimero) {
					if (estado.equalsIgnoreCase("C")) {
						sql += "WHERE (FACTURA.COBRADA = 'TRUE') ";
					} else {
						sql += "WHERE (FACTURA.COBRADA = 'FALSE') ";
					}
					esPrimero = false;
				} else {
					if (estado.equalsIgnoreCase("C")) {
						sql += "AND (FACTURA.COBRADA = 'TRUE') ";
					} else {
						sql += "AND (FACTURA.COBRADA = 'FALSE') ";
					}
				}
			}

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				f = new Factura(rs.getInt("IDFACTURA"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getInt("KMS"), rs.getString("NUMFACTURA"), rs.getString("NUMPRESUPUESTO"),
						rs.getString("NUMORDENREP"), rs.getString("NUMRESGUARDO"), rs.getDate("FECHA"),
						rs.getDate("FECHAENTREGA"), rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"),
						rs.getFloat("GRUA"), rs.getFloat("SUMA"), rs.getFloat("SUMAIVA"), rs.getBoolean("RDEFOCULTOS"),
						rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"), rs.getBoolean("NOPIEZAS"),
						rs.getBoolean("COBRADA"), rs.getFloat("IMPORTETOTAL"));
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"), rs.getString("TIPO"));
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"),
						rs.getInt("TIPOID"));
				fcv = new FacturaClienteVehiculo(f, c, v);
				listaFacturas.add(fcv);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return listaFacturas;
	}

	/**
	 * Busca los clientes que coincidan con los parámetros introducidos
	 * 
	 * @param tipo
	 * @param nombre
	 * @param modelo
	 * @param matricula
	 * @param dni
	 * @param fijo
	 * @param movil
	 * @param domicilio
	 * @param poblacion
	 * @return ArrayList con los clientes encontrados
	 */
	public ArrayList<ClienteParticularEmpresaDireccion> buscarClientes(int tipo, String nombre, String modelo,
			String matricula, String dni, String fijo, String movil, String domicilio, String poblacion) {

		String sql = "";
		Cliente c;
		Particular p = null;
		Empresa e = null;
		Direccion d = null;
		ClienteParticularEmpresaDireccion cped;
		ArrayList<ClienteParticularEmpresaDireccion> listaClientes = new ArrayList<ClienteParticularEmpresaDireccion>();
		boolean esPrimero = true;
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			if (tipo == 1) {
				sql = "SELECT DISTINCT * FROM CLIENTE INNER JOIN PARTICULAR ON CLIENTE.IDCLIENTE = PARTICULAR.CLIENTEID LEFT JOIN DIRECCION ON CLIENTE.DIRECCIONID = DIRECCION.IDDIRECCION ";
				if (!dni.equalsIgnoreCase("")) {
					if (esPrimero) {
						sql += "WHERE UPPER(PARTICULAR.NIF) LIKE UPPER('%" + dni + "%') ";
						esPrimero = false;
					} else {
						sql += "AND UPPER(PARTICULAR.NIF) LIKE UPPER('%" + dni + "%') ";
					}
				}

			} else if (tipo == 2) {
				sql = "SELECT DISTINCT * FROM CLIENTE INNER JOIN EMPRESA ON CLIENTE.IDCLIENTE = EMPRESA.CLIENTEID LEFT JOIN DIRECCION ON CLIENTE.DIRECCIONID = DIRECCION.IDDIRECCION ";
				if (!dni.equalsIgnoreCase("")) {
					if (esPrimero) {
						sql += "WHERE UPPER(EMPRESA.CIF) LIKE UPPER('%" + dni + "%') ";
						esPrimero = false;
					} else {
						sql += "AND UPPER(EMPRESA.CIF) LIKE UPPER('%" + dni + "%') ";
					}
				}
			}

			if (!nombre.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
				}
			}

			// ESTAS CAMBIAN UN POCO
			if (!modelo.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE CLIENTE.IDCLIENTE IN (SELECT CLIENTEID FROM VEHICULO WHERE UPPER(VEHICULO.MODELO) LIKE UPPER('%"
							+ modelo + "%'))";
					// sql += "WHERE VEHICULO.MODELO LIKE '%" + modelo + "%' ";
					esPrimero = false;
				} else {
					sql += "AND CLIENTE.IDCLIENTE IN (SELECT CLIENTEID FROM VEHICULO WHERE UPPER(VEHICULO.MODELO) LIKE UPPER('%"
							+ modelo + "%'))";
				}
			}
			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE CLIENTE.IDCLIENTE IN (SELECT CLIENTEID FROM VEHICULO WHERE UPPER(VEHICULO.MATRICULA) LIKE UPPER('%"
							+ matricula + "%'))";
					// sql += "WHERE VEHICULO.MATRICULA LIKE '%" + matricula +
					// "%' ";
					esPrimero = false;
				} else {
					sql += "AND CLIENTE.IDCLIENTE IN (SELECT CLIENTEID FROM VEHICULO WHERE UPPER(VEHICULO.MATRICULA) LIKE UPPER('%"
							+ matricula + "%'))";
				}
			}

			if (!fijo.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE CLIENTE.TELF1 = '" + fijo + "' ";
					esPrimero = false;
				} else {
					sql += "AND CLIENTE.TELF1 = '" + fijo + "' ";
				}
			}
			if (!movil.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE CLIENTE.TELF2 = '" + movil + "' ";
					esPrimero = false;
				} else {
					sql += "AND CLIENTE.TELF2 = '" + movil + "' ";
				}
			}

			if (!domicilio.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(DIRECCION.CALLE) LIKE UPPER('%" + domicilio + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(DIRECCION.CALLE) LIKE UPPER('%" + domicilio + "%') ";
				}
			}
			if (!poblacion.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(DIRECCION.LOCALIDAD) LIKE UPPER('%" + poblacion + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(DIRECCION.LOCALIDAD) LIKE UPPER('%" + poblacion + "%') ";
				}
			}

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"), rs.getString("TIPO"));
				d = new Direccion(rs.getInt("IDDIRECCION"), rs.getString("CALLE"), rs.getInt("NUMERO"),
						rs.getString("PISO"), rs.getString("LETRA"), rs.getInt("CPOSTAL"), rs.getString("LOCALIDAD"),
						rs.getString("PROVINCIA"));
				if (tipo == 1) {
					p = new Particular(rs.getInt("IDPARTICULAR"), rs.getInt("IDCLIENTE"),
							rs.getString("PARTICULAR.NOMBRE"), rs.getString("PARTICULAR.APELLIDOS"),
							rs.getString("NIF"));
				} else if (tipo == 2) {
					e = new Empresa(rs.getInt("IDEMPRESA"), rs.getInt("IDCLIENTE"), rs.getString("EMPRESA.NOMBRE"),
							rs.getString("CIF"), rs.getBoolean("ESPROVEEDOR"));
				}
				cped = new ClienteParticularEmpresaDireccion(c, p, e, d);
				listaClientes.add(cped);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listaClientes;
	}

	/**
	 * Busca en la BD las compañías que coincidan con los parámetros
	 * 
	 * @param nombre
	 * @param telf
	 * @return ArrayList con los resultados obtenidos
	 */
	public ArrayList<ProveedorCompaniaDireccion> buscarCias(boolean esCia, boolean esDesguace, String nombre,
			String telf, String poblacion, String provincia) {
		String sql = "";
		ProveedorCompaniaDireccion pcd = new ProveedorCompaniaDireccion(esCia);
		ProveedorCompania pc;
		Direccion d;
		ArrayList<ProveedorCompaniaDireccion> lista = new ArrayList<ProveedorCompaniaDireccion>();
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM PROVEEDORCOMPANIA INNER JOIN DIRECCION ON PROVEEDORCOMPANIA.DIRECCIONID = DIRECCION.IDDIRECCION";
			if (esCia) {
				sql += " WHERE PROVEEDORCOMPANIA.ESCOMPANIA = TRUE ";
			} else if (esDesguace) {
				sql += " WHERE PROVEEDORCOMPANIA.ESDESGUACE = TRUE ";
			} else {
				sql += " WHERE PROVEEDORCOMPANIA.ESCOMPANIA = FALSE AND PROVEEDORCOMPANIA.ESDESGUACE = FALSE ";
			}
			if (!nombre.equalsIgnoreCase("")) {
				sql += " AND UPPER(PROVEEDORCOMPANIA.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
			}

			if (!telf.equalsIgnoreCase("")) {
				sql += " AND UPPER(PROVEEDORCOMPANIA.TELF1) LIKE UPPER('%" + telf + "%') ";
			}

			if (!poblacion.isEmpty()) {
				sql += " AND UPPER(DIRECCION.LOCALIDAD) LIKE UPPER('%" + poblacion + "%') ";
			}

			if (!provincia.isEmpty()) {
				sql += " AND UPPER(DIRECCION.PROVINCIA) LIKE UPPER('%" + provincia + "%') ";
			}

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				// InputStream is = rs.getBinaryStream("LOGO");
				// Blob b = getCon().createBlob();
				// b.setBytes(1, new byte[is.read()]);
				pc = new ProveedorCompania(rs.getInt("IDPROVECOMPA"), rs.getString("CIF"), rs.getString("NOMBRE"),
						rs.getInt("DIRECCIONID"), rs.getString("TELF1"), rs.getString("TELF2"), rs.getBlob("LOGO"),
						rs.getBoolean("ESDESGUACE"), rs.getBoolean("ESCOMPANIA"));
				d = leerDireccionPorID(rs.getInt("DIRECCIONID"));
				pcd = new ProveedorCompaniaDireccion(pc, d);
				lista.add(pcd);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	/**
	 * Busca un vehículo en la BD que coincida con los parámetros pasados
	 * 
	 * @param tipo
	 * @param matricula
	 * @param marca
	 * @param modelo
	 * @param nombre
	 * @return ArrayList con los resultados obtenidos
	 */
	public ArrayList<Vehiculo> buscarVehiculos(int tipo, String matricula, String marca, String modelo, String nombre) {
		String sql = "";
		Vehiculo v;
		ArrayList<Vehiculo> listaVehiculos = new ArrayList<Vehiculo>();
		boolean esPrimero = true;
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULO INNER JOIN CLIENTE ON VEHICULO.CLIENTEID = CLIENTE.IDCLIENTE ";
			if (esPrimero) {
				sql += "WHERE VEHICULO.TIPOID = " + tipo;
				esPrimero = false;
			} else {
				sql += "AND VEHICULO.TIPOID = " + tipo;
			}

			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%') ";
				}
			}

			if (!marca.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(VEHICULO.MARCA) LIKE UPPER('%" + marca + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(VEHICULO.MARCA) LIKE UPPER('%" + marca + "%') ";
				}
			}

			if (!modelo.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(VEHICULO.MODELO) LIKE UPPER('%" + modelo + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(VEHICULO.MODELO) LIKE UPPER('%" + modelo + "%') ";
				}
			}

			if (!nombre.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
				}
			}

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
				listaVehiculos.add(v);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listaVehiculos;
	}

	/**
	 * Busca en la BD los vehículos de un cliente
	 * 
	 * @param ID
	 *            del cliente
	 * @return ObservableList<Vehiculo> con los vehículos
	 */
	public ObservableList<Vehiculo> buscarVehiculosPorClienteID(int clienteID) {
		String sql = "";
		Vehiculo v = null;
		ObservableList<Vehiculo> l = FXCollections.observableArrayList();
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULO WHERE CLIENTEID = " + clienteID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
				l.add(v);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

	/**
	 * Busca en la BD los presupuestos de un cliente dado
	 * 
	 * @param clienteID
	 * @return
	 */
	public ObservableList<FacturaClienteVehiculo> buscarPresupuestosPorClienteID(int clienteID) {
		String sql = "";
		Factura f = null;
		Cliente c = null;
		Vehiculo v = null;
		FacturaClienteVehiculo fcv;
		ObservableList<FacturaClienteVehiculo> l = FXCollections.observableArrayList();
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM FACTURA INNER JOIN VEHICULO ON FACTURA.VEHICULOID = VEHICULO.IDVEHICULO WHERE FACTURA.NUMPRESUPUESTO > 0 AND FACTURA.CLIENTEID = "
					+ clienteID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				f = new Factura(rs.getInt("IDFACTURA"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getInt("KMS"), rs.getString("NUMFACTURA"), rs.getString("NUMPRESUPUESTO"),
						rs.getString("NUMORDENREP"), rs.getString("NUMRESGUARDO"), rs.getDate("FECHA"),
						rs.getDate("FECHAENTREGA"), rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"),
						rs.getFloat("GRUA"), rs.getFloat("SUMA"), rs.getFloat("SUMAIVA"), rs.getBoolean("RDEFOCULTOS"),
						rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"), rs.getBoolean("NOPIEZAS"),
						rs.getBoolean("COBRADA"), rs.getFloat("IMPORTETOTAL"));
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
				c = leerClientePorID(clienteID);
				fcv = new FacturaClienteVehiculo(f, c, v);
				l.add(fcv);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

	/**
	 * Busca en la BD las facturas de un cliente dado
	 * 
	 * @param clienteID
	 * @return
	 */
	public ObservableList<FacturaClienteVehiculo> buscarFacturasPorClienteID(int clienteID) {
		String sql = "";
		Factura f = null;
		Cliente c = null;
		Vehiculo v = null;
		FacturaClienteVehiculo fcv;
		ObservableList<FacturaClienteVehiculo> l = FXCollections.observableArrayList();
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM FACTURA INNER JOIN VEHICULO ON FACTURA.VEHICULOID = VEHICULO.IDVEHICULO WHERE FACTURA.NUMFACTURA > 0 AND FACTURA.CLIENTEID = "
					+ clienteID + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				f = new Factura(rs.getInt("IDFACTURA"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getInt("KMS"), rs.getString("NUMFACTURA"), rs.getString("NUMPRESUPUESTO"),
						rs.getString("NUMORDENREP"), rs.getString("NUMRESGUARDO"), rs.getDate("FECHA"),
						rs.getDate("FECHAENTREGA"), rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"),
						rs.getFloat("GRUA"), rs.getFloat("SUMA"), rs.getFloat("SUMAIVA"), rs.getBoolean("RDEFOCULTOS"),
						rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"), rs.getBoolean("NOPIEZAS"),
						rs.getBoolean("COBRADA"), rs.getFloat("IMPORTETOTAL"));
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
				c = leerClientePorID(clienteID);
				fcv = new FacturaClienteVehiculo(f, c, v);
				l.add(fcv);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

	/**
	 * Busca las últimas facturas añadidas a la BD
	 * 
	 * @return
	 */
	public ObservableList<FacturaClienteVehiculo> buscarUltimasFacturas() {
		String sql = "";
		Factura f = null;
		Cliente c = null;
		Vehiculo v = null;
		FacturaClienteVehiculo fcv;
		ObservableList<FacturaClienteVehiculo> l = FXCollections.observableArrayList();
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT TOP 20 * FROM FACTURA WHERE NUMFACTURA>0 AND NUMFACTURA<>'' ORDER BY NUMFACTURA DESC";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				f = new Factura(rs.getInt("IDFACTURA"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getInt("KMS"), rs.getString("NUMFACTURA"), rs.getString("NUMPRESUPUESTO"),
						rs.getString("NUMORDENREP"), rs.getString("NUMRESGUARDO"), rs.getDate("FECHA"),
						rs.getDate("FECHAENTREGA"), rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"),
						rs.getFloat("GRUA"), rs.getFloat("SUMA"), rs.getFloat("SUMAIVA"), rs.getBoolean("RDEFOCULTOS"),
						rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"), rs.getBoolean("NOPIEZAS"),
						rs.getBoolean("COBRADA"), rs.getFloat("IMPORTETOTAL"));
				v = leerVehiculoPorID(rs.getInt("VEHICULOID"));
				c = leerClientePorID(rs.getInt("CLIENTEID"));
				fcv = new FacturaClienteVehiculo(f, c, v);
				l.add(fcv);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return l;
	}

	/**
	 * Busca en la BD los vehiculos de sustitucion que estén actualmente
	 * disponibles
	 * 
	 * @return
	 */
	public ArrayList<Vehiculo> buscarDisponibles() {
		ArrayList<Vehiculo> listaDisponibles = new ArrayList<Vehiculo>();
		Vehiculo v;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULO WHERE ESVEHICULOSUSTITUCION = TRUE AND IDVEHICULO NOT IN (SELECT VEHICULOID FROM VEHICULOSUSTITUCION WHERE FECHADEVUELVE IS NULL)";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
				listaDisponibles.add(v);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listaDisponibles;
	}

	/**
	 * Busca en la BD los vehiculos de sustitucion que estén actualmente
	 * prestados
	 * 
	 * @return
	 */
	public ArrayList<VehiculoSustitucion> buscarPrestados() {
		ArrayList<VehiculoSustitucion> listaPrestados = new ArrayList<VehiculoSustitucion>();
		VehiculoSustitucion vs;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULOSUSTITUCION WHERE FECHADEVUELVE IS NULL";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				vs = new VehiculoSustitucion(rs.getInt("IDVEHICULOSUSTI"), rs.getDate("FECHACOGE"), null,
						rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"), rs.getString("OBSERVACIONES"));
				listaPrestados.add(vs);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listaPrestados;
	}

	/**
	 * Busca en la BD el histórico de los vehiculos de sustitucion
	 * 
	 * @return
	 */
	public ArrayList<VehiculoSustitucion> buscarHistorico() {
		ArrayList<VehiculoSustitucion> lista = new ArrayList<VehiculoSustitucion>();
		VehiculoSustitucion vs;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULOSUSTITUCION WHERE FECHADEVUELVE IS NOT NULL";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				vs = new VehiculoSustitucion(rs.getInt("IDVEHICULOSUSTI"), rs.getDate("FECHACOGE"),
						rs.getDate("FECHADEVUELVE"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getString("OBSERVACIONES"));
				lista.add(vs);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	/**
	 * Actualiza / crea el registro en la BD del vehiculo de sustitucion. Pone
	 * la fecha de devolución ó crea un nuevo registro
	 * 
	 * @param tipo:
	 *            D = Devuelto ; E = Entregado
	 * @param vscv
	 *            datos del vehículo de sustitución
	 * @return true si fue bien, false si no
	 */
	public boolean actualizarVehiculoSustitucion(String tipo, VehiculoSustitucionClienteVehiculo vscv) {
		String sql = "";
		PreparedStatement st;
		boolean res = true;
		try {
			if (tipo.equalsIgnoreCase("D")) {
				sql = "UPDATE VEHICULOSUSTITUCION SET FECHADEVUELVE = ?, OBSERVACIONES = ? WHERE IDVEHICULOSUSTI = "
						+ vscv.getIdvehiculosusti();
				st = getCon().prepareStatement(sql);
				// Añadimos los parametros
				st.setDate(1, new java.sql.Date(vscv.getFechadevuelve().getTime()));
				st.setString(2, vscv.getObservaciones());
				// Ejecutamos la sentencia
				st.executeUpdate();
			} else if (tipo.equalsIgnoreCase("E")) {
				// Crear el registro del vehiculo de sustitucion en la base de
				// datos
				sql = "INSERT INTO VEHICULOSUSTITUCION (FECHACOGE, FECHADEVUELVE, CLIENTEID, VEHICULOID, OBSERVACIONES) VALUES (?,?,?,?,?)";
				st = getCon().prepareStatement(sql);
				// Añadimos los parametros
				st.setDate(1, new java.sql.Date(vscv.getFechacoge().getTime()));
				st.setDate(2, null);
				st.setInt(3, Inicio.CLIENTE_ID);
				st.setInt(4, vscv.getVehiculo().getIdvehiculo());
				st.setString(5, vscv.getObservaciones());
				// Ejecutamos la sentencia
				st.executeUpdate();
			}
			res = true;
		} catch (Exception e) {
			res = false;
		}
		return res;
	}

	/**
	 * Busca en la BD los vehiculos de sustitucion que hayan estado vinculados
	 * al cliente con idcliente id
	 * 
	 * @return lista
	 */
	public ObservableList<VehiculoSustitucionClienteVehiculo> buscarVehiculosSustitucionPorClienteID(int id) {
		ObservableList<VehiculoSustitucionClienteVehiculo> lista = FXCollections.observableArrayList();
		VehiculoSustitucionClienteVehiculo vscv;
		VehiculoSustitucion vs;
		Vehiculo v;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM VEHICULOSUSTITUCION INNER JOIN VEHICULO ON VEHICULOSUSTITUCION.VEHICULOID = VEHICULO.IDVEHICULO WHERE VEHICULOSUSTITUCION.CLIENTEID = "
					+ id;

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
				vs = new VehiculoSustitucion(rs.getInt("IDVEHICULOSUSTI"), rs.getDate("FECHACOGE"),
						rs.getDate("FECHADEVUELVE"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getString("OBSERVACIONES"));
				vscv = new VehiculoSustitucionClienteVehiculo(vs, null, v);
				lista.add(vscv);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	/**
	 * Busca en la BD los golpes predefinidos que hay almacenados
	 * 
	 * @return lista con los golpes encontrados
	 */
	public ObservableList<Golpe> buscarGolpes() {
		ObservableList<Golpe> lista = FXCollections.observableArrayList();
		Golpe g;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM GOLPES";

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				g = new Golpe(rs.getInt("IDGOLPE"), rs.getString("NOMBREGOLPE"));
				lista.add(g);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
			Utilidades.mostrarError(ex);
		}
		return lista;
	}

	/**
	 * Busca en la BD los elementos que contenga el golpe predefinido cuyo id se
	 * pasa como parámetro
	 * 
	 * @param idgolpe
	 * @return ArrayList<ElementosGolpe> con los elementos encontrados
	 */
	public ArrayList<ElementosGolpes> buscarElementosPorGolpeID(int idgolpe) {
		ArrayList<ElementosGolpes> lista = new ArrayList<ElementosGolpes>();
		ElementosGolpes e;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM ELEMENTOSGOLPES WHERE ELEMENTOSGOLPES.GOLPEID = " + idgolpe;

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				e = new ElementosGolpes(rs.getInt("IDELEMENTO"), rs.getString("NOMBREELEMENTO"), rs.getString("TIPO"),
						rs.getInt("GOLPEID"));
				lista.add(e);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
			Utilidades.mostrarError(ex);
		}
		return lista;
	}

	/**
	 * Guarda en la BD el golpe con los elementos que se pasan por parámetro
	 * 
	 * @param g
	 * @return true si fue ok, false si no
	 */
	public boolean guardarGolpe(Golpe g, ObservableList<ElementosGolpes> elementos) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia para introducir los datos del golpe
			sql = "INSERT INTO GOLPES (NOMBREGOLPE) VALUES (?)";
			java.sql.PreparedStatement st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setString(1, g.getNombreGolpe());

			// Ejecutamos la sentencia
			st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				long idGolpe = rs.getLong(1);
				// Guardar elementos
				sql = "INSERT INTO ELEMENTOSGOLPES (NOMBREELEMENTO, TIPO, GOLPEID) VALUES (?,?,?)";
				st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (ElementosGolpes e : elementos) {
					st.setString(1, e.getNombreElemento());
					st.setString(2, e.getTipo());
					st.setInt(3, (int) idGolpe);
					st.executeUpdate();
				}
			}
			res = true;
		} catch (Exception e) {
			res = false;
			Utilidades.mostrarError(e);
		}
		return res;
	}

	/**
	 * Edita en la BD el golpe con los elementos que se pasan por parámetro
	 * 
	 * @param g
	 * @return true si fue ok, false si no
	 */
	public boolean editarGolpe(int idgolpe, Golpe g, ObservableList<ElementosGolpes> elementos) {
		boolean res = true;
		String sql = "";
		try {
			sql = "UPDATE GOLPES SET NOMBREGOLPE = ? WHERE IDGOLPE = " + idgolpe;
			PreparedStatement st = getCon().prepareStatement(sql);
			st.setString(1, g.getNombreGolpe());

			// Ejecutamos la sentencia
			st.executeUpdate();

			// Borrar los elementos de ese golpe y añadir los nuevos que se le
			// pasan
			sql = "DELETE FROM ELEMENTOSGOLPES WHERE ELEMENTOSGOLPES.GOLPEID = " + idgolpe;
			st = getCon().prepareStatement(sql);
			st.executeUpdate();

			// Guardar elementos
			sql = "INSERT INTO ELEMENTOSGOLPES (NOMBREELEMENTO, TIPO, GOLPEID) VALUES (?,?,?)";
			st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (ElementosGolpes e : elementos) {
				st.setString(1, e.getNombreElemento());
				st.setString(2, e.getTipo());
				st.setInt(3, idgolpe);
				st.executeUpdate();
			}

			// Se cierra la conexion
			getCon().close();
			res = true;

		} catch (Exception e) {
			res = false;
			Utilidades.mostrarError(e);
		}
		return res;
	}

	/**
	 * Elimina de la BD el golpe cuyo id se pasa como parámetro
	 * 
	 * @param idgolpe
	 * @return true si fue ok, false si no
	 */
	public boolean eliminarGolpe(int idgolpe) {
		boolean res = true;
		String sql = "";
		try {
			// Eliminar elementos
			sql = "DELETE FROM ELEMENTOSGOLPES WHERE ELEMENTOSGOLPES.GOLPEID = " + idgolpe;
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();

			// Eliminar golpe
			sql = "DELETE FROM GOLPES WHERE GOLPES.IDGOLPE = " + idgolpe;
			st = getCon().prepareStatement(sql);
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			Utilidades.mostrarError(ex);
			res = false;
		}
		return res;
	}

	/**
	 * Elimina de la BD el elemento cuyo id se pasa como parámetro
	 * 
	 * @param nombreelemento
	 * @return true si fue ok, false si no
	 */
	public boolean eliminarElementoGolpe(String nombreelemento) {
		boolean res = true;
		String sql = "";
		try {
			// Eliminar elemento
			sql = "DELETE FROM ELEMENTOSGOLPES WHERE ELEMENTOSGOLPES.NOMBREELEMENTO = '" + nombreelemento + "'";
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			Utilidades.mostrarError(ex);
			res = false;
		}
		return res;
	}

	/**
	 * Guarda en la BD el documento que se pasa por parámetro
	 * 
	 * @param documento
	 *            a guardar
	 * @return id generado o cero
	 */
	public int guardarDocumento(Documento d) {
		int res = 0;
		String sql = "";
		try {
			// Se prepara la sentencia para introducir los datos del golpe
			sql = "INSERT INTO DOCUMENTO (CLIENTEID, TITULO, DOCUMENTO, EXTENSION) VALUES (?,?,?,?)";
			java.sql.PreparedStatement st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setInt(1, d.getClienteID());
			st.setString(2, d.getTitulo());
			st.setBlob(3, (Blob) d.getDocumento());
			st.setString(4, d.getExtension());

			// Ejecutamos la sentencia
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				long idGenerado = rs.getLong(1);
				res = (int) idGenerado;
			}
		} catch (Exception e) {
			res = 0;
			Utilidades.mostrarError(e);
		}
		return res;
	}

	/**
	 * Busca en la BD el documento cuyo id se pasa como parámetro
	 * 
	 * @param id
	 *            del documento a buscar
	 * @return documento temporal encontrado o null
	 */
	public File leerDocumentoPorID(int id) {
		Documento d = null;
		File f = null;
		InputStream is;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM DOCUMENTO WHERE DOCUMENTO.IDDOCUMENTO = " + id;
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				d = new Documento(rs.getInt("IDDOCUMENTO"), rs.getInt("CLIENTEID"), rs.getString("TITULO"),
						rs.getBlob("DOCUMENTO"), rs.getString("EXTENSION"));

				is = rs.getBinaryStream("DOCUMENTO");
				String ext = "." + d.getExtension();
				f = File.createTempFile("Archivo_TEMP", ext);
				f.deleteOnExit();

				OutputStream out = new FileOutputStream(f);
				byte buf[] = new byte[1024];
				int len;
				while ((len = is.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				is.close();

				// Runtime.getRuntime().exec("rundll32url.dll,FileProtocolHandler
				// " + f.getAbsolutePath());
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return f;
	}

	/**
	 * Busca en la BD los documentos vinculados al cliente con idcliente id
	 * 
	 * @return lista de documentos
	 */
	public ObservableList<Documento> buscarDocumentosPorClienteID(int id) {
		ObservableList<Documento> lista = FXCollections.observableArrayList();
		Documento d;
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM DOCUMENTO WHERE DOCUMENTO.CLIENTEID = " + id;

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				d = new Documento(rs.getInt("IDDOCUMENTO"), rs.getInt("CLIENTEID"), rs.getString("TITULO"),
						rs.getBlob("DOCUMENTO"), rs.getString("EXTENSION"));
				lista.add(d);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
		}
		return lista;
	}

	/**
	 * Elimina de la BD el documento cuyo id se pasa como parámetro
	 * 
	 * @param id
	 *            del documento a eliminar
	 * @return true si ha ido bien, false si no
	 */
	public boolean eliminarDocumentoPorID(int id) {
		boolean res = true;
		String sql = "";
		try {
			// Se prepara la sentencia
			sql = "DELETE FROM DOCUMENTO WHERE DOCUMENTO.IDDOCUMENTO = " + id;
			PreparedStatement st = getCon().prepareStatement(sql);
			st.executeUpdate();
			// Se cierra la conexion
			getCon().close();

			res = true;
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
			res = false;
		}
		return res;
	}

	/**
	 * Edita en la BD el título del documento cuyo id se pasa como parámetro
	 * 
	 * @param id
	 *            del documento y nuevo titulo a ponerle
	 * @return true si fue bien, false si no
	 */
	public boolean editarTituloDocumento(int id, String titulo) {
		boolean res = true;
		String sql = "";
		try {
			sql = "UPDATE DOCUMENTO SET TITULO = ? WHERE DOCUMENTO.IDDOCUMENTO = " + id;
			PreparedStatement st = getCon().prepareStatement(sql);
			st.setString(1, titulo);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Se cierra la conexion
			getCon().close();
			res = true;
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
			res = false;
		}
		return res;
	}

	/**
	 * Busca los documentos que coincidan con los parámetros introducidos
	 * 
	 * @param nombre
	 *            documento
	 * @param nombre
	 *            cliente asociado al documento
	 * @return ArrayList con los clientes encontrados
	 */
	public ArrayList<ClienteParticularEmpresaDireccionDocumento> buscarDocumentos(String nombreDocumento,
			String nombreCliente) {

		String sql = "";
		Statement st;
		Cliente c;
		Particular p = null;
		Empresa e = null;
		Direccion d = null;
		Documento docu = null;
		ClienteParticularEmpresaDireccionDocumento cpedd;
		ArrayList<ClienteParticularEmpresaDireccionDocumento> listaDocumentos = new ArrayList<ClienteParticularEmpresaDireccionDocumento>();
		boolean esPrimero = true;
		try {
			// Se prepara la sentencia
			st = getCon().createStatement();
			sql = "SELECT DISTINCT * FROM DOCUMENTO INNER JOIN CLIENTE ON DOCUMENTO.CLIENTEID = CLIENTE.IDCLIENTE ";
			if (!nombreDocumento.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(DOCUMENTO.TITULO) LIKE UPPER('%" + nombreDocumento + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(DOCUMENTO.TITULO) LIKE UPPER('%" + nombreDocumento + "%') ";
				}
			}

			if (!nombreCliente.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombreCliente + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombreCliente + "%') ";
				}
			}

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				int idcliente = rs.getInt("IDCLIENTE");
				sql = "SELECT * FROM PARTICULAR WHERE PARTICULAR.CLIENTEID = " + idcliente;
				Statement st2 = getCon().createStatement();
				;
				ResultSet rs2 = st2.executeQuery(sql);
				if (rs2.isBeforeFirst()) {
					while (rs2.next()) {
						p = new Particular(rs2.getInt("IDPARTICULAR"), rs2.getInt("PARTICULAR.CLIENTEID"),
								rs2.getString("PARTICULAR.NOMBRE"), rs2.getString("PARTICULAR.APELLIDOS"),
								rs2.getString("PARTICULAR.NIF"));
					}
				} else {
					sql = "SELECT * FROM PARTICULAR WHERE PARTICULAR.CLIENTEID = " + idcliente;
					rs2 = st2.executeQuery(sql);
					while (rs2.next()) {
						e = new Empresa(rs2.getInt("IDEMPRESA"), rs2.getInt("IDCLIENTE"),
								rs2.getString("EMPRESA.NOMBRE"), rs2.getString("CIF"), rs2.getBoolean("ESPROVEEDOR"));
					}
				}
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"), rs.getString("TIPO"));
				d = leerDireccionPorID(c.getDireccionID());
				docu = new Documento(rs.getInt("IDDOCUMENTO"), rs.getInt("CLIENTEID"), rs.getString("TITULO"),
						rs.getBlob("DOCUMENTO"), rs.getString("EXTENSION"));
				cpedd = new ClienteParticularEmpresaDireccionDocumento(c, p, e, d, docu);
				listaDocumentos.add(cpedd);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
		}
		return listaDocumentos;
	}

	/**
	 * Guarda en las variables globales los precios de Hora y de IVA
	 */
	public void getPrecioHoraIva() {
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			// Se coge el precio hora
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'PRECIOHORA'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.PRECIO_HORA = rs.getFloat("VALOR");
				// Inicio.PRECIO_IVA = rs.getFloat("IVA");
				// Inicio.NUM_PRESUPUESTO = rs.getInt("PRESUPUESTO");
				// Inicio.NUM_FACTURA = rs.getInt("FACTURA");
			}
			// Se coge el iva
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'IVA'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.PRECIO_IVA = rs.getFloat("VALOR");
				// Inicio.NUM_PRESUPUESTO = rs.getInt("PRESUPUESTO");
				// Inicio.NUM_FACTURA = rs.getInt("FACTURA");
			}
			// Se coge el numpresupuesto
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'PRESUPUESTO'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.NUM_PRESUPUESTO = rs.getString("VALOR");
				// Inicio.NUM_FACTURA = rs.getInt("FACTURA");
			}
			// Se coge el numfactura
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'FACTURA'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.NUM_FACTURA = rs.getString("VALOR");
			}
			// Se coge el usuario
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'USUARIO'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.USUARIO = rs.getString("VALOR");
			}
			// Se coge el pass
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'PASS'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.PASS = rs.getString("VALOR");
			}
			// Se coge la sal
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'SAL'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.SAL = rs.getString("VALOR");
			}
			// Se coge si tiene puesto el autologin
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'AUTOLOGIN'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.AUTOLOGIN = rs.getBoolean("VALOR");
			}
			// Se coge la ruta donde se guardan las facturas generadas
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'RUTAFACTURAS'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.RUTA_FACTURAS = rs.getString("VALOR");
			}
			// Se coge la ruta donde se guardan las copias de seguridad
			sql = "SELECT VALOR FROM AUXILIAR WHERE CLAVE = 'RUTABACKUP'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Inicio.RUTA_BACKUP = rs.getString("VALOR");
			}

			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			Utilidades.mostrarError(ex);
		}
	}

	/**
	 * Guarda en la BD el usuario y la contraseña
	 * 
	 * @param user
	 * @param pass
	 */
	public void guardarUserPass(String user, String pass) {
		String sql = "";
		PreparedStatement st;
		try {
			// Actualizar usuario
			sql = "UPDATE AUXILIAR SET VALOR = '" + user + "' WHERE CLAVE = 'USUARIO'";
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Añadir sal, encriptar pass y actualizarlo en la BD
			Random r = new SecureRandom();
			byte[] sal = new byte[20];
			r.nextBytes(sal);
			pass += sal;
			String passEncriptado = DigestUtils.sha512Hex(pass);

			sql = "UPDATE AUXILIAR SET VALOR = '" + passEncriptado + "' WHERE CLAVE = 'PASS'";
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();

			sql = "UPDATE AUXILIAR SET VALOR = '" + sal + "' WHERE CLAVE = 'SAL'";
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();
		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atención", "Error al guardar usuario y contraseña",
					"Ocurrió un error al guardar el usuario y contraseña en la base de datos");
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene la versión actual de BD
	 * @return
	 */
	public String getVersionDB(){
	    String sql = "";
	    String res = "";
	    try {
		// Obtener version
		Statement st = getCon().createStatement();
		sql = "SELECT valor FROM AUXILIAR WHERE CLAVE = 'VERSION_DB'";
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			res = rs.getString("VALOR");
		}else{
		    res = "";
		}
		
	    } catch (Exception e) {
	    	Utilidades.mostrarError(e);
	    }
	    return res;
	}
	
	/**
	 * Crea por primera vez el versionado de la BD 
	 */
	public void crearDBVersion(){
	    String sql = "";
		try {
			// Se prepara la sentencia para introducir los datos
			sql = "INSERT INTO AUXILIAR (CLAVE, VALOR) VALUES (?,?)";
			PreparedStatement st = getCon().prepareStatement(sql);
			
			// Añadimos los parametros
			st.setString(1, "VERSION_DB");
			st.setString(2, "1");
						
			// Ejecutamos la sentencia
			st.executeUpdate();
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
	}
	
	/**
	 * Actualiza la BD mediante el fichero database.xml
	 * @throws Exception 
	 */
	public void actualizaDB() throws Exception{
		//InputStream ips = ClassLoader.getSystemClassLoader().getResourceAsStream("database.xml");
		InputStream ips = Inicio.class.getResourceAsStream("/recursos/database.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(ips);
		Element version = (Element) document.getElementsByTagName("database").item(0);

		int maxItems = version.getElementsByTagName("sql").getLength();

		String sql;
		String currentVersion = getVersionDB();
		
		for (int i = 0; i<maxItems; i++){ 
			Element versionElement = (Element) version.getElementsByTagName("sql").item(i);
			String versionNumber = versionElement.getAttribute("version");

			if (Integer.parseInt(currentVersion) >= Integer.parseInt(versionNumber)) {
				continue;
			}

			System.out.println("Version: " + versionNumber);

			sql = versionElement.getTextContent();
			executeTransaction(sql, versionNumber);
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @param version
	 * @throws Exception 
	 */
	public void executeTransaction(String sql, String version) throws SQLException{
		Connection cnx = getCon();
		PreparedStatement st;
		try{
			cnx.setAutoCommit(false);
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();
			
			//Actualizar version
			String newSql = "UPDATE AUXILIAR SET VALOR = '" + version + "' WHERE CLAVE = 'VERSION_DB'";
			st = getCon().prepareStatement(newSql);
			// Ejecutamos la sentencia
			st.executeUpdate();
			cnx.commit();
		}catch(Exception e){
			cnx.rollback();
		}
	}
	
	/**
	 * Actualiza la dirección para poner únicamente un campo para calle, número, piso y letra
	 * @throws SQLException 
	 */
	public void actualizaDireccionVersion2() throws SQLException{
		PreparedStatement st;
		String sql;
		ArrayList<Integer> lista = new ArrayList<>();
		Connection cnx = getCon();
		try{
			cnx.setAutoCommit(false);
			sql = "SELECT IDDIRECCION FROM DIRECCION";
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				lista.add(rs.getInt(1));
			}
			for(int id : lista){
				//Actualizar datos
				sql = "UPDATE DIRECCION SET DIRECCION = (SELECT CONCAT(DIRECCION, ' ', NUMERO, ' ', PISO, ' ', LETRA) FROM DIRECCION WHERE iddireccion  = ?) WHERE iddireccion = ?";
				st = getCon().prepareStatement(sql);
				
				// Añadimos los parametros
				st.setInt(1, id);
				st.setInt(2, id);
							
				// Ejecutamos la sentencia
				st.executeUpdate();
			}
			
			// Eliminar las columnas numero, piso, letra
			sql = "ALTER TABLE DIRECCION DROP COLUMN NUMERO";
			st.executeUpdate(sql);
			sql = "ALTER TABLE DIRECCION DROP COLUMN PISO";
			st.executeUpdate(sql);
			sql = "ALTER TABLE DIRECCION DROP COLUMN LETRA";
			st.executeUpdate(sql);
			cnx.commit();
		}catch(Exception e){
			Utilidades.mostrarError(e);
			cnx.rollback();
		}
	}

	public Connection getCon() {
		try {
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection(Inicio.DBURL + ";AUTO_SERVER=TRUE;CIPHER=AES", "sa", "1234 1234");
			con.setAutoCommit(true);
		} catch (Exception e) {
			Utilidades.mostrarError(e);
		}
		return con;
	}
}
