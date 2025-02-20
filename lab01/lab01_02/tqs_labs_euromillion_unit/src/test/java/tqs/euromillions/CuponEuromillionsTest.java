package tqs.euromillions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CuponEuromillions
 */
class CuponEuromillionsTest {

    private CuponEuromillions cupon;
    private Dip dip1;
    private Dip dip2;

    @BeforeEach
    void setUp() {
        cupon = new CuponEuromillions();
        dip1 = new Dip();
        dip2 = new Dip();
    }

    @Test
    void testAppendDip() {
        cupon.appendDip(dip1);
        assertEquals(1, cupon.countDips());

        cupon.appendDip(dip2);
        assertEquals(2, cupon.countDips());
    }

    @Test
    void testCountDips() {
        assertEquals(0, cupon.countDips());
        cupon.appendDip(dip1);
        assertEquals(1, cupon.countDips());
    }

    @Test
    void testGetDipByIndex() {
        cupon.appendDip(dip1);
        cupon.appendDip(dip2);

        assertEquals(dip1, cupon.getDipByIndex(0));
        assertEquals(dip2, cupon.getDipByIndex(1));
    }

    @Test
    void testFormat() {
        cupon.appendDip(dip1);
        cupon.appendDip(dip2);

        String formattedOutput = cupon.format();
        assertTrue(formattedOutput.contains("Dip #1:"));
        assertTrue(formattedOutput.contains("Dip #2:"));
    }
}
