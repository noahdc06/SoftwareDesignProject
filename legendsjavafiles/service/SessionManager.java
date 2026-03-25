package com.legends.service;

import com.legends.model.Profile;

public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private Profile currentProfile;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    public boolean isLoggedIn() {
        return currentProfile != null;
    }

    public void logout() {
        currentProfile = null;
    }
}
