package badnieces.entities.strategies.merge;

import badnieces.interfaces.strategy.merge.StringMergeStrategy;
import oddschecker.utils.comparator.CharsArrayComparator;

/**
 * Merges an overlap pair from the head of the first input string to the second input string.
 * If no overlap exists then the second string as unmerged is returned (as there is no overlap).
 */
public class MergeOverlapPair implements StringMergeStrategy {
    private final String strategyName;

    public MergeOverlapPair() {
        strategyName = "MergeOverlapPair";
    }

    /**
     * Merges an ordered pair of overlapping strings where the overlap occurs.
     *
     * @param fromString The string to merge from.
     * @param intoString The string to merge to.
     * @return Returns the merged String object on success or returns the fromString object as a representation of the merge if there is no overlap, by the reasoning if a mergable set of characters returns nil, then the result of the merge is a nil merge result.
     */
    @Override
    public String merge(String fromString, String intoString) {
        // The merge of a string into a null will return the string (that can't be merged).
        if (intoString == null && fromString != null) {
            return fromString;
        }
        // The merge of an object into a null object will produce a null object.
        if (fromString == null) {
            return null;
        }
        int[] overlap = CharsArrayComparator.getHeadIndexesGreedy(fromString.toCharArray(), intoString.toCharArray());
        if (overlap.length == 0) {
            return fromString;
        }
        /*
         * Build a string of the the second array of chars substring until the last elem of the index where the head of the first string occurs in the second.
         * Then build the rest of the first string, then build the rest of the second string again*/
        return intoString.substring(0, overlap[0]) + fromString + intoString.substring(overlap[overlap.length - 1] + 1);
    }

    @Override
    public String strategyToName() {
        return strategyName;
    }

    @Override
    public String toString() {
        return "String Merge Strategy: " + strategyName;
    }
}
