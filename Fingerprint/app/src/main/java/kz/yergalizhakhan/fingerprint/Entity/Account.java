package kz.yergalizhakhan.fingerprint.Entity;

import java.io.Serializable;

/**
 * Created by zhakhanyergali on 10.01.18.
 */

public class Account implements Serializable {

    private int id;
    private String title;
    private String login;
    private String password;
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
