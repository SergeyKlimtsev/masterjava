package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.skife.jdbi.v2.DBI;
import org.thymeleaf.util.ObjectUtils;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.MailStatusDao;
import ru.javaops.masterjava.persist.model.MailStatus;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * gkislin
 * 15.11.2016
 */
@Slf4j
public class MailSender {
    private static final MailStatusDao DAO = new DBI("jdbc:postgresql://localhost:5432/masterjava", "user", "password").open(MailStatusDao.class);

   /* public static void main(String[] args) {
        sendMail(ImmutableList.of(new Addressee("grayfox666893@gmail.com", null)), null, "Subject", "Body");
    }*/

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        Config mailConfig = Configs.getConfig("mail.conf", "mail");

        Email email = new SimpleEmail();
        email.setHostName(mailConfig.getString("host"));
        email.setSmtpPort(mailConfig.getInt("port"));
        email.setAuthenticator(new DefaultAuthenticator(mailConfig.getString("username"), mailConfig.getString("password")));
        email.setSSLOnConnect(mailConfig.getBoolean("useSSL"));
        email.setStartTLSEnabled(mailConfig.getBoolean("useTLS"));
        email.setDebug(mailConfig.getBoolean("debug"));
        try {
            email.setFrom(mailConfig.getString("fromName"));
            email.setSubject(subject);
            email.setMsg(body);
            email.setTo(toInternetAdress(to));

            List<InternetAddress> ccAddresses = toInternetAdress(cc);
            if (ccAddresses != null && !ccAddresses.isEmpty()) {
                email.setCc(toInternetAdress(cc));
            }
              email.send();

            MailStatus mailStatus = new MailStatus(String.valueOf(to), String.valueOf(cc), String.valueOf(subject), String.valueOf(body), true);
            DAO.insert(mailStatus);
        } catch (EmailException e) {
            log.info(e.toString());
            e.printStackTrace();
            MailStatus mailStatus = new MailStatus(String.valueOf(to), String.valueOf(cc), String.valueOf(subject), String.valueOf(body), false);
            DAO.insert(mailStatus);
        }
    }

    private static List<InternetAddress> toInternetAdress(List<Addressee> addressees) {
        if (addressees == null || addressees.size() == 0) {
            return new ArrayList<>();
        }
        return StreamEx.of(addressees).map(addr -> {
            try {
                return new InternetAddress(addr.getEmail(), addr.getName());
            } catch (Exception e) {
                log.info(e.toString());
                log.info("Invalid address parameter: " + Objects.toString(addr));
                return null;
            }
        })
                .filter(internetAddress -> internetAddress != null)
                .toList();

    }
}
