package com.myicpc.service.utils.lists;

import com.myicpc.enums.GalleryMediaType;

import java.util.ArrayList;

/**
 * List of {@link GalleryMediaType} which allows fluent interface
 *
 * @author Roman Smetana
 */
public class MediaList extends ArrayList<GalleryMediaType> {
    private static final long serialVersionUID = 7109072774414505810L;

    public static MediaList newList() {
        return new MediaList();
    }

    public MediaList addInstagramImage() {
        this.add(GalleryMediaType.INSTAGRAM_IMAGE);
        return this;
    }

    public MediaList addInstagramVideo() {
        this.add(GalleryMediaType.INSTAGRAM_VIDEO);
        return this;
    }

    public MediaList addVine() {
        this.add(GalleryMediaType.VINE);
        return this;
    }

    public MediaList addYoutube() {
        this.add(GalleryMediaType.YOUTUBE_VIDEO);
        return this;
    }

    public MediaList addGallery() {
        this.add(GalleryMediaType.GALLERY);
        return this;
    }
}
