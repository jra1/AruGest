package GUI.Contabilidad;

import Modelo.Factura;
import Modelo.Material;
import Modelo.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class FacturaDataSource implements JRDataSource {

	// private Factura factura;
	// private List<Servicio> listaServicios = new ArrayList<Servicio>();
	private ObservableList<Material> listaMaterial = FXCollections.observableArrayList();

	private int indiceParticipanteActual = -1;

	public FacturaDataSource(Factura f, ObservableList<Servicio> servicios, ObservableList<Material> materiales) {
		// factura = f;
		// listaServicios = servicios;
		listaMaterial = materiales;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Object valor = null;

		if (jrField.getName().equals("material")) {
			valor = listaMaterial.get(indiceParticipanteActual).getNombre();
		} else if (jrField.getName().equals("precio")) {
			valor = listaMaterial.get(indiceParticipanteActual).getPreciounit();
		}
		return valor;
	}

	@Override
	public boolean next() throws JRException {
		return ++indiceParticipanteActual < listaMaterial.size();
	}

	public ObservableList<Material> getListaMaterial() {
		return listaMaterial;
	}

	public void setListaMaterial(ObservableList<Material> listaMaterial) {
		this.listaMaterial = listaMaterial;
	}

}
