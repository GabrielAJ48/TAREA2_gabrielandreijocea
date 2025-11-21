package servicios;

import java.util.Set;
import dao.NumeroDAO;
import entidades.Artista;
import entidades.Numero;

public class NumeroService {

    private NumeroDAO numDAO = new NumeroDAO();

    public String validarDatosNumero(String nombre, double duracion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del número no puede estar vacío.";
        }

        double resto = duracion % 1;
        if (resto != 0.0 && resto != 0.5) {
            return "La duración debe ser en minutos enteros o medios minutos (ej: 5,0 o 5,5).";
        }
        
        if (duracion <= 0) {
            return "La duración debe ser mayor a 0.";
        }
        return null; 
    }

    public String modificarNumero(Numero n) {
        String error = validarDatosNumero(n.getNombre(), n.getDuracion());
        if (error != null) return error;
        
        boolean ok = numDAO.actualizarNumero(n);
        return ok ? null : "Error al actualizar el número en BD.";
    }

    public Set<Artista> obtenerArtistasDelNumero(long numeroId) {
        return numDAO.obtenerArtistasAsignados(numeroId);
    }

    public String agregarArtistaANumeroExistente(long numeroId, long artistaId) {
        Set<Artista> actuales = numDAO.obtenerArtistasAsignados(numeroId);
        
        for (Artista a : actuales) {
            if (a.getIdArt() == artistaId) {
                return "El artista ya está asignado a este número.";
            }
        }
        
        boolean ok = numDAO.asignarArtistaANumero(numeroId, artistaId);
        return ok ? null : "Error al asignar artista en la BD.";
    }

    public String quitarArtistaDeNumero(long numeroId, long artistaId) {
        Set<Artista> actuales = numDAO.obtenerArtistasAsignados(numeroId);
        
        if (actuales.size() <= 1) {
            return "No se puede eliminar el artista. El número debe tener al menos un participante.";
        }
        
        boolean existe = actuales.stream().anyMatch(a -> a.getIdArt() == artistaId);
        if (!existe) {
            return "El artista seleccionado no pertenece a este número.";
        }

        boolean ok = numDAO.eliminarArtistaDeNumero(numeroId, artistaId);
        return ok ? null : "Error al eliminar artista de la BD.";
    }
}