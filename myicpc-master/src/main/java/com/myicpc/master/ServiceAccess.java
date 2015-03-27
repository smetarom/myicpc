package com.myicpc.master;

import javax.ejb.Remote;

/**
 * Business interface to access the SingletonService via this EJB
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Remote
public interface ServiceAccess {
    /**
     * Provide the node name where the scheduler is started.
     *
     * @return name of the cluster node where the schedule timer is running
     */
    String getNodeNameOfTimerService();
}