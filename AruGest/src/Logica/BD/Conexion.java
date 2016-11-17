package Logica.BD;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.h2.tools.Server;

import Logica.Inicio;
import Modelo.Cliente;
import Modelo.ClienteParticularEmpresaDireccion;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.FacturaClienteVehiculo;
import Modelo.Material;
import Modelo.Particular;
import Modelo.Servicio;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
			if (cped.getDireccion() != null) {
				sql = "INSERT INTO DIRECCION (CALLE, NUMERO, PISO, LETRA, CPOSTAL, LOCALIDAD, PROVINCIA) VALUES (?,?,?,?,?,?,?)";
				st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					idGenerado = rs.getLong(1);
				}
			}

			// 2º Guardar Cliente
			if (idGenerado != 0) {
				sql = "INSERT INTO CLIENTE (NOMBRE, TELF1, TELF2, TELF3, DIRECCIONID) VALUES (?,?,?,?,?)";
				st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				// Añadimos los parametros
				st.setString(1, cped.getCliente().getNombre());
				st.setString(2, cped.getCliente().getTelf1());
				st.setString(3, cped.getCliente().getTelf2());
				st.setString(4, cped.getCliente().getTelf3());
				st.setInt(5, (int) idGenerado);
			} else {
				sql = "INSERT INTO CLIENTE (NOMBRE, TELF1, TELF2, TELF3) VALUES (?,?,?,?)";
				st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				// Añadimos los parametros
				st.setString(1, cped.getCliente().getNombre());
				st.setString(2, cped.getCliente().getTelf1());
				st.setString(3, cped.getCliente().getTelf2());
				st.setString(4, cped.getCliente().getTelf3());
			}
			// Ejecutamos la sentencia
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				idGenerado = rs.getLong(1);
				// 3º Guardar Particular / Empresa
				if (cped.getParticular() != null) {
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
				} else if (cped.getEmpresa() != null) {
					// Guardar Empresa
					sql = "INSERT INTO EMPRESA (CLIENTEID, NOMBRE, CIF, ESPROVEEDOR) VALUES (?,?,?,?)";
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
	 * Guarda en la base de datos el vehículo que se le pasa como parámetro
	 * 
	 * @param vehiculo
	 *            a guardar en la BD, el ID del cliente dueño y el tipo de
	 *            vehiculo que es
	 * @return true si se guardo correctamente, false si no
	 */
	public boolean guardarVehiculo(Vehiculo v) {
		boolean res = true;
		try {
			// Para guardar factura -> 1º Factura 2º Servicios 3º Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO VEHICULO (CLIENTEID, MARCA, MODELO, VERSION, MATRICULA, ANIO, BASTIDOR, LETRASMOTOR, COLOR, CODRADIO, TIPOID) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// Añadimos los parametros
			st.setInt(1, v.getClienteID());
			st.setString(2, v.getMarca());
			st.setString(3, v.getModelo());
			st.setString(4, v.getVersion());
			st.setString(5, v.getMatricula());
			st.setInt(6, v.getAnio());
			st.setString(7, v.getBastidor());
			st.setString(8, v.getLetrasmotor());
			st.setString(9, v.getColor());
			st.setString(10, v.getCodradio());
			st.setInt(11, v.getTipoID());

			// Ejecutamos la sentencia
			st.executeUpdate();
			res = true;
		} catch (Exception ex) {
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
			sql = "SELECT IDDOCUMENTO FROM DOCUMENTO WHERE VEHICULOID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarDocumentosPorVehiculoID(id);
			}

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
					+ "BASTIDOR = ?, LETRASMOTOR = ?, COLOR = ?, CODRADIO = ?, TIPOID = ?" + "WHERE IDVEHICULO = "
					+ v.getIdvehiculo();
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
		try {
			// 1º Direccion
			sql = "UPDATE DIRECCION SET CALLE = ?, NUMERO = ?, PISO = ?, "
					+ "LETRA = ?, CPOSTAL = ?, LOCALIDAD = ?, PROVINCIA = ? " + "WHERE IDDIRECCION = "
					+ cped.getCliente().getDireccionID();
			PreparedStatement st = getCon().prepareStatement(sql);
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
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"));
			}
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
			e.printStackTrace();
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
	public Direccion buscarDireccionPorID(int iddireccion) {
		String sql = "";
		Direccion d = null;
		try {
			// Se prepara la sentencia para buscar los datos del cliente
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM DIRECCION WHERE IDDIRECCION = " + iddireccion + "";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				d = new Direccion(rs.getString("CALLE"), rs.getInt("NUMERO"), rs.getString("PISO"),
						rs.getString("LETRA"), rs.getString("LOCALIDAD"));
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return d;
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
				f = new Factura(rs.getInt("IDFACTURA"), rs.getInt("CLIENTEID"), rs.getInt("VEHICULOID"),
						rs.getInt("NUMFACTURA"), rs.getInt("NUMPRESUPUESTO"), rs.getInt("NUMORDENREP"),
						rs.getInt("NUMRESGUARDO"), rs.getDate("FECHA"), rs.getDate("FECHAENTREGA"),
						rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"), rs.getFloat("GRUA"), rs.getString("ESTADO"),
						rs.getBoolean("RDEFOCULTOS"), rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"),
						rs.getBoolean("NOPIEZAS"), rs.getBoolean("MODIFICABLE"), rs.getFloat("IMPORTETOTAL"));
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"));
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"),
						rs.getInt("TIPOID"));
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
				sql = "SELECT * FROM CLIENTE INNER JOIN PARTICULAR ON CLIENTE.IDCLIENTE = PARTICULAR.CLIENTEID LEFT JOIN DIRECCION ON CLIENTE.DIRECCIONID = DIRECCION.IDDIRECCION ";
				if (!dni.equalsIgnoreCase("")) {
					if (esPrimero) {
						sql += "WHERE PARTICULAR.NIF LIKE '%" + dni + "%' ";
						esPrimero = false;
					} else {
						sql += "AND PARTICULAR.NIF LIKE '%" + dni + "%' ";
					}
				}

			} else if (tipo == 2) {
				sql = "SELECT * FROM CLIENTE INNER JOIN EMPRESA ON CLIENTE.IDCLIENTE = EMPRESA.CLIENTEID LEFT JOIN DIRECCION ON CLIENTE.DIRECCIONID = DIRECCION.IDDIRECCION ";
				if (!dni.equalsIgnoreCase("")) {
					if (esPrimero) {
						sql += "WHERE EMPRESA.CIF LIKE '%" + dni + "%' ";
						esPrimero = false;
					} else {
						sql += "AND EMPRESA.CIF LIKE '%" + dni + "%' ";
					}
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
			/*
			 * if (!modelo.equalsIgnoreCase("")) { if (esPrimero) { sql +=
			 * "WHERE VEHICULO.MODELO LIKE '%" + modelo + "%' "; esPrimero =
			 * false; } else { sql += "AND VEHICULO.MODELO LIKE '%" + modelo +
			 * "%' "; } } if (!matricula.equalsIgnoreCase("")) { if (esPrimero)
			 * { sql += "WHERE VEHICULO.MATRICULA LIKE '%" + matricula + "%' ";
			 * esPrimero = false; } else { sql +=
			 * "AND VEHICULO.MATRICULA LIKE '%" + matricula + "%' "; } }
			 */
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
					sql += "WHERE DIRECCION.CALLE LIKE '%" + domicilio + "%' ";
					esPrimero = false;
				} else {
					sql += "AND DIRECCION.CALLE LIKE '%" + domicilio + "%' ";
				}
			}
			if (!poblacion.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE DIRECCION.LOCALIDAD LIKE '%" + poblacion + "%' ";
					esPrimero = false;
				} else {
					sql += "AND DIRECCION.LOCALIDAD LIKE '%" + poblacion + "%' ";
				}
			}

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				c = new Cliente(rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("TELF1"),
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"));
				d = new Direccion(rs.getString("CALLE"), rs.getInt("NUMERO"), rs.getString("PISO"),
						rs.getString("LETRA"), rs.getInt("CPOSTAL"), rs.getString("LOCALIDAD"),
						rs.getString("PROVINCIA"));
				if (tipo == 1) {
					p = new Particular(rs.getInt("IDPARTICULAR"), rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("APELLIDOS"), rs.getString("NIF"));
				} else if (tipo == 2) {
					e = new Empresa(rs.getInt("IDEMPRESA"), rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"), rs.getString("CIF"), rs.getBoolean("ESPROVEEDOR"));
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
						rs.getString("CODRADIO"), rs.getInt("TIPOID"));
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
	 * Guarda en las variables globales los precios de Hora y de IVA
	 */
	public void getPrecioHoraIva() {
		String sql = "";
		try {
			// Se prepara la sentencia
			Statement st = getCon().createStatement();
			sql = "SELECT * FROM AUXILIAR";

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Inicio.PRECIO_HORA = rs.getFloat("PRECIOHORA");
				Inicio.PRECIO_IVA = rs.getFloat("IVA");
			}
			// Se cierra la conexion
			getCon().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
