package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;

/**
 * Created by root on 03.04.2017.
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao<Group> {

    public Group insert(Group group) {
        if (group.isNew()) {
            int id = insertGeneratedId(group, group.getProject().getName());
            group.setId(id);
        } else {
            insertWitId(group, group.getProject().getName());
        }
        return group;
    }

    @SqlUpdate("INSERT INTO groups (name, project_name, type) VALUES (:name, :projectName, CAST(:type AS group_type)) ")
    abstract int insertGeneratedId(@BindBean Group group, @Bind("projectName") String projectName);

    @SqlUpdate("INSERT INTO groups (id, name, project_name, type) VALUES (:id, :name, :projectName, CAST(:type AS group_type)) ")
    abstract void insertWitId(@BindBean Group group, @Bind("projectName") String projectName);

    @SqlUpdate("TRUNCATE groups")
    @Override
    public abstract void clean();
}
