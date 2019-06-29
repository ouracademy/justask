package org.ouracademy.justask;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.Map.entry;

import com.google.common.io.CharStreams;

/**
 * Interpreter
 */
public class Interpreter {
    Robot robot;
    List<String> commands = List.of("type");
    public Map<Character, Integer> charToKeyboardMap;

    public Interpreter(Robot robot) {
        this.robot = robot;
        this.charToKeyboardMap = Stream
                .concat(Stream.of(entry('.', KeyEvent.VK_PERIOD), entry(' ', KeyEvent.VK_SPACE)),
                        IntStream.rangeClosed('a', 'z').mapToObj(x -> entry((char) x, x - 'a' + KeyEvent.VK_A)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void interpret(String text) {
        var command = this.commands.stream().filter(x -> text.startsWith(x)).findFirst();
        var args = text.substring(command.orElseThrow().length() + 1);

        System.out.println("'" + args + "'");
        for (int charUnicodeIndex : args.chars().toArray()) {
            var character = (char) charUnicodeIndex;
            type(robot, charToKeyboardMap.get(character));
        }
    }

    private void type(Robot robot, int... keys) {
        robot.delay(40);
        Arrays.stream(keys).forEach(i -> robot.keyPress(i));
        Arrays.stream(keys).forEach(i -> robot.keyRelease(i));
    }
}