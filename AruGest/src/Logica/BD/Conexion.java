package Logica.BD;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.h2.tools.Server;

import Logica.Inicio;
import Logica.Utilidades;
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
import Modelo.VehiculoSustitucion;
import Modelo.VehiculoSustitucionClienteVehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

/**
 * Esta clase se utilizar� para todas las funciones que tengan que hacer accesos
 * a la base de datos, as� como para crear y cerrar las conexiones.
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
	 * Guarda en la base de datos la factura que se le pasa como par�metro
	 * 
	 * @param factura
	 *            a guardar en la base de datos
	 */
	public void guardarFactura(Factura f, ObservableList<Servicio> servicios, ObservableList<Material> materiales) {
		try {
			// Para guardar factura -> 1� Factura 2� Servicios 3� Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO FACTURA (CLIENTEID, VEHICULOID, NUMFACTURA, NUMPRESUPUESTO, NUMORDENREP, NUMRESGUARDO, FECHA, "
							+ "FECHAENTREGA, MANOOBRA, MATERIALES, GRUA, ESTADO, RDEFOCULTOS, PORCENTAJEDEFOCUL, PERMISOPRUEBAS, "
							+ "NOPIEZAS, MODIFICABLE, IMPORTETOTAL) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// A�adimos los parametros
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
				// Actualizar pr�ximo n�mero de presupuesto y factura
				actualizarNumPresuFactura();
				// Actualizar los valores de pr�ximos presupuestos/facturas
				Inicio.CONEXION.getPrecioHoraIva();
				Utilidades.mostrarAlerta(AlertType.CONFIRMATION, "Atenci�n", "Factura guardada",
						"La factura ha sido guardada en la base de datos");
			}

			// Se cierra la conexion
			getCon().close();

		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atenci�n", "Error al guardar factura",
					"Ocurri� un error al guardar la factura en la base de datos");
			e.printStackTrace();
		}
	}

	/**
	 * Se actualiza en la BD el n� siguiente de presupuesto/factura
	 */
	public void actualizarNumPresuFactura() {
		String sql = "";
		PreparedStatement st;
		try {
			// Actualizar n� presupuesto
			sql = "UPDATE AUXILIAR SET PRESUPUESTO = ((SELECT MAX(NUMPRESUPUESTO) FROM FACTURA) + 1) WHERE IDAUXILIAR = 0";
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();

			// Actualizar n� factura
			sql = "UPDATE AUXILIAR SET FACTURA = ((SELECT MAX(NUMFACTURA) FROM FACTURA) + 1) WHERE IDAUXILIAR = 0";
			st = getCon().prepareStatement(sql);
			// Ejecutamos la sentencia
			st.executeUpdate();
		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atenci�n",
					"Error al actualizar el n�mero de presupuesto/factura",
					"Ocurri� un error al actualizar el n�mero de presupuesto/factura en la base de datos");
			e.printStackTrace();
		}

	}

	public boolean actualizarOpciones(float precioHora, float iva, int numPresupuesto, int numFactura) {
		String sql = "";
		PreparedStatement st;
		try {
			// Actualizar n� presupuesto
			sql = "UPDATE AUXILIAR SET PRECIOHORA = ?, IVA = ?, PRESUPUESTO = ?, FACTURA = ? WHERE IDAUXILIAR = 0";
			st = getCon().prepareStatement(sql);
			// A�adimos los parametros
			st.setFloat(1, precioHora);
			st.setFloat(2, iva);
			st.setInt(3, numPresupuesto);
			st.setInt(4, numFactura);
			// Ejecutamos la sentencia
			st.executeUpdate();
			return true;
		} catch (Exception e) {
			Utilidades.mostrarAlerta(AlertType.ERROR, "Atenci�n", "Error al guardar las opciones",
					"Ocurri� un error al actualizar las opciones en la base de datos");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Guarda la direccion pasada como par�metro en la BD
	 * 
	 * @param d
	 * @return id generado en la BD o CERO si ocurri� alg�n error
	 */
	public long guardarDireccion(Direccion d) {
		PreparedStatement st;
		ResultSet rs;
		long idGenerado = 0;
		String sql = "";
		try {
			sql = "INSERT INTO DIRECCION (CALLE, NUMERO, PISO, LETRA, CPOSTAL, LOCALIDAD, PROVINCIA) VALUES (?,?,?,?,?,?,?)";
			st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// A�adimos los parametros
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
	 * @return true si fue bien, false si hubo alg�n error
	 */
	public boolean actualizarIDDireccionCliente(int idcliente, int iddireccion) {
		String sql = "";
		boolean resul = true;
		PreparedStatement st;
		try {
			sql = "UPDATE CLIENTE SET DIRECCIONID = ? WHERE IDCLIENTE = " + idcliente;
			st = getCon().prepareStatement(sql);
			// A�adimos los parametros
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
	 * Guarda en la base de datos el cliente que se le pasa como par�metro
	 * 
	 * @param Direccion,
	 *            Cliente, Particular o Empresa (El que no sea, ser� null) a
	 *            guardar en la BD
	 */
	public boolean guardarCliente(ClienteParticularEmpresaDireccion cped) {
		boolean res = true;
		// 1� Guardar Direccion
		try {
			PreparedStatement st;
			ResultSet rs;
			long idGenerado = 0;
			String sql = "";
			// Se prepara la sentencia para introducir los datos de la direccion
			// SI NO ES NULL
			if (cped.getDireccion() != null) {
				idGenerado = guardarDireccion(cped.getDireccion());
			}

			// 2� Guardar Cliente
			sql = "INSERT INTO CLIENTE (NOMBRE, TELF1, TELF2, TELF3, DIRECCIONID) VALUES (?,?,?,?,?)";
			st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// A�adimos los parametros
			st.setString(1, cped.getCliente().getNombre());
			st.setString(2, cped.getCliente().getTelf1());
			st.setString(3, cped.getCliente().getTelf2());
			st.setString(4, cped.getCliente().getTelf3());
			if (idGenerado != 0) {
				st.setInt(5, (int) idGenerado);
			} else {
				st.setInt(5, 0);
			}
			// Ejecutamos la sentencia
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				idGenerado = rs.getLong(1);
				// 3� Guardar Particular / Empresa
				if (cped.getParticular() != null) {
					// Guardar Particular
					sql = "INSERT INTO PARTICULAR (CLIENTEID, NOMBRE, APELLIDOS, NIF) VALUES (?,?,?,?)";
					st = getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					// A�adimos los parametros
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
					// A�adimos los parametros
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
	 * Guarda en la base de datos el veh�culo que se le pasa como par�metro
	 * 
	 * @param vehiculo
	 *            a guardar en la BD, el ID del cliente due�o y el tipo de
	 *            vehiculo que es
	 * @return true si se guardo correctamente, false si no
	 */
	public boolean guardarVehiculo(Vehiculo v) {
		boolean res = true;
		try {
			// Para guardar factura -> 1� Factura 2� Servicios 3� Materiales
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement(
					"INSERT INTO VEHICULO (CLIENTEID, MARCA, MODELO, VERSION, MATRICULA, ANIO, BASTIDOR, LETRASMOTOR, COLOR, CODRADIO, TIPOID, ESVEHICULOSUSTITUCION) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			// A�adimos los parametros
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
			st.setBoolean(12, v.isEsVehiculoSustitucion());

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
	 * Elimina de la base de datos el veh�culo cuyo id se le pasa como par�metro
	 * 
	 * @param id
	 *            del vehiculo a eliminar
	 * @return true si se elimin� correctamente, false si no
	 */
	public boolean eliminarVehiculo(int id) {
		/*
		 * 1� comprobar si tiene facturas: - si: coger id factura, eliminar
		 * servicios, eliminar materiales, eliminar factura. 2� comprobar si
		 * tiene documentos - si: eliminar documento 3� comprobar si es vehiculo
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

			// 2� comprobar si tiene documentos
			sql = "SELECT IDDOCUMENTO FROM DOCUMENTO WHERE VEHICULOID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarDocumentosPorVehiculoID(id);
			}

			// 3� comprobar si es vehiculo de sustitucion
			sql = "SELECT IDVEHICULOSUSTI FROM VEHICULOSUSTITUCION WHERE VEHICULOID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarVehiculoSustiPorVehiculoID(id);
			}

			// 4� eliminar vehiculo
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
	 * Edita en la BD el veh�culo pasado como par�metro
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
	 * Edita en la BD el cliente pasado como par�metro
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
			// 1� Direccion
			// Si ya tiene direccion, se actualiza
			// Si no tiene direccion (iddireccion = 0), se crea
			if (cped.getCliente().getDireccionID() != 0) {
				sql = "UPDATE DIRECCION SET CALLE = ?, NUMERO = ?, PISO = ?, "
						+ "LETRA = ?, CPOSTAL = ?, LOCALIDAD = ?, PROVINCIA = ? " + "WHERE IDDIRECCION = "
						+ cped.getCliente().getDireccionID();
				st = getCon().prepareStatement(sql);
				// A�adimos los parametros
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
				// Guardar direccion y asignarle su iddireccion al cliente
				idGenerado = guardarDireccion(cped.getDireccion());
				if (idGenerado > 0) { // Si es 0 es que hubo un error al guardar
										// la direccion
					res = actualizarIDDireccionCliente(cped.getCliente().getIdcliente(), (int) idGenerado);
					// Acabar aqu� la funcion si res = false
				}

			}
			// 2� Cliente
			sql = "UPDATE CLIENTE SET NOMBRE = ?, TELF1 = ?, TELF2 = ?, " + "TELF3 = ? " + "WHERE IDCLIENTE = "
					+ cped.getCliente().getIdcliente();
			st = getCon().prepareStatement(sql);
			// A�adimos los parametros
			st.setString(1, cped.getCliente().getNombre());
			st.setString(2, cped.getCliente().getTelf1());
			st.setString(3, cped.getCliente().getTelf2());
			st.setString(4, cped.getCliente().getTelf3());
			// Ejecutamos la sentencia
			st.executeUpdate();
			// 3� Particular / Empresa
			if (cped.getParticular() != null) {
				// Guardar Particular
				sql = "UPDATE PARTICULAR SET NOMBRE = ?, APELLIDOS = ?, NIF = ? " + "WHERE IDPARTICULAR = "
						+ cped.getParticular().getIdparticular();
				st = getCon().prepareStatement(sql);
				// A�adimos los parametros
				st.setString(1, cped.getParticular().getNombre());
				st.setString(2, cped.getParticular().getApellidos());
				st.setString(3, cped.getParticular().getNif());
			} else if (cped.getEmpresa() != null) {
				// Guardar Empresa
				sql = "UPDATE EMPRESA SET NOMBRE = ?, CIF = ?, ESPROVEEDOR = ? " + "WHERE IDEMPRESA = "
						+ cped.getEmpresa().getIdempresa();
				st = getCon().prepareStatement(sql);
				// A�adimos los parametros
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
	 * Elimina el cliente pasado como par�metro de la BD con todo lo que tenga
	 * asociado (Facturas, vehiculos de sustitucion, documentos...)
	 * 
	 * @param cped
	 * @return
	 */
	public boolean eliminarCliente(ClienteParticularEmpresaDireccion cped) {
		/*
		 * Orden para eliminar un cliente: 1� Facturas asociadas 2�
		 * VehiculosSustitucion asociados 3� Documentos asociados 4� Vehiculos
		 * asociados 5� Particular / Empresa asociada 6� Cliente 7� Direccion
		 * asociada
		 */
		String sql = "";
		boolean res = true;
		PreparedStatement pt;
		int id = cped.getCliente().getIdcliente();
		int iddireccion = cped.getCliente().getDireccionID();
		try {
			// 1� Facturas
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

				// Actualizar pr�ximo n�mero de presupuesto y factura
				actualizarNumPresuFactura();
				// Actualizar los valores de pr�ximos presupuestos/facturas
				Inicio.CONEXION.getPrecioHoraIva();
			}

			// 2� VehiculosSustitucion
			sql = "SELECT IDVEHICULOSUSTI FROM VEHICULOSUSTITUCION WHERE CLIENTEID = " + id;
			rs = st.executeQuery(sql);
			if (rs.next()) {
				eliminarVehiculoSustiPorClienteID(id);
			}

			// 3� Documentos
			sql = "SELECT IDDOCUMENTO FROM DOCUMENTO WHERE CLIENTEID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarDocumentosPorClienteID(id);
			}

			// 4� Vehiculos
			sql = "SELECT IDVEHICULO FROM VEHICULO WHERE CLIENTEID = " + id;
			rs = st.executeQuery(sql);
			while (rs.next()) {
				eliminarVehiculo(rs.getInt("IDVEHICULO"));
			}

			// 5� Particular / Empresa
			if (cped.getParticular() != null) {
				sql = "SELECT IDPARTICULAR FROM PARTICULAR WHERE CLIENTEID = " + id;
				rs = st.executeQuery(sql);
				while (rs.next()) {
					eliminarParticularPorClienteID(id);
				}
			} else if (cped.getEmpresa() != null) {
				sql = "SELECT IDEMPRESA FROM EMPRESA WHERE CLIENTEID = " + id;
				rs = st.executeQuery(sql);
				while (rs.next()) {
					eliminarEmpresaPorClienteID(id);
				}
			}

			// 6� Cliente
			sql = "DELETE FROM CLIENTE WHERE IDCLIENTE = " + id;
			pt = getCon().prepareStatement(sql);
			pt.executeUpdate();

			// 7� Direccion
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
	 * Elimina de la BD la factura cuyo ID se pasa como par�metro
	 * 
	 * @param idFactura
	 * @return
	 */
	public boolean eliminarFacturaPorID(int idFactura) {
		String sql = "";
		boolean res = true;
		PreparedStatement pt;
		try {
			// 1� Eliminar servicios y materiales y despu�s la factura
			eliminarServiciosPorFacturaID(idFactura);
			eliminarMaterialesPorFacturaID(idFactura);

			sql = "DELETE FROM FACTURA WHERE IDFACTURA = " + idFactura;
			pt = getCon().prepareStatement(sql);
			pt.executeUpdate();

			// Actualizar pr�ximo n�mero de presupuesto y factura
			actualizarNumPresuFactura();
			// Actualizar los valores de pr�ximos presupuestos/facturas
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
	 * Elimina la direcci�n con id direccionID
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
	 *            a buscar en la BD tipo de documento a buscar: 1-Dni, 2-Cif
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * Busca un Particular en la BD con el clienteID que se pasa como par�metro
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
	 * Busca una Empresa en la BD con el clienteID que se pasa como par�metro
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
	 * Busca las factura que coincidan con los par�metros introducidos
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
					sql += "WHERE UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(CLIENTE.NOMBRE) LIKE UPPER('%" + nombre + "%') ";
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
			if (!matricula.equalsIgnoreCase("")) {
				if (esPrimero) {
					sql += "WHERE UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%') ";
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
					sql += "WHERE UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%') ";
					esPrimero = false;
				} else {
					sql += "AND UPPER(VEHICULO.MATRICULA) LIKE UPPER('%" + matricula + "%') ";
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

	/**
	 * Busca los clientes que coincidan con los par�metros introducidos
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
				sql = "SELECT DISTINCT * FROM CLIENTE INNER JOIN EMPRESA ON CLIENTE.IDCLIENTE = EMPRESA.CLIENTEID ";
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
						rs.getString("TELF2"), rs.getString("TELF3"), rs.getInt("DIRECCIONID"));
				d = new Direccion(rs.getString("CALLE"), rs.getInt("NUMERO"), rs.getString("PISO"),
						rs.getString("LETRA"), rs.getInt("CPOSTAL"), rs.getString("LOCALIDAD"),
						rs.getString("PROVINCIA"));
				if (tipo == 1) {
					p = new Particular(rs.getInt("IDPARTICULAR"), rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"),
							rs.getString("APELLIDOS"), rs.getString("NIF"));
				} else if (tipo == 2) {
					e = new Empresa(rs.getInt("IDEMPRESA"), rs.getInt("IDCLIENTE"), rs.getString("NOMBRE"),
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
	 * Busca en la BD los veh�culos de un cliente
	 * 
	 * @param ID
	 *            del cliente
	 * @return ObservableList<Vehiculo> con los veh�culos
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
						rs.getInt("NUMFACTURA"), rs.getInt("NUMPRESUPUESTO"), rs.getInt("NUMORDENREP"),
						rs.getInt("NUMRESGUARDO"), rs.getDate("FECHA"), rs.getDate("FECHAENTREGA"),
						rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"), rs.getFloat("GRUA"), rs.getString("ESTADO"),
						rs.getBoolean("RDEFOCULTOS"), rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"),
						rs.getBoolean("NOPIEZAS"), rs.getBoolean("MODIFICABLE"), rs.getFloat("IMPORTETOTAL"));
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
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
						rs.getInt("NUMFACTURA"), rs.getInt("NUMPRESUPUESTO"), rs.getInt("NUMORDENREP"),
						rs.getInt("NUMRESGUARDO"), rs.getDate("FECHA"), rs.getDate("FECHAENTREGA"),
						rs.getFloat("MANOOBRA"), rs.getFloat("MATERIALES"), rs.getFloat("GRUA"), rs.getString("ESTADO"),
						rs.getBoolean("RDEFOCULTOS"), rs.getFloat("PORCENTAJEDEFOCUL"), rs.getBoolean("PERMISOPRUEBAS"),
						rs.getBoolean("NOPIEZAS"), rs.getBoolean("MODIFICABLE"), rs.getFloat("IMPORTETOTAL"));
				v = new Vehiculo(rs.getInt("IDVEHICULO"), rs.getInt("CLIENTEID"), rs.getString("MARCA"),
						rs.getString("MODELO"), rs.getString("VERSION"), rs.getString("MATRICULA"), rs.getInt("ANIO"),
						rs.getString("BASTIDOR"), rs.getString("LETRASMOTOR"), rs.getString("COLOR"),
						rs.getString("CODRADIO"), rs.getInt("TIPOID"), rs.getBoolean("ESVEHICULOSUSTITUCION"));
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
	 * Busca en la BD los vehiculos de sustitucion que est�n actualmente
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
	 * Busca en la BD los vehiculos de sustitucion que est�n actualmente
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
	 * Busca en la BD el hist�rico de los vehiculos de sustitucion
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
	 * la fecha de devoluci�n � crea un nuevo registro
	 * 
	 * @param tipo:
	 *            D = Devuelto ; E = Entregado
	 * @param vscv
	 *            datos del veh�culo de sustituci�n
	 * @return true si fue bien, false si no
	 */
	public boolean actualizarVehiculoSustitucion(String tipo, VehiculoSustitucionClienteVehiculo vscv) {
		String sql = "";
		PreparedStatement st;
		boolean res = true;
		try {
			if (tipo.equalsIgnoreCase("D")) {
				sql = "UPDATE VEHICULOSUSTITUCION SET FECHADEVUELVE = ?, OBSERVACIONES = ? WHERE IDVEHICULOSUSTI = "
						+ vscv.getVehiculoSustitucion().getIdvehiculosusti();
				st = getCon().prepareStatement(sql);
				// A�adimos los parametros
				st.setDate(1, new java.sql.Date(vscv.getVehiculoSustitucion().getFechadevuelve().getTime()));
				st.setString(2, vscv.getVehiculoSustitucion().getObservaciones());
				// Ejecutamos la sentencia
				st.executeUpdate();
			} else if (tipo.equalsIgnoreCase("E")) {
				// Crear el registro del vehiculo de sustitucion en la base de
				// datos
				sql = "INSERT INTO VEHICULOSUSTITUCION (FECHACOGE, FECHADEVUELVE, CLIENTEID, VEHICULOID, OBSERVACIONES) VALUES (?,?,?,?,?)";
				st = getCon().prepareStatement(sql);
				// A�adimos los parametros
				st.setDate(1, new java.sql.Date(vscv.getVehiculoSustitucion().getFechacoge().getTime()));
				st.setDate(2, null);
				st.setInt(3, Inicio.CLIENTE_ID);
				st.setInt(4, vscv.getVehiculo().getIdvehiculo());
				st.setString(5, vscv.getVehiculoSustitucion().getObservaciones());
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
				Inicio.NUM_PRESUPUESTO = rs.getInt("PRESUPUESTO");
				Inicio.NUM_FACTURA = rs.getInt("FACTURA");
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
