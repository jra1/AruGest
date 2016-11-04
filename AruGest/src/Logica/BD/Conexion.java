package Logica.BD;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JOptionPane;

import org.h2.tools.Server;

import Logica.Inicio;
import Modelo.Cliente;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.FacturaClienteVehiculo;
import Modelo.Material;
import Modelo.Particular;
import Modelo.Servicio;
import Modelo.Vehiculo;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;

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
	public Connection crearConexion() {

		try {
			/*
			 * Con Base de datos MySql // Se carga el driver (tipo 4)
			 * Class.forName("com.mysql.jdbc.Driver");
			 * 
			 * // Se abre la conexion a la base de datos String url =
			 * "jdbc:mysql://localhost:3306/movedb"; String login = "root";
			 * String pass = "root"; con = DriverManager.getConnection(url,
			 * login, pass);
			 */

			// Con base de datos H2 (PRUEBA)
			// Usuario: sa
			// Pass: (No hay)
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection("jdbc:h2:tcp://localhost/C:/H2DB/AruGestDB;AUTO_SERVER=TRUE", "sa", "");
			getCon().setAutoCommit(true);
			// JOptionPane.showMessageDialog(null, "Servidor: "+
			// con.getSchema().toString());
			// DriverManager.getConnection("jdbc:h2:C:/H2DB/BD_Ruara", "sa",
			// "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public void arrancarServidor(/* String args */) {
		try {
			Server server = Server.createTcpServer("-tcpAllowOthers");
			server.start();

			int seleccion = JOptionPane.showOptionDialog(null, "Servidor arrancado en: " + server.getStatus(),
					"Servidor arrancado", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, // null
																												// para
																												// icono
																												// por
																												// defecto.
					new Object[] { "Aceptar", "Parar servidor" }, // null para
																	// YES, NO y
																	// CANCEL
					"Aceptar");

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
	 *            a guardar en la base de datos
	 */
	public void guardarFactura(Factura f, ObservableList<Servicio> servicios, ObservableList<Material> materiales) {
		try {
			// Para guardar factura -> 1º Factura 2º Servicios 3º Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO FACTURA (CLIENTEID, VEHICULOID, NUMFACTURA, NUMPRESUPUESTO, NUMORDENREP, NUMRESGUARDO, FECHA, "
							+ "FECHAENTREGA, MANOOBRA, MATERIALES, GRUA, ESTADO, RDEFOCULTOS, PORCENTAJEDEFOCUL, PERMISOPRUEBAS, "
							+ "NOPIEZAS, MODIFICABLE, IMPORTETOTAL) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setInt(1, f.getClienteID());
			st.setInt(2, f.getVehiculoID());
			st.setInt(3, f.getNumfactura());
			st.setInt(4, f.getNumpresupuesto());
			st.setInt(5, f.getNumordenrep());
			st.setInt(6, f.getNumresguardo());
			st.setDate(7, new java.sql.Date(f.getFecha().getTime()));
			st.setDate(8, new java.sql.Date(f.getFechaentrega().getTime()));
			st.setFloat(9, f.getManoobra());
			st.setFloat(10, f.getMateriales());
			st.setFloat(11, f.getGrua());
			st.setString(12, f.getEstado());
			st.setBoolean(13, f.isRdefocultos());
			st.setFloat(14, f.getPorcentajedefocul());
			st.setBoolean(15, f.isPermisopruebas());
			st.setBoolean(16, f.isNopiezas());
			st.setBoolean(17, f.isModificable());
			st.setFloat(18, f.getImporteTotal());

			// Ejecutamos la sentencia
			int i = st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				long idFactura = rs.getLong(1);
				// Guardar servicios
				st = getCon().prepareStatement(
						"INSERT INTO SERVICIO (SERVICIO, HORAS, FACTURAID, TIPOSERVICIO) VALUES (?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				for (Servicio serv : servicios) {
					st.setString(1, serv.getServicio());
					st.setBigDecimal(2, new BigDecimal(serv.getHoras()));
					st.setInt(3, (int) idFactura);
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
					st.setInt(3, (int) idFactura);
					st.setInt(4, mat.getCantidad());
					st.setBigDecimal(5, new BigDecimal(mat.getPreciototal()));
					st.executeUpdate();
				}
			}

			if (i > 0) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Atención");
				alert.setHeaderText("Factura guardada");
				alert.setContentText("La factura ha sido guardada en la base de datos");

				alert.showAndWait();
			}

			// Se cierra la conexion
			getCon().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Guarda en la base de datos el cliente que se le pasa como parámetro
	 * 
	 * @param Direccion,
	 *            Cliente, Particular o Empresa (El que no sea, será null) a
	 *            guardar en la BD
	 */
	public void guardarCliente(Direccion d, Cliente c, Particular p, Empresa e) {
		// 1º Guardar Direccion
		try {
			java.sql.PreparedStatement st;
			ResultSet rs;
			long idGenerado = 0;
			String sql = "";
			// Se prepara la sentencia para introducir los datos de la direccion
			// SI NO ES NULL
			if (d != null) {
				st = getCon().prepareStatement(
						"INSERT INTO DIRECCION (CALLE, NUMERO, PISO, LETRA, LOCALIDAD) VALUES (?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);

				// Añadimos los parametros
				st.setString(1, d.getCalle());
				st.setInt(2, d.getNumero());
				st.setString(3, d.getPiso());
				st.setString(4, d.getLetra());
				st.setString(5, d.getLocalidad());

				// Ejecutamos la sentencia
				st.executeUpdate();

				rs = st.getGeneratedKeys();

				if (rs.next()) {
					idGenerado = rs.getLong(1);
				}
			}

			// 2º Guardar Cliente
			if (idGenerado != 0) {
				st = getCon().prepareStatement(
						"INSERT INTO CLIENTE (NOMBRE, TELF1, TELF2, DIRECCIONID) VALUES (?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				// Añadimos los parametros
				st.setString(1, c.getNombre());
				st.setString(2, c.getTelf1());
				st.setString(3, c.getTelf2());
				st.setInt(4, (int) idGenerado);
			} else {
				st = getCon().prepareStatement("INSERT INTO CLIENTE (NOMBRE, TELF1, TELF2) VALUES (?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				// Añadimos los parametros
				st.setString(1, c.getNombre());
				st.setString(2, c.getTelf1());
				st.setString(3, c.getTelf2());
			}
			// Ejecutamos la sentencia
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				idGenerado = rs.getLong(1);
				// 3º Guardar Particular / Empresa
				if (p != null) {
					// Guardar Particular
					st = getCon().prepareStatement(
							"INSERT INTO PARTICULAR (CLIENTEID, NOMBRE, APELLIDOS, NIF) VALUES (?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
					// Añadimos los parametros
					st.setInt(1, (int) idGenerado);
					st.setString(2, p.getNombre());
					st.setString(3, p.getApellidos());
					st.setString(4, p.getNif());
					// Ejecutamos la sentencia
					st.executeUpdate();
				} else if (e != null) {
					// Guardar Empresa
					st = getCon().prepareStatement(
							"INSERT INTO EMPRESA (CLIENTEID, NOMBRE, CIF, ESPROVEEDOR) VALUES (?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
					// Añadimos los parametros
					st.setInt(1, (int) idGenerado);
					st.setString(2, e.getNombre());
					st.setString(3, e.getCif());
					st.setBoolean(4, false);
					// Ejecutamos la sentencia
					st.executeUpdate();
				}
			}

			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Guarda en la base de datos el vehículo que se le pasa como parámetro
	 * 
	 * @param vehiculo
	 *            a guardar en la BD, el ID del cliente dueño y el tipo de
	 *            vehiculo que es
	 */
	public void guardarVehiculo(Vehiculo v) {
		try {
			// Para guardar factura -> 1º Factura 2º Servicios 3º Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO VEHICULO (CLIENTEID, MARCA, MODELO, VERSION, MATRICULA, TIPOID) VALUES (?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setInt(1, v.getClienteID());
			st.setString(2, v.getMarca());
			st.setString(3, v.getModelo());
			st.setString(4, v.getVersion());
			st.setString(5, v.getMatricula());
			st.setInt(6, v.getTipoID());

			// Ejecutamos la sentencia
			st.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
						+ "NIF = '" + dni + "'";// + "NIF LIKE '%" + dni + "%'";
			} else if (tipo == 2) {
				sql = "SELECT * FROM EMPRESA INNER JOIN CLIENTE ON EMPRESA.CLIENTEID = CLIENTE.IDCLIENTE WHERE "
						+ "CIF LIKE '%" + dni + "%'";
			}
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"));
			}

			/*
			 * String s = ""; if (c == null) { s = "Cliente no encontrado"; }
			 * else { s = "Cliente encontrado"; } Alert alert = new
			 * Alert(AlertType.INFORMATION); alert.setTitle("Atención");
			 * alert.setHeaderText(s);
			 * 
			 * alert.showAndWait();
			 */
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			e.printStackTrace();
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
			sql = "SELECT * FROM VEHICULO  WHERE MATRICULA = '" + matricula + "'";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MATRICULA"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	/**
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
	 * @return
	 */
	public ArrayList<FacturaClienteVehiculo> buscarFacturas(int numFactura, int numPresu, int numOrden, int numResgu,
			String nombre, String modelo, String matricula, String fijo, String movil, LocalDate fechaDesde,
			LocalDate fechaHasta) {

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
			if (numFactura != 0) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMFACTURA = " + numFactura + " ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMFACTURA = " + numFactura + " ";
				}
			}
			if (numPresu != 0) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMPRESUPUESTO = " + numPresu + " ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMPRESUPUESTO = " + numPresu + " ";
				}
			}
			if (numOrden != 0) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMORDENREP = " + numOrden + " ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMORDENREP = " + numOrden + " ";
				}
			}
			if (numResgu != 0) {
				if (esPrimero) {
					sql += "WHERE FACTURA.NUMRESGUARDO = " + numResgu + " ";
					esPrimero = false;
				} else {
					sql += "AND FACTURA.NUMRESGUARDO = " + numResgu + " ";
				}
			}
			if (!nombre.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE CLIENTE.NOMBRE LIKE '%" + nombre + "%' ";
					esPrimero = false;
				} else {
					sql += "AND CLIENTE.NOMBRE LIKE '%" + nombre + "%' ";
				}
			}
			if (!modelo.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE VEHICULO.MODELO LIKE '%" + modelo + "%' ";
					esPrimero = false;
				} else {
					sql += "AND VEHICULO.MODELO LIKE '%" + modelo + "%' ";
				}
			}
			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE VEHICULO.MATRICULA LIKE '%" + matricula + "%' ";
					esPrimero = false;
				} else {
					sql += "AND VEHICULO.MATRICULA LIKE '%" + matricula + "%' ";
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
			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE VEHICULO.MATRICULA LIKE '%" + matricula + "%' ";
					esPrimero = false;
				} else {
					sql += "AND VEHICULO.MATRICULA LIKE '%" + matricula + "%' ";
				}
			}

			// Para comparar fechas, hay que ponerlas en el formato aaaa-mm-dd
			sql += "AND FACTURA.FECHA BETWEEN '" + fechaDesde + "' AND '" + fechaHasta + "'";

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				f = new Factura(rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"), rs.getInt("NUMFACTURA"),
						rs.getInt("NUMPRESUPUESTO"), rs.getInt("NUMORDENREP"), rs.getInt("NUMRESGUARDO"),
						rs.getDate("FECHA"), rs.getDate("FECHAENTREGA"), rs.getFloat("MANOOBRA"),
						rs.getFloat("MATERIALES"), rs.getFloat("GRUA"), rs.getString("ESTADO"),
						rs.getBoolean("RDEFOCULTOS"), rs.getFloat("PORCENTAJEDEFOCUL"),
						rs.getBoolean("PERMISOPRUEBAS"), rs.getBoolean("NOPIEZAS"), rs.getBoolean("MODIFICABLE"),
						rs.getFloat("IMPORTETOTAL"));
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"), rs.getString("TELF2"));
				v = new Vehiculo(rs.getInt("CLIENTEID"), rs.getString("MARCA"), rs.getString("MODELO"), rs.getString("VERSION"),
						rs.getString("MATRICULA"), rs.getInt("TIPOID"));
				fcv = new FacturaClienteVehiculo(f, c, v);
				listaFacturas.add(fcv);
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaFacturas;
	}

	public Connection getCon() {
		try {
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection("jdbc:h2:tcp://localhost/C:/H2DB/AruGestDB;AUTO_SERVER=TRUE", "sa", "");
			con.setAutoCommit(true);
		} catch (Exception e) {

		}
		return con;
	}
}
