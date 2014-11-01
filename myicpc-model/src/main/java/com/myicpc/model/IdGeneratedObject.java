package com.myicpc.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Common object to all objects stored in database, which have auto generated
 * primary key
 *
 * @author Roman Smetana
 */
@MappedSuperclass
public abstract class IdGeneratedObject extends EntityObject {
    private static final long serialVersionUID = 7895772817824754628L;

    /**
     * Object primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
