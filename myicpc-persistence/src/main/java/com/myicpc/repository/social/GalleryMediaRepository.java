package com.myicpc.repository.social;

import com.myicpc.enums.GalleryMediaType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.GalleryMedia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface GalleryMediaRepository extends JpaRepository<GalleryMedia, Long> {
    List<GalleryMedia> findByContest(Contest contest);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.galleryMediaType IN ?1 AND gm.contest = ?2 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypesAndContest(List<GalleryMediaType> types, Contest contest, Pageable pageable);

    // ----

    List<GalleryMedia> findByUsernameAndGalleryMediaType(String username, GalleryMediaType galleryMediaType);

    GalleryMedia findByMediaIdAndGalleryMediaType(String mediaId, GalleryMediaType galleryMediaType);

    /**
     * @return gallery records which contains the first or second hashtag and
     * are type of gallery media type
     */
    @Query("SELECT gm FROM GalleryMedia gm WHERE (gm.description LIKE ?1 OR gm.description LIKE ?2) AND gm.galleryMediaType IN ?3")
    List<GalleryMedia> findByHashtags(String hashtag1, String hashtag2, List<GalleryMediaType> galleryMediaTypes);

    /**
     * @return gallery records which contains the first or second hashtag, are
     * type of gallery media type and have ID bigger than sinceId
     */
    @Query("SELECT gm FROM GalleryMedia gm WHERE (gm.description LIKE ?1 OR gm.description LIKE ?2) AND gm.galleryMediaType IN ?3 AND gm.id > ?4")
    List<GalleryMedia> findByHashtagsSinceId(String hashtag1, String hashtag2, List<GalleryMediaType> galleryMediaTypes, Long sinceId);

    @Query("SELECT MAX(gm.timestamp) FROM GalleryMedia gm WHERE gm.galleryMediaType = ?1")
    Date findLatestTimestampByMediaType(GalleryMediaType type);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.galleryMediaType = ?1 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(GalleryMediaType type1);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.galleryMediaType = ?1 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(GalleryMediaType type1, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.id < ?1 AND gm.galleryMediaType = ?2 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(Long lastId, GalleryMediaType type1, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.galleryMediaType = ?1 OR gm.galleryMediaType = ?2 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(GalleryMediaType type1, GalleryMediaType type2, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.id < ?1 AND (gm.galleryMediaType = ?2 OR gm.galleryMediaType = ?3) ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(Long lastId, GalleryMediaType type1, GalleryMediaType type2, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.galleryMediaType = ?1 OR gm.galleryMediaType = ?2 OR gm.galleryMediaType = ?3 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(GalleryMediaType type1, GalleryMediaType type2, GalleryMediaType type3, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.id < ?1 AND (gm.galleryMediaType = ?2 OR gm.galleryMediaType = ?3 OR gm.galleryMediaType = ?4) ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(Long lastId, GalleryMediaType type1, GalleryMediaType type2, GalleryMediaType type3, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.galleryMediaType IN ?1 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(List<GalleryMediaType> types, Pageable pageable);

    @Query("SELECT gm FROM GalleryMedia gm WHERE gm.id < ?1 AND gm.galleryMediaType IN ?2 ORDER BY gm.id DESC")
    List<GalleryMedia> findByGalleryMediaTypes(Long lastId, List<GalleryMediaType> types, Pageable pageable);
}
