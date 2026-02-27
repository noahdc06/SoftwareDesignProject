package com.legends.service;

import com.legends.model.Profile;

public class CampaignService {

    public static void exitCampaign(PvECampaign campaign, Profile profile) {
        if (campaign.isInBattle()) {
            System.out.println("Cannot exit during battle!");
            return;
        }

        System.out.println("Saving progress...");
        System.out.println("Room: " + profile.getCurrentRoom());
        System.out.println("Gold: " + profile.getGold());
        System.out.println("Campaign exited successfully.");
    }
}