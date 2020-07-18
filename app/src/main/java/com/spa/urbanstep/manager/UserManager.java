package com.spa.urbanstep.manager;

import com.spa.carrythistoo.model.User;
import com.spa.urbanstep.network.RestClient;
import com.spa.urbanstep.prefrences.AppPreference;

/**
 * Created by shweta on 30/1/18.
 */

public class UserManager {
    private static UserManager _instance;
    private User user;

    private UserManager() {
        user = AppPreference.getInstance().getUser();

    }

    public static UserManager getInstance() {
        if (_instance == null) {
            _instance = new UserManager();
        }
        return _instance;
    }


    public void setUser(User user) {
        this.user = user;
        AppPreference.getInstance().setUser(user);
    }


    public User getUser() {
        return user;
    }


    public void logout() {
        AppPreference.getInstance().setLogin(false);
        AppPreference.getInstance().setUser(null);
        AppPreference.getInstance().setSessionToken(null);
        RestClient.getInstance().destroy();
    }

}

