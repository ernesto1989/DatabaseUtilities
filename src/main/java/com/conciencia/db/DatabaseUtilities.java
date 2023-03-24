package com.conciencia.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase abstracta que provee la definición genérica de operaciones a realizarse
 * en una base de datos.
 * 
 * Algunas operaciones de esta clase dependen directamente del DBMS, por lo cual
 * debe ser extendida por una clase que represente al DBMS.
 * 
 * @author ernesto
 * 07 Enero 2018
 */
public abstract class DatabaseUtilities <T extends ExpectedResult> {
    
    /**
     * Constructor de la clase. 
     * Vacío
     */
    public DatabaseUtilities(){
    }
       
    /**
     * Método que regresa la URL de conexión. Todos los manejadores de BD utilizan
     * el mísmo método para conectarse.
     * 
     * @return La URL de conexión a la bd ya preparada.
     */
    protected abstract String getDbUrl();
    
    /**
     * La clase de cada implementación JDBC particular.
     * @return la clase que contiene el driver.
     */
    public abstract String getDriver();
    
    /**
     * Método que ejecuta una consulta a la base de datos sín parámetros.
     * Regresa una lista de objetos ExpectedResult.
     * 
     * @param query el query a ejecutar
     * @param clazz la clase que implementa el expected result
     * @return Lista de objetos convertidos de Result Set a Expected Result.
     */
    public List<T> executeQueryNoParams(String query,Class<T> clazz){
        ExpectedResult t;
        List<T> results = new ArrayList<>();
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            //Para cada registro del resultset.
            while(rs.next()){
                t = clazz.newInstance(); //Creo una instancia del objeto ER
                t.mapResult(rs); //Envío el registro del result set a convertir
                results.add((T) t); //añado el objeto a la lista final
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    /**
     * Método que asigna una lista de parámetros al query enviado.
     * 
     * Se recibe un array de Objetos, los cuales deben ser casteados a cada 
     * clase particular
     * 
     * @param stmt
     * @param params 
     */
    public abstract void setParams(PreparedStatement stmt,Object...params); 
    
    /**
     * Método que recibe el query con su identificación de parámetros, la clase 
     * a convertir el Result Set y los parámetros en un ra
     * @param query
     * @param clazz
     * @param params
     * @return 
     */
    public List<T> executeQueryWithParams(String query, Class<T> clazz, Object... params){
        ExpectedResult t;
        List<T> results = new ArrayList<>();
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            PreparedStatement stmt = conn.prepareStatement(query);
            setParams(stmt, params);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                t = clazz.newInstance();
                t.mapResult(rs);
                results.add((T) t);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    /**
     * Método que ejecuta un insert y regresa el id autogenerado. 
     * 
     * @param query query a ejecutar el inser
     * @param params el id autogenerado
     * @return el id autogenerado en tipo Long
     */
    public Long executeInsert(String query, Object...params){
        Long generatedId = null;
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            setParams(stmt, params);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                generatedId = rs.getLong(1);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return generatedId;
    }
    
    /**
     * Método que ejecuta un Update sobre la base de datos y regresa la cantidad 
     * de registros actualizados.
     * 
     * @param query query update
     * @param params los parámetros del update
     * @return número de registros actualizados
     */
    public Integer executeInsertWithUpdateCount(String query, Object...params){
        Integer count = null;
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            PreparedStatement stmt = conn.prepareStatement(query);
            setParams(stmt, params);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            count = stmt.getUpdateCount();
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return count;
    }
    
    /**
     * Método que ejecuta un update sencillo sin retorno de valores
     * @param query
     * @param params
     * @return 
     */
    public Long executeUpdate(String query, Object...params){
        Integer result = 0;
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            PreparedStatement stmt = conn.prepareStatement(query);
            setParams(stmt, params);
            result = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return result.longValue();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return result.longValue();
    }
    
    //por implementar el Bulk Insert
}
