package com.myicpc.model.social;

import com.myicpc.model.IdGeneratedContestObject;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 * Represents a Picasa gallery album
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "GalleryAlbum_id_seq")
public class GalleryAlbum extends IdGeneratedContestObject {
    private static final long serialVersionUID = 1286345731712688771L;

    /**
     * Album name
     */
    private String name;
    /**
     * Is album announced to public?
     */
    private boolean published;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
