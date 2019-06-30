package org.ouracademy.justask;

import java.awt.Robot;
import java.util.List;

import org.ouracademy.justask.TypeCommand;

/**
 * Interpreter
 */
public class Interpreter {
    Robot robot;
    List<Command> commands;

    public Interpreter(Robot robot) {
        this.robot = robot;
        this.commands = List.of(new TypeCommand());
    }

    public void interpret(String text) {
        var command = this.commands.stream().filter(x -> text.startsWith(x.getName())).findFirst().orElseThrow();
        var args = text.substring(command.getName().length() + 1);

        System.out.println("'" + args + "'");
        command.execute(robot, args);
    }

}