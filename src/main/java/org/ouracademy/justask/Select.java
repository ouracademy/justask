package org.ouracademy.justask;

import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * Select
 */
public class Select extends Command {

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public void execute(Robot robot, String text) {
        type(robot, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_LEFT);

    }

}