package tqs.sets;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.Set;

/**
 * Unit tests for BoundedSetOfNaturals
 */
class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;

    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(2);
        setB = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        setC = BoundedSetOfNaturals.fromArray(new int[]{50, 60});
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = null;
    }

    @DisplayName("Adds an element to the set")
    @Test
    public void testAddElement() {
        setA.add(99);
        assertTrue(setA.contains(99), "Element should be in the set");
        assertEquals(1, setA.size(), "Set size should be 1");
    }

    @DisplayName("Should not add the same element to the set")
    @Test
    public void testAddDuplicateElement() {
        setA.add(10);
        assertThrows(IllegalArgumentException.class, () -> setA.add(10), "Should not allow duplicate elements");
    }

    @DisplayName("Should not add negative elements to the set")
    @Test
    public void testAddNegativeElement() {
        assertThrows(IllegalArgumentException.class, () -> setA.add(-5), "Should not allow negative numbers");
    }

    @DisplayName("Should not add to a full set")
    @Test
    public void testAddToFullSet() {
        setA.add(1);
        setA.add(2);
        assertThrows(IllegalArgumentException.class, () -> setA.add(3), "Should not allow adding to a full set");
    }

    @DisplayName("The set must have the correct size")
    @Test
    public void testFromArrayCreatesCorrectSize() {
        assertEquals(6, setB.size(), "Set B should have 6 elements");
    }

    @DisplayName("The set must be able to find the intersection with another set")
    @Test
    public void testIntersectionTrue() {
        assertTrue(setB.intersects(setC), "Sets should have common elements");
    }

    @DisplayName("The set must be able to find the lack of intersection with another set")
    @Test
    public void testIntersectionFalse() {
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{100, 200});
        assertFalse(setB.intersects(setD), "Sets should not have common elements");
    }

    @DisplayName("The set must know if it contains an element")
    @Test
    public void testContainsExistingElement() {
        assertTrue(setB.contains(20), "Set should contain element 20");
    }

    @DisplayName("The set must know if it does not contain an element")
    @Test
    public void testContainsNonExistingElement() {
        assertFalse(setB.contains(99), "Set should not contain element 99");
    }

    @DisplayName("The set must be able recognize it self")
    @Test
    public void testEqualsSameSet(){
        assertEquals(setB, setB, "The set must be equal to itself");
    }

    @DisplayName("The set must be able to recognize a set with the same elements")
    @Test
    public void testEqualsMethod() {
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        assertEquals(setB, setD, "Sets with the same elements should be equal");
    }

    @DisplayName("The set must be able to recognize a set with different elements")
    @Test
    public void testNotEqualsMethod() {
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30});
        assertNotEquals(setB, setD, "Sets with different elements should not be equal");

    }

    @DisplayName("The set must ignore other types of classes (and null) in the equals method")
    @Test
    public void testEqualsMethodEdgeCase(){
        String dummy = "dummy";
        assertFalse(setB.equals(dummy), "Sets should not be equal to any other Classe apart from Sets");
        assertFalse(setB.equals(null), "Sets should not be equal to null");
    }

    @DisplayName("Different Sets with different values must have different hash codes")
    @Test
    public void testHashCode(){
        assertNotEquals(setB.hashCode(), setA.hashCode(), "Different sets should have different hash codes");
    }

    @DisplayName("Sets should iterate through all elements")
    @Test
    public void testIteratorFunctionality() {
        int count = 0;
        for (int elem : setB) {
            count++;
        }
        assertEquals(6, count, "Iterator should iterate through all elements");
    }


}
