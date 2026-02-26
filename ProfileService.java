// With the use of AI
import java.util.HashMap;
import java.util.Map;

public class ProfileService {
    private static Map<String, Profile> profiles = new HashMap<>();

    public static Profile createProfile(String username, String password) {
        if (profiles.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        Profile profile = new Profile(username, password);
        profiles.put(username, profile);
        return profile;
    }

    public static Profile login(String username, String password) {
        Profile profile = profiles.get(username);
        if (profile == null || !profile.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        return profile;
    }
}