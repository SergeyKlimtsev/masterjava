package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 15.05.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JmsMail implements Serializable {
    private Set<Addressee> to;
    private Set<Addressee> cc;
    private String subject;
    private String body;
    private List<ByteArrayAttach> attaches;

    public JmsMail withTo(Set<Addressee> to) {
        this.to = to;
        return this;
    }

    public JmsMail withCc(Set<Addressee> cc) {
        this.cc = cc;
        return this;
    }

    public JmsMail withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public JmsMail withBody(String body) {
        this.body = body;
        return this;
    }

    public JmsMail withAttaches(List<ByteArrayAttach> attaches) {
        this.attaches = attaches;
        return this;
    }
}
