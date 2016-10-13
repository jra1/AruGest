package Logica.BD;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
	/**
	 * Crea la conexion con la base de datos
	 * 
	 * @return - Connection a la base de datos
	 */
	public static Connection getConexion() {
		Connection con = null;
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
		    //con = DriverManager.getConnection("jdbc:h2:tcp://"+pIp+"/C:/H2DB/AruGestDB;AUTO_SERVER=TRUE", "sa", "");
		    		
		    //DriverManager.getConnection("jdbc:h2:C:/H2DB/BD_Ruara", "sa", "");
		    
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
