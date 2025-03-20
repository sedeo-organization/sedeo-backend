package com.sedeo.bdd.context;

import java.util.UUID;

public class UserContext {

    public UUID userId;
    public String email;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String password;
    public String jwt;

    public String bearer() {
        return "Bearer " + jwt;
    }

}
