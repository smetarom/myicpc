package com.myicpc.master;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.singleton.SingletonServiceBuilderFactory;

/**
 *
 */
public class MasterServiceActivator implements ServiceActivator {

    private static final String CONTAINER_NAME = "server";
    private static final String CACHE_NAME = "default";

    @Override
    public void activate(ServiceActivatorContext context) {
        InjectedValue<ServerEnvironment> socialEnvironment = new InjectedValue<>();
        // TODO uncomment
        install(new HATimerService(socialEnvironment), HATimerService.SOCIAL_SERVICE_NAME, socialEnvironment, context);

        InjectedValue<ServerEnvironment> scoreboardEnvironment = new InjectedValue<>();
//        install(new ScoreboardMasterService(socialEnvironment), ScoreboardMasterService.SCOREBOARD_SERVICE_NAME, scoreboardEnvironment, context);
    }

    private static void install(GeneralMasterService masterService, ServiceName name, InjectedValue<ServerEnvironment> env, ServiceActivatorContext context) {
        install(masterService, name, env, 1, context);
    }

    private static void install(GeneralMasterService masterService, ServiceName name, InjectedValue<ServerEnvironment> env, int quorum, ServiceActivatorContext context) {
        ServiceController<?> factoryService = context.getServiceRegistry().getRequiredService(SingletonServiceBuilderFactory.SERVICE_NAME.append(CONTAINER_NAME, CACHE_NAME));
        SingletonServiceBuilderFactory factory = (SingletonServiceBuilderFactory) factoryService.getValue();
        factory.createSingletonServiceBuilder(name, masterService)
            .requireQuorum(quorum)
            .build(context.getServiceTarget())
                .addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, env)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install()
        ;
    }
}
