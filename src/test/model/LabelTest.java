package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LabelTest {
    private Label testNrLabel;
    private Label testOasisLabel;

    @BeforeEach
    void runBefore() {
        testNrLabel = new Label("nrCan", 0.1, 0.2, 0.3, 0.4, "pTest", true);
        testOasisLabel = new Label("oasis", 0.1, 0.2, 0.3, 0.4, "pTest", false);
    }

    @Test
    void testConstructorNrCan() {
        assertEquals("nrCan", testNrLabel.getDescription());
        assertEquals(0.1, testNrLabel.getUFactor());
        assertEquals(0.2, testNrLabel.getShgc());
        assertEquals(0.3, testNrLabel.getEr());
        assertEquals(0.4, testNrLabel.getVt());
        assertEquals(Label.PERFORMANCE + "pTest", testNrLabel.getPerformance());
        assertTrue(testNrLabel.isNrCan());
    }

    @Test
    void testConstructorOasis() {
        assertFalse(testOasisLabel.isNrCan());
    }

    @Test
    void testGenerateImage() {
        try {
            assertNotSame(testNrLabel.generateImage(), testOasisLabel.generateImage());
        } catch (IOException e) {
            fail();
        }
    }
}
