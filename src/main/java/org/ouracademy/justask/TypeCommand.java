package org.ouracademy.justask;

import static java.util.Map.entry;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.ouracademy.justask.Command;

/**
 * TypeCommand
 */
public class TypeCommand extends Command {

    public Map<Character, Integer> charToKeyboardMap;

    public TypeCommand() {
        this.charToKeyboardMap = Stream
                .concat(Stream.of(entry('.', KeyEvent.VK_PERIOD), entry(' ', KeyEvent.VK_SPACE)),
                        IntStream.rangeClosed('a', 'z').mapToObj(x -> entry((char) x, x - 'a' + KeyEvent.VK_A)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public String getName() {
        return "type";
    }

    public void execute(Robot robot, String text) {
        for (int charUnicodeIndex : text.chars().toArray()) {
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