package com.myicpc.controller.scoreboard;

import com.google.gson.JsonArray;
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
        JsonArray teamsFullTemplate = scoreboardService.getTeamsFullTemplate(contest);
        model.addAttribute("teamJSON", teamsFullTemplate.toString());
        model.addAttribute("problems", problems);
        model.addAttribute("numProblems", problems.size());
        model.addAttribute("scoreboardAvailable", teamsFullTemplate.size() > 0);
        model.addAttribute("sideMenuActive", "scoreboard");
        return resolveView("scoreboard/scoreboard", "scoreboard/scoreboard_mobile", sitePreference);
    }

    @RequestMapping(value = {"/{contestCode}/scorebar"}, method = RequestMethod.GET)
    public String scoreboard(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);
        JsonArray teamsScorebarTemplate = scoreboardService.getTeamsScorebarTemplate(contest);

        model.addAttribute("teamJSON", teamsScorebarTemplate.toString());
        model.addAttribute("problemCount", problemRepository.countByContest(contest));
        model.addAttribute("teamCount", teamRepository.countByContest(contest));
        model.addAttribute("scorebarAvailable", teamsScorebarTemplate.size() > 0);
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
        model.addAttribute("teamCoordinates", "{\"173970\":{\"latitude\":51.537914,\"longtitude\":46.008679,\"country\":\"RUS\"},\"173946\":{\"latitude\":50.061113,\"longtitude\":19.933167,\"country\":\"POL\"},\"174213\":{\"latitude\":22.680955,\"longtitude\":75.879788,\"country\":\"IND\"},\"173971\":{\"latitude\":55.728189,\"longtitude\":37.609521,\"country\":\"RUS\"},\"174209\":{\"latitude\":29.865906,\"longtitude\":77.901017,\"country\":\"IND\"},\"172612\":{\"latitude\":51.079978,\"longtitude\":-114.125551,\"country\":\"CAN\"},\"174238\":{\"latitude\":45.746998,\"longtitude\":126.630692,\"country\":\"CHN\"},\"173941\":{\"latitude\":52.156071,\"longtitude\":4.486949,\"country\":\"NLD\"},\"172613\":{\"latitude\":40.8075,\"longtitude\":-73.961944,\"country\":\"USA\"},\"173987\":{\"latitude\":-12.019722,\"longtitude\":-77.048611,\"country\":\"PER\"},\"173309\":{\"latitude\":49.666667,\"longtitude\":-112.863889,\"country\":\"CAN\"},\"174199\":{\"latitude\":36.372,\"longtitude\":127.363,\"country\":\"KOR\"},\"173767\":{\"latitude\":49.261111,\"longtitude\":-123.253056,\"country\":\"CAN\"},\"174228\":{\"latitude\":22.284167,\"longtitude\":114.137778,\"country\":\"HKG\"},\"174203\":{\"latitude\":-6.889852,\"longtitude\":107.609968,\"country\":\"IDN\"},\"173778\":{\"latitude\":41.389444,\"longtitude\":2.115833,\"country\":\"ESP\"},\"174001\":{\"latitude\":40.110539,\"longtitude\":-88.228411,\"country\":\"USA\"},\"173520\":{\"latitude\":54.087903,\"longtitude\":12.133323,\"country\":\"DEU\"},\"173956\":{\"latitude\":59.9564,\"longtitude\":30.31,\"country\":\"RUS\"},\"174009\":{\"latitude\":29.988203,\"longtitude\":31.441325,\"country\":\"EGY\"},\"174225\":{\"latitude\":39.959655,\"longtitude\":116.316127,\"country\":\"CHN\"},\"174012\":{\"latitude\":35.521478,\"longtitude\":35.807334,\"country\":\"SYR\"},\"174208\":{\"latitude\":12.828186,\"longtitude\":80.219893,\"country\":\"IND\"},\"174008\":{\"latitude\":31.210606,\"longtitude\":29.913073,\"country\":\"EGY\"},\"174207\":{\"latitude\":19.131231,\"longtitude\":72.916484,\"country\":\"IND\"},\"174206\":{\"latitude\":35.656093,\"longtitude\":139.544074,\"country\":\"JPN\"},\"174221\":{\"latitude\":22.421343,\"longtitude\":114.20714,\"country\":\"HKG\"},\"174007\":{\"latitude\":30.027326,\"longtitude\":31.207941,\"country\":\"EGY\"},\"172614\":{\"latitude\":42.358873,\"longtitude\":-71.093834,\"country\":\"USA\"},\"172611\":{\"latitude\":-33.9575,\"longtitude\":18.460556,\"country\":\"ZAF\"},\"173518\":{\"latitude\":37.43,\"longtitude\":-122.17,\"country\":\"USA\"},\"173765\":{\"latitude\":41.806667,\"longtitude\":-72.252167,\"country\":\"USA\"},\"173948\":{\"latitude\":51.113611,\"longtitude\":17.033333,\"country\":\"POL\"},\"173947\":{\"latitude\":52.240278,\"longtitude\":21.019167,\"country\":\"POL\"},\"173986\":{\"latitude\":-34.599722,\"longtitude\":-58.373056,\"country\":\"ARG\"},\"173989\":{\"latitude\":4.637767,\"longtitude\":-74.083987,\"country\":\"COL\"},\"173996\":{\"latitude\":19.373816,\"longtitude\":-99.183702,\"country\":\"MEX\"},\"174231\":{\"latitude\":40.0,\"longtitude\":116.326667,\"country\":\"CHN\"},\"174004\":{\"latitude\":44.073889,\"longtitude\":-103.206111,\"country\":\"USA\"},\"173995\":{\"latitude\":19.505,\"longtitude\":-99.146667,\"country\":\"MEX\"},\"173984\":{\"latitude\":-19.871904,\"longtitude\":-43.966248,\"country\":\"BRA\"},\"174217\":{\"latitude\":23.091361,\"longtitude\":113.298013,\"country\":\"CHN\"},\"172854\":{\"latitude\":32.467,\"longtitude\":-94.727,\"country\":\"USA\"},\"173294\":{\"latitude\":40.443322,\"longtitude\":-79.943583,\"country\":\"USA\"},\"171935\":{\"latitude\":-33.887778,\"longtitude\":151.187222,\"country\":\"AUS\"},\"172610\":{\"latitude\":50.441903,\"longtitude\":30.511314,\"country\":\"UKR\"},\"173519\":{\"latitude\":48.153333,\"longtitude\":17.065833,\"country\":\"SVK\"},\"173945\":{\"latitude\":50.004444,\"longtitude\":36.228333,\"country\":\"UKR\"},\"173967\":{\"latitude\":53.35,\"longtitude\":83.783333,\"country\":\"RUS\"},\"173268\":{\"latitude\":38.9875,\"longtitude\":-76.94,\"country\":\"USA\"},\"173712\":{\"latitude\":29.64833,\"longtitude\":-82.34944,\"country\":\"USA\"},\"174235\":{\"latitude\":39.989722,\"longtitude\":116.305278,\"country\":\"CHN\"},\"173944\":{\"latitude\":44.435556,\"longtitude\":26.101117,\"country\":\"ROU\"},\"171936\":{\"latitude\":-33.916921,\"longtitude\":151.232514,\"country\":\"AUS\"},\"174010\":{\"latitude\":31.26475,\"longtitude\":29.998799,\"country\":\"EGY\"},\"173981\":{\"latitude\":-22.817222,\"longtitude\":-47.069444,\"country\":\"BRA\"},\"173993\":{\"latitude\":22.987346,\"longtitude\":-82.465804,\"country\":\"CUB\"},\"173990\":{\"latitude\":4.636944,\"longtitude\":-74.083889,\"country\":\"COL\"},\"173982\":{\"latitude\":51.761111,\"longtitude\":-1.253333,\"country\":\"BRA\"},\"173988\":{\"latitude\":-38.701389,\"longtitude\":-62.269444,\"country\":\"ARG\"},\"173992\":{\"latitude\":23.134303,\"longtitude\":-82.382033,\"country\":\"CUB\"},\"173985\":{\"latitude\":-23.55488,\"longtitude\":-46.729902,\"country\":\"BRA\"},\"173991\":{\"latitude\":6.199733,\"longtitude\":-75.578686,\"country\":\"COL\"},\"173980\":{\"latitude\":-23.210133,\"longtitude\":-45.876781,\"country\":\"BRA\"},\"173994\":{\"latitude\":-33.418791,\"longtitude\":-70.656193,\"country\":\"CHL\"},\"173943\":{\"latitude\":47.376417,\"longtitude\":8.548103,\"country\":\"CHE\"},\"173942\":{\"latitude\":48.150833,\"longtitude\":11.580278,\"country\":\"DEU\"},\"173983\":{\"latitude\":-22.007676,\"longtitude\":-47.89486,\"country\":\"BRA\"},\"173965\":{\"latitude\":53.21188,\"longtitude\":50.177671,\"country\":\"RUS\"},\"173955\":{\"latitude\":59.940929,\"longtitude\":30.298267,\"country\":\"RUS\"},\"173968\":{\"latitude\":54.898611,\"longtitude\":23.912222,\"country\":\"LTU\"},\"173966\":{\"latitude\":54.725,\"longtitude\":55.9422,\"country\":\"RUS\"},\"173973\":{\"latitude\":54.843611,\"longtitude\":83.093611,\"country\":\"RUS\"},\"173958\":{\"latitude\":53.893219,\"longtitude\":27.549321,\"country\":\"BLR\"},\"173972\":{\"latitude\":58.0075,\"longtitude\":56.1867,\"country\":\"RUS\"},\"174236\":{\"latitude\":25.016,\"longtitude\":121.536,\"country\":\"TWN\"},\"174216\":{\"latitude\":35.7328,\"longtitude\":51.3889,\"country\":\"IRN\"},\"174218\":{\"latitude\":23.15137,\"longtitude\":113.344656,\"country\":\"CHN\"},\"174201\":{\"latitude\":10.031907,\"longtitude\":105.768832,\"country\":\"VNM\"},\"174234\":{\"latitude\":39.108164,\"longtitude\":117.178813,\"country\":\"CHN\"},\"174229\":{\"latitude\":35.713429,\"longtitude\":139.762305,\"country\":\"JPN\"},\"174200\":{\"latitude\":37.588234,\"longtitude\":126.993603,\"country\":\"KOR\"},\"174204\":{\"latitude\":1.295556,\"longtitude\":103.776667,\"country\":\"SGP\"},\"174205\":{\"latitude\":35.60477,\"longtitude\":139.684204,\"country\":\"JPN\"},\"173959\":{\"latitude\":56.840346,\"longtitude\":60.616206,\"country\":\"RUS\"},\"173963\":{\"latitude\":56.862341,\"longtitude\":53.183655,\"country\":\"RUS\"},\"174214\":{\"latitude\":17.4456,\"longtitude\":78.3497,\"country\":\"IND\"},\"174233\":{\"latitude\":31.200833,\"longtitude\":121.429722,\"country\":\"CHN\"},\"174224\":{\"latitude\":43.862027,\"longtitude\":125.331441,\"country\":\"CHN\"},\"173961\":{\"latitude\":43.255948,\"longtitude\":76.942877,\"country\":\"KAZ\"},\"174226\":{\"latitude\":39.982875,\"longtitude\":116.340932,\"country\":\"CHN\"},\"174227\":{\"latitude\":31.297287,\"longtitude\":121.503676,\"country\":\"CHN\"},\"174202\":{\"latitude\":22.337453,\"longtitude\":114.262938,\"country\":\"HKG\"},\"173962\":{\"latitude\":55.929479,\"longtitude\":37.518329,\"country\":\"RUS\"},\"174212\":{\"latitude\":35.704403,\"longtitude\":51.409,\"country\":\"IRN\"},\"174219\":{\"latitude\":39.962813,\"longtitude\":116.358098,\"country\":\"CHN\"},\"173964\":{\"latitude\":60.0025,\"longtitude\":30.373611,\"country\":\"RUS\"},\"174211\":{\"latitude\":24.91205,\"longtitude\":91.832228,\"country\":\"BGD\"},\"174220\":{\"latitude\":30.582574,\"longtitude\":114.262274,\"country\":\"CHN\"},\"174222\":{\"latitude\":29.815076,\"longtitude\":121.572866,\"country\":\"CHN\"},\"174223\":{\"latitude\":39.966916,\"longtitude\":116.321219,\"country\":\"CHN\"},\"174237\":{\"latitude\":30.26345,\"longtitude\":120.124993,\"country\":\"CHN\"},\"174215\":{\"latitude\":35.701797,\"longtitude\":51.351439,\"country\":\"IRN\"},\"173960\":{\"latitude\":43.234995,\"longtitude\":76.909842,\"country\":\"KAZ\"},\"174210\":{\"latitude\":23.725606,\"longtitude\":90.392487,\"country\":\"BGD\"},\"173969\":{\"latitude\":55.808,\"longtitude\":37.503,\"country\":\"RUS\"},\"173266\":{\"latitude\":41.789633,\"longtitude\":-87.598847,\"country\":\"USA\"},\"173295\":{\"latitude\":34.018839,\"longtitude\":-118.283211,\"country\":\"USA\"},\"173314\":{\"latitude\":28.602439,\"longtitude\":-81.200062,\"country\":\"USA\"},\"173312\":{\"latitude\":44.973982,\"longtitude\":-93.227752,\"country\":\"USA\"},\"173766\":{\"latitude\":43.783556,\"longtitude\":-79.186401,\"country\":\"CAN\"},\"174000\":{\"latitude\":38.036623,\"longtitude\":-78.502974,\"country\":\"USA\"},\"173999\":{\"latitude\":43.470129,\"longtitude\":-80.535775,\"country\":\"CAN\"},\"174003\":{\"latitude\":43.0766,\"longtitude\":-89.412501,\"country\":\"USA\"},\"174002\":{\"latitude\":39.482166,\"longtitude\":-87.322343,\"country\":\"USA\"},\"174013\":{\"latitude\":49.805463,\"longtitude\":-97.139829,\"country\":\"CAN\"},\"174011\":{\"latitude\":30.019569,\"longtitude\":31.502687,\"country\":\"EGY\"},\"174232\":{\"latitude\":30.675111,\"longtitude\":104.100356,\"country\":\"CHN\"},\"174230\":{\"latitude\":1.342784,\"longtitude\":103.68143,\"country\":\"SGP\"},\"173957\":{\"latitude\":55.70305,\"longtitude\":37.53075,\"country\":\"RUS\"}}");
        return "scoreboard/worldMap";
    }
}
