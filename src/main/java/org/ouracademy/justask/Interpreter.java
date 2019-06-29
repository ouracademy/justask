package org.ouracademy.justask;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Interpreter
 */
public class Interpreter {
    Robot robot;
    List<String> commands = List.of("type");

    public Interpreter(Robot robot) {
        this.robot = robot;
    }

    public void interpret(String text) {
        var command = this.commands.stream().filter(x -> text.startsWith(x)).findFirst();
        var args = text.substring(command.orElseThrow().length() + 1);

        System.out.println("'" + args + "'");
        for (int charUnicodeIndex : args.chars().toArray()) {
            if ('.' == (char) charUnicodeIndex) {
                type(robot, KeyEvent.VK_PERIOD);
            } else if (' ' == (char) charUnicodeIndex) {
                type(robot, KeyEvent.VK_SPACE);
            } else {
                int unicodeA = 97;
                type(robot, charUnicodeIndex - unicodeA + KeyEvent.VK_A);
            }
        }

    }

    private void type(Robot robot, int... keys) {
        robot.delay(40);
        Arrays.stream(keys).forEach(i -> robot.keyPress(i));
        Arrays.stream(keys).forEach(i -> robot.keyRelease(i));
    }
}