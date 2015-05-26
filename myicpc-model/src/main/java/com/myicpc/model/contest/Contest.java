package com.myicpc.model.contest;

import com.myicpc.model.IdGeneratedObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Contest for which MyICPC runs
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Contest_id_seq")
public class Contest extends IdGeneratedObject {
    private static final long serialVersionUID = 4939779370882579754L;

    /**
     * Name of the contest
     */
    @Column(unique = true)
    @NotNull
    private String name;

    @Column(unique = true)
    @NotNull
    private String shortName;

    @Column(unique = true)
    @NotNull
    private String code;

    private Long externalId;

    private String hashtag;

    /**
     * Start date time of the contest
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd H:mm:ss")
    private Date startTime;
    /**
     * Length of the contest in seconds
     */
    private int length;
    /**
     * Time penalty in minutes for failed submission
     */
    private double penalty;
    /**
     * Represents the settings for the contest
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contestSettingsId")
    private ContestSettings contestSettings = new ContestSettings();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "webServiceSettingsId")
    private WebServiceSettings webServiceSettings = new WebServiceSettings();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mapConfigurationId")
    private MapConfiguration mapConfiguration = new MapConfiguration();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "questConfigurationId")
    private QuestConfiguration questConfiguration = new QuestConfiguration();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "moduleConfigurationId")
    private ModuleConfiguration moduleConfiguration = new ModuleConfiguration();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(final double penalty) {
        this.penalty = penalty;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public ContestSettings getContestSettings() {
        return contestSettings;
    }

    public void setContestSettings(ContestSettings contestSettings) {
        this.contestSettings = contestSettings;
    }

    public WebServiceSettings getWebServiceSettings() {
        return webServiceSettings;
    }

    public void setWebServiceSettings(WebServiceSettings webServiceSettings) {
        this.webServiceSettings = webServiceSettings;
    }

    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    public void setMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    public QuestConfiguration getQuestConfiguration() {
        return questConfiguration;
    }

    public void setQuestConfiguration(QuestConfiguration questConfiguration) {
        this.questConfiguration = questConfiguration;
    }

    public ModuleConfiguration getModuleConfiguration() {
        return moduleConfiguration;
    }

    public void setModuleConfiguration(ModuleConfiguration moduleConfiguration) {
        this.moduleConfiguration = moduleConfiguration;
    }
}
