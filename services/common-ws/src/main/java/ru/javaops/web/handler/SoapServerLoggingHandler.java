package ru.javaops.web.handler;


import com.typesafe.config.Config;
import org.slf4j.event.Level;
import ru.javaops.masterjava.config.Configs;

import java.util.HashMap;
import java.util.Map;

public class SoapServerLoggingHandler extends SoapLoggingHandler {

    private static String DEBUG_LOG_LEVEL;
    private static final Map<String, Level> LOG_LEVELS = new HashMap<String, Level>() {{
        put("ERROR", Level.ERROR);
        put("WARN", Level.WARN);
        put("INFO", Level.INFO);
        put("DEBUG", Level.DEBUG);
        put("TRACE", Level.TRACE);
    }};

    static {
        Config config = Configs.getConfig("hosts.conf", "hosts").getConfig("mail");
        DEBUG_LOG_LEVEL = config.getString("debug.server");
    }

    public SoapServerLoggingHandler() {
        super(LOG_LEVELS.get(DEBUG_LOG_LEVEL));
    }

    @Override
    protected boolean isRequest(boolean isOutbound) {
        return !isOutbound;
    }
}