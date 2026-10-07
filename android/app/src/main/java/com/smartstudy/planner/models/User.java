package com.smartstudy.planner.models;

public class User {
    private String username;
    private String passwordHash;
    private String recoveryHash;

    public User(String username, String passwordHash, String recoveryHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.recoveryHash = recoveryHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRecoveryHash() {
        return recoveryHash;
    }
}
