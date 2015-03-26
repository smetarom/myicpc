package com.myicpc.model.codeInsight;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Code edit activity, which captures team activity on a problem solution
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "CodeInsightActivity_id_seq")
public class CodeInsightActivity extends IdGeneratedObject {
    private static final long serialVersionUID = 5398398010855681655L;

    @Column(unique = true)
    private Long externalId;

    /**
     * Number of total number of lines of the code
     */
    private int lineCount;
    /**
     * Number of changed lines compare to previous submission
     */
    private int diffLineCount;
    /**
     * Size of the file in bytes
     */
    private long fileSize;
    /**
     * Time, when the submission was submitted
     */
    private int modifyTime;

    /**
     * {@link Language} of the solution
     */
    @ManyToOne
    @JoinColumn(name = "languageId")
    private Language language;
    /**
     * {@link Team} who submitted
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;
    /**
     * {@link Problem} the solution tries to solve
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "problemId")
    private Problem problem;

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(final Long externalId) {
        this.externalId = externalId;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(final int lineCount) {
        this.lineCount = lineCount;
    }

    public int getDiffLineCount() {
        return diffLineCount;
    }

    public void setDiffLineCount(final int diffLineCount) {
        this.diffLineCount = diffLineCount;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(final long fileSize) {
        this.fileSize = fileSize;
    }

    public int getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(final int modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(final Team team) {
        this.team = team;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(final Problem problem) {
        this.problem = problem;
    }
}
