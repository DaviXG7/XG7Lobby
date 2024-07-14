package com.xg7plugins.xg7lobby.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public abstract class Task {
    @Setter
    private String name;
    private long delay;

    public abstract void run();
}
