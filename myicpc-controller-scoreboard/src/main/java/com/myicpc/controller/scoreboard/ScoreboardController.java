package com.myicpc.controller.scoreboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.controller.GeneralAbstractController;
import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.scoreboard.ScoreboardService;
import com.myicpc.service.scoreboard.problem.ProblemService;
import com.myicpc.service.scoreboard.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Controller
public class ScoreboardController extends GeneralController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ScoreboardService scoreboardService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemRepository problemRepository;

    @RequestMapping(value = {"/{contestCode}/scoreboard"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model, HttpSession session, HttpServletRequest request,
                             SitePreference sitePreference, @CookieValue(value = "followedTeams", required = false) String followedTeams) {
        Contest contest = getContest(contestCode, model);

        List<Problem> problems = problemService.findByContest(contest);

        model.addAttribute("teamJSON", scoreboardService.getTeamsFullTemplate(contest).toString());
        model.addAttribute("problems", problems);
        model.addAttribute("numProblems", problems.size());
        model.addAttribute("scoreboardAvailable", true);
        model.addAttribute("sideMenuActive", "scoreboard");
        return resolveView("scoreboard/scoreboard", "scoreboard/scoreboard_mobile", sitePreference);
    }

    @RequestMapping(value = {"/{contestCode}/scorebar"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("teamJSON", scoreboardService.getTeamsScorebarTemplate(contest).toString());
        model.addAttribute("problemCount", problemRepository.countByContest(contest));
        model.addAttribute("teamCount", teamRepository.countByContest(contest));
        return "scoreboard/scorebar";
    }

    @RequestMapping(value = {"/{contestCode}/map"}, method = RequestMethod.GET)
    public String map(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("teamJSON", scoreboardService.getTeamsFullTemplate(contest));
        model.addAttribute("problemJSON", scoreboardService.getProblemsJSON(contest));
        model.addAttribute("teamCoordinatesJSON", scoreboardService.getTeamMapCoordinates(contest).toString());
        model.addAttribute("problemCount", problemRepository.countByContest(contest));
        model.addAttribute("teamCount", teamRepository.countByContest(contest));
        // TODO
        model.addAttribute("mapConfigurations", "[{width: 700, ratio: 0.55, scale: 115, translate: [ 340,210 ], areas: {\"EU\": {\"scale\":220,\"translate\":[\"50\",\"420\"]}, \"AS\": {\"scale\":260,\"translate\":[\"-80\",\"310\"]}, \"AF\": {\"scale\":250,\"translate\":[\"300\",\"200\"]}, \"NA\" : {\"scale\":320,\"translate\":[\"950\",\"500\"]}, \"SA\" : {\"scale\":210,\"translate\":[\"500\",\"130\"]}, \"AU\" : {\"scale\":400,\"translate\":[\"-700\",\"-10\"]}}},{width: 900, scale: 145, translate: [ 440,360 ], areas: {\"EU\": {\"scale\":280,\"translate\":[\"50\",\"530\"]}, \"AS\": {\"scale\":325,\"translate\":[\"-80\",\"390\"]}, \"AF\": {\"scale\":310,\"translate\":[\"410\",\"250\"]}, \"NA\" : {\"scale\":370,\"translate\":[\"1200\",\"580\"]}, \"SA\" : {\"scale\":250,\"translate\":[\"600\",\"150\"]}, \"AU\" : {\"scale\":450,\"translate\":[\"-700\",\"-10\"]}}},{width: 1300, ratio: 0.6, scale: 210, translate: [ 620,480 ], circleSize: 8, areas: {\"EU\": {\"scale\":410,\"translate\":[\"80\",\"780\"]}, \"AS\": {\"scale\":450,\"translate\":[\"-80\",\"540\"]}, \"AF\": {\"scale\":420,\"translate\":[\"580\",\"340\"]}, \"NA\" : {\"scale\":500,\"translate\":[\"1550\",\"780\"]}, \"SA\" : {\"scale\":370,\"translate\":[\"900\",\"220\"]}, \"AU\" : {\"scale\":640,\"translate\":[\"-900\",\"-10\"]}}}]");
        model.addAttribute("teamCoordinates", "{\"239612\":{\"latitude\":-31.981229,\"longtitude\":115.819883,\"country\":\"AUS\"},\"239611\":{\"latitude\":-33.916921,\"longtitude\":151.232514,\"country\":\"AUS\"},\"239610\":{\"latitude\":-35.277669,\"longtitude\":149.118538,\"country\":\"AUS\"},\"240854\":{\"latitude\":40.443322,\"longtitude\":-79.943583,\"country\":\"USA\"},\"240855\":{\"latitude\":42.2683219,\"longtitude\":-83.7362956,\"country\":\"USA\"},\"240856\":{\"latitude\":38.9875,\"longtitude\":-76.94,\"country\":\"USA\"},\"240865\":{\"latitude\":33.775676,\"longtitude\":-84.395829,\"country\":\"USA\"},\"240513\":{\"latitude\":28.602439,\"longtitude\":-81.200062,\"country\":\"USA\"},\"240085\":{\"latitude\":43.470129,\"longtitude\":-80.535775,\"country\":\"CAN\"},\"240996\":{\"latitude\":44.461329,\"longtitude\":-93.155257,\"country\":\"USA\"},\"240514\":{\"latitude\":34.018839,\"longtitude\":-118.283211,\"country\":\"USA\"},\"240863\":{\"latitude\":37.43,\"longtitude\":-122.17,\"country\":\"USA\"},\"240857\":{\"latitude\":37.2283843,\"longtitude\":-80.4234167,\"country\":\"USA\"},\"240510\":{\"latitude\":37.8706401,\"longtitude\":-122.2797647,\"country\":\"USA\"},\"240862\":{\"latitude\":42.377094,\"longtitude\":-71.116483,\"country\":\"USA\"},\"240208\":{\"latitude\":42.0564594,\"longtitude\":-87.675267,\"country\":\"USA\"},\"240207\":{\"latitude\":40.157961,\"longtitude\":-76.987953,\"country\":\"USA\"},\"240859\":{\"latitude\":40.110539,\"longtitude\":-88.228411,\"country\":\"USA\"},\"240209\":{\"latitude\":43.0766,\"longtitude\":-89.412501,\"country\":\"USA\"},\"240861\":{\"latitude\":44.973982,\"longtitude\":-93.227752,\"country\":\"USA\"},\"241233\":{\"latitude\":42.453453,\"longtitude\":-76.473326,\"country\":\"USA\"},\"240511\":{\"latitude\":51.079978,\"longtitude\":-114.125551,\"country\":\"CAN\"},\"240860\":{\"latitude\":42.0266187,\"longtitude\":-93.6464654,\"country\":\"USA\"},\"241385\":{\"latitude\":34.068792,\"longtitude\":118.444543,\"country\":\"USA\"},\"240512\":{\"latitude\":29.717389,\"longtitude\":-95.401375,\"country\":\"USA\"},\"240858\":{\"latitude\":41.789633,\"longtitude\":-87.598847,\"country\":\"USA\"},\"240509\":{\"latitude\":40.344034,\"longtitude\":-74.651217,\"country\":\"USA\"},\"240219\":{\"latitude\":42.358873,\"longtitude\":-71.093834,\"country\":\"USA\"},\"239909\":{\"latitude\":-22.817222,\"longtitude\":-47.069444,\"country\":\"BRA\"},\"239903\":{\"latitude\":4.602149,\"longtitude\":-74.06618,\"country\":\"COL\"},\"239911\":{\"latitude\":-17.762118,\"longtitude\":-63.147033,\"country\":\"BOL\"},\"239899\":{\"latitude\":20.0330859,\"longtitude\":-75.811491,\"country\":\"CUB\"},\"239920\":{\"latitude\":25.65155,\"longtitude\":100.289181,\"country\":\"MEX\"},\"239917\":{\"latitude\":-32.959457,\"longtitude\":-60.628153,\"country\":\"ARG\"},\"239910\":{\"latitude\":-19.869154,\"longtitude\":-43.965885,\"country\":\"BRA\"},\"239906\":{\"latitude\":-22.954255,\"longtitude\":-43.174629,\"country\":\"BRA\"},\"239912\":{\"latitude\":-34.599722,\"longtitude\":-58.373056,\"country\":\"ARG\"},\"239902\":{\"latitude\":4.638224,\"longtitude\":-74.084352,\"country\":\"COL\"},\"239904\":{\"latitude\":-23.5613991,\"longtitude\":-46.7307891,\"country\":\"BRA\"},\"239900\":{\"latitude\":23.134303,\"longtitude\":-82.382033,\"country\":\"CUB\"},\"239905\":{\"latitude\":-6.7607108,\"longtitude\":-38.2291272,\"country\":\"BRA\"},\"239916\":{\"latitude\":-16.3897524,\"longtitude\":-71.5356732,\"country\":\"PER\"},\"239914\":{\"latitude\":19.505,\"longtitude\":-99.146667,\"country\":\"MEX\"},\"239901\":{\"latitude\":10.408363,\"longtitude\":-66.875573,\"country\":\"VEN\"},\"239919\":{\"latitude\":21.9005134,\"longtitude\":-102.2957502,\"country\":\"MEX\"},\"241238\":{\"latitude\":44.435556,\"longtitude\":26.101117,\"country\":\"ROU\"},\"241228\":{\"latitude\":55.8107873,\"longtitude\":37.5011242,\"country\":\"RUS\"},\"241219\":{\"latitude\":55.7023332,\"longtitude\":37.5269015,\"country\":\"RUS\"},\"241223\":{\"latitude\":60.0028372,\"longtitude\":30.3738928,\"country\":\"RUS\"},\"241240\":{\"latitude\":41.389444,\"longtitude\":2.115833,\"country\":\"ESP\"},\"241246\":{\"latitude\":50.061113,\"longtitude\":19.933167,\"country\":\"POL\"},\"241218\":{\"latitude\":59.958512,\"longtitude\":30.3025046,\"country\":\"RUS\"},\"241250\":{\"latitude\":59.347313,\"longtitude\":18.073746,\"country\":\"SWE\"},\"241230\":{\"latitude\":55.7613085,\"longtitude\":37.63298,\"country\":\"RUS\"},\"241226\":{\"latitude\":55.7290315,\"longtitude\":37.6094869,\"country\":\"RUS\"},\"241244\":{\"latitude\":52.240278,\"longtitude\":21.019167,\"country\":\"POL\"},\"241249\":{\"latitude\":49.597863,\"longtitude\":11.005243,\"country\":\"DEU\"},\"241227\":{\"latitude\":41.806335,\"longtitude\":44.768794,\"country\":\"GEO\"},\"241245\":{\"latitude\":51.113611,\"longtitude\":17.033333,\"country\":\"POL\"},\"241232\":{\"latitude\":51.0912488,\"longtitude\":71.3969171,\"country\":\"KAZ\"},\"241237\":{\"latitude\":50.441903,\"longtitude\":30.511314,\"country\":\"UKR\"},\"241242\":{\"latitude\":45.8105703,\"longtitude\":15.9698015,\"country\":\"HRV\"},\"241241\":{\"latitude\":47.376417,\"longtitude\":8.548103,\"country\":\"CHE\"},\"241221\":{\"latitude\":51.5111782,\"longtitude\":46.0430318,\"country\":\"RUS\"},\"241224\":{\"latitude\":53.8932137,\"longtitude\":27.5493818,\"country\":\"BLR\"},\"241231\":{\"latitude\":55.7769432,\"longtitude\":50.5959215,\"country\":\"RUS\"},\"241225\":{\"latitude\":53.9177928,\"longtitude\":27.5948989,\"country\":\"BLR\"},\"241247\":{\"latitude\":50.087204,\"longtitude\":14.423827,\"country\":\"CZE\"},\"241248\":{\"latitude\":55.680191,\"longtitude\":12.572952,\"country\":\"DNK\"},\"241220\":{\"latitude\":55.9294425,\"longtitude\":37.518334,\"country\":\"RUS\"},\"241236\":{\"latitude\":49.840497,\"longtitude\":24.023277,\"country\":\"UKR\"},\"241239\":{\"latitude\":46.4875336,\"longtitude\":30.7312247,\"country\":\"UKR\"},\"241222\":{\"latitude\":59.9408684,\"longtitude\":30.3001095,\"country\":\"RUS\"},\"241229\":{\"latitude\":53.876692,\"longtitude\":27.605519,\"country\":\"BLR\"},\"241428\":{\"latitude\":40.0,\"longtitude\":116.326667,\"country\":\"CHN\"},\"241473\":{\"latitude\":39.989722,\"longtitude\":116.305278,\"country\":\"CHN\"},\"241446\":{\"latitude\":23.158078,\"longtitude\":113.351187,\"country\":\"CHN\"},\"241449\":{\"latitude\":39.962205,\"longtitude\":116.365597,\"country\":\"CHN\"},\"241450\":{\"latitude\":23.15137,\"longtitude\":113.344656,\"country\":\"CHN\"},\"241442\":{\"latitude\":31.835194,\"longtitude\":117.251895,\"country\":\"CHN\"},\"241632\":{\"latitude\":35.7328,\"longtitude\":51.3889,\"country\":\"IRN\"},\"241433\":{\"latitude\":31.200833,\"longtitude\":121.429722,\"country\":\"CHN\"},\"241624\":{\"latitude\":-6.8609707,\"longtitude\":107.5929206,\"country\":\"IDN\"},\"241439\":{\"latitude\":30.31022,\"longtitude\":120.34414,\"country\":\"CHN\"},\"241557\":{\"latitude\":23.8815,\"longtitude\":90.267219,\"country\":\"BGD\"},\"241472\":{\"latitude\":30.675111,\"longtitude\":104.100356,\"country\":\"CHN\"},\"241628\":{\"latitude\":37.590799,\"longtitude\":127.0277773,\"country\":\"KOR\"},\"241621\":{\"latitude\":36.372,\"longtitude\":127.363,\"country\":\"KOR\"},\"241445\":{\"latitude\":30.582574,\"longtitude\":114.262274,\"country\":\"CHN\"},\"241443\":{\"latitude\":39.952159,\"longtitude\":116.343407,\"country\":\"CHN\"},\"241577\":{\"latitude\":35.798806,\"longtitude\":51.394928,\"country\":\"IRN\"},\"241623\":{\"latitude\":36.1114386,\"longtitude\":140.1040355,\"country\":\"JPN\"},\"241558\":{\"latitude\":17.4456,\"longtitude\":78.3497,\"country\":\"IND\"},\"241554\":{\"latitude\":28.543665,\"longtitude\":77.192292,\"country\":\"IND\"},\"241618\":{\"latitude\":1.342784,\"longtitude\":103.68143,\"country\":\"SGP\"},\"241627\":{\"latitude\":35.713429,\"longtitude\":139.762305,\"country\":\"JPN\"},\"241625\":{\"latitude\":1.295556,\"longtitude\":103.776667,\"country\":\"SGP\"},\"241635\":{\"latitude\":30.313544,\"longtitude\":120.357521,\"country\":\"CHN\"},\"241441\":{\"latitude\":31.297287,\"longtitude\":121.503676,\"country\":\"CHN\"},\"241626\":{\"latitude\":25.016,\"longtitude\":121.536,\"country\":\"TWN\"},\"241634\":{\"latitude\":22.314892,\"longtitude\":87.310676,\"country\":\"IND\"},\"241440\":{\"latitude\":30.26345,\"longtitude\":120.124993,\"country\":\"CHN\"},\"241553\":{\"latitude\":12.9911563,\"longtitude\":80.2363137,\"country\":\"IND\"},\"241437\":{\"latitude\":31.320893,\"longtitude\":121.388973,\"country\":\"CHN\"},\"241435\":{\"latitude\":43.87374,\"longtitude\":125.34865,\"country\":\"CHN\"},\"241620\":{\"latitude\":37.459895,\"longtitude\":126.951867,\"country\":\"KOR\"},\"241619\":{\"latitude\":21.006648,\"longtitude\":105.504268,\"country\":\"VNM\"},\"241622\":{\"latitude\":35.0262444,\"longtitude\":135.7808218,\"country\":\"JPN\"},\"241436\":{\"latitude\":39.959655,\"longtitude\":116.316127,\"country\":\"CHN\"},\"241551\":{\"latitude\":29.865906,\"longtitude\":77.901017,\"country\":\"IND\"},\"241641\":{\"latitude\":9.093823,\"longtitude\":76.491635,\"country\":\"IND\"},\"241438\":{\"latitude\":39.982875,\"longtitude\":116.340932,\"country\":\"CHN\"},\"241556\":{\"latitude\":35.701797,\"longtitude\":51.351439,\"country\":\"IRN\"},\"241444\":{\"latitude\":28.234156,\"longtitude\":113.012445,\"country\":\"CHN\"},\"241552\":{\"latitude\":24.91205,\"longtitude\":91.832228,\"country\":\"BGD\"},\"241555\":{\"latitude\":31.832909,\"longtitude\":54.349958,\"country\":\"IRN\"},\"241448\":{\"latitude\":26.057751,\"longtitude\":119.198362,\"country\":\"CHN\"},\"241642\":{\"latitude\":19.131231,\"longtitude\":72.916484,\"country\":\"IND\"},\"239740\":{\"latitude\":35.521478,\"longtitude\":35.807334,\"country\":\"SYR\"},\"239739\":{\"latitude\":30.027326,\"longtitude\":31.207941,\"country\":\"EGY\"},\"239926\":{\"latitude\":-33.9575,\"longtitude\":18.460556,\"country\":\"ZAF\"},\"239737\":{\"latitude\":29.988203,\"longtitude\":31.441325,\"country\":\"EGY\"},\"239742\":{\"latitude\":33.539611,\"longtitude\":-5.105815,\"country\":\"MAR\"},\"239741\":{\"latitude\":31.647812,\"longtitude\":-8.011074,\"country\":\"MAR\"},\"239734\":{\"latitude\":31.26475,\"longtitude\":29.998799,\"country\":\"EGY\"},\"239738\":{\"latitude\":30.06416,\"longtitude\":31.279072,\"country\":\"EGY\"},\"239735\":{\"latitude\":30.019569,\"longtitude\":31.502687,\"country\":\"EGY\"},\"239736\":{\"latitude\":30.078336,\"longtitude\":31.284926,\"country\":\"EGY\"}}");
        return "scoreboard/worldMap";
    }
}
