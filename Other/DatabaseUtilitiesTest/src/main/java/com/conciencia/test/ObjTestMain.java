package com.conciencia.test;

import com.conciencia.db.DatabaseUtilities;
import com.conciencia.db.impl.MSAccessUtilities;
import java.util.List;

/**
 *
 * @author Ernesto Cantu
 */
public class ObjTestMain {
    
    public static void main(String[] args) {
        DatabaseUtilities<ObjTest> testConnection = new MSAccessUtilities<>("C:/Aleph5/AccessTest.accdb");
        List<ObjTest> result = testConnection.executeQueryNoParams("Select * from Test_table_01", ObjTest.class);
        for(ObjTest ot : result){
            System.out.println(ot);
        }
    }
}
