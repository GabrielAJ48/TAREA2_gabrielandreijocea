package servicios;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

import dao.EspectaculoDAO;
import dao.NumeroDAO;
import entidades.Artista;
import entidades.Espectaculo;
import entidades.Numero;

public class EspectaculoService {

    private EspectaculoDAO espDAO = new EspectaculoDAO();
    private NumeroDAO numDAO = new NumeroDAO();

    public List<Espectaculo> obtenerEspectaculos() {
        return espDAO.obtenerTodos();
    }
    
    public boolean comprobarFechasEspectaculo(String fechaini, String fechafin) {
        if (fechaini.isBlank() || fechafin.isBlank()) {
            System.out.println("Las fechas no pueden estar vacías.");
            return false;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio;
        LocalDate fechaFin;

        try {
            fechaInicio = LocalDate.parse(fechaini, formato);
            fechaFin = LocalDate.parse(fechafin, formato);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Asegúrese de usar dd/MM/yyyy y que sea una fecha real (ej: 29/02/2024).");
            return false;
        }

        if (fechaFin.isBefore(fechaInicio)) {
            System.out.println("La fecha de fin no puede ser anterior a la fecha de inicio.");
            return false;
        }

        LocalDate fechaMaxima = fechaInicio.plusYears(1);
        if (fechaFin.isAfter(fechaMaxima)) {
            System.out.println("El periodo del espectáculo no puede ser superior a 1 año.");
            return false;
        }
        
        return true;
    }
    
    public boolean comprobarNombreEspectaculo(String nombre) {
        
        if (nombre.length() > 25) {
            System.out.println("El nombre del espectáculo no puede ser superior a 25 caracteres");
            return false;
        }
        if (nombre == null || nombre.isBlank()) {
            System.out.println("El nombre no puede estar vacío");
            return false;
        }

        if (espDAO.existeNombreEsp(nombre)) {
            System.out.println("Ya existe un espectáculo con ese nombre asignado");
            return false;
        }
        
        return true;
    }

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

	public String registrarEspectaculoCompleto(Espectaculo nuevoEsp, List<Numero> listaNumeros) {
		if (listaNumeros == null || listaNumeros.size() < 3) {
            return "El espectáculo debe tener al menos 3 números.";
        }
        

        long idEsp = espDAO.insertarEspectaculo(nuevoEsp);
        if (idEsp == -1) {
            return "Error crítico al guardar el espectáculo en la Base de Datos.";
        }


        for (entidades.Numero n : listaNumeros) {
            n.setIdEspectaculo(idEsp);
            long idNum = numDAO.insertarNumero(n);
            
            if (idNum != -1) {
                for (entidades.Artista a : n.getArtistas()) {
                    numDAO.asignarArtistaANumero(idNum, idEsp);
                }
            } else {
                return "Error al guardar el número: " + n.getNombre();
            }
        }
        
        return null;
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
	
	public String modificarEspectaculo(Espectaculo e) {
	    String fIniStr = e.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	    String fFinStr = e.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	    
	    if (!comprobarFechasEspectaculo(fIniStr, fFinStr)) {
	        return "Las nuevas fechas no son válidas.";
	    }

	    boolean ok = espDAO.actualizarEspectaculo(e);
	    return ok ? null : "Error al actualizar en BD.";
	}

	public String modificarNumero(Numero n) {
	    String error = validarDatosNumero(n.getNombre(), n.getDuracion());
	    if (error != null) return error;
	    
	    boolean ok = numDAO.actualizarNumero(n);
	    return ok ? null : "Error al actualizar el número en BD.";
	}

	public Set<Numero> obtenerNumerosDeEspectaculo(long idEspectaculo) {
	    return numDAO.obtenerNumerosPorEspectaculo(idEspectaculo);
	}
	
	public Espectaculo obtenerDetalleEspectaculo(long id) {
        Espectaculo e = espDAO.obtenerEspectaculoCompleto(id);
        if (e != null) {
            e.setNumeros(numDAO.obtenerNumerosPorEspectaculo(id));
        }
        return e;
    }
}