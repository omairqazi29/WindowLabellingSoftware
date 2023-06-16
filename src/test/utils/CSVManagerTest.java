package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVManagerTest {
    private CSVManager testCSVManager;

    @BeforeEach
    void runBefore() {
        testCSVManager = CSVManager.getInstance();
    }

    @Test
    void testConstructor() {
        assertEquals("300 Series", testCSVManager.getRecords().get(1)[0]);
        assertEquals("Sliding Window", testCSVManager.getPerformanceRecords().get(1)[1]);
        assertEquals("Delisting Date", testCSVManager.getNrCanRecords().get(0)[2]);
    }

    @Test
    void testException() {
        try {
            CSVManager.testCSVManager();
            fail("RuntimeException expected");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    void testWindowTypeOne() {
        List<String> windowTypes = testCSVManager.getWindowType("Nova Sliding Patio Door");
        assertEquals(1, windowTypes.size());
        assertEquals("Sliding Patio Door", windowTypes.get(0));
    }

    @Test
    void testGetWindowTypeMultiple() {
        List<String> windowTypes = testCSVManager.getWindowType("5000 Series");
        assertEquals(4, windowTypes.size());
        assertEquals("Casement Window", windowTypes.get(0));
        assertEquals("Awning Window", windowTypes.get(1));
        assertEquals("Picture Window", windowTypes.get(2));
        assertEquals("Balanced Sash Picture Window", windowTypes.get(3));
    }

    @Test
    void testGetPerformanceType() {
        List<String> windowTypes = testCSVManager.getPerformanceType("300 Series");
        assertEquals(3, windowTypes.size());
        assertEquals("Sliding Window", windowTypes.get(0));
        assertEquals("Vertical Sliding Window", windowTypes.get(1));
        assertEquals("Picture Window", windowTypes.get(2));
    }

    @Test
    void testGetGlassOption() {
        assertEquals("",
                testCSVManager.getGlassOption("5000 Series", "Balanced Sash Picture Window").get(0));
        assertTrue(3 <= (testCSVManager.getGlassOption("300 Series", "Sliding Window").size()));
    }

    @Test
    void testGetRatings() {
        List<Double> ratings = testCSVManager.getRatings("300 Series", "Sliding Window",
                "3,180(2)-3,i89(4)");
        assertEquals(1.42, ratings.get(0));
        assertEquals(0.49, ratings.get(1));
        assertEquals(37, ratings.get(2));
        assertEquals(0.61, ratings.get(3));
    }

    @Test
    void testGetPerformanceRatings() {
        String performance = "Class CW  –  PG2640 (metric)\n";
        performance += "Size tested:   800 x 1600 mm (Type C)\n";
        performance += "Positive Design Pressure (DP):   2640 Pa\n";
        performance += "Negative Design Pressure (DP):   -2640 Pa\n";
        performance += "Water Penetration Resistance Test Pressure:   730 Pa\n";
        performance += "Canadian Air Filtration/Exfiltration:   A3 Level\n";
        performance += "Report:   T636-6 (Issued June 30, 2010)";
        assertEquals(performance,
                testCSVManager.getPerformanceRatings("5000 Series", "Casement Window - Push-out"));
    }

    @Test
    void testGetModelCode() {
        assertTrue(testCSVManager.getModelCode("s","w","g").isEmpty());
        assertEquals("OASW-SS-3,CL-3,180(3)-13AR90EN",
                testCSVManager.getModelCode("300 Series","Sliding Window","3,CL-3,180(3)"));
    }

    @Test
    void testGetReport() {
        assertTrue(testCSVManager.getReport("s","w","g").isEmpty());
        assertEquals("T636-63 (Issued July 15, 2020)",
                testCSVManager.getReport("300 Series","Sliding Window","3,CL-3,180(3)"));
    }

    @Test
    void testIsNrCan() {
        assertTrue(testCSVManager.isNRCan("OASW-SS-3,CL-3,180(3)-13AR90EN"));
        assertFalse(testCSVManager.isNRCan("OASW-SS-3,CL-3,180(3)-NotNrCan"));
    }
}
