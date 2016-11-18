/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;

import org.apache.log4j.*;

/**
 * La clase LOG4J se encarga de realizar bitácora de acciones realizadas en el sistema
 * @author AGAV Team
 * @version Prototipo 1
 */

import static javax.ws.rs.client.Entity.entity;
import org.apache.log4j.Logger;

/**
 *
 * @author AGAV Team
 */
public abstract class LOG4J<T> {
    private Class<T> entityClass;
    private Logger log;
    
     public LOG4J(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.log = Logger.getLogger(entityClass.getName());
    } 

    public Logger getLog() {
        return log;
    }
     
     
}