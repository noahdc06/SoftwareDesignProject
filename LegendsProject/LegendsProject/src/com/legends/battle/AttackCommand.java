package com.legends.battle;

//Encapsulates a player action using Command pattern
public class AttackCommand {
    private final Command command;   //attack, recover, spells
    private final int targetIndex;   //index of target

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