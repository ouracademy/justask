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
        this.commands = List.of(new TypeCommand(), new Delete(), new Replicate(this), new Enter(), new Select(),
                new Open());
    }

    public void interpret(String text) {
        var command = this.commands.stream().filter(x -> text.startsWith(x.getName())).findFirst().orElseThrow();
        var position = command.getName().length() + 1;
        var args = text.length() > position ? text.substring(position) : "";

        System.out.println("'" + args + "'");
        command.execute(robot, args);
    }

}