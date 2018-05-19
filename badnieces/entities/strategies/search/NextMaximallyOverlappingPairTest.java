package badnieces.entities.strategies.search;

import badnieces.interfaces.strategy.search.StringsListSearchStrategy;
import oddschecker.utils.comparator.CharsArrayComparator;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NextMaximallyOverlappingPairTest {
    private StringsListSearchStrategy stringsListSearchStrategy = new NextMaximallyOverlappingPair();

    @Test
    public void OddscheckerReferenceSpecTest() {
        // Tests in line with the Oddschecker specification document.
        // Overlap length is validated by calling length parameter using a static method to get the overlap of string1 in string2 (where we already abstracted s2 has maximal overlap with s1 in the strategy since that's what we are interested in).
        // Test1.
        // Process...
        // In ABCE, BCE, ABCE, we expect the next maximally overlapping pair to be DEF with an overlap of 3.
        // 1. Discover that s2 is the maximally overlapping pair to s1 (represented [1 (overlapping head),0 (contained substring)]).
        // 2. Recompute the head index s2 in s1 (DEF).
        // 3. The result should be 3.
        String staticString = "ABCDEF";
        String s2 = "DEFG";
        String[] test1Strings = {s2, staticString};
        int[] test1Result = stringsListSearchStrategy.search(test1Strings);
        // Use the CharsArrayComparator.getHeadIndexesGreedy static method to validate the overlap of s2 in s1 (returning all the positions of overlap).
        assertEquals(3, CharsArrayComparator.getHeadIndexesGreedy(test1Strings[test1Result[0]].toCharArray(), test1Strings[test1Result[1]].toCharArray()).length);

        // Test2.
        s2 = "XYZABC";
        String[] test2Strings = {s2, staticString};
        int[] test2Result = stringsListSearchStrategy.search(test2Strings);
        assertEquals(3, (CharsArrayComparator.getHeadIndexesGreedy(test2Strings[test2Result[0]].toCharArray(), test2Strings[test2Result[1]].toCharArray())).length);

        // Test3.
        staticString = "ABCDEF";
        s2 = "BCDE";
        String[] test3Strings = {s2, staticString};
        int[] test3Result = stringsListSearchStrategy.search(test3Strings);
        // Use the CharsArrayComparator.getHeadIndexesGreedy static method to validate the overlap of s2 in s1 (returning all the positions of overlap).
        assertEquals(4, CharsArrayComparator.getHeadIndexesGreedy(test3Strings[test3Result[0]].toCharArray(), test3Strings[test3Result[1]].toCharArray()).length);
        // Test4.
        staticString = "ABCDEF";
        s2 = "XCDEZ";
        String[] test4Strings = {s2, staticString};
        int[] test4Result = stringsListSearchStrategy.search(test3Strings);
        assertEquals(0, CharsArrayComparator.getHeadIndexesGreedy(test4Strings[test4Result[0]].toCharArray(), test4Strings[test4Result[1]].toCharArray()).length);

    }

    @Test
    public void validSearch() {
        // Test1.
        // In ABCE, BCE, ABCE, we expect the next maximally overlapping pair to be ABCE, ABCE.
        String s1 = "ABCE";
        String s2 = "BCE";
        String s3 = "ABCE";
        String[] strings = {s1, s2, s3};
        // The result should be [2,0] where ABCE is contained in the superset ABCE.
        int[] expectedResult = {2, 0};
        int[] test1Result = stringsListSearchStrategy.search(strings);
        assertArrayEquals(expectedResult, test1Result);

        // Test2
        // In ABCE, BCE, we expect the next maximally overlapping pair to be 1, 0.
        // Where BCE is the head contained in ABCE.
        s1 = "ABCE";
        s2 = "BCE";
        strings = new String[]{s1, s2};
        // The result should be [1,0] where BCE is contained in the superset ABCE.
        expectedResult = new int[]{1, 0};
        int[] test2Result = stringsListSearchStrategy.search(strings);
        assertArrayEquals(expectedResult, test2Result);

        // Test3. Case sensitivity.
        // Test case sensitivity.
        s1 = "Abce";
        s2 = "bCe";
        strings = new String[]{s1, s2};
        // The result should be {1,0} where b is the head contained in the superset Abce.
        expectedResult = new int[]{1, 0};
        int[] test3Result = stringsListSearchStrategy.search(strings);
        assertArrayEquals(expectedResult, test3Result);

        // Test4 Chars.
        // Test indifference to characters.
        s1 = "ABC;;;;";
        s2 = ";;;;B";
        strings = new String[]{s1, s2};
        // The result should be {1,0} where ;;;; is contained in the superset ABC;;;;.
        expectedResult = new int[]{1, 0};
        int[] test4Result = stringsListSearchStrategy.search(strings);
        assertArrayEquals(expectedResult, test4Result);
    }

    @Test
    public void extremeValidSearch() {
        // Test
        String input = "O draconia;conian devil! Oh la;h lame sa;saint! ";
        // First merge FROM is conian devil (the string with the head).
        // First merge INTO is O draconia (the superstring).
        // Test 1. Input 1.
        String[] fragments = input.split(";");
        int[] expectedTest1Result = {1, 0};
        int[] test1Result = stringsListSearchStrategy.search(fragments);
        assertArrayEquals(expectedTest1Result, test1Result);
        // Test
        input = "m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al";
        // Test 2. Input 2.
        fragments = input.split(";");
        int[] expectedTest2Result = {19, 17};
        int[] test2Result = stringsListSearchStrategy.search(fragments);
        assertArrayEquals(expectedTest2Result, test2Result);
    }

    @Test
    public void invalidSearch() {
        int[] expectedResult = {0, 0};
        // Test
        String[] inputTest1 = {"", ""};

        int[] testResult1 = stringsListSearchStrategy.search(inputTest1);
        assertArrayEquals(expectedResult, testResult1);
        String[] inputTest2 = {""};

        int[] testResult2 = stringsListSearchStrategy.search(inputTest2);
        assertArrayEquals(expectedResult, testResult2);

    }

    @Test
    public void extremeInvalidSearch() {
        // Test1
        int[] expectedResult = {0, 0};
        String[] inputTest1 = {};
        assertArrayEquals(expectedResult, stringsListSearchStrategy.search(inputTest1));
        // Test2
        String[] inputTest2 = {null, null, null};
        assertArrayEquals(expectedResult, stringsListSearchStrategy.search(inputTest2));
    }
}