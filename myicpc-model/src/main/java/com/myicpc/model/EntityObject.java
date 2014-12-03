package com.myicpc.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Common object to all objects stored in database
 *
 * @author Roman Smetana
 */
@MappedSuperclass
public abstract class EntityObject implements Serializable {
    private static final long serialVersionUID = -7776179125313830253L;

    /**
     * Optimistic locking mechanism
     */
    @Version
    protected Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public abstract Long getId();

    public abstract void setId(final Long id);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (getId() == null ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IdObject other = (IdObject) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }
}
