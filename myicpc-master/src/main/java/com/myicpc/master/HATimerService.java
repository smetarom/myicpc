package com.myicpc.master;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 * @author <a href="mailto:ralf.battenfeld@bluewin.ch">Ralf Battenfeld</a>
 */
public class HATimerService extends GeneralMasterService {
    private static final Logger logger = LoggerFactory.getLogger(HATimerService.class);
    private static final String SCHEDULER_LOOKUP = "global/master-myicpc/SocialSchedulerBean!com.myicpc.master.bean.IMasterBean";

    public static final ServiceName SOCIAL_SERVICE_NAME = ServiceName.JBOSS.append("myicpc", "master", "scheduler");

    public HATimerService(Value<ServerEnvironment> env) {
        super(env);
    }

    @Override
    protected String getServiceName() {
        return "HATimerService";
    }

    @Override
    protected String getBeanLookup() {
        return SCHEDULER_LOOKUP;
    }


}
