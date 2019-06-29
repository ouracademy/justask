package org.ouracademy.justask;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
        interpreter.interpret("write " + text);

        keysPressed.forEach(x -> verify(robot).keyPress(x));
    }

    static Stream<Arguments> interpret() {
        return Stream.of(arguments("hi", List.of(KeyEvent.VK_H, KeyEvent.VK_I)),
                arguments("c.", List.of(KeyEvent.VK_C, KeyEvent.VK_PERIOD)));
    }
}