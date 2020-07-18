package com.spa.urbanstep.eventbus;

/**
 * Created by Admin on 6/26/2017.
 */

public interface EventConstant {
    int NETWORK_ERROR = -1;
    int TOKEN_EXPIRE = 0;


    int SERVER_ERROR = 500;

    int IDLE_TIMEOUT = 1000;

    int LOGIN_SUCCESS = 1;
    int LOGIN_ERROR = 2;

    int REGISTER_SUCCESS = 3;
    int REGISTER_ERROR = 4;

    int RECORD_LIST_SUCCESS = 5;
    int RECORD_LIST_ERROR = 6;
    int ONGOING_PROJECT_LIST_SUCCESS = 7;
    int ONGOING_PROJECT_LIST_ERROR = 8;
    int EDIT_PROFILE_SUCCESS = 9;
    int EDIT_PROFILE_ERROR = 10;
    int FORGOT_PASSWORD_SUCCESS = 11;
    int FORGOT_PASSWORD_ERROR = 12;
    int All_LIST_SUCCESS = 13;
    int ALL_LIST_ERROR = 14;
    int SAVE_RECORD_SUCCESS = 15;
    int SAVE_RECORD_ERROR = 16;
    int GET_PROFILE_SUCCESS = 17;
    int GET_PROFILE_ERROR = 18;

    int COLONY_LIST_SUCCESS = 19;
    int COLONY_LIST_ERROR = 20;

    int KNOW_YOUR_LIST_SUCCESS = 21;
    int KNOW_YOUR_LIST_ERROR = 22;

    int KNOW_YOUR_VIEW_LIST_SUCCESS = 23;
    int KNOW_YOUR_VIEW_LIST_ERROR = 24;
    int HOME_MAP_URL_SUCCESS = 25;
    int HOME_MAP_URL_ERROR = 26;
    int FEEDBACK_SUCCESS = 27;
    int FEEDBACK_ERROR = 28;
}
