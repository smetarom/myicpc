package com.myicpc.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Roman Smetana
 */
public abstract class EntityManagerService {
    /**
     * Entity manager
     */
    @PersistenceContext
    protected EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
}
