package com.myicpc.service.scoreboard.team;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.TextUtils;
import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.eventFeed.TeamRankHistory;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.AttendedContest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.ContestParticipantAssociation;
import com.myicpc.model.teamInfo.RegionalResult;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.University;
import com.myicpc.repository.eventFeed.LastTeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRankHistoryRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.repository.teamInfo.AttendedContestRepository;
import com.myicpc.repository.teamInfo.ContestParticipantAssociationRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.repository.teamInfo.UniversityRepository;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.scoreboard.dto.SubmissionDTO;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class TeamService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TeamService.class);

    @Autowired
    private TeamWSService teamWSService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    @Autowired
    private ContestParticipantAssociationRepository contestParticipantAssociationRepository;

    @Autowired
    private AttendedContestRepository attendedContestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private LastTeamProblemRepository lastTeamProblemRepository;

    @Autowired
    private TeamRankHistoryRepository teamRankHistoryRepository;

    /**
     * Synchronize team and university info via web services
     *
     * @throws WebServiceException communication with WS failed
     */
    public void synchronizeTeamsWithCM(Contest contest) throws WebServiceException, ValidationException {
        try {
            synchronizeUniversities(teamWSService.getUniversitiesFromCM(contest));
            synchronizeTeams(teamWSService.getTeamsFromCM(contest), contest);
            logger.info("Team & university synchronization finished.");
            initTeamHashtagsAndAbbreviations(contest);
            logger.info("Team hashtags and abbreviations synchronized.");
        } catch (ValidationException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error parsing synchornization files", ex);
            throw new ValidationException("Error during response parsing. Check the Contest Management web service settings.", ex);
        }
    }

    /**
     * Synchronize team and university info via file upload
     *
     * @param universityJSON file with university info
     * @param teamJSON       file with team info
     * @throws IOException parsing uploaded files failed
     */
    public void synchronizeTeamsFromFile(final MultipartFile universityJSON, final MultipartFile teamJSON, final Contest contest) throws IOException,
            ValidationException {
        try (InputStream universityInputStream = universityJSON.getInputStream(); InputStream teamInputStream = teamJSON.getInputStream()) {
            String universityString = IOUtils.toString(universityInputStream, TextUtils.DEFAULT_ENCODING);
            String teamString = IOUtils.toString(teamInputStream, TextUtils.DEFAULT_ENCODING);

            synchronizeUniversities(universityString);
            synchronizeTeams(teamString, contest);
            logger.info("Team & university synchronization finished.");
            initTeamHashtagsAndAbbreviations(contest);
            logger.info("Team hashtags and abbreviations synchronized.");
        }
    }

    /**
     * Synchronize universities based on given JSON data
     *
     * @param universityJSON university JSON data
     */
    private void synchronizeUniversities(String universityJSON) {
        try {
            // Parse string into JSON object and get JSON array of institutions
            JsonObject universityRoot = new JsonParser().parse(universityJSON).getAsJsonObject();
            JsonArray universitiesArray = universityRoot.getAsJsonArray("institutions");
            // Iterate through all JSON representations of universities
            for (JsonElement jsonElement : universitiesArray) {
                // Tries to find an university by externalId in the database and
                // if the university is not found, it returns a new university
                JSONAdapter universityAdapter = new JSONAdapter(jsonElement);
                Long externalId = universityAdapter.getLong("institutionId");
                University university = universityRepository.findByExternalId(externalId);
                if (university == null) {
                    university = new University();
                }
                // Get data from JSON and set them to university object. and
                // save the university after that
                university.setExternalId(externalId);
                university.setName(universityAdapter.getString("name"));
                university.setShortName(universityAdapter.getString("shortName"));
                university.setTwitterHash(universityAdapter.getString("twitterhash", university.getTwitterHash()));
                university.setHomepageURL(universityAdapter.getString("homepageurl", university.getHomepageURL()));
                university.setState(universityAdapter.getString("state", university.getState()));
                university.setCountry(universityAdapter.getString("country", university.getCountry()));

                universityRepository.save(university);
                logger.info("CM import: university " + university.getExternalId());

            }
        } catch (JsonParseException | IllegalStateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new ValidationException("University parsing failed. Check if it has valid JSON format", ex);
        }
    }

    /**
     * Synchronize teams based on given JSON data
     *
     * @param teamJSON team JSON data
     */
    private void synchronizeTeams(String teamJSON, Contest contest) {
        try {
            JsonObject teamRoot = new JsonParser().parse(teamJSON).getAsJsonObject();
            JsonArray teamArray = teamRoot.getAsJsonArray("teams");

            List<Team> teamsToSync = teamRepository.findByContest(contest);
            Map<Long, Team> externalIdTeamMap = new HashMap<>();
            for (Team team : teamsToSync) {
                externalIdTeamMap.put(team.getExternalId(), team);
            }

            // Iterate through all JSON representations of teams
            for (JsonElement teamJE : teamArray) {
                // Tries to find a team by externalReservationId in the database
                // and
                // if the team is not found, it returns a new team
                JsonObject root = teamJE.getAsJsonObject();
                JSONAdapter teamAdapter = new JSONAdapter(teamJE);
                Long externalId = teamAdapter.getLong("externalReservationId");
                TeamInfo teamInfo = teamInfoRepository.findByExternalIdAndContest(externalId, contest);
                if (teamInfo == null) {
                    teamInfo = new TeamInfo();
                }
                University university = universityRepository.findByExternalId(teamAdapter.getLong("institutionId"));
                teamInfo.setContest(contest);
                teamInfo.setUniversity(university);
                teamInfo.setExternalId(externalId);
                teamInfo.setName(root.get("name").getAsString());

                // set team results of the team
                teamInfo.setRegionalResults(parseRegionalContests(teamInfo, teamAdapter));

                teamInfo = teamInfoRepository.save(teamInfo);

                if (externalIdTeamMap.containsKey(externalId)) {
                    Team team = externalIdTeamMap.get(externalId);
                    team.setTeamInfo(teamInfo);
                    teamRepository.save(team);
                }

                // process team members and assign them to the team
                parsePeople(teamInfo, teamAdapter);

                logger.info("CM import: team " + teamInfo.getExternalId());
            }
        } catch (JsonParseException | IllegalStateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new ValidationException("Team parsing failed. Check if it has valid JSON format", ex);
        }
    }

    private void initTeamHashtagsAndAbbreviations(Contest contest) {
        List<TeamInfo> teamInfos = teamInfoRepository.findByContest(contest);
        List<TeamInfo> teamInfosWithoutHashtag = new ArrayList<>();
        int count = 1;
        for (TeamInfo teamInfo : teamInfos) {
            if (contest.getContestSettings().isShowTeamNames()) {
                teamInfo.setShortName(TextUtils.getTeamShortName(teamInfo.getName()));
                teamInfo.setAbbreviation(teamInfo.getShortName());
                if (StringUtils.isEmpty(teamInfo.getHashtag())) {
                    teamInfosWithoutHashtag.add(teamInfo);
                } else {
                    count++;
                }
            } else {
                University university = teamInfo.getUniversity();
                if (university != null) {
                    teamInfo.setShortName(university.getShortName());
                    teamInfo.setAbbreviation(university.getShortName());
                    teamInfo.setHashtag(TextUtils.clearHashtag(university.getTwitterHash()));
                }
            }
        }
        for (TeamInfo teamInfo : teamInfosWithoutHashtag) {
            teamInfo.setHashtag("team" + (count++));
        }
    }

    /**
     * Parses regional contests where the team participates
     *
     * @param teamInfo    team
     * @param teamAdapter JSON representation
     * @return list of regional contests
     */
    private List<RegionalResult> parseRegionalContests(TeamInfo teamInfo, JSONAdapter teamAdapter) {
        List<RegionalResult> results = new ArrayList<>();
        JsonArray array = teamAdapter.getJsonArray("results");
        // Iterate through all JSON representations of regional results
        for (JsonElement jsonElement : array) {
            JSONAdapter resultAdapter = new JSONAdapter(jsonElement);
            RegionalResult result = teamInfoRepository.findRegionalResultByContestIdAndTeamInfoExternalId(resultAdapter.getLong("externalContestId"),
                    teamAdapter.getLong("externalReservationId"));
            if (result == null) {
                result = new RegionalResult();
            }
            result.setTeamInfo(teamInfo);
            result.setContestId(resultAdapter.getLong("externalContestId"));
            result.setContestName(resultAdapter.getString("contestName"));
            result.setTeamName(resultAdapter.getString("teamName"));
            result.setRank(resultAdapter.getInteger("rank"));
            result.setProblemSolved(resultAdapter.getInteger("problemssolved"));
            result.setTotalTime(resultAdapter.getInteger("totaltime"));
            result.setLastProblemSolved(resultAdapter.getInteger("lastproblemtime"));
            results.add(result);
        }
        return results;
    }

    /**
     * Parses people from JSON
     *
     * @param teamInfo    team
     * @param teamAdapter team JSON representation
     */
    private void parsePeople(TeamInfo teamInfo, JSONAdapter teamAdapter) {
        JsonArray persons = teamAdapter.getJsonArray("persons");
        // Iterate through all JSON representations of team members
        for (JsonElement jsonElement : persons) {
            // JsonObject personObject = jsonElement.getAsJsonObject();
            JSONAdapter personAdapter = new JSONAdapter(jsonElement);

            Long externalId = personAdapter.getLong("personId");
            ContestParticipant member = parsePerson(personAdapter);

            // create team role
            ContestParticipantAssociation association = contestParticipantAssociationRepository.findByTeamInfoAndContestParticipant(teamInfo, member);
            if (association == null) {
                association = new ContestParticipantAssociation();
                association.setTeamInfo(teamInfo);
                association.setContestParticipant(member);
            }
            association.setContestParticipantRole(ContestParticipantRole.getTeamRoleByCode(personAdapter.getString("role")));
            contestParticipantAssociationRepository.save(association);

            JsonArray contests = personAdapter.getJsonArray("contests");
            // Process each attended contest record
            for (JsonElement je : contests) {
                // JsonObject contestObject = je.getAsJsonObject();
                JSONAdapter contestAdapter = new JSONAdapter(je);
                Long contestExternalId = contestAdapter.getLong("externalContestId");
                AttendedContest contest = attendedContestRepository.findByExternalIdAndContestParticipantExternalId(contestExternalId, externalId);
                if (contest == null) {
                    contest = new AttendedContest();
                }
                contest.setContestParticipant(member);
                contest.setExternalId(contestExternalId);
                contest.setName(contestAdapter.getString("contestName"));
                contest.setHomepageURL(contestAdapter.getString("homepageUrl"));
                contest.setYear(contestAdapter.getInteger("year"));
                contest.setContestParticipantRole(ContestParticipantRole.getTeamRoleByCode(contestAdapter.getString("role")));
                attendedContestRepository.save(contest);
            }

        }
    }

    /**
     * Parse person from JSON representation
     *
     * @param personAdapter person JSON representation
     * @return parsed person
     */
    private ContestParticipant parsePerson(JSONAdapter personAdapter) {
        Long externalId = personAdapter.getLong("personId");
        ContestParticipant contestParticipant = contestParticipantRepository.findByExternalId(externalId);
        if (contestParticipant == null) {
            contestParticipant = new ContestParticipant();
        }
        contestParticipant.setExternalId(externalId);
        contestParticipant.setFirstname(personAdapter.getString("firstname"));
        contestParticipant.setLastname(personAdapter.getString("lastname"));
        contestParticipant.setProfilePictureUrl(teamWSService.getContestParticipantProfileUrl(personAdapter.getString("publicURLKey")));

        return contestParticipantRepository.save(contestParticipant);
    }

    /**
     * Synchronize staff info via web services
     *
     * @throws WebServiceException communication with WS failed
     */
    public void synchronizeStaffMembersWithCM(Contest contest) throws WebServiceException {
        try {
            String json = teamWSService.getStaffMembersFromCM(contest);
            JsonObject root = new JsonParser().parse(json).getAsJsonObject();
            JSONAdapter peopleAdapter = new JSONAdapter(root);
            // Iterate through all JSON representations of staff members
            for (JsonElement e : peopleAdapter.getJsonArray("persons")) {
                JSONAdapter personAdapter = new JSONAdapter(e);

                ContestParticipant contestParticipant = parsePerson(personAdapter);

                if (!contestParticipant.isStaffMember()) {
                    ContestParticipantAssociation association = new ContestParticipantAssociation();
                    association.setContestParticipant(contestParticipant);
                    association.setContestParticipantRole(ContestParticipantRole.STAFF);
                    contestParticipantAssociationRepository.save(association);
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new ValidationException("Error with web service communication.", ex);
        }
    }

    /**
     * Synchronize social info (social accounts, etc.) info via web services
     *
     * @throws WebServiceException communication with WS failed
     */
    public void synchronizeSocialInfosWithCM(Contest contest) throws WebServiceException {
        try {
            String json = teamWSService.getSocialInfosFromCM(contest);
            processSocialInfos(json);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * Parses social info (social networks accounts, etc.) info
     *
     * @param socialInfosJson JSON data
     */
    public void processSocialInfos(String socialInfosJson) {
        JsonArray arr = new JsonParser().parse(socialInfosJson).getAsJsonArray();
        // Iterate through all JSON representations of social network usernames
        for (JsonElement e : arr) {
            JSONAdapter infoAdapter = new JSONAdapter(e);
            ContestParticipant contestParticipant = contestParticipantRepository.findByExternalId(infoAdapter.getLong("externalPersonId"));
            if (contestParticipant != null) {
                contestParticipant.setTwitterUsername(infoAdapter.getString("twitterUsername", contestParticipant.getTwitterUsername(), false));
                contestParticipant.setVineUsername(infoAdapter.getString("vineUsername", contestParticipant.getVineUsername(), false));
                contestParticipant.setInstagramUsername(infoAdapter.getString("instagramUsername", contestParticipant.getInstagramUsername(), false));
                contestParticipant.setLinkedinOauthToken(infoAdapter.getString("linkedinOauthToken"));
                contestParticipant.setLinkedinOauthSecret(infoAdapter.getString("linkedinOauthSecret"));
                contestParticipantRepository.save(contestParticipant);
            }
        }
    }

    public List<SubmissionDTO> getTeamSubmissionDTOs(final Team team) {
        List<Long> teamProblemIds = new ArrayList<>();
        Map<Long, TeamProblem> map = new HashMap<>();
        List<TeamProblem> teamProblems = teamProblemRepository.findByTeam(team);
        for (TeamProblem teamProblem : teamProblems) {
            teamProblemIds.add(teamProblem.getId());
            map.put(teamProblem.getId(), teamProblem);
        }

        List<Notification.NotificationType> expectedTypes = NotificationList.newList().addScoreboardSuccess().addScoreboardSubmitted().addScoreboardFailed();
        Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
        List<Notification> notifications = notificationRepository.findByEntityIdsAndTypes(teamProblemIds, expectedTypes, sort);
        List<SubmissionDTO> submissionDTOs = new ArrayList<>(notifications.size());
        for (Notification notification : notifications) {
            SubmissionDTO submissionDTO = new SubmissionDTO(notification);
            submissionDTO.setTeamProblem(map.get(notification.getEntityId()));
            int contestTime = Minutes.minutesBetween(new DateTime(team.getContest().getStartTime().getTime()), new DateTime(notification.getTimestamp().getTime())).getMinutes();
            submissionDTO.setContestTime(Math.abs(contestTime));
            submissionDTOs.add(submissionDTO);
        }

        return submissionDTOs;
    }

    /**
     * Get latest runs for a team
     *
     * @param team team
     * @return latest runs
     */
    public Map<Long, TeamProblem> getLatestTeamProblems(final Team team) {
        List<LastTeamProblem> list = lastTeamProblemRepository.findByTeam(team);
        Map<Long, TeamProblem> map = new HashMap<Long, TeamProblem>();
        for (LastTeamProblem lastTeamProblem : list) {
            map.put(lastTeamProblem.getProblem().getId(), lastTeamProblem.getTeamProblem());
        }
        return map;
    }

    public JsonArray getRankHistoryChartData(final Team team) {
        List<TeamRankHistory> history = teamRankHistoryRepository.findByTeam(team);
        JsonArray historyArray = new JsonArray();
        for (TeamRankHistory teamRankHistory : history) {
            JsonArray data = new JsonArray();
            data.add(new JsonPrimitive(teamRankHistory.getTimestamp().getTime()));
            data.add(new JsonPrimitive(teamRankHistory.getRank()));
            historyArray.add(data);
        }
        JsonObject item = new JsonObject();
        item.addProperty("Key", "Rank history");
        item.add("values", historyArray);

        JsonArray wrapper = new JsonArray();
        wrapper.add(item);
        return wrapper;
    }

}
