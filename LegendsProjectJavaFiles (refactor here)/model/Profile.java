package com.legends.model;

public class Profile {
    private int id;//database user id
    private String username;//player username
    private int partyLevel;//current party level
    private int roomCount;//current room progress

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
        this.id = id;//update id
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;//update username
    }

    public int getPartyLevel() {
        return partyLevel;
    }

    public void setPartyLevel(int partyLevel) {
        this.partyLevel = partyLevel;//update partylevel
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;//update room progress
    }
}