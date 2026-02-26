// With the use of AI
public class GameMain {

    public static void main(String[] args) {

        Profile profile = ProfileService.createProfile("player1", "1234");

        PvECampaign campaign = new PvECampaign(profile);
        campaign.startCampaign();

        CampaignService.exitCampaign(campaign, profile);
    }
}