import java.util.Scanner;

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


    public static void main(String[] args) {
        KMeansRunner kMeansRunner = new KMeansRunner();
        kMeansRunner.runKMeans();
    }

    public KMeansRunner() {
        dataExtractor = new DataExtractor();
    }

    private void runKMeans() {
        System.out.println(START_UP_GREET);
        System.out.println(START_UP_STATE_PURPOSE);
        int[] kAndIterations = getKAndAmountOfIterations();
        if (kAndIterations != null) {
            dataExtractor.extractDataFromCSV();
            kMeansClusterer = new KMeansClusterer(dataExtractor, kAndIterations[K], kAndIterations[ITERATIONS]);
        }
        System.out.println(SHUT_DOWN);
    }

    private int[] getKAndAmountOfIterations() {
        Scanner user_input = new Scanner(System.in);
        int k;
        int iterations;
        int[] storedInput = new int[REQUIRED_PARAMETERS];
        try {
            k = getInputAsString(user_input, INPUT_K);
            iterations = getInputAsString(user_input, INPUT_ITERATIONS);
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
        System.out.println(INPUT_K);
        input = Integer.parseInt(user_input.next());
        return input;
    }
}
