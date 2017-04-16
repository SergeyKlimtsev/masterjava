package ru.javaops.masterjava.persist.model;

import lombok.*;

/**
 * Created by root on 10.04.2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)

@ToString(callSuper = true)
public class MailStatus extends BaseEntity {
    private String to;
    private String cc;
    private String subject;
    private String body;
    private Boolean success;

    public MailStatus() {
    }

    public MailStatus(String to, String cc, String subject, String body, Boolean success) {
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        this.success = success;
    }
}
