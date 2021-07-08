package com.conciencia.db;

import com.conciencia.db.impl.SqliteUtilities;
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
    
    public DatabaseUtilities(){
    }
       
    protected abstract String getDbUrl();
    
    public abstract String getDriver();
    
    public List<T> executeQueryNoParams(String query,Class<T> clazz){
        ExpectedResult t;
        List<T> results = new ArrayList<>();
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                t = clazz.newInstance();
                t.mapResult(rs);
                results.add((T) t);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    public abstract void setParams(PreparedStatement stmt,Object...params); 
    
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DatabaseUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    public Long executeInsert(String query, Object...params){
        Long generatedId = null;
        try {
            Class.forName(getDriver());
            Connection conn = DriverManager.getConnection(getDbUrl());
            PreparedStatement stmt = conn.prepareStatement(query);
            setParams(stmt, params);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                generatedId = rs.getLong(1);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return generatedId;
    }
    
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return count;
    }
    
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return result.longValue();
    }
}
