package badnieces.entities.reader.encoding;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UTF8Reader is an Readable responsible for reading well-formed UTF8 files.
 */
class UTF8Reader extends EncodingReadable {

    /**
     * Constructs a new UTF8 reader.
     */
    UTF8Reader() {
        // UTF8 EncodingReadable constructor supports no modification.
        super("UTF8 EncodingReadable", "UTF8");
    }


    /**
     * Reads the file at the absPath specified to a string.
     *
     * @param absPath The absPath to the file.
     * @return Returns the data at the absPath to a string.
     * @throws IOException                  Throws an IO exception if the file cannot be read.
     * @throws UnsupportedEncodingException Throws an UnsupportedEncodingException if the file read is detected to an invalid format.
     */
    @Override
    public String readToString(String absPath) throws IOException {
        FileInputStream inStream = new FileInputStream(absPath);
        InputStreamReader inReader = new InputStreamReader(inStream);
        if (!inReader.getEncoding().equals("UTF8")) {
            throw new UnsupportedEncodingException("File must be a well formed UTF8 document");
        }
        BufferedReader br = new BufferedReader(inReader);
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    /**
     * Reads each line of the file at the absPath specified to a different string contained in an array.
     *
     * @param absPath The absPath to the file.
     * @return Returns the data at the absPath to a string.
     * @throws IOException                  Throws an IO exception if the file cannot be read.
     * @throws UnsupportedEncodingException Throws an UnsupportedEncodingException if the file read is detected to an invalid format.
     */
    public String[] readToStrings(String absPath) throws IOException {
        FileInputStream inStream = new FileInputStream(absPath);
        InputStreamReader inReader = new InputStreamReader(inStream);
        if (!inReader.getEncoding().equals("UTF8")) {
            throw new UnsupportedEncodingException("File must be a well formed UTF8 document");
        }
        BufferedReader br = new BufferedReader(inReader);
        String line;
        List<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }

}
