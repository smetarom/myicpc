package com.myicpc.service.settings;

import com.myicpc.service.dto.GlobalSettings;
import org.springframework.stereotype.Service;

/**
 * @author Roman Smetana
 */
@Service
public class GlobalSettingsService {

    public GlobalSettings getGlobalSettings() {
        String mapConfig = "[{width: 700, ratio: 0.55, scale: 115, translate: [ 340,210 ], areas: {'EU': {\"scale\":220,\"translate\":[\"50\",\"420\"]}, 'AS': {\"scale\":260,\"translate\":[\"-80\",\"310\"]}, 'AF': {\"scale\":250,\"translate\":[\"300\",\"200\"]}, 'NA' : {\"scale\":320,\"translate\":[\"950\",\"500\"]}, 'SA' : {\"scale\":210,\"translate\":[\"500\",\"130\"]}, 'AU' : {\"scale\":400,\"translate\":[\"-700\",\"-10\"]}}},{width: 900, scale: 145, translate: [ 440,360 ], areas: {'EU': {\"scale\":280,\"translate\":[\"50\",\"530\"]}, 'AS': {\"scale\":325,\"translate\":[\"-80\",\"390\"]}, 'AF': {\"scale\":310,\"translate\":[\"410\",\"250\"]}, 'NA' : {\"scale\":370,\"translate\":[\"1200\",\"580\"]}, 'SA' : {\"scale\":250,\"translate\":[\"600\",\"150\"]}, 'AU' : {\"scale\":450,\"translate\":[\"-700\",\"-10\"]}}},{width: 1300, ratio: 0.6, scale: 210, translate: [ 620,480 ], circleSize: 8, areas: {'EU': {\"scale\":410,\"translate\":[\"80\",\"780\"]}, 'AS': {\"scale\":450,\"translate\":[\"-80\",\"540\"]}, 'AF': {\"scale\":420,\"translate\":[\"580\",\"340\"]}, 'NA' : {\"scale\":500,\"translate\":[\"1550\",\"780\"]}, 'SA' : {\"scale\":370,\"translate\":[\"900\",\"220\"]}, 'AU' : {\"scale\":640,\"translate\":[\"-900\",\"-10\"]}}}];";
        GlobalSettings gs = new GlobalSettings(mapConfig);
        return gs;
    }
}
