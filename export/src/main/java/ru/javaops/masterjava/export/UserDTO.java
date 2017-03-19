package ru.javaops.masterjava.export;

/**
 * Created by root on 19.03.2017.
 */
public class UserDTO {
    private String name;
    private String flag;
    private String email;

    public UserDTO(String name, String flag, String email) {
        this.name = name;
        this.flag = flag;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
