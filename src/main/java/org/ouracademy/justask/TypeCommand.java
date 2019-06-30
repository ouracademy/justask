package org.ouracademy.justask;

import static java.util.Map.entry;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Map.Entry;
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
                .of(Stream.of(entry('.', KeyEvent.VK_PERIOD), entry(' ', KeyEvent.VK_SPACE)),
                        keyboardMappingFor('a', 'z', KeyEvent.VK_A), keyboardMappingFor('0', '9', KeyEvent.VK_0))
                .flatMap(i -> i).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Stream<Entry<Character, Integer>> keyboardMappingFor(char start, char end, int startKey) {
        return IntStream.rangeClosed(start, end).mapToObj(x -> entry((char) x, x - start + startKey));
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
}