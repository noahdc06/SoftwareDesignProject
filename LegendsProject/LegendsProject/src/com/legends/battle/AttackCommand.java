package com.legends.battle;

public class AttackCommand {
    private final Command command;
    private final int targetIndex;

    public AttackCommand(Command command, int targetIndex) {
        this.command = command;
        this.targetIndex = targetIndex;
    }

    public Command getCommand() {
        return command;
    }

    public int getTargetIndex() {
        return targetIndex;
    }
}