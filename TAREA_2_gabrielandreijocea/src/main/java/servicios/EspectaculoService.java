package servicios;

import java.util.List;

import dao.EspectaculoDAO;
import entidades.Espectaculo;

public class EspectaculoService {

    private EspectaculoDAO espDAO = new EspectaculoDAO();

    public List<Espectaculo> obtenerEspectaculos() {
        return espDAO.obtenerTodos();
    }
}