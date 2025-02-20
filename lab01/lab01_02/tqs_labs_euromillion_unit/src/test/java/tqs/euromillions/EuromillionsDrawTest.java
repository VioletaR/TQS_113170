package tqs.euromillions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class EuromillionsDrawTest {

    private CuponEuromillions sampleCoupon;

    @BeforeEach
    public void setUp() {
        sampleCoupon = new CuponEuromillions();
        sampleCoupon.appendDip(Dip.generateRandomDip());
        sampleCoupon.appendDip(Dip.generateRandomDip());
        sampleCoupon.appendDip(new Dip(new int[]{1, 2, 3, 48, 49}, new int[]{1, 9}));
    }

    @DisplayName("reports correct matches in a coupon")
    @Test
    public void testCompareBetWithDrawToGetResults() {
        Dip winningDip, matchesFound;

        // Test for full match, using the 3rd dip in the coupon as the Draw results
        winningDip = sampleCoupon.getDipByIndex(2);
        EuromillionsDraw testDraw = new EuromillionsDraw(winningDip);
        matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);

        assertEquals(winningDip, matchesFound, "Expected the bet and the matches found to be equal");

        // Test for no matches at all
        testDraw = new EuromillionsDraw(new Dip(new int[]{9, 10, 11, 12, 13}, new int[]{2, 3}));
        matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);
        assertEquals(new Dip(), matchesFound, "Expected no matches found");
    }

    @DisplayName("test partial matches")
    @Test
    public void testPartialMatches() {
        Dip winningDip = new Dip(new int[]{1, 2, 10, 20, 30}, new int[]{1, 2});
        EuromillionsDraw testDraw = new EuromillionsDraw(winningDip);
        Dip matchesFound = testDraw.findMatchesFor(sampleCoupon).get(2);
        assertNotEquals(new Dip(), matchesFound, "Expected partial matches");
    }

    @DisplayName("test empty coupon")
    @Test
    public void testEmptyCoupon() {
        CuponEuromillions emptyCoupon = new CuponEuromillions();
        EuromillionsDraw testDraw = new EuromillionsDraw(Dip.generateRandomDip());
        assertTrue(testDraw.findMatchesFor(emptyCoupon).isEmpty(), "Expected no matches for empty coupon");
    }

    @DisplayName("test random draw generation")
    @Test
    public void testRandomDrawGeneration() {
        EuromillionsDraw randomDraw = EuromillionsDraw.generateRandomDraw();
        assertNotNull(randomDraw.getDrawResults(), "Expected a non-null random draw result");
    }
}