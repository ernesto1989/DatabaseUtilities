package com.conciencia.test;

import com.conciencia.db.ExpectedResult;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ernesto Cantu
 */
public class ObjTest implements ExpectedResult {
    private int id;
    private String name;
    private int age;

    public ObjTest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void mapResult(ResultSet rs) {
        try {
            this.id = rs.getInt("Id");
            this.name = rs.getString("nombre");
            this.age = rs.getInt("edad");
        } catch (SQLException ex) {
            Logger.getLogger(ObjTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString() {
        return "ObjTest{" + "id=" + id + ", name=" + name + ", age=" + age + '}';
    }   
}
