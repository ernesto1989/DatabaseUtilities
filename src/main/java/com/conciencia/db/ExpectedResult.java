package com.conciencia.db;

import java.sql.ResultSet;


/**
 * Interfase que permite definir como mapear un result set de un query ejecutado
 * @author Ernesto Cantú
 * 07 Enero 2018
 */
public interface ExpectedResult {
   
    public void mapResult(ResultSet rs);
}
