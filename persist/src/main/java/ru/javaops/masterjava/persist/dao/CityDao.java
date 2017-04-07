package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

/**
 * Created by root on 03.04.2017.
 */
@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao<City> {


    @Override
    public City insert(City city) {
        insertCity(city);
        return city;
    }


    @SqlUpdate("INSERT INTO cities (id, name) VALUES (:id, :name)")
    abstract void insertCity(@BindBean City city);

    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();
}
