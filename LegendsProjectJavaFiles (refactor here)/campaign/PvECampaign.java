package com.legends.campaign;

import com.legends.database.User;
import com.legends.model.Profile;

import java.util.Random;

public class PvECampaign {

    //Possible rooms
    public enum RoomType {
        BATTLE,
        INN,
        CAMPAIGN_COMPLETE
    }

    private final Profile profile;   //stores level, rooms cleared
    private final User userDAO;      //updates database
    private final Random random;     //used for room randomness

    public PvECampaign(Profile profile) {
        this.profile = profile;
        this.userDAO = new User();
        this.random = new Random();
    }

    public Profile getProfile() {
        return profile;
    }

    //Determines next room type based on progress + randomness
    public RoomType determineCurrentRoomType() {

        //Victory after 10 rooms
        if (profile.getRoomCount() >= 10) {
            return RoomType.CAMPAIGN_COMPLETE;
        }

        //Inn first always
        if (profile.getRoomCount() == 1) {
            return RoomType.INN;
        }

        //Increases batlle % on level up
        int battleChance = Math.min(100, 60 + profile.getPartyLevel());
        int roll = random.nextInt(100) + 1;

        return roll <= battleChance ? RoomType.BATTLE : RoomType.INN;
    }

    //Increases level and room count after battle victory
    public void completeBattle() {
        profile.setPartyLevel(profile.getPartyLevel() + 1);
        profile.setRoomCount(profile.getRoomCount() + 1);
    }

    //Increases room count and saves
    public void completeInnRoomAndSave() {
        profile.setRoomCount(profile.getRoomCount() + 1);
        userDAO.updateProgress(profile);
    }

    //Save progress at an inn without moving forward (stay in same room)
    public void saveAtInnWithoutAdvancing() {
        userDAO.updateProgress(profile);
    }

    //Reset campaign (only doable by completing a campaign)
    public void resetCampaign() {
        profile.setPartyLevel(1);
        profile.setRoomCount(1);
        userDAO.updateProgress(profile);
    }
}