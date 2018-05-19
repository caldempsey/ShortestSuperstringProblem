package badnieces.interfaces.strategy.merge;

/**
 * A StringMergeStrategy object provides methods to merge two String types to one.
 */


public interface StringMergeStrategy {
    String merge(String s1, String s2);

    /**
     * A method must be provided corresponding to the name of the implemented strategy.
     *
     * @return The name of the strategy.
     */

    String strategyToName();

}
