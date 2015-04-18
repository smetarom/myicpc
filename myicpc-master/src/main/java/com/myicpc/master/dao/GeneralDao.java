package com.myicpc.master.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Roman Smetana
 */
public class GeneralDao {

    @PersistenceContext(name = "MasterMyICPC")
    protected EntityManager em;
}
