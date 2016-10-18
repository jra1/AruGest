package GUI;

import Logica.Inicio;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class NuevaFacturaController {

	private Inicio main;
	
	//Datos Factura
	@FXML
	private CheckBox chckbxFactura;
	@FXML
	private TextField txtNumfactura;
	
	@FXML
	private CheckBox chckbxPresupuesto;
	@FXML
	private TextField txtNumPresupuesto;
	
	@FXML
	private CheckBox chckbxOrdenDeReparacion;
	@FXML
	private TextField txtNumOrden;
	
	@FXML
	private CheckBox chckbxResguardoDeposito;
	@FXML
	private TextField txtNumResguardo;
	
	@FXML
	private DatePicker txtFecha;
	
	//Datos cliente
	
	/*
	private JButton btnImprimir;
	private JTable tablaServicios;
	private JScrollPane scrollPane;
	private JLabel lblFecha;
	
	private Date fecha = new Date();
	private DateFormat df1 = DateFormat.getDateInstance(DateFormat.SHORT);
	private String s1 = df1.format(fecha);
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String s1 = sdf.format(cal.getTime());
	
	private JPanel panelFecha;
	private JPanel panelCliente;
	private JLabel lblNombre;
	private JTextField txtNombre;
	private JLabel lblDireccin;
	private JTextField txtDireccion;
	private JLabel lblPoblacin;
	private JTextField txtCiudad;
	private JLabel lblDni;
	private JTextField txtDni;
	private JLabel lblTelfFijo;
	private JTextField txtFijo;
	private JLabel lblTelfMovil;
	private JTextField txtMovil;
	private JSeparator separator;
	private JLabel lblMarca;
	private JLabel lblModelo;
	private JLabel lblMatrcula;
	private JLabel lblKms;
	private JTextField txtMarca;
	private JTextField txtModelo;
	private JTextField txtMatricula;
	private JTextField txtKms;
	private JComboBox combo;
	private JTextField txtServicio;
	private JLabel lblHoras;
	private JFormattedTextField txtHoras;
	private JLabel lblE;
	private JFormattedTextField txtE;
	private JButton btnAadir;
	private JScrollPane scrollPane_1;
	private JTable tablaMaterial;
	private JLabel lblIva;
	private JLabel lblManoDeObra;
	private JLabel lblMateriales;
	private JLabel lblOtrosCargosgrua;
	private JLabel lblSubtotal;
	private JLabel lblTotal;
	private JFormattedTextField txtManoObra;
	private JFormattedTextField  txtMateriales;
	private JFormattedTextField txtOtros;
	private JFormattedTextField txtSubtotal;
	private JFormattedTextField txtIva;
	private JFormattedTextField txtTotal;
	private JButton btnQuitarServ;
	private JButton btnQuitarMater;
	private ArrayList<Servicio> listaServicios = new ArrayList<Servicio>();
	private ArrayList<Material> listaMaterial = new ArrayList<Material>();
	private JLabel lblPrecioHora;
	private JFormattedTextField txtPrecioHora;
	private JButton btnActualizar;
	private JButton btnNuevaFactura;
	private JLabel lblutilizarSiempreComa;
	private JLabel label;
	private JFormattedTextField txtSumIva;
	private JTextField txtFechaEntrega;
	private JLabel lblFechaEntregaEn;
	private JCheckBox chckbxRepararDefectosOcultos;
	private JCheckBox chckbxPermisoParaRealizar;
	private JCheckBox chckbxNoDeseoRecoger;
	private JTextField txtPor;
	private JLabel lblMsDe;
	private JPanel botones;
	private JLabel label_1;
	*/
	public void setMainAPP(Inicio p){
	     main=p;
	}
}