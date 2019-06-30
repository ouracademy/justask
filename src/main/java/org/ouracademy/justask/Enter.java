package org.ouracademy.justask;

import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * Enter
 */
public class Enter extends Command {

    @Override
    public String getName() {
        return "enter";
    }

    @Override
    public void execute(Robot robot, String text) {
        type(robot, KeyEvent.VK_ENTER);
    }

}