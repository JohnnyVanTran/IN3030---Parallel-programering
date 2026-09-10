import java.util.Arrays;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {            

        int n = 0;
        int k = 0;
        

        //Input checks
        if (!(args.length == 2)) {
            System.out.println("Given args: " + Arrays.toString(args));
            System.out.println("To use the program, give N >= 16 and K (number of threads to be used)");
            System.exit(0);
        }
        try {
            n = Integer.valueOf(args[0]);
        } catch (Exception e) {
            System.out.println("Input cant be converted to Int");
            System.exit(0);
        }

        if (n < 16) {
            System.out.println("N must be equal to or greater than 16");
            System.out.println(n + " was given");
            System.exit(0); 
        }

        try {
            k = Integer.valueOf(args[1]);
        } catch (Exception e) {
            System.out.println("Input cant be converted to Int");
            System.exit(0);
        }

        if (k < 1) {
            int available_threads = Runtime.getRuntime().availableProcessors(); 
            System.out.println("Number of threads given: " + k);
            System.out.println("Default numbers of thread available: " + available_threads);
            k = available_threads;
        }

        //Starting sieve comparison
        System.out.println("Sequential sieve performace: ");
        double[] seq_sieve_times = new double[7];
        for (int i = 0; i < 7; i++) {
            long start = System.nanoTime();

            //Using given class of Sieve
            SieveOfEratosthenes seq = new SieveOfEratosthenes(n);
            int[] primes = seq.getPrimes();
            double end = (double) (System.nanoTime() - start) / 1000000;
            seq_sieve_times[i] = end;
            System.out.printf("Run nr %d: %f ms\n", i, end);
        }

        System.out.println("Parallel sieve performace: ");
        double[] para_sieve_times = new double[7];
        for (int i = 0; i < 7; i++) {
            long start = System.nanoTime();

            //Using Thread Class ParaSieve
            ParaSieve para = new ParaSieve(n, k);
            int[] primes = para.get_primes();
            double end = (double) (System.nanoTime() - start) / 1000000;
            para_sieve_times[i] = end;
            System.out.printf("Run nr %d: %f ms\n", i, end);
        }
        System.gc();

        Arrays.sort(seq_sieve_times);
        Arrays.sort(para_sieve_times);
        System.out.println("\nMedians:");
        System.out.println("Sequential: " + seq_sieve_times[4] + " ms");
        System.out.println("Parallel: " + para_sieve_times[4] + " ms");
        System.out.println("Speed up ratio: " + (double) ((seq_sieve_times[4] / para_sieve_times[4])));

        System.out.println("------------------------------");
        System.out.println("Sequential factorization performace: ");
        double[] seq_factor_times = new double[7];
        for (int i = 0; i < 7; i++) {
            long start = System.nanoTime();
            SequentialFactorization seq = new SequentialFactorization(n);
            Oblig3Precode writer_seq = seq.factorize();
            writer_seq.writeFactors("Seq");
            double end = (double) (System.nanoTime() - start) / 1000000;
            seq_factor_times[i] = end;
            System.out.printf("Run nr %d: %f ms\n", i, end);
        }


        System.out.println("\n------------------------------");
        System.out.println("Parallel factorization performace: ");
        double[] para_factor_times = new double[7];
        for (int i = 0; i < 7; i++) {
            long start = System.nanoTime();
            ParallelFactorization para = new ParallelFactorization(n, 8);
            Oblig3Precode writer_para =  para.factorize();
            writer_para.writeFactors("Para");
            double end = (double) (System.nanoTime() - start) / 1000000;
            para_factor_times[i] = end;
            System.out.printf("Run nr %d: %f ms\n", i, end);
        }
        Arrays.sort(seq_factor_times);
        Arrays.sort(para_factor_times);
        System.out.println("\nMedians:");
        System.out.println("Sequential: " + seq_factor_times[4] + " ms");
        System.out.println("Parallel: " + para_factor_times[4] + " ms");
        System.out.println("Speed up ratio: " + (double) ((seq_factor_times[4] / para_factor_times[4])));
    }
}