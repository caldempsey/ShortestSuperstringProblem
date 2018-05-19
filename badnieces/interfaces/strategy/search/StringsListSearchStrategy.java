package badnieces.interfaces.strategy.search;

/**
 * An object which can be said tests can be merged with an object of the same kind to produce a single object.
 * There may be many ways we would want to tests objects.
 * Following the strategy design pattern, implementing types of the MergeStrategy interface thus define the behaviour to perform the tests operation.
 * Future work may involve extension to provide support for Lambda expressions (to provide Merge functions by implementing objects that can be chained together). We may be able to avoid re-inventing the wheel and extend the BiFunction interface alternatively.
 */
public interface StringsListSearchStrategy {
    /**
     * Returns a set of tolkens. based on a search algo.
     * Find the next token from String1 to String2.
     * Implementation should ignore nulls.
     *
     * @param search The array of strings to search.
     * @return Returns a set of tolkens corresponding to the results of the search.
     */
    int[] search(String[] search);

    /**
     * A method must be provided corresponding to the name of the implemented strategy.
     *
     * @return The name of the strategy.
     */
    String getStrategyName();
}