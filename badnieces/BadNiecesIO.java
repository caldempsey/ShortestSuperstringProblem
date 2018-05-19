package badnieces;

import badnieces.entities.compositor.document.DocumentCompositor;
import badnieces.entities.reader.encoding.EncodingReadable;
import badnieces.entities.strategies.merge.MergeOverlapPair;
import badnieces.entities.strategies.search.NextMaximallyOverlappingPair;
import badnieces.interfaces.read.Readable;
import badnieces.interfaces.strategy.merge.StringMergeStrategy;
import badnieces.interfaces.strategy.search.StringsListSearchStrategy;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The BadNiecesIO class is responsible providing an interface to the application.
 * As providing the application entry point this class also involves handling argument input validation from the main method.
 * Where a project requirement is no other output is printed to the console, all logs are written to an error file (never run in production without logs).
 */
public final class BadNiecesIO {
    // Logs Configuration.
    // TODO Future work will include creating a config file. Also foreseeable log files won't update.
    // TODO Future work includes having logger update target file: it's no good having log files for one day log in the same file as log files for the previous day.
    private final static Logger LOGGER = Logger.getLogger("BadNiecesLogs");
    private final static String LOGS_DIR = System.getProperty("user.dir") + "\\";
    private final static String LOG_FILENAME = "BadNieces-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".log";
    private final static SimpleFormatter LOGS_FORMAT = new SimpleFormatter();

    /**
     * The main method of the application performs the following...
     * Initialises the logger. Exits on status 2 if failure.
     * Validates arguments for type errors. Exists on status 1 if failure.
     * Passes the arguments to run-time.
     *
     * @param args Arguments passed to the main method.
     */
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        // Validate pre-runtime conditions of the application. Run the application if validation passes.
        validate(args);
        run(args);
        // Terminate the application with a status code of 0 once all methods have finished.
        System.exit(0);
    }

    /**
     * Validate is responsible for performing all the operations of the validation stage (see class docs).
     * Validate will terminate the application if
     *
     * @param args Arguments from the main method to validate.
     */
    private static void validate(String[] args) {
        // Initialise a standard logger implementation. If this fails exit with a status of 1.
        try {
            initialiseLogger(false);
        } catch (IOException e) {
            System.exit(1);
        }
        if (args.length != 1) {
            LOGGER.severe("Arguments passed must be of length 1");
            System.exit(1);
        }
        File f = new File(args[0]);
        if (!f.exists() || !f.canRead()) {
            LOGGER.severe("The argument supplied could not be detected as pointing to a readable file.");
            System.exit(1);
        }

    }

    /**
     * Initialises a logger to the default configuration. If a file at the filepath at the configuration does not exist, the program will create one.
     *
     * @param console Specifies whether log output is to be printed to the console.
     * @throws IOException Thrown by the security manager to indicate a security violation.
     */
    private static void initialiseLogger(boolean console) throws IOException {
        FileHandler fileHandler = new FileHandler(LOGS_DIR + LOG_FILENAME);
        LOGGER.addHandler(fileHandler);
        fileHandler.setFormatter(LOGS_FORMAT);
        LOGGER.setUseParentHandlers(console);
    }

    /**
     * The run method is responsible for consuming arguments (if they exist) and processing the rest of the application (sticking to conventions of Threaded components).
     *
     * @param args Input arguments from the main method.
     */
    private static void run(String[] args) {
        // Input path.
        String inputPath = args[0];
         /*
            For each input line, search the collection of fragments to locate the pair with the maximal overlap.
            Match then merge those two fragments.
            This operation will decrease the total number of fragments by one.
            Repeat until there is only one fragment remaining in the collection.
            This is the de-fragmented line / reassembled document.
         */
        try {
            Readable reader = EncodingReadable.getInstance("UTF8");
            String[] fragmentedDocument = reader.readToStrings(inputPath);
            for (String line : fragmentedDocument) {
                // Search fragmented document for double semi-colon pairs.
                if (line.matches("(.*;{2,}.*|;{2,})")) {
                    throw new IllegalArgumentException("Could not process string. Expected no double semi-colons from input string: " + args[0]);
                }
                // Search document for no semi-colon pairs.
                if (!line.matches("(.+;.+)")) {
                    throw new IllegalArgumentException("Could not process string. Expected at least two fragments from input string: " + args[0]);
                }
            }
            // For each line in the document.
            for (String line : fragmentedDocument) {
                // Create a new document (which is just represented by our desired list of Strings).
                String[] stringsArr = line.split(";");
                // Create a new DocumentCompositor and pass the search strategy (denoting each element we will merge) and merge strategy (denoting how we will perform each merge).
                // The search strategy will be "Search for the next maximally overlapping pair in the document".
                // The merge strategy will be "Merge the overlap of s1 to s2 (assuming there is overlap)".
                // Combining these two strategies in recursion can produce the desired result, so lets do that.
                StringsListSearchStrategy stringsListSearchStrategy = new NextMaximallyOverlappingPair();
                StringMergeStrategy mergeStrategy = new MergeOverlapPair();
                DocumentCompositor documentCompositor = new DocumentCompositor(stringsListSearchStrategy, mergeStrategy, stringsArr);
                // Critical section, we should backup the document.
                documentCompositor.writeBackup();
                // We could handle the merge operation in different ways. If the merge operation fails by illegal state (something went very wrong) we can always choose to restore the backup.
                // In this case we don't want to print additional system output so we cleanly let the application fail and write a log.
                documentCompositor.recursiveMerge();
                String reassembledDocument = documentCompositor.getToString();
                System.out.println(reassembledDocument);
            }
        }
        // Since we are using a Logger we can helpfully collapse the catch blocks to different use cases.
        catch (IllegalArgumentException e) {
            LOGGER.warning(String.valueOf(e));
        } catch (IllegalStateException | IOException e) {
            LOGGER.severe(String.valueOf(e));
            System.exit(2);
        }
    }
}