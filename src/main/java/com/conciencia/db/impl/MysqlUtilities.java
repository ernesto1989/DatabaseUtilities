
package com.conciencia.db.impl;

import com.conciencia.db.DatabaseUtilities;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.conciencia.db.ExpectedResult;

/**
 * Clase que extiende a DatabaseUtilities que permite conectarse con MySQL
 * 
 * @author Ernesto Cantu
 * 11 diciembre 2018
 */
public class MysqlUtilities<T extends ExpectedResult> extends DatabaseUtilities {
    
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private String server;
    private String dbName;
    private String user;
    private String password;

    public MysqlUtilities(String server,String dbName, String user, String password) {
        this.server = server;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
    }
    
    @Override
    public String getDriver(){
        return this.DRIVER;
    }
    
    @Override
    protected String getDbUrl() {
        String url = "jdbc:mysql://" + this.server + "/"  + this.dbName 
                + "?user=" + this.user + "&password="+this.password;
        return url;
    }

    @Override
    public void setParams(PreparedStatement stmt, Object... params) {
        int i = 1;
        try {
            for(Object param:params){
                if(param instanceof String){
                    stmt.setString(i, (String)param);
                }if(param instanceof Integer){
                    stmt.setInt(i, (Integer)param);
                }if(param instanceof Long){
                    stmt.setInt(i, ((Long) param).intValue());
                }if(param instanceof Double){
                    stmt.setDouble(i, (Double)param);
                }
                i+=1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
