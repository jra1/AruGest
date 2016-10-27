package Logica.BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import org.h2.tools.Server;

import Logica.Inicio;
import Modelo.Cliente;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Factura;
import Modelo.Particular;
import Modelo.Vehiculo;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Conexion{
	
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
			/*Con Base de datos MySql
			// Se carga el driver (tipo 4)
			Class.forName("com.mysql.jdbc.Driver");

			// Se abre la conexion a la base de datos
			String url = "jdbc:mysql://localhost:3306/movedb";
			String login = "root";
			String pass = "root";
			con = DriverManager.getConnection(url, login, pass);
			*/
			
			//Con base de datos H2 (PRUEBA) 
			//Usuario: sa
			//Pass: (No hay)
			Class.forName("org.h2.Driver");
		    con = DriverManager.getConnection("jdbc:h2:tcp://localhost/C:/H2DB/AruGestDB;AUTO_SERVER=TRUE", "sa", "");
		    		
		    //JOptionPane.showMessageDialog(null, "Servidor: "+ con.getSchema().toString());
		    //DriverManager.getConnection("jdbc:h2:C:/H2DB/BD_Ruara", "sa", "");
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	

	public void arrancarServidor(/*String args*/){
		try 
		{ 
			Server server = Server.createTcpServer("-tcpAllowOthers");
			server.start();
			
			int seleccion = JOptionPane.showOptionDialog(
					   null,
					   "Servidor arrancado en: "+ server.getStatus(), 
					   "Servidor arrancado",
					   JOptionPane.YES_NO_CANCEL_OPTION,
					   JOptionPane.QUESTION_MESSAGE,
					   null,    // null para icono por defecto.
					   new Object[] { "Aceptar", "Parar servidor"},   // null para YES, NO y CANCEL
					   "Aceptar");
			
			if (seleccion == 1){
				server.stop();
				server.shutdown();				
			}
			
			//System.out.println(InetAddress.getLocalHost().getHostAddress());
			//System.out.println(InetAddress.getLocalHost().getHostName());
			//System.out.println(NetworkInterface.getNetworkInterfaces());
			//new Console().runTool(args);
			//directorio/ejecutable es el path del ejecutable y un nombre 
			//Process p = Runtime.getRuntime().exec("C:/Program Files (x86)/H2/bin/h2.bat"); 
			//Runtime.getRuntime().exec("C:/Program Files (x86)/iTunes/iTunes.exe");
		} 
		catch (Exception e) 
		{ 
			JOptionPane.showMessageDialog(null, "Hubo un error al arrancar el servidor: "+ e.getMessage());
		}
	}
	
	/**
	 * Guarda en la base de datos la factura que se le pasa como parámetro
	 * @param factura a guardar en la base de datos
	 */
	public void guardarFactura(Factura f){
		try {	
			// Se prepara la sentencia para introducir los datos de la factura
			java.sql.PreparedStatement st = getCon().prepareStatement("INSERT INTO FACTURA (NUMFACTURA, NUMPRESUPUESTO, NUMORDENREP, NUMRESGUARDO, FECHA, "
							+ "FECHAENTREGA, MANOOBRA, MATERIALES, GRUA, ESTADO, RDEFOCULTOS, PORCENTAJEDEFOCUL, PERMISOPRUEBAS, "
							+ "NOPIEZAS, MODIFICABLE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			// Añadimos los parametros
			st.setInt(1, f.getNumfactura());
			st.setInt(2, f.getNumpresupuesto());
			st.setInt(3, f.getNumordenrep());
			st.setInt(4, f.getNumresguardo());
			st.setDate(5, new java.sql.Date(f.getFecha().getTime()));
			st.setDate(6, new java.sql.Date(f.getFechaentrega().getTime()));
			st.setFloat(7, f.getManoobra());
			st.setFloat(8, f.getMateriales());
			st.setFloat(9, f.getGrua());
			st.setString(10, f.getEstado());
			st.setBoolean(11, f.isRdefocultos());
			st.setFloat(12, f.getPorcentajedefocul());
			st.setBoolean(13, f.isPermisopruebas());
			st.setBoolean(14, f.isNopiezas());
			st.setBoolean(15, f.isModificable());
			
			// Ejecutamos la sentencia
			int i = st.executeUpdate();
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
	 * @param Direccion, Cliente, Particular o Empresa (El que no sea, será null) a guardar en la BD
	 */
	public void guardarCliente(Direccion d, Cliente c, Particular p, Empresa e){
		//1º Guardar Direccion
		//2º Guardar Cliente
		//3º Guardar Particular / Empresa
	}
	
	/**
	 * Guarda en la base de datos el vehículo que se le pasa como parámetro
	 * @param vehiculo a guardar en la BD
	 */
	public void guardarVehiculo(Vehiculo v){
		//TO-DO
	}
	
	/**
	 * Busca un cliente en la BD por su DNI
	 * @param dni a buscar en la BD
	 * @return el cliente encontrado o null si no existe ese DNI
	 */
	public Cliente buscarClientePorDni(String dni){
		return null;
	}
	
	/**
	 * Busca un vehiculo en la BD por su matricula
	 * @param matricula a buscar en la BD
	 * @return el vehiculo encontrado o null si no existe
	 */
	public Vehiculo buscarVehiculoPorMatricula(String matricula){
		return null;
	}

	public Connection getCon() {
		return con;
	}
}
