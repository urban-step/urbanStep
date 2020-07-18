package com.spa.urbanstep.prefrences;


import android.text.TextUtils;


import com.spa.carrythistoo.model.User;
import com.spa.urbanstep.App;
import com.spa.urbanstep.manager.LogManager;
import com.spa.urbanstep.utils.GsonUtil;


/**
 * Created by craterzone on 21/9/16.
 */

public class AppPreference extends BasePreferences {

    public final static String TAG = AppPreference.class.getSimpleName();

    private static AppPreference mInstance;

    private static final String SHARED_PREF_NAME = "carry_this_too_pref";
    private static final int PRIVATE_MODE = 0;


    private interface Keys {
        String DEVICE_TOKEN = "device_token";
        String SESSION_TOKEN = "session_token";
        String DEVICE_ID = "device_id";
        String DEVICE_TYPE = "device_type";
        String USER_ID = "user_id";
        String IS_LOGIN = "is_login";
        String IS_FIRST_TIME_USER = "is_first_time";
        String USER = "user";
    }

    private AppPreference() {
        super(App.Companion.applicationContext().
                getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE));
    }

    public static AppPreference getInstance() {
        if (mInstance == null) {
            synchronized (AppPreference.class) {
                if (mInstance == null) {
                    mInstance = new AppPreference();
                }
            }
        }
        return mInstance;
    }

    public void setDeviceId(String deviceId) {
        setString(Keys.DEVICE_ID, null);
    }

    public String getDeviceID() {
        return getString(Keys.DEVICE_ID, null);
    }


    public String getDeviceToken() {
        return getString(Keys.DEVICE_TOKEN, null);
    }

    public void setDeviceToken(String token) {
        LogManager.d(TAG, "save device token value: " + token);
        setString(Keys.DEVICE_TOKEN, token);
    }


    public void setSessionToken(String token) {
        LogManager.d(TAG, "save session token value: " + token);
        setString(Keys.SESSION_TOKEN, token);
    }

    public String getSessionToken() {
        return getString(Keys.SESSION_TOKEN, null);
    }

    public long getUserId() {
        return getLong(Keys.USER_ID, 0);
    }

    public void setUserId(long userId) {
        LogManager.d(TAG, "save user id value: " + userId);
        setLong(Keys.USER_ID, userId);
    }


    public void setLogin(boolean login) {
        LogManager.d(TAG, "save loginRequest status value: " + login);
        setBoolean(Keys.IS_LOGIN, login);
    }


    public boolean isLogin() {
        return getBoolean(Keys.IS_LOGIN, false);
    }

    public void setFirstTimeUser(boolean isFirst) {
        LogManager.d(TAG, "save first time user status: " + isFirst);
        setBoolean(Keys.IS_FIRST_TIME_USER, isFirst);
    }

    public boolean isFirstTimeUser() {
        return getBoolean(Keys.IS_FIRST_TIME_USER, true);
    }

    public void setUser(User user) {
        setString(Keys.USER, GsonUtil.toJson(user));
    }


    public User getUser() {

        String userString = getString(Keys.USER, "");

        if (!TextUtils.isEmpty(userString)) {
            return (User) GsonUtil.toModel(userString, User.class);
        }
        return null;
    }

}
