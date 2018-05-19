package badnieces.entities.reader.encoding;

import badnieces.interfaces.read.Readable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * An EncodingReadable is a Readable such that implementing objects will read file input in from a well-formed encoded format (determined by the object).
 * Hence each read operation throws an UnsupportedEncodingException.
 */
public abstract class EncodingReadable implements Readable {
    private final String type;
    private final String encoding;

    /**
     * Constructs a new EncodingReadable object.
     *
     * @param type     The type of object created.
     * @param encoding The encoding supported by the object.
     */
    EncodingReadable(String type, String encoding) {
        if (encoding == null || type == null) {
            throw new IllegalArgumentException("Object type and encoding must be specified");
        }
        this.type = type;
        this.encoding = encoding;
    }

    /**
     * Static factory constructor which returns an EncodingReadable subclass.
     * Supported types (input params) include: UTF8.
     *
     * @param type The type of read to construct.
     * @return Returns an EncodingReadable object.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the EncodingReadable requested is not a supported type.
     */
    public static EncodingReadable getInstance(String type) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("Input parameter cannot be a null object");
        }
        type = type.toUpperCase();
        switch (type) {
            case "UTF8":
                return new UTF8Reader();
            default:
                break;
        }
        throw new IllegalArgumentException("Unsupported EncodingReadable type specified to getInstance method.");
    }


    /**
     * Reads the object to a String.
     *
     * @param path The path of the object to be read.
     * @return A string containing the contents of the file data at the path.
     * @throws IOException Throws an IO exception if the file cannot be read.
     */
    @Override
    public String readToString(String path) throws IOException {
        FileInputStream inStream = new FileInputStream(path);
        InputStreamReader inReader = new InputStreamReader(inStream);
        BufferedReader br = new BufferedReader(inReader);
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    public String[] readToStrings(String absPath) throws IOException {
        FileInputStream inStream = new FileInputStream(absPath);
        InputStreamReader inReader = new InputStreamReader(inStream);
        BufferedReader br = new BufferedReader(inReader);
        String line;
        List<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }

    public final String toString() {
        return "Encoding Reader {Type:" + type + " Encoding:" + encoding + "}";
    }
}
