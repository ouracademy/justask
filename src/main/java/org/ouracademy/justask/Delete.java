package org.ouracademy.justask;

import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * Delete
 */
public class Delete extends Command {

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public void execute(Robot robot, String text) {
        type(robot, KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SPACE);
    }

}