package Logica;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Modelo.GestorVentana;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Utilidades {

	/**
	 * Se ajusta la resoluion del escenario a la pantalla en que se esté
	 * mostrando
	 * 
	 * @param stage
	 * @param x
	 * @param y
	 */
	public static void ajustarResolucionEscenario(Stage stage, double x, double y) {
		stage.setWidth(x);
		stage.setHeight(y);
	}

	public static void ajustarResolucionAnchorPane(AnchorPane ap, double x, double y) {
		ap.setPrefWidth(x - 262);
		ap.setPrefHeight(y - 89);
	}

	/**
	 * Se elimina el botón que contenga el nombre que se le pasa como parámetro
	 * 
	 * @param pNombre
	 */
	public static void quitarBoton(String pNombre) {
		for (GestorVentana gv : Inicio.LISTA_VENTANAS) {
			if (gv.getNombre().indexOf(pNombre) != -1) {
				Inicio.LISTA_VENTANAS.remove(gv);
			}
		}
	}

	/**
	 * Gestiona los botones del gestor de pantallas abiertas
	 * 
	 * @param pGgv
	 */
	public static void gestionarPantallas(GestorVentana pGgv) {
		boolean esta = false;
		switch (Inicio.LISTA_VENTANAS.size()) {
		case 0:
			Inicio.LISTA_VENTANAS.add(0, pGgv);
			break;
		case 1:
			for (GestorVentana gv : Inicio.LISTA_VENTANAS) {
				if (gv.getNombre().equalsIgnoreCase(pGgv.getNombre())) {
					esta = true;
				}
			}
			if (!esta) {
				Inicio.LISTA_VENTANAS.add(0, pGgv);
			}
			break;
		case 2:
			for (GestorVentana gv : Inicio.LISTA_VENTANAS) {
				if (gv.getNombre().equalsIgnoreCase(pGgv.getNombre())) {
					esta = true;
				}
			}
			if (!esta) {
				Inicio.LISTA_VENTANAS.add(0, pGgv);
			}
			break;
		case 3:
			for (GestorVentana gv : Inicio.LISTA_VENTANAS) {
				if (gv.getNombre().equalsIgnoreCase(pGgv.getNombre())) {
					esta = true;
				}
			}
			if (!esta) {
				Inicio.LISTA_VENTANAS.remove(2);
				Inicio.LISTA_VENTANAS.add(0, pGgv);
			}
			break;
		default:
			break;
		}

		switch (Inicio.LISTA_VENTANAS.size()) {
		case 0:
			Inicio.BOTON1.setNum(1);
			Inicio.BOTON1.setVisible(false);
			Inicio.BOTON1.setNombre("");

			Inicio.BOTON2.setNum(2);
			Inicio.BOTON2.setVisible(false);
			Inicio.BOTON2.setNombre("");

			Inicio.BOTON3.setNum(3);
			Inicio.BOTON3.setVisible(false);
			Inicio.BOTON3.setNombre("");

			break;
		case 1:
			Inicio.BOTON1.setNum(1);
			Inicio.BOTON1.setVisible(true);
			Inicio.BOTON1.setNombre(Inicio.LISTA_VENTANAS.get(0).getNombre());

			Inicio.BOTON2.setNum(2);
			Inicio.BOTON2.setVisible(false);
			Inicio.BOTON2.setNombre("");

			Inicio.BOTON3.setNum(3);
			Inicio.BOTON3.setVisible(false);
			Inicio.BOTON3.setNombre("");

			break;
		case 2:
			Inicio.BOTON1.setNum(1);
			Inicio.BOTON1.setVisible(true);
			Inicio.BOTON1.setNombre(Inicio.LISTA_VENTANAS.get(0).getNombre());

			Inicio.BOTON2.setNum(2);
			Inicio.BOTON2.setVisible(true);
			Inicio.BOTON2.setNombre(Inicio.LISTA_VENTANAS.get(1).getNombre());

			Inicio.BOTON3.setNum(3);
			Inicio.BOTON3.setVisible(false);
			Inicio.BOTON3.setNombre("");

			break;
		case 3:
			Inicio.BOTON1.setNum(1);
			Inicio.BOTON1.setVisible(true);
			Inicio.BOTON1.setNombre(Inicio.LISTA_VENTANAS.get(0).getNombre());

			Inicio.BOTON2.setNum(2);
			Inicio.BOTON2.setVisible(true);
			Inicio.BOTON2.setNombre(Inicio.LISTA_VENTANAS.get(1).getNombre());

			Inicio.BOTON3.setNum(3);
			Inicio.BOTON3.setVisible(true);
			Inicio.BOTON3.setNombre(Inicio.LISTA_VENTANAS.get(2).getNombre());

			break;
		default:
			break;
		}

	}

	/**
	 * Limpia la lista de ventanas recientes abiertas
	 */
	public static void eliminaBotones() {
		Inicio.LISTA_VENTANAS.clear();
		Inicio.BOTON1.setVisible(false);
		Inicio.BOTON2.setVisible(false);
		Inicio.BOTON3.setVisible(false);
	}

	public static Date LocalDateADate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate DateALocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Se le pasa como parámetro el entero del tipo vehiculo y devuelve el
	 * String correspondiente
	 * 
	 * @param tipoID
	 * @return
	 */
	public static String tipoIDtoString(int tipoID) {
		String respuesta = "";
		switch (tipoID) {
		case 1:
			respuesta = "Turismo";
			break;
		case 2:
			respuesta = "Furgoneta";
			break;
		case 3:
			respuesta = "Camión";
			break;
		case 4:
			respuesta = "Autobús";
			break;
		case 5:
			respuesta = "Autocaravana";
			break;
		case 6:
			respuesta = "Moto";
			break;
		case 7:
			respuesta = "Remolque";
			break;
		}
		return respuesta;
	}

	/**
	 * Se le pasa como parámetro el entero del tipo vehiculo y devuelve el
	 * StringProperty correspondiente (Para las tablas)
	 * 
	 * @param tipoID
	 * @return
	 */
	public static StringProperty tipoIDtoStringProperty(int tipoID) {
		StringProperty respuesta = new SimpleStringProperty("");
		switch (tipoID) {
		case 1:
			respuesta = new SimpleStringProperty("Turismo");
			break;
		case 2:
			respuesta = new SimpleStringProperty("Furgoneta");
			break;
		case 3:
			respuesta = new SimpleStringProperty("Camión");
			break;
		case 4:
			respuesta = new SimpleStringProperty("Autobús");
			break;
		case 5:
			respuesta = new SimpleStringProperty("Autocaravana");
			break;
		case 6:
			respuesta = new SimpleStringProperty("Moto");
			break;
		case 7:
			respuesta = new SimpleStringProperty("Remolque");
			break;
		}
		return respuesta;
	}

	/**
	 * Se comprueba el valor elegido en el combo de tipo vehiculo para definir
	 * el valor de la variable tipoVehiculo
	 * 
	 * @param valor
	 */
	public static int StringToTipoID(String valor) {
		int a = 1;
		switch (valor) {
		case "Turismo":
			a = 1;
			break;
		case "Furgoneta":
			a = 2;
			break;
		case "Camión":
			a = 3;
			break;
		case "Autobús":
			a = 4;
			break;
		case "Autocaravana":
			a = 5;
			break;
		case "Moto":
			a = 6;
			break;
		case "Remolque":
			a = 7;
			break;
		}
		return a;
	}

	/**
	 * Muestra una alerta con los parámetros que se le pasan
	 * 
	 * @param at:
	 *            Tipo de alerta (Error, Information, ... )
	 * @param titulo:
	 *            Titulo de la alerta
	 * @param header:
	 *            Cabecera de la alerta
	 * @param texto:
	 *            Mensaje de la alerta
	 */
	public static Optional<ButtonType> mostrarAlerta(AlertType at, String titulo, String header, String texto) {
		Alert alert = new Alert(at);
		// Point p = MouseInfo.getPointerInfo().getLocation();
		// alert.setX(p.getX()); /**CON ESTAS LINEAS EL MENSAJE APARECE EN LA
		// POSICION DEL RATON**/
		// alert.setY(p.getY());
		alert.setTitle(titulo);
		alert.setHeaderText(header);
		alert.setContentText(texto);
		DialogPane dialogPane = alert.getDialogPane();
		// dialogPane.getStylesheets().add(Inicio.class.getResource("../GUI/EstiloRoot.css").toExternalForm());
		dialogPane.getStylesheets().add("GUI/EstiloRoot.css");
		dialogPane.getStyleClass().add("my-dialog");
		return alert.showAndWait();
	}

	public static Optional<ButtonType> mostrarError(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error inesperado");
		alert.setHeaderText("Ocurrió un error inesperado");
		// alert.setContentText("Could not find file blabla.txt!");

		// Exception ex = new FileNotFoundException("Could not find file
		// blabla.txt");

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Información sobre el error:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		return alert.showAndWait();
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * Método para identificar la extensión de un fichero
	 * 
	 * @param ruta
	 * @return extensión del fichero ó cadena vacía
	 */
	public static String getExtension(String ruta) {
		File f = new File(ruta);
		if (f == null || f.isDirectory()) {
			return "";
		} else if (f.isFile()) {
			int index = ruta.lastIndexOf('.');
			if (index == -1) {
				return "";
			} else {
				return ruta.substring(index + 1);
			}
		} else {
			return "";
		}
	}

	/**
	 * Comprueba que el texto que se le pasa puede convertirse a número
	 * 
	 * @param pNum
	 * @return número convertido ó -1 si no se puede convertir
	 */
	public static int validaNumero(String pNum) {
		try {
			return Integer.parseInt(pNum);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Comprueba si está creada la carpeta correspondiente para guardar esa
	 * factura y si no está, la crea
	 * 
	 * @param fecha
	 * @return ruta a guardar
	 */
	public static String dameRuta(Date fecha, String nombreFactura) {
		if (fecha != null) {
			String ruta = "";
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			int anio = cal.get(Calendar.YEAR);
			String nombreMes = dameMes(cal.get(Calendar.MONTH) + 1);
			File carpeta = new File(Inicio.RUTA_FACTURAS + "\\" + nombreMes + "_" + anio);
			if (!carpeta.exists()) {
				carpeta.mkdir();
			}
			ruta = Inicio.RUTA_FACTURAS + "\\" + nombreMes + "_" + anio + "\\" + nombreFactura;
			return ruta;
		} else {
			return Inicio.RUTA_FACTURAS + "\\" + nombreFactura;
		}
	}

	/**
	 * Convierte el entero que se le pasa a un string con el nombre de mes
	 * correspondiente
	 * 
	 * @param mes
	 * @return
	 */
	private static String dameMes(int mes) {
		String nombre = "";
		switch (mes) {
		case 1:
			nombre = "Enero";
			break;
		case 2:
			nombre = "Febrero";
			break;
		case 3:
			nombre = "Marzo";
			break;
		case 4:
			nombre = "Abril";
			break;
		case 5:
			nombre = "Mayo";
			break;
		case 6:
			nombre = "Junio";
			break;
		case 7:
			nombre = "Julio";
			break;
		case 8:
			nombre = "Agosto";
			break;
		case 9:
			nombre = "Septiembre";
			break;
		case 10:
			nombre = "Octubre";
			break;
		case 11:
			nombre = "Noviembre";
			break;
		case 12:
			nombre = "Diciembre";
			break;
		}
		return nombre;
	}

	public static String formateaNumFactura(String pNum) {
		String num = "";
		try {
			int entero = Integer.parseInt(pNum);
			num = String.format("%05d", entero);
		} catch (Exception e) {
			num = "#ERROR";
		}
		return num;
	}

	public static boolean validaMatricula(String pMat) {
		pMat = pMat.toUpperCase();
		pMat = pMat.replaceAll("-", "");
		pMat = pMat.replaceAll(" ", "");
		pMat = pMat.replaceAll("/", "");
		Pattern pat = Pattern.compile("(([a-zA-Z]{1,2})(\\d{4})([a-zA-Z]{0,2}))|((\\d{4})([a-zA-Z]{3}))");
		Matcher mat = pat.matcher(pMat);
		return mat.matches();
	}
	
	/**
	 * Valida que pDni sea un dni correcto (letra incluída)
	 * @param pDni
	 * @return
	 */
	public static boolean validaDni(String pDni) {
		String numero = "";
		String letra = "";
		String let = "";
		pDni = pDni.toUpperCase();
		pDni = pDni.replaceAll("-", "");
		pDni = pDni.replaceAll(" ", "");
		pDni = pDni.replaceAll("/", "");
		Pattern patNif = Pattern.compile("([XYZ]?\\d{5,8}[A-Z])");
		Matcher mat = patNif.matcher(pDni);
		if (mat.matches()) {
			// Es NIF o NIE
			numero = pDni.substring(0, pDni.length() - 1);
			numero = numero.replace('X', '0');
			numero = numero.replace('Y', '1');
			numero = numero.replace('Z', '2');
			let = pDni.substring(pDni.length() - 1);
			letra = "TRWAGMYFPDXBNJZSQVHLCKET";
			int num = Integer.parseInt(numero);
			num = num % 23;
			letra = letra.substring(num, num + 1);
			return letra.equals(let);
		} else {
			// Es CIF
			Pattern patCif = Pattern.compile("(^[a-zA-Z]{1}\\d{7}[a-jA-J0-9]{1}$)");
			Matcher mat2 = patCif.matcher(pDni);
			return mat2.matches();
		}
	}
	
	/**
	 * Devuelve pCad con la primera letra en mayúscula
	 * @param pCad
	 * @return
	 */
	public static String upperFirstLetter(String pCad) {
		if(!"".equalsIgnoreCase(pCad)) {
			return pCad.substring(0, 1).toUpperCase() + pCad.substring(1, pCad.length());			
		}else {
			return pCad;
		}
	}
}
