package genetic_algorithm;

import java.util.Random;

/**
 * Created by Jeroen Stravers on 07-Jul-16.
 */
public class PostProcessor {

    public static void main (String[] args) {
        int foo = Integer.parseInt("01111", 2);
        System.out.println(foo);


        //for(int i = 0; i < 50; i++){
        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt(2);
        String[] bob = generateInitialGenes();
            for(String gene: bob) {System.out.println(gene);}
        //System.out.println(generateInitialGenes());}
    }

    private static String[] generateInitialGenes(){
        Random rand = new Random();
        String[] generatedGenes = new String[32];
        for (int i = 0; i < 32; i++) {
            String geneSequence = "";
            for (int j = 0; j < 5; j++){
                int gene = rand.nextInt(2);
                geneSequence += gene;
            }
            generatedGenes[i] = geneSequence;
        }
        return generatedGenes;
    }


    }

