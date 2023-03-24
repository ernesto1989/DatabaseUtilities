
package com.conciencia.db.impl;

import com.conciencia.db.DatabaseUtilities;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.conciencia.db.ExpectedResult;

/**
 * Clase que extiende a DatabaseUtilities que permite conectarse con SQLite
 * 
 * @author ernesto
 * 07 Enero 2018
 */
public class MSAccessUtilities<T extends ExpectedResult> extends DatabaseUtilities {
    
    private final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private String dbName;

    public MSAccessUtilities(String dbName) {
        this.dbName = dbName;
    }
    
    @Override
    public String getDriver(){
        return this.DRIVER;
    }
    
    @Override
    protected String getDbUrl() {
        String url = "jdbc:ucanaccess://" + this.dbName;
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
            Logger.getLogger(MSAccessUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
