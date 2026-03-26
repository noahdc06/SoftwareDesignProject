package com.legends.service;

import com.legends.model.Profile;

//Singleton instance managing the user and game menu
public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private Profile currentProfile;//stores logged-in user

    private SessionManager() {
    }

    //return single instance
    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }
    //set active user
    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    public boolean isLoggedIn() {
        return currentProfile != null;
    }

    public void logout() {
        currentProfile = null;//clear session with Logout button
    }
}