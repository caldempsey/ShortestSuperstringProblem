package badnieces.entities.compositor.document;

import badnieces.interfaces.strategy.merge.StringMergeStrategy;
import badnieces.interfaces.strategy.search.StringsListSearchStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The DocumentCompositor class is responsible for holding a mutable representation of a document (represented by a list of strings).
 * The DocumentCompositor implements the strategy design pattern to perform document composition from a list of Strings.
 * The DocumentCompisitor provides methods to perform merge or search operations over the constructed data structure.
 * In line with Bloch Item 15, mutability of the internal data structure is minimised by restricting the operations that can be performed on those objects to well-formed strategies (enforced by merge and search interfaces).
 * Further the Document class performs defensive copying and null element checks to protect the internal data structure.
 * In line with the Open-closed principle the DocumentCompositor class aims to be closed for modification and open for extension: it is not possible to remove existing strategies (and the DocumentComposer enforces at least a Merge and Search strategy for recursive merging), but it is possible to extend the class to provide additional functionality.
 * The approach allows for the flexibility to composite documents in different ways depending on a mix of strategy configurations (in case we want to extend the functionality of the application while minimising changes to the code).
 * The DocumentCompositor class further allows for backups to be made of the immutable data structure (to support rollbacks when errors occur). Using these methods is not enforced, but recommended.
 * <p>
 * Some goals of the object design were...
 * 1. Provide a flexible solution to mutate an internal data representation of Strings, in particular to answer, what if we want to do merges in different ways in the future?
 * 2. Provide a scalable design by creating objects for more suitable components for multi-threaded software (no shared state between objects).
 * 3. Provide a solution for the Java Test but also provide a responsible and re-usable class design without being coupled to the project (a good object on its own, and in its own right).
 * 4. Follow mutability design considerations paying close attention to the dangers of exposing references to the internal data structure.
 */
public class DocumentCompositor {
    private final StringsListSearchStrategy searchStrategy;
    private final StringMergeStrategy mergeStrategy;
    private List<String> document;
    private List<String> documentBackup;


    /**
     * Instantiates a DocumentCompositor and creates a backup of the input document.
     * Removes null references on instantiation.
     *
     * @param searchStrategy An input search strategy.
     * @param mergeStrategy  An input merge strategy.
     * @param document       An input document (represented by a list of Strings).
     */
    public DocumentCompositor(StringsListSearchStrategy searchStrategy, StringMergeStrategy mergeStrategy, String[] document) {
        // No need for defensive copying as initialising from primitives.
        List<String> documentToList = new ArrayList<>(Arrays.asList(document));
        // Guarantee no null document fall into the Strings Merger (nulls are not mergable).
        // Collections provides a method to shift elements n*m where n is the number of elements such that it is a singleton time complexity so O(n).
        documentToList.removeAll(Collections.singleton(null));
        this.searchStrategy = searchStrategy;
        this.mergeStrategy = mergeStrategy;
        this.document = documentToList;
        this.documentBackup = documentToList;
    }

    public DocumentCompositor(StringsListSearchStrategy searchStrategy, StringMergeStrategy mergeStrategy, List<String> document) {
        // Each string is an immutable object so no need to worry about contained object references
        List<String> documentCopy = new ArrayList<>(document);
        // Guarantee no null document fall into the Strings Merger (nulls are not mergable).
        // Collections provides a method to shift elements n*m where n is the number of elements such that it is a singleton time complexity so O(n).
        document.removeAll(Collections.singleton(null));
        this.searchStrategy = searchStrategy;
        this.mergeStrategy = mergeStrategy;
        this.document = documentCopy;
        this.documentBackup = documentCopy;
    }

    /**
     * Creates a backup of the current state of the Document.
     */
    public void writeBackup() {
        documentBackup = document;
    }

    /**
     * Restores a backup of the last good backup of a document.
     */
    public void restoreBackup() {
        document = documentBackup;
    }

    /**
     * Merge will merge document at specified indexes using the MergeStrategy implemented.
     *
     * @param indexOfString1 The first index of to merge within the stored document.
     * @param indexOfString2 The second index to merge within the stored document.
     * @return Returns true if the operation was successful indicating String1 has been merged to String2, otherwise returns false.
     */
    public boolean merge(int indexOfString1, int indexOfString2) {
        // If passed parameters are null, raise an exception (nulls should not be possible).
        if (document.get(indexOfString1) == null || document.get(indexOfString2) == null) return false;
        // Return false if index out of bounds.
        if (document.size() - 1 < indexOfString1 || document.size() - 1 < indexOfString2) return false;
        // Attempt merge.
        String mergedString = mergeStrategy.merge(document.get(indexOfString1), document.get(indexOfString2));
        // Replace the old string with the new string.
        document.set(indexOfString2, mergedString);
        // Post merge remove the string (don't do this before you will mess up the indexes!!).
        document.remove(indexOfString1);
        // Complete operation by returning true.
        return true;
    }

    /**
     * Uses the StringsListSearchStrategy implemented to return search tokens or hits.
     * For example consider a document is represented by the list of document {"Cat", "Sat", "Hat"}.
     * If the search strategy implemented returns all document containing the letter 'a', the array returned is {0,1,2}.
     *
     * @return The array of integers returned by the search strategy.
     */
    // Search document for hits specified by the SearchStrategy implemented.
    public int[] search() {
        // Cast list to primitive type (which is what strategies prefer as shared objects).
        return searchStrategy.search(document.toArray(new String[0]));
    }

    /**
     * Recursively performs a merge operation using the first and second element of the search strategy implemented per iteration.
     * The function will merge until no more merges are possible (the size of the list of document is 1).
     * It is recommended to backup the document before completing this operation and handling the IllegalStateException appropriately (commonly to restore the last good backup).
     *
     * @throws IllegalStateException Throws an illegal state exception if there are not enough elements in the search token to perform a merge.
     */
    public void recursiveMerge() throws IllegalStateException {
        // If the document size is smaller than or equal to 1 then merging is not possible.
        if (document.size() <= 1) {
            return;
        }
        // Get next token.
        int[] searchTokens = search();
        if (searchTokens.length < 2) {
            throw new IllegalStateException("" +
                    "Merge operation in " + toString() + " was cancelled. Please restore from the last good backup." +
                    "A minimum of two search tokens must be returned per every iteration of a merge operation. A search strategy which is guaranteed to always return a minimum of two search tokens (passed to the next recursive merge) should be used to perform a recursive merge (until exhaustion).");
        }
        // Attempt to merge
        boolean mergeSuccess = merge(searchTokens[0], searchTokens[1]);
        // If a merge can no longer be completed then end the loop (we merge to the extent that a search returns good output).
        if (!mergeSuccess) {
            return;
        }
        recursiveMerge();
    }

    /**
     * Return the current document stored within the DocumentCompositor.
     *
     * @return The document as a list of strings.
     */
    public List<String> get() {
        // The document is defensively copied to retain the immutability of the internal data.
        return new ArrayList<>(document);
    }

    /**
     * Get to String will create a string out of the current document contents.
     *
     * @return Returns a string representation of the internal document.
     */
    public String getToString() {
        StringBuilder stringBuilder = new StringBuilder();
        // Prefer checking for nulls rather than asserting non-nulls since the class could be extended without method override.
        for (String s : document) {
            if (s != null) {
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }
}
