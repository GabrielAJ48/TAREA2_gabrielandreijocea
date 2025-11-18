package servicios;

import java.time.LocalDate;

import dao.CoordinadorDAO;

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
}