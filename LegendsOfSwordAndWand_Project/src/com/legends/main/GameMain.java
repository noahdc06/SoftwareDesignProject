package com.legends.main;

import com.legends.model.Profile;
import com.legends.database.UserDAO;
import com.legends.service.PvECampaign;

public class GameMain {

    public static void main(String[] args) {

        try {
            UserDAO dao = new UserDAO();

            Profile profile = new Profile("player1", "1234");
            dao.createUser(profile);

            Profile loggedIn = dao.login("player1", "1234");

            if (loggedIn != null) {
                PvECampaign campaign = new PvECampaign(loggedIn);
                campaign.startCampaign();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}