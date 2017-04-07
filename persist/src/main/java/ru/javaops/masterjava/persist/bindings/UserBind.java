package ru.javaops.masterjava.persist.bindings;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;
import ru.javaops.masterjava.persist.model.User;

import java.lang.annotation.*;

/**
 * Created by root on 06.04.2017.
 */
@BindingAnnotation(UserBind.UserBinding.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface UserBind {
    public static class UserBinding implements BinderFactory {
        @Override
        public Binder build(Annotation annotation) {
            return (Binder<UserBind, User>) (sqlStatement, userBindAnnotation, user) -> {
                sqlStatement.bind("id", user.getId());
                sqlStatement.bind("fullName", user.getFullName());
                sqlStatement.bind("email", user.getEmail());
                sqlStatement.bind("flag", user.getFlag());
                sqlStatement.bind("cityId", user.getCity().getId());
            };
        }
    }
}
