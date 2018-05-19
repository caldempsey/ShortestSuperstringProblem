package oddschecker.utils.comparator;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class CharsArrayComparatorTest {


    @Test
    public void getHeadIndexesGreedyValid() {
        // Test1
        char[] test1string1 = "DEFABC".toCharArray();
        char[] test1string2 = "123DEF".toCharArray();
        int[] expectedResult1 = {3, 4, 5};
        int[] testResult1 = CharsArrayComparator.getHeadIndexesGreedy(test1string1, test1string2);
        assertArrayEquals(expectedResult1, testResult1);

        // Test2
        char[] test2string1 = {'a', 'b', 'c', 'a', 'b', 'c'};
        char[] test2string2 = {'a', 'b', 'c', 'a', 'b'};
        int[] expectedResult2 = {0, 1, 2, 3, 4};
        int[] testResult2 = CharsArrayComparator.getHeadIndexesGreedy(test2string1, test2string2);
        assertArrayEquals(expectedResult2, testResult2);
    }


}