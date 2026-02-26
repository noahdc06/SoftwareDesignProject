// With the use of AI
public class Hero {
    private String name;
    private int attack;
    private int defense;
    private int hp;
    private int mana;

    public Hero(String name) {
        this.name = name;
        this.attack = 5;
        this.defense = 5;
        this.hp = 100;
        this.mana = 50;
    }

    public void attack(Hero target) {
        int damage = this.attack - target.defense;
        if (damage < 0) damage = 0;
        target.hp -= damage;
        System.out.println(name + " attacks " + target.name + " for " + damage + " damage.");
    }

    public void defend() {
        hp += 10;
        mana += 5;
        System.out.println(name + " defends and restores 10 HP + 5 Mana.");
    }

    public void castFireball(Hero target) {
        if (mana < 30) {
            System.out.println("Not enough mana!");
            return;
        }
        mana -= 30;
        int damage = attack * 2;
        target.hp -= damage;
        System.out.println(name + " casts Fireball for " + damage + " damage.");
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getHp() { return hp; }
    public String getName() { return name; }
}