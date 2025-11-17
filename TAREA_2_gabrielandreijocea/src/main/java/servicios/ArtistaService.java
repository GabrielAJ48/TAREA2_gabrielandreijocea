package servicios;

import java.util.List;
import dao.ArtistaDAO;

public class ArtistaService {

    private ArtistaDAO artDAO = new ArtistaDAO();

    public String registrarArtista(long persona_id, String apodo, List<String> habilidades) {

        if (habilidades == null || habilidades.isEmpty()) {
            return "Debe introducir al menos una habilidad.";
        }

        boolean okArt = artDAO.insertarArtista(persona_id, apodo);

        if (!okArt) {
            return "Error al registrar al artista.";
        }

        for (String hab : habilidades) {
            boolean ok = artDAO.insertarEspecialidad(persona_id, hab);
            if (!ok) {
                return "Error al insertar la habilidad: " + hab;
            }
        }

        return null;
    }
}