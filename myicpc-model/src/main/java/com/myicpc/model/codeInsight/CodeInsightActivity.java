package com.myicpc.model.codeInsight;

import com.myicpc.model.IdGeneratedContestObject;
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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"externalId", "contestId"})})
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "CodeInsightActivity_id_seq")
public class CodeInsightActivity extends IdGeneratedContestObject {
    private static final long serialVersionUID = 5398398010855681655L;

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
    private String languageCode;
    /**
     * {@link Team} who submitted
     */
    @NotNull
    private Long teamId;
    /**
     * {@link Problem} the solution tries to solve
     */
    @NotNull
    private String problemCode;

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

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(final Long teamId) {
        this.teamId = teamId;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }
}
