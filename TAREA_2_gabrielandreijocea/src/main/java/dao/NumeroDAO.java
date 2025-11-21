package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import entidades.Artista;
import entidades.Numero;

public class NumeroDAO {

	private Connection conex;
    private PreparedStatement p;
    private ResultSet rs;

    public NumeroDAO() {
        conex = ConexionBD.getConexion();
    }

    public long insertarNumero(Numero n) {
        long id = -1;
        String sql = "INSERT INTO numero (nombre, duracion, orden, espectaculo_id) VALUES (?, ?, ?, ?)";
        
        try {
        	p = conex.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, n.getNombre());
            p.setDouble(2, n.getDuracion());
            p.setInt(3, n.getOrden());
            p.setLong(4, n.getIdEspectaculo());
            
            p.executeUpdate();
            
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
        } catch (SQLException e) {

        }
        return id;
    }

    public boolean asignarArtistaANumero(long numeroId, long artistaId) {
        String sql = "INSERT INTO numero_artista (numero_id, artista_id) VALUES (?, ?)";
        try {
        	PreparedStatement p = conex.prepareStatement(sql);
            p.setLong(1, numeroId);
            p.setLong(2, artistaId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {

            return false;
        }
    }

    public Set<Numero> obtenerNumerosPorEspectaculo(long espectaculoId) {
        Set<Numero> numeros = new HashSet<>();
        String sql = "SELECT * FROM numero WHERE espectaculo_id = ? ORDER BY orden ASC";
        
        try {
        	PreparedStatement p = conex.prepareStatement(sql);
            p.setLong(1, espectaculoId);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Numero n = new Numero();
                n.setId(rs.getLong("numero_id"));
                n.setNombre(rs.getString("nombre"));
                n.setDuracion(rs.getDouble("duracion"));
                n.setOrden(rs.getInt("orden"));
                n.setIdEspectaculo(espectaculoId);
                numeros.add(n);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numeros;
    }
    
    public Set<Artista> obtenerArtistasAsignados(long numeroId) {
        Set<Artista> artistas = new HashSet<>();
        String sql = "SELECT a.artista_id, a.apodo, p.nombre " +
                     "FROM artista a " +
                     "JOIN persona p ON a.persona_id = p.persona_id " +
                     "JOIN numero_artista na ON a.artista_id = na.artista_id " +
                     "WHERE na.numero_id = ?";
        try {
            p = conex.prepareStatement(sql);
            p.setLong(1, numeroId);
            rs = p.executeQuery();
            while (rs.next()) {
                Artista a = new Artista();
                a.setIdArt(rs.getLong("artista_id"));
                a.setApodo(rs.getString("apodo"));
                a.setNombre(rs.getString("nombre"));
                artistas.add(a);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistas;
    }

    public boolean eliminarArtistaDeNumero(long numeroId, long artistaId) {
        boolean ok = false;
        String sql = "DELETE FROM numero_artista WHERE numero_id = ? AND artista_id = ?";
        try {
            p = conex.prepareStatement(sql);
            p.setLong(1, numeroId);
            p.setLong(2, artistaId);
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) {
            ok = false;
        }
        return ok;
    }
    
    public boolean actualizarNumero(Numero n) {
        boolean ok = false;
        String sql = "UPDATE numero SET nombre = ?, duracion = ? WHERE numero_id = ?";
        try {
            p = conex.prepareStatement(sql);
            p.setString(1, n.getNombre());
            p.setDouble(2, n.getDuracion());
            p.setLong(3, n.getId());
            
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) {

        }
        return ok;
    }

}