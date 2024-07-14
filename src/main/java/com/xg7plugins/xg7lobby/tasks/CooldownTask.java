package com.xg7plugins.xg7lobby.tasks;

public abstract class CooldownTask extends Task{

    public CooldownTask(String name) {
        super(name,20);
    }

    @Override
    public abstract void run();
}
