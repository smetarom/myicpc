package com.myicpc.master;

import com.myicpc.master.bean.IMasterBean;
import com.myicpc.master.dto.Environment;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Roman Smetana
 */
public abstract class GeneralMasterService implements Service<Environment> {
    private static final Logger logger = LoggerFactory.getLogger(HATimerService.class);

    protected final Value<ServerEnvironment> env;
    private final AtomicBoolean started = new AtomicBoolean(false);

    public GeneralMasterService(Value<ServerEnvironment> env) {
        this.env = env;
    }

    @Override
    public Environment getValue() {
        if (!this.started.get()) {
            throw new IllegalStateException();
        }
        return new Environment(this.env.getValue().getNodeName());
    }

    @Override
    public void start(StartContext context) throws StartException {
        if (!started.compareAndSet(false, true)) {
            throw new StartException("The service is still started!");
        }
        logger.info("Starting " + getServiceName());
        try {
            InitialContext ic = new InitialContext();
            ((IMasterBean) ic.lookup(getBeanLookup())).initialize();
        } catch (NamingException e) {
            throw new StartException("Could not initialize timer", e);
        }
    }

    @Override
    public void stop(StopContext context) {
        if (!started.compareAndSet(true, false)) {
            logger.warn("The service '" + getServiceName() + "' is not active!");
        } else {
            logger.info("Stopping " + getServiceName());
            try {
                InitialContext ic = new InitialContext();
                ((IMasterBean) ic.lookup(getBeanLookup())).stop();
            } catch (NamingException e) {
                logger.error("Could not stop timer", e);
            }
        }
    }

    protected abstract String getServiceName();

    protected abstract String getBeanLookup();
}
