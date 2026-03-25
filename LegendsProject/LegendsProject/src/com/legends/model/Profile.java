package com.legends.model;

public class Profile {
    private int id;
    private String username;
    private int partyLevel;
    private int roomCount;

    public Profile() {
    }

    public Profile(int id, String username, int partyLevel, int roomCount) {
        this.id = id;
        this.username = username;
        this.partyLevel = partyLevel;
        this.roomCount = roomCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPartyLevel() {
        return partyLevel;
    }

    public void setPartyLevel(int partyLevel) {
        this.partyLevel = partyLevel;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }
}
