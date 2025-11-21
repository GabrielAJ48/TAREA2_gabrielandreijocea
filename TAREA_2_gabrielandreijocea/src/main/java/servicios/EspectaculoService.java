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
    private NumeroService numServ = new NumeroService();

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

	public String registrarEspectaculoCompleto(Espectaculo nuevoEsp, List<Numero> listaNumeros) {
		if (listaNumeros == null || listaNumeros.size() < 3) {
            return "El espectáculo debe tener al menos 3 números.";
        }
        
		for (Numero n : listaNumeros) {
            String error = numServ.validarDatosNumero(n.getNombre(), n.getDuracion());
            if (error != null) return "Error en número '" + n.getNombre() + "': " + error;
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
                    numDAO.asignarArtistaANumero(idNum, a.getIdArt()); 
                }
            } else {
                return "Error al guardar el número: " + n.getNombre();
            }
        }
        
        return null;
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