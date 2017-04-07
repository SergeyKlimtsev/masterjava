package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

/**
 * Created by root on 06.04.2017.
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserGroupRefs implements AbstractDao {

    @SqlBatch("INSERT INTO user_to_group_refs (user_id, group_name) VALUES (:userId, :name)" +
            "ON CONFLICT DO NOTHING")
    public abstract int[] insertBatch(@BindBean List<Group> groups, @Bind("userId") int userId);
}
