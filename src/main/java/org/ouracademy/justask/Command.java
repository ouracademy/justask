package org.ouracademy.justask;

import java.awt.Robot;

/**
 * Command
 */
public abstract class Command {
    public abstract String getName();

    public abstract void execute(Robot robot, String text);
}