package org.ouracademy.justask;

import java.awt.Robot;
import java.util.Arrays;

/**
 * Command
 */
public abstract class Command {
    public abstract String getName();

    public abstract void execute(Robot robot, String text);

    public void type(Robot robot, int... keys) {
        robot.delay(40);
        Arrays.stream(keys).forEach(i -> robot.keyPress(i));
        Arrays.stream(keys).forEach(i -> robot.keyRelease(i));
    }
}