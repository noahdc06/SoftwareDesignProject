// With the use of AI
import java.util.Random;

public class PvECampaign {
    private Profile profile;
    private boolean inBattle;

    public PvECampaign(Profile profile) {
        this.profile = profile;
        this.inBattle = false;
    }

    public void startCampaign() {
        System.out.println("Starting PvE Campaign...");
        nextRoom();
    }

    public void nextRoom() {
        profile.setCurrentRoom(profile.getCurrentRoom() + 1);
        System.out.println("Entering Room " + profile.getCurrentRoom());

        Random rand = new Random();
        int chance = rand.nextInt(100);

        if (chance < 60) {
            System.out.println("Battle Room!");
            inBattle = true;
        } else {
            System.out.println("Inn Room!");
            visitInn();
        }
    }

    public void visitInn() {
        System.out.println("All heroes healed and revived.");
        inBattle = false;
    }

    public boolean isInBattle() {
        return inBattle;
    }
}