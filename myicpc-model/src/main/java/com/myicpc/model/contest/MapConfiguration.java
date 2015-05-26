package com.myicpc.model.contest;

import com.myicpc.model.IdGeneratedObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "MapConfiguration_id_seq")
public class MapConfiguration extends IdGeneratedObject {
    private static final long serialVersionUID = 2883711611800779397L;

    private boolean zoomableMap;
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String mapConfig;
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String teamCoordinates;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mapConfiguration")
    private Contest contest;

    public boolean isZoomableMap() {
        return zoomableMap;
    }

    public void setZoomableMap(boolean zoomableMap) {
        this.zoomableMap = zoomableMap;
    }

    public String getMapConfig() {
        return mapConfig;
    }

    public void setMapConfig(String mapConfig) {
        this.mapConfig = mapConfig;
    }

    public String getTeamCoordinates() {
        return teamCoordinates;
    }

    public void setTeamCoordinates(String universityCoordinates) {
        this.teamCoordinates = universityCoordinates;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

}
