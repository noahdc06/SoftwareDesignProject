public class BattleFeature {

    private Profile player;
    private Profile enemy;

    public BattleFeature(Profile player, Profile enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public void startBattle() {
        System.out.println("Battle started!");
    }

    public void attack() {
        System.out.println(player.getName() + " attacks!");

        enemy.takeHit();

        if (!enemy.isAlive()) {
            System.out.println("Enemy died!");
            recordDeath(enemy);
            return;
        }

        enemyAttack();
    }

    public void defend() {
        System.out.println(player.getName() + " defends!");
        player.setDefending(true);
    }

    private void enemyAttack() {
        System.out.println("Enemy attacks!");

        player.takeHit();

        if (!player.isAlive()) {
            System.out.println("Player died!");
            recordDeath(player);
        }
    }

    private void recordDeath(Profile profile) {
        System.out.println("Saving death for: " + profile.getName());
    }
}
