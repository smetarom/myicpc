package com.myicpc.master.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Roman Smetana
 */
public class GeneralDao {

    @PersistenceContext(name = "MasterMyICPC")
    protected EntityManager em;

    public <T> T findOne(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    public <T> T save(T entity) {
        return em.merge(entity);
    }

    public void flush() {
        em.flush();
    }
}
