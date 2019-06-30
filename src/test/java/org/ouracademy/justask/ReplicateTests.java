package org.ouracademy.justask;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * ReplicateTests
 */
public class ReplicateTests {

    @Test
    public void replicate() {
        var robot = mock(Robot.class);
        var interpreter = new Interpreter(robot);
        interpreter.interpret("replicate 3 type hi");

        InOrder inOrder = inOrder(robot);
        List.of(KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_H, KeyEvent.VK_I)
                .forEach(x -> inOrder.verify(robot).keyPress(x));

    }

}