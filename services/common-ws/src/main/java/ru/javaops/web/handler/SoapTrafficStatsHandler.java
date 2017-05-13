package ru.javaops.web.handler;

import org.slf4j.event.Level;

/**
 * Created by root on 10.05.2017.
 */
public class SoapTrafficStatsHandler extends SoapLoggingHandler {
    public SoapTrafficStatsHandler() {
        super(Level.TRACE);
    }

    @Override
    protected boolean isRequest(boolean isOutbound) {
        return !isOutbound;
    }
}
