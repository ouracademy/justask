package org.ouracademy.justask;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * InterpreterTests
 */
public class InterpreterTests {

    @ParameterizedTest
    @MethodSource
    public void interpret(String text, List<Integer> keysPressed) {
        var robot = mock(Robot.class);
        var interpreter = new Interpreter(robot);
        interpreter.interpret("type " + text);

        keysPressed.forEach(x -> verify(robot).keyPress(x));
    }

    static Stream<Arguments> interpret() {
        return Stream.of(arguments("hi", List.of(KeyEvent.VK_H, KeyEvent.VK_I)),
                arguments("c.", List.of(KeyEvent.VK_C, KeyEvent.VK_PERIOD)),
                arguments("a b", List.of(KeyEvent.VK_A, KeyEvent.VK_SPACE, KeyEvent.VK_B)));
    }

    @Test
    public void interpretNoCommand() {
        var robot = mock(Robot.class);
        var interpreter = new Interpreter(robot);
        assertThrows(NoSuchElementException.class, () -> interpreter.interpret("this is not a command :("));
    }
}