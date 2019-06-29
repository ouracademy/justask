package org.ouracademy.justask;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Interpreter
 */
public class Interpreter {
    Robot robot;

    public Interpreter(Robot robot) {
        this.robot = robot;
    }

    public void interpret(String text) {
        var command = text.split(" ")[0];
        var args = text.split(" ")[1];

        for (int charUnicodeIndex : args.chars().toArray()) {
            if (command.equals("type")) {
                if ('.' == (char) charUnicodeIndex) {
                    type(robot, KeyEvent.VK_PERIOD);
                } else {
                    int unicodeA = 97;
                    type(robot, charUnicodeIndex - unicodeA + KeyEvent.VK_A);
                }
            }
        }
    }

    private void type(Robot robot, int... keys) {
        robot.delay(40);
        Arrays.stream(keys).forEach(i -> robot.keyPress(i));
        Arrays.stream(keys).forEach(i -> robot.keyRelease(i));
    }

}