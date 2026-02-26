// With the use of AI
public class Profile {
    private String username;
    private String password;
    private int gold;
    private int currentRoom;

    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
        this.gold = 1000;
        this.currentRoom = 0;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getGold() { return gold; }
    public int getCurrentRoom() { return currentRoom; }

    public void setGold(int gold) { this.gold = gold; }
    public void setCurrentRoom(int room) { this.currentRoom = room; }
}