package oddschecker.utils.comparator;

/**
 * CharsComparator is responsible for providing reusable static helper methods to assist with comparing character arrays.
 * Each member of the class follows a contract of return and must always obey that contract (detailed in JavaDoc).
 */
public class CharsArrayComparator {

    /**
     * Greedily takes and evaluates the leading characters of charSet for the first complete subset of their index positions in charSuperSet.
     * Returns the result as an ordered array of ints indicating the indexes where those characters occur in charSuperSet.
     * <p>
     * The method must unilaterally return indexes, to illustrate...
     * Given any set of chars i.e. "DEFG" and "ABCDEF" produce the output {3,4,5}
     * Given "XYZABC" and "DEFABC" produce the output {}
     * - "ABCDEF" and "BCDE" overlap with overlap length 4
     * - "ABCDEF" and "XCDEZ" do *not* overlap (they have matching characters in the middle, but the overlap does not extend to the end of either string).
     *
     * @param charSet      Input charset with leading head indexes i.e. ABC of ABCD for BCDABC
     * @param charSuperSet Input superset expected to contain those leading head indexes.
     * @return The position of the head indexes from the charSet found in the charSuperSet.
     */
    public static int[] getHeadIndexesGreedy(char[] charSet, char[] charSuperSet) {
        // Meets the contract by simple regular expression matching.
        if (charSet.length == 0 || charSuperSet.length == 0) {
            // An empty array cannot possibly return a set of characters contained in one array or the other.
            return new int[0];
        }
        String superString = String.valueOf(charSuperSet);
        StringBuilder headBuilder = new StringBuilder();
        // For each character c in the character set with the head we wish to find in the containing character set.
        for (char c : charSet) {
            headBuilder.append(c);
            // Append that character to a string builder and test to see if its contained within the containing super-string.
            if (!superString.matches(".*" + headBuilder.toString() + ".*")) {
                // Delete the character that breaks the loop.
                headBuilder.deleteCharAt(headBuilder.length() - 1);
                break;
            }
        }
        // At this point we have the greatest head which is contained in the charSuperSet.
        // Find the substring in the string index.
        // Produce the rest by ranging over the value of the index of the string, plus the size of the string builder..
        int startIndex = superString.indexOf(headBuilder.toString());
        int endIndex = (startIndex + headBuilder.length());
        int[] headCharPositions = new int[headBuilder.length()];
        int headCharPosition = 0;
        for (int i = startIndex; i < endIndex; i++) {
            headCharPositions[headCharPosition] = i;
            headCharPosition++;
        }
        // Send back the positions.
        return headCharPositions;
    }
}
