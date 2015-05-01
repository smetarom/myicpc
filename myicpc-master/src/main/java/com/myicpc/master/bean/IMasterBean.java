package com.myicpc.master.bean;

import javax.ejb.Local;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Local
public interface IMasterBean {

    void initialize();

    void stop();

    AtomicBoolean getStarted();

}