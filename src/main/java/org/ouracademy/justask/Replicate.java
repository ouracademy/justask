package org.ouracademy.justask;

import java.awt.Robot;
import java.util.stream.LongStream;

/**
 * Replicate
 */
public class Replicate extends Command {
    private Interpreter interpreter;

    public Replicate(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public String getName() {
        return "replicate";
    }

    @Override
    public void execute(Robot robot, String text) {
        var timesToReplicate = text.split(" ")[0];
        System.out.println("timesToReplicate: '" + timesToReplicate + "'");

        LongStream.rangeClosed(1, Long.parseLong(timesToReplicate)).sequential()
                .forEach(x -> this.interpreter.interpret(text.substring(timesToReplicate.length() + 1)));
    }

}