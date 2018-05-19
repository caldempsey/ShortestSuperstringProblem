package badnieces.entities.strategies.merge;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MergeOverlapPairTest {
    private MergeOverlapPair mergeOverlapPair = new MergeOverlapPair();


    @Test
    public void validMerge() {
        // Test the pair edge case.
        // Test 1
        String mergeFrom = "ABC123";
        String mergeInto = "431ABC";
        String merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        String expectedResult = "431ABC123";
        assertEquals(expectedResult, merge);
        // Test 2
        mergeFrom = ";;;341";
        mergeInto = "DEF;;;";
        merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        expectedResult = "DEF;;;341";
        assertEquals(expectedResult, merge);
    }

    @Test
    public void extremeValidMerge() {
        // Test the pair edge case.
        // Test 1
        String mergeFrom = "DEFABC";
        String mergeInto = "ABCDEF";
        String merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        String expectedResult = "ABCDEFABC";
        assertEquals(expectedResult, merge);
        // Test 2
        mergeFrom = "ABCDEF";
        mergeInto = "DEFABC";
        merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        expectedResult = "DEFABCDEF";
        assertEquals(expectedResult, merge);
    }

    @Test
    public void invalidMerge() {
        String mergeFrom = "";
        String mergeInto = "C";
        String merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        assertEquals(mergeFrom, merge);
    }

    @Test
    public void extremeInvalidMerge() {
        // Test 1
        String mergeFrom = null;
        String mergeInto = "C";
        String merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        assertNull(merge);
        // Test 2
        mergeFrom = "C";
        mergeInto = null;
        merge = mergeOverlapPair.merge(mergeFrom, mergeInto);
        assertEquals(mergeFrom, merge);
    }
}