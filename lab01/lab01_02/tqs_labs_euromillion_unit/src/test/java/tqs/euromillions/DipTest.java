/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.euromillions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author ico0
 */
public class DipTest {

    private Dip dipA;


    @BeforeEach
    public void setUp() {
        dipA = new Dip(new int[]{10, 20, 30, 40, 50}, new int[]{1, 2});
    }

    @AfterEach
    public void tearDown() {
        dipA = null;
    }

    @DisplayName("format as string show all elements")
    @Test
    public void testFormat() {
        String result = dipA.format();
        assertEquals("N[ 10 20 30 40 50] S[  1  2]", result, "format as string: formatted string not as expected. ");
    }

    @DisplayName("new Dip rejects wrong size ou negatives")
    @Test
    public void testConstructorFromBadArrays() {

        // insufficient args
        assertThrows(IllegalArgumentException.class,
                () -> new Dip(new int[]{10, 11}, new int[]{}));

        //negative numbers
        assertThrows(IllegalArgumentException.class,
                () -> new Dip(new int[]{10, 11, 12, 13, -1}, new int[]{1, 2}));


    }

    @DisplayName("new Dip rejects out of range elements")
    @Test
    public void testConstructorFromBadRanges() {
        // creating Dip with numbers or starts outside the expected range
        // expects an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Dip(new int[]{10, 11, 12, 13, Dip.NUMBERS_RANGE_MAX * 2}, new int[]{1, 2}));
        assertThrows(IllegalArgumentException.class,
                () -> new Dip(new int[]{11, 12, 13, 14, 15}, new int[]{Dip.STARS_RANGE_MAX * 2, 1}));

    }

    @DisplayName("Dip should be equal to itself")
    @Test
    public void testEquals() {
        assertEquals(dipA, dipA, "Dip should be equal to itself");
    }

    @DisplayName("The dip must ignore other types of classes (and null) in the equals method")
    @Test
    public void testEqualsMethodEdgeCase(){
        String dummy = "dummy";
        assertFalse(dipA.equals(dummy), "Dips should not be equal to any other Classe apart from Dips");
        assertFalse(dipA.equals(null), "Dips should not be equal to null");
    }

    @DisplayName("Different Dips with different values must have different hash codes")
    @Test
    public void testHashCode(){
        Dip dipB = new Dip(new int[]{10, 20, 30, 40, 5}, new int[]{1, 2});
        assertNotEquals(dipA.hashCode(), dipB.hashCode(), "Different dips should have different hash codes");
    }

}