package servicios;

import java.time.LocalDate;
import java.util.Map;

import dao.CoordinadorDAO;
import entidades.Coordinacion;

public class CoordinadorService {

    private CoordinadorDAO coordDAO = new CoordinadorDAO();

    public String registrarCoordinador(long persona_id, boolean senior, LocalDate fecha) {

        if (senior && fecha == null) {
            return "Debe introducir una fecha válida para un coordinador senior.";
        }

        long ok = coordDAO.insertarCoordinador(persona_id, senior, fecha);

        if (ok == -1) {
            return "Error al insertar el coordinador en la base de datos.";
        }

        return null;
    }
    
    public Map<Long,String> obtenerEspectaculosQueCoordina(long personaId) {
        return coordDAO.getEspectaculosPorCoordinador(personaId);
    }

    public Map<Long,String> obtenerTodosEspectaculos() {
        return coordDAO.obtenerListaEspectaculosBasica();
    }

    public String asignarCoordinacion(long espectaculoId, long personaId) {
        boolean ok = coordDAO.asignarCoordinadorAEspectaculo(espectaculoId, personaId);
        return ok ? null : "Error al asignar coordinador al espectáculo.";
    }

    public String quitarCoordinacion(long espectaculoId) {
        boolean ok = coordDAO.quitarCoordinadorDeEspectaculo(espectaculoId);
        return ok ? null : "Error al quitar coordinador del espectáculo.";
    }

    public String marcarComoSenior(long personaId, LocalDate fechaSenior) {
        if (fechaSenior == null) return "Debe introducir una fecha para marcar como senior.";

        Coordinacion actual = coordDAO.obtenerCoordinacionPorPersonaId(personaId);
        if (actual == null) return "La persona no está registrada como coordinador en la BD.";

        if (actual.isSenior()) {
            return "El coordinador ya es senior; no se puede revertir.";
        }

        boolean updSenior = coordDAO.updateSenior(personaId, true);
        boolean updFecha = coordDAO.updateFechaSenior(personaId, fechaSenior);

        if (!updSenior || !updFecha) {
            return "Error al actualizar los datos de senior.";
        }

        return null;
    }
    
    public long obtenerIdCoord(long persona_id) {
    	return coordDAO.obtenerIdCoord(persona_id);
    }

	public Map<Long, String> obtenerListaCoordinadores() {
		return coordDAO.listaCoordinadores();
	}
}