package com.myicpc.dto.eventFeed;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class TeamDTO implements Serializable {
    private static final long serialVersionUID = -860906903918517823L;

    private Long id;
    private Long externalId;
    private Integer rank;
    private String name;
    private String nationality;
    private Integer problemsSolved;
    private Integer totalTime;
    private Long universityId;
    private String universityName;
    private String universityShortName;
    private Long regionId;
    private String regionName;

    public TeamDTO(Long id, Long externalId, Integer rank, String name, String nationality, Integer problemsSolved, Integer totalTime, Long universityId, String universityName, String universityShortName, Long regionId, String regionName) {
        this.id = id;
        this.externalId = externalId;
        this.rank = rank;
        this.name = name;
        this.nationality = nationality;
        this.problemsSolved = problemsSolved;
        this.totalTime = totalTime;
        this.universityId = universityId;
        this.universityName = universityName;
        this.universityShortName = universityShortName;
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public Long getId() {
        return id;
    }

    public Long getExternalId() {
        return externalId;
    }

    public Integer getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public Integer getProblemsSolved() {
        return problemsSolved;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public String getUniversityShortName() {
        return universityShortName;
    }

    public Long getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }
}

