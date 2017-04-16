package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.MailStatus;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 10.04.2017.
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailStatusDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail_status CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM mail_status")
    public abstract List<MailStatus> getAll();


    @SqlUpdate("INSERT INTO mail_status (\"to\", cc, subject, body, success)  VALUES (:to, :cc, :subject, :body, :success)")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean MailStatus mailStatus);

    public void insert(MailStatus mailStatus) {
        int id = insertGeneratedId(mailStatus);
        mailStatus.setId(id);
    }
}
