package Logica;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Utilidades {

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
	 * @param at: Tipo de alerta (Error, Information, ... )
	 * @param titulo: Titulo de la alerta
	 * @param header: Cabecera de la alerta
	 * @param texto: Mensaje de la alerta
	 */
	public static Optional<ButtonType> mostrarAlerta(AlertType at, String titulo, String header, String texto){
		Alert alert = new Alert(at);
		alert.setTitle(titulo);
		alert.setHeaderText(header);
		alert.setContentText(texto);
		return alert.showAndWait();
	}
	
	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
