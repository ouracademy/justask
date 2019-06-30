package org.ouracademy.justask;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import org.junit.jupiter.api.Test;

public class EnterTests {

    @Test
    public void interpret() {
        var robot = mock(Robot.class);
        var interpreter = new Interpreter(robot);
        interpreter.interpret("enter");

        verify(robot).keyPress(KeyEvent.VK_ENTER);
    }

}