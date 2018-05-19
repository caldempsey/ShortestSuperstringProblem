package badnieces.interfaces.read;

import java.io.IOException;

/**
 * The Readable interface declares an implementing object will be able to read filesystem input.
 */
public interface Readable {

    /**
     * All readable objects provide a toString representation of the data they read to describe the data.
     *
     * @param absPath The path of the file.
     * @return The data to a String.
     */
    String readToString(String absPath) throws IOException;

    String[] readToStrings(String absPath) throws IOException;


}
