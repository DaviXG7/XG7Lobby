package com.xg7plugins.xg7lobby.tasks;

public interface Task {

    boolean isEnabled();

    long getDelay();

    void run();

}
