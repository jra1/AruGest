package Logica.BD;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

import org.h2.tools.Server;

public class Conexion {
	
	private Connection con = null;
	
	public Conexion() {
	}
	
	/**
	 * Crea la conexion con la base de datos
	 * 
	 * @return - Connection a la base de datos
	 */
	public Connection getConexion() {
		
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
}
