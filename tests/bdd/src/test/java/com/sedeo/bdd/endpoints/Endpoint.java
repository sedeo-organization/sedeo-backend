package com.sedeo.bdd.endpoints;

import com.sedeo.bdd.Config;

public class Endpoint {

    public static final String REGISTRATION = Config.BASE_ENDPOINT + "/registration";
    public static final String LOGIN = Config.BASE_ENDPOINT + "/login";
    public static final String PASSWORD_RESET_REQUEST = Config.BASE_ENDPOINT + "/users/password";
    public static final String PROFILE = Config.BASE_ENDPOINT + "/users/me";
}
