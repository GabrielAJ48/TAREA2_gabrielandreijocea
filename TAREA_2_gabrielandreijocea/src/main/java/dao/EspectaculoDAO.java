package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entidades.Espectaculo;

public class EspectaculoDAO {

    private Connection conex;
    private PreparedStatement p;
    private ResultSet rs;

    public EspectaculoDAO() {
        conex = ConexionBD.getConexion();
    }

    public List<Espectaculo> obtenerTodos() {
        List<Espectaculo> lista = new ArrayList<>();

        String consulta = "SELECT espectaculo_id, nombre, fecha_inicio, fecha_fin FROM espectaculo";

        try {
            p = conex.prepareStatement(consulta);
            rs = p.executeQuery();

            while (rs.next()) {
                Espectaculo esp = new Espectaculo();

                esp.setId(rs.getLong("espectaculo_id"));
                esp.setNombre(rs.getString("nombre"));
                esp.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                esp.setFechaFin(rs.getDate("fecha_fin").toLocalDate());

                lista.add(esp);
            }

            rs.close();
            p.close();

        } catch (SQLException e) {
        	
        }

        return lista;
    }
}