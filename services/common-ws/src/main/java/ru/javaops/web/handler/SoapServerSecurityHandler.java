package ru.javaops.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import com.typesafe.config.Config;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.web.AuthUtil;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 12.05.2017.
 */
public class SoapServerSecurityHandler extends SoapBaseHandler {
    private static String USERNAME;
    private static String PASSWORD;

    static {
        Config config = Configs.getConfig("hosts.conf", "hosts").getConfig("mail");
        USERNAME = config.getString("user");
        PASSWORD = config.getString("password");
    }

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        if (!isOutbound(context)) {
            @SuppressWarnings("unchecked")
            Map<String, List<String>> headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
            String credentialsBase64 = AuthUtil.encodeBasicAuthHeader(USERNAME, PASSWORD);
            return AuthUtil.checkBasicAuth(headers, credentialsBase64) == 0;
        } else {
            return true;
        }
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        return false;
    }
}
