package com.myicpc.master;

import javax.ejb.Stateless;

import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;

/**
 * A simple SLSB to access the internal SingletonService.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class ServiceAccessBean implements ServiceAccess {
    private static final Logger LOGGER = Logger.getLogger(ServiceAccessBean.class);

    public String getNodeNameOfTimerService() {
        LOGGER.info("Method getNodeNameOfTimerService() is invoked");
        ServiceController<?> service = CurrentServiceContainer.getServiceContainer().getService(
                HATimerService.DEFAULT_SERVICE_NAME);

        // Example how to leverage JBoss Logging to do expensive String concatenation only when needed:
        LOGGER.debugf("Service: %s", service);

        if (service != null) {
            return ((Environment)service.getValue()).getNodeName();
        } else {
            throw new IllegalStateException("Service '" + HATimerService.DEFAULT_SERVICE_NAME + "' not found!");
        }
    }
}
