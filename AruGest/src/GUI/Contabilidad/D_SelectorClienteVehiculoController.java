package GUI.Contabilidad;

import Logica.Utilidades;
import Modelo.ClienteParticularEmpresaDireccionVehiculo;
import Modelo.Direccion;
import Modelo.Empresa;
import Modelo.Particular;
import Modelo.Vehiculo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class D_SelectorClienteVehiculoController {
	// Variables de la vista
	// Datos cliente
	@FXML
	private TextField txtNombre;
	@FXML
	private Label lblApellidos;
	@FXML
	private TextField txtApellidos;
	@FXML
	private TextField txtCalle;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtPiso;
	@FXML
	private TextField txtLetra;
	@FXML
	private TextField txtPoblacion;
	@FXML
	private TextField txtDni;
	@FXML
	private TextField txtTel1;
	// @FXML
	// private TextField txtMovil;

	// Datos Vehiculo
	@FXML
	private ComboBox<String> comboTipoVehiculo;
	@FXML
	private TextField txtMarca;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtVersion;
	@FXML
	private TextField txtMatricula;
	@FXML
	private TextField txtKms;
	@FXML
	private ComboBox<String> comboTipoCliente;

	// Resto de variables
	private Stage dialogStage;
	private boolean okClicked = false;
	private ClienteParticularEmpresaDireccionVehiculo cpedv;
	private int tipoCliente = 1; // 1-Particular, 2-Empresa
	private int tipoVehiculo = 1;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		comboTipoCliente.getItems().addAll("Particular", "Empresa");
		comboTipoCliente.setValue("Particular");
		comboTipoVehiculo.getItems().addAll("Turismo", "Furgoneta", "Camión", "Autobús", "Autocaravana", "Moto",
				"Remolque");
		comboTipoVehiculo.setValue("Turismo");
		comboTipoCliente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipoCliente(newValue));

		comboTipoVehiculo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> comprobarComboTipoVehiculo(newValue));
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			// Datos del cliente
			cpedv.getCliente().setNombre(txtNombre.getText() + " " + txtApellidos.getText());
			cpedv.getCliente().setTelf1(txtTel1.getText());

			if (tipoCliente == 1) { // Particular
				cpedv.getCliente().setTipo("P");
				if (cpedv.getParticular() == null) {
					Particular p = new Particular(0, cpedv.getCliente().getIdcliente(), txtNombre.getText(),
							txtApellidos.getText(), txtDni.getText());
					cpedv.setParticular(p);
				} else {
					cpedv.getParticular().setNif(txtDni.getText());
					cpedv.getParticular().setNombre(txtNombre.getText());
					cpedv.getParticular().setApellidos(txtApellidos.getText());
				}

				// cpedv.getEmpresa().setCif("");
				// cpedv.getEmpresa().setNombre("");
			} else if (tipoCliente == 2) { // Empresa
				cpedv.getCliente().setTipo("E");
				if (cpedv.getEmpresa() == null) {
					Empresa e = new Empresa(0, cpedv.getCliente().getIdcliente(), txtNombre.getText(), txtDni.getText(),
							false);
					cpedv.setEmpresa(e);
				} else {
					cpedv.getEmpresa().setCif(txtDni.getText());
					cpedv.getEmpresa().setNombre(txtNombre.getText());
				}

				// cpedv.getParticular().setNif("");
				// cpedv.getParticular().setNombre("");
				// cpedv.getParticular().setApellidos("");
			}

			if (!txtCalle.getText().isEmpty() || !txtPoblacion.getText().isEmpty()) {
				if (cpedv.getDireccion() == null) {
					Direccion d = new Direccion(txtCalle.getText(), Integer.parseInt(txtNumero.getText()),
							txtPiso.getText(), txtLetra.getText(), txtPoblacion.getText());
					cpedv.setDireccion(d);
				} else {
					cpedv.getDireccion().setCalle(txtCalle.getText());
					cpedv.getDireccion().setNumero(Integer.parseInt(txtNumero.getText()));
					cpedv.getDireccion().setPiso(txtPiso.getText());
					cpedv.getDireccion().setLetra(txtLetra.getText());
					cpedv.getDireccion().setLocalidad(txtPoblacion.getText());
				}
			}

			// Datos del vehiculo
			if (!txtKms.getText().isEmpty()) {
				cpedv.setKms(Integer.parseInt(txtKms.getText()));
			} else {
				cpedv.setKms(0);
			}
			if (cpedv.getVehiculo() == null) {
				Vehiculo v = new Vehiculo(1, cpedv.getCliente().getIdcliente(), txtMarca.getText(), txtModelo.getText(),
						txtVersion.getText(), txtMatricula.getText(), tipoVehiculo);
				cpedv.setVehiculo(v);
			} else {
				cpedv.getVehiculo().setTipoID(tipoVehiculo);
				cpedv.getVehiculo().setMatricula(txtMatricula.getText());
				cpedv.getVehiculo().setMarca(txtMarca.getText());
				cpedv.getVehiculo().setModelo(txtModelo.getText());
				cpedv.getVehiculo().setVersion(txtVersion.getText());
			}

			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validaciones de los campos introducidos por el cliente
	 * 
	 * @return true si los campos son correctos
	 */
	private boolean isInputValid() {
		String mensaje = "";
		// Comprobar entradas de datos son correctas
		if (!txtNumero.getText().isEmpty()) {
			try {
				Integer.parseInt(txtNumero.getText());
			} catch (Exception e) {
				mensaje = "El número de vivienda no es válido. Introduce sólo números.";
			}
		}
		if (!txtKms.getText().isEmpty()) {
			try {
				Integer.parseInt(txtKms.getText());
			} catch (Exception e) {
				mensaje = "Los  kilómetros del vehículo no son correctos. Introduce sólo números.";
			}
		}
		if (mensaje == "") {
			return true;
		} else {
			Utilidades.mostrarAlerta(AlertType.INFORMATION, "Atención", "Datos erróneos", mensaje);
			return false;
		}
	}

	public void setClienteVehiculo(ClienteParticularEmpresaDireccionVehiculo cpedv) {
		this.cpedv = cpedv;
		if (cpedv != null) {
			if (cpedv.getCliente() != null) {
				// Particular p =
				// Inicio.CONEXION.buscarParticularPorClienteID(Inicio.CLIENTE_ID);
				if (cpedv.getParticular() != null) {
					comboTipoCliente.setValue("Particular");
					txtNombre.setText(cpedv.getParticular().getNombre());
					txtApellidos.setText(cpedv.getParticular().getApellidos());
					txtDni.setText(cpedv.getParticular().getNif());
				} else {
					// Empresa e =
					// Inicio.CONEXION.buscarEmpresaPorClienteID(Inicio.CLIENTE_ID);
					if (cpedv.getEmpresa() != null) {
						comboTipoCliente.setValue("Empresa");
						txtNombre.setText(cpedv.getEmpresa().getNombre());
						txtDni.setText(cpedv.getEmpresa().getCif());
					}
				}
				if (cpedv.getCliente().getDireccionID() != 0) {
					// Direccion d =
					// Inicio.CONEXION.leerDireccionPorID(c.getDireccionID());
					txtCalle.setText(cpedv.getDireccion().getCalle());
					txtNumero.setText("" + cpedv.getDireccion().getNumero());
					txtPiso.setText(cpedv.getDireccion().getPiso());
					txtLetra.setText(cpedv.getDireccion().getLetra());
					txtPoblacion.setText(cpedv.getDireccion().getLocalidad());
				}
				txtTel1.setText(cpedv.getCliente().getTelf1());
				// txtMovil.setText(c.getTelf2());
			}
			if (cpedv.getVehiculo() != null) {
				// Cargar datos vehiculo
				txtMatricula.setText(cpedv.getVehiculo().getMatricula());
				txtMarca.setText(cpedv.getVehiculo().getMarca());
				txtModelo.setText(cpedv.getVehiculo().getModelo());
				txtVersion.setText(cpedv.getVehiculo().getVersion());
				comboTipoVehiculo.setValue(Utilidades.tipoIDtoString(cpedv.getVehiculo().getTipoID()));

			}
		}
	}

	/**
	 * Se comprueba el valor elegido en el combo de tipo cliente para ocultar o
	 * no el TextField de "Apellidos"
	 * 
	 * @param valor
	 */
	private void comprobarComboTipoCliente(String valor) {
		if (valor.equalsIgnoreCase("Empresa")) {
			lblApellidos.setVisible(false);
			txtApellidos.setVisible(false);
			txtNombre.setPrefWidth(300);
			tipoCliente = 2;
		} else {
			lblApellidos.setVisible(true);
			txtApellidos.setVisible(true);
			txtNombre.setPrefWidth(100);
			tipoCliente = 1;
		}
	}

	private void comprobarComboTipoVehiculo(String valor) {
		tipoVehiculo = Utilidades.StringToTipoID(valor);
	}
}
