package com.myicpc.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Common object to all objects stored in database, which have given primary key
 * from other system and MyICPC uses then as primary key
 *
 * @author Roman Smetana
 */
@MappedSuperclass
public abstract class IdObject extends EntityObject {
    private static final long serialVersionUID = 2199682738849456677L;

    /**
     * Object primary key
     */
    @Id
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }
}
