package com.myicpc.master;

import java.io.Serializable;

/**
 * @author Paul Ferraro
 */
public class Environment implements Serializable {
    private static final long serialVersionUID = -7845251073515304583L;

    private final String nodeName;

    public Environment(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return this.nodeName;
    }
}
