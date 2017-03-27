package ru.javaops.masterjava.export.vo;

import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 26.03.2017.
 */
public class UsersExportResultVO {
    private List<User> users = new ArrayList<>();
    private int successful;
    private int errors;
    private List<String> errorsLocations = new ArrayList<>();

    public UsersExportResultVO() {
    }

    public void addErrorLocation(String errorLocation) {
        errorsLocations.add(errorLocation);
    }

    public void incrementSuccessful() {
        successful++;
    }

    public void incrementErrors() {
        errors++;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<String> getErrorsLocations() {
        return errorsLocations;
    }

    public void setErrorsLocations(List<String> errorsLocations) {
        this.errorsLocations = errorsLocations;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getSuccessful() {
        return successful;
    }

    public void setSuccessful(int successful) {
        this.successful = successful;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }
}
