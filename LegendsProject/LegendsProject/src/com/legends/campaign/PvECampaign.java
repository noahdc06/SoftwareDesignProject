package com.legends.campaign;

import com.legends.database.User;
import com.legends.model.Profile;

import java.util.Random;

public class PvECampaign {
    public enum RoomType {
        BATTLE,
        INN,
        CAMPAIGN_COMPLETE
    }

    private final Profile profile;
    private final User userDAO;
    private final Random random;

    public PvECampaign(Profile profile) {
        this.profile = profile;
        this.userDAO = new User();
        this.random = new Random();
    }

    public Profile getProfile() {
        return profile;
    }

    public RoomType determineCurrentRoomType() {
        if (profile.getRoomCount() >= 10) {
            return RoomType.CAMPAIGN_COMPLETE;
        }

        if (profile.getRoomCount() == 1) {
            return RoomType.INN;
        }

        int battleChance = Math.min(100, 60 + profile.getPartyLevel());
        int roll = random.nextInt(100) + 1;
        return roll <= battleChance ? RoomType.BATTLE : RoomType.INN;
    }

    public void completeBattle() {
        profile.setPartyLevel(profile.getPartyLevel() + 1);
        profile.setRoomCount(profile.getRoomCount() + 1);
    }

    public void completeInnRoomAndSave() {
        profile.setRoomCount(profile.getRoomCount() + 1);
        userDAO.updateProgress(profile);
    }

    public void saveAtInnWithoutAdvancing() {
        userDAO.updateProgress(profile);
    }

    public void resetCampaign() {
        profile.setPartyLevel(1);
        profile.setRoomCount(1);
        userDAO.updateProgress(profile);
    }
}