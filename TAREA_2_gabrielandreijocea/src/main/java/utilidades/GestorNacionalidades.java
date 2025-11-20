package utilidades;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GestorNacionalidades {

	private static Map<String, String> mapaPaises = new HashMap<>();

	public static void cargarPaises(String ruta) {
		try {
			File xmlFile = new File(ruta);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList listaPaises = doc.getElementsByTagName("pais");
			for (int i = 0; i < listaPaises.getLength(); i++) {
				Node nodo = listaPaises.item(i);
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nodo;
					String id = e.getElementsByTagName("id").item(0).getTextContent().trim();
					String nombre = e.getElementsByTagName("nombre").item(0).getTextContent().trim();
					mapaPaises.put(id.toUpperCase(), nombre);
				}
			}
		} catch (Exception e) {
			
		}
	}

	public static boolean comprobarNacionalidad(String id) {
		if (id == null || id.isBlank()) {
			return false;
		}
		return mapaPaises.containsKey(id.trim().toUpperCase());
	}

	public static String obtenerNombreCompleto(String id) {
		if (id == null || id.isBlank()) {
			return "";
		}
		return mapaPaises.get(id.trim().toUpperCase());
	}

	public static Map<String, String> listaPaises() {
		return mapaPaises;
	}
}