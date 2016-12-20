package Logica;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import Modelo.GestorVentana;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
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

			// btnPantalla1.setVisible(false);
			// btnPantalla2.setVisible(false);
			// btnPantalla3.setVisible(false);
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

			// btnPantalla1.setText(Inicio.LISTA_VENTANAS.get(0).getNombre());
			// btnPantalla1.setVisible(true);
			// btnPantalla2.setVisible(false);
			// btnPantalla3.setVisible(false);
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

			// btnPantalla1.setText(Inicio.LISTA_VENTANAS.get(0).getNombre());
			// btnPantalla1.setVisible(true);
			// btnPantalla2.setText(Inicio.LISTA_VENTANAS.get(1).getNombre());
			// btnPantalla2.setVisible(true);
			// btnPantalla3.setVisible(false);
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

			// btnPantalla1.setText(Inicio.LISTA_VENTANAS.get(0).getNombre());
			// btnPantalla1.setVisible(true);
			// btnPantalla2.setText(Inicio.LISTA_VENTANAS.get(1).getNombre());
			// btnPantalla2.setVisible(true);
			// btnPantalla3.setText(Inicio.LISTA_VENTANAS.get(2).getNombre());
			// btnPantalla3.setVisible(true);
			break;
		default:
			break;
		}

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
		dialogPane.getStylesheets().add(Inicio.class.getResource("../GUI/EstiloRoot.css").toExternalForm());
		dialogPane.getStyleClass().add("my-dialog");
		return alert.showAndWait();
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
