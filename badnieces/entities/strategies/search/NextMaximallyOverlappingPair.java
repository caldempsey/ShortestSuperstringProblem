package badnieces.entities.strategies.search;

import badnieces.interfaces.strategy.search.StringsListSearchStrategy;
import oddschecker.utils.comparator.CharsArrayComparator;

/**
 * NextMaximallyOverlappingPair is a strategy which takes an array of strings and identifies the next maximally overlapping string.
 * The next maximally overlapping string is delivered as part of an ordered pair.
 * For instance in ABCDE BCDE the head of BCDE at element 1 will be featured in ABCDE element 0, and so the int array returned is [1,0].
 * The design consideration is implemented as providing more of the same information (if all you need is to know there exists an overlap between the two indexes then the conjunction of each element will do).
 */

public class NextMaximallyOverlappingPair implements StringsListSearchStrategy {
    private final String strategyName;

    /**
     * The constructor for the strategy NextMaximallyOverlappingPair.
     */
    public NextMaximallyOverlappingPair() {
        strategyName = "NextMaximallyOverlappingPair";
    }

    /**
     * Searches the input list of strings for the next maximally overlapping string header CONTAINED IN another string in the array (which isn't identical to the first).
     * For example if in the array [ABCE, BCE], then BCE is CONTAINED IN ABCE.
     * In this circumstance the result array will be 1,0 representing BCE is CONTAINED IN, ABCE.
     * Thus the search will always prioritise returning the next string HEAD CONTAINED IN the SUPERSTRING and must always maintain consistency in those search results.
     * If no match can be found the array will cleanly return [0,0].
     *
     * @param strings An array of strings to be searched.
     * @return Returns the array of positions which are the next maximally overlapping pair else returns [0,0]
     */
    // Set of tokens returned == [0,1]
    @Override
    public int[] search(String[] strings) {
        // There are two tests for overlapping pair heads.
        // ABCDEF -> DEFABC where ABC matches in the second string from the first (the straight case).
        // 123DEF -> DEFABC where DEF matches in the first string from the second (the reverse case).
        // ABCDEF -> 123DEF is invalid (DEF and DEF do not overlap).
        // We test both the reverse case and straight case as follows...
        // Find the head ABCDEF in DEFABC (so ABC, straight case).
        // Find the head DEFABC in 123DEF (so DEF, reverse case).
        // Add the greatest of the two to the current list of pairs and record the greatest overlap.
        // If the greatest overlap is ever exceeded update it with the new pair of overlapping strings.
        int currMaximalOverlap = 0;
        boolean reverseCase;
        int[] greatestOverlapIndexes = new int[2];
        // For each string in the array locate the maximally overlapping pair.
        for (int i = 0; i < strings.length; i++) {
            // Avoid nulls
            if (strings[i] == null) {
                continue;
            }
            char[] currFragment = strings[i].toCharArray();
            for (int j = 0; j < strings.length; j++) {
                // Skip nulls or if i and j are the same item don't self-check.
                if (strings[j] == null || i == j) {
                    continue;
                }
                char[] nextFragment = strings[j].toCharArray();
                // Test the straight case.
                int[] headPositions = CharsArrayComparator.getHeadIndexesGreedy(nextFragment, currFragment);
                // Test the reverse case
                int[] reverseHeadPositions = CharsArrayComparator.getHeadIndexesGreedy(currFragment, nextFragment);
                reverseCase = headPositions.length < reverseHeadPositions.length;
                if (currMaximalOverlap < headPositions.length && (!reverseCase)) {
                    // Update our array of indexes (from, into).
                    greatestOverlapIndexes[1] = i;
                    greatestOverlapIndexes[0] = j;
                    currMaximalOverlap = headPositions.length;
                }
                if (currMaximalOverlap < reverseHeadPositions.length && reverseCase) {
                    // Update our array of indexes (from, into).
                    greatestOverlapIndexes[0] = i;
                    greatestOverlapIndexes[1] = j;
                    currMaximalOverlap = reverseHeadPositions.length;
                }
            }
        }
        return greatestOverlapIndexes;
    }

    /**
     * Gets the name of the strategy.
     *
     * @return Returns the strategy name.
     */
    @Override
    public String getStrategyName() {
        return this.strategyName;
    }

    @Override
    public String toString() {
        return "Strings List Search Strategy " + strategyName;
    }

}