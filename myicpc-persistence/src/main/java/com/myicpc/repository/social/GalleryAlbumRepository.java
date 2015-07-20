package com.myicpc.repository.social;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.GalleryAlbum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryAlbumRepository extends JpaRepository<GalleryAlbum, Long> {
    GalleryAlbum findByNameAndContest(String name, Contest contest);

    List<GalleryAlbum> findByContest(Contest contest);

    List<GalleryAlbum> findByContest(Contest contest, Sort sort);

    List<GalleryAlbum> findByContestAndPublished(Contest contest, boolean published, Sort sort);
}
