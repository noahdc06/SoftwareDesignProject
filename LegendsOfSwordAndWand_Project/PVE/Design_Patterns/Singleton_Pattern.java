package com.legends.database;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void saveDeath(String name) {
        System.out.println("Saving death for: " + name);
    }
}
