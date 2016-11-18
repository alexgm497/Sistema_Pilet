/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Mauricio
 */
public class Conexion {
    private DataSource ds;
    private Connection cn = null;

    public Connection getCn() throws NamingException, SQLException {
        ds = (DataSource) new InitialContext().lookup("jdbc/piletPool");
        cn = ds.getConnection();
        return cn;
    }
    
}
