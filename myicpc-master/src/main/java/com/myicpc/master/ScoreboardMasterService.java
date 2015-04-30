package com.myicpc.master;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Smetana
 */
public class ScoreboardMasterService extends GeneralMasterService {
    private static final Logger logger = LoggerFactory.getLogger(HATimerService.class);
    private static final String SCOREBOARD_LOOKUP = "global/master-myicpc/ScoreboardBean!com.myicpc.master.bean.IMasterBean";

    public static final ServiceName SCOREBOARD_SERVICE_NAME = ServiceName.JBOSS.append("myicpc", "master", "scoreboard");

    public ScoreboardMasterService(Value<ServerEnvironment> env) {
        super(env);
    }

    @Override
    protected String getServiceName() {
        return "ScoreboardMasterService";
    }

    @Override
    protected String getBeanLookup() {
        return SCOREBOARD_LOOKUP;
    }


}
