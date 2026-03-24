package com.legends.model;

public class Profile {
    private String username;
    private String password;
    private int gold;
    private int currentRoom;
    
    private int hitCount = 0;
    private boolean isAlive = true;
    private boolean isDefending = false;


    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
        this.gold = 1000;
        this.currentRoom = 0;
    }



        public void takeHit() {
        int damage = 1;

        if (isDefending) {
            damage = 0;
            isDefending = false;
        }

        hitCount += damage;

        if (hitCount >= 3) {
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setDefending(boolean defending) {
        this.isDefending = defending;
    }

    public int getHitCount() {
        return hitCount;
    }
}

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getGold() { return gold; }
    public int getCurrentRoom() { return currentRoom; }

    public void setGold(int gold) { this.gold = gold; }
    public void setCurrentRoom(int currentRoom) { this.currentRoom = currentRoom; }
}
