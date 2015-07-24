package com.myicpc.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * General class for all DAOs, which works with entity manager
 *
 * @author Roman Smetana
 */
public abstract class AbstractDao {
    /**
     * Entity manager
     */
    @PersistenceContext(name = "MyICPC")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
}
