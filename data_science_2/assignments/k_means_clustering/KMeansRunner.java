package k_means_clustering;

import java.util.Scanner;
import java.util.Vector;

public class KMeansRunner {

    private static final String START_UP_GREET = "-++- Greetings, human. I am K-Meansatronator 4000 -++-";
    private static final String START_UP_STATE_PURPOSE = "-++- I can perform K-Means clustering for you. -++-";
    private static final String INPUT_K = "-++- Please enter the desired number of clusters and press enter -++-";
    private static final String INPUT_ITERATIONS = "-++- Enter the desired number of iterations and press enter -++-";
    private static final String INPUT_FAIL = "-++- All you had to do was enter 2 digits. Pffft, humans... -++-";
    private static final String SHUT_DOWN = "-++- Shutting down -++-";
    private static final int K = 0;
    private static final int ITERATIONS = 1;
    private static final int REQUIRED_PARAMETERS = 2;

    private DataExtractor dataExtractor;
    private KMeansClusterer kMeansClusterer;
    private PostProcessor postProcessor;


    public static void main(String[] args) {
        KMeansRunner kMeansRunner = new KMeansRunner();
        kMeansRunner.runKMeans();
    }

    public KMeansRunner() {
        dataExtractor = new DataExtractor();
        postProcessor = new PostProcessor();
    }

    private void runKMeans() {
        System.out.println(START_UP_GREET);
        System.out.println(START_UP_STATE_PURPOSE);
        int[] kAndIterations = getKAndAmountOfIterations();
        if (kAndIterations != null) {
            dataExtractor.extractDataFromCSV();
            kMeansClusterer = new KMeansClusterer(dataExtractor, kAndIterations[K], kAndIterations[ITERATIONS]);
            Vector clusteredResults = kMeansClusterer.performKMeansClustering();
            postProcessor.processClusteredResult(clusteredResults);
        }
        System.out.println(SHUT_DOWN);
    }

    /**prompt user to input K and amount of desired iterations*/
    private int[] getKAndAmountOfIterations() {
        Scanner userInput = new Scanner(System.in);
        int k;
        int iterations;
        int[] storedInput = new int[REQUIRED_PARAMETERS];
        try {
            k = getInputAsString(userInput, INPUT_K);
            iterations = getInputAsString(userInput, INPUT_ITERATIONS);
            storedInput[K] = k;
            storedInput[ITERATIONS] = iterations;
        } catch(Exception e) {
            System.out.println(INPUT_FAIL);
            return null;
        }
        return storedInput;
    }

    private int getInputAsString(Scanner user_input, String message) {
        int input;
        System.out.println(message);
        input = Integer.parseInt(user_input.next());
        return input;
    }
}
