import java.util.Arrays;

public class Parallel {

    public static double[][] transpose(double[][] m){

        double[][] trans = new double[m.length][m.length];
        int cores = Runtime.getRuntime().availableProcessors();

        //Find number of thread
        int numberOfThreads = 0;
        if (m.length > cores) {
            numberOfThreads = cores;
        } else {
            numberOfThreads = m.length;
        }
        //Find workload for thread
        int workload = m.length / numberOfThreads;
        int extraWork = m.length % numberOfThreads;         //If the m.length / numberOfThreads has a remainder
                                                            //store it and give it to some of the strings
        Thread[] threads = new Thread[numberOfThreads];     
        int sectionStart = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            //Give the extra work to the first thread
            if(i == 0){
                threads[i] = new Thread(new TransposeThread(i, m, trans, sectionStart, (workload + extraWork)));
                sectionStart += workload + extraWork; 
                continue;
            }
            threads[i] = new Thread(new TransposeThread(i, m, trans, sectionStart, workload));
            sectionStart += workload; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try { threads[i].join(); } 
            catch (Exception e) {}
        }
        return trans;
    }

    public static double[][] multiplyNormal(double[][] a, double[][] b){

        double[][] product = new double[a.length][a.length];
        int cores = Runtime.getRuntime().availableProcessors();
        //Find number of thread
        int numberOfThreads = 0;
        if (a.length > cores) {
            numberOfThreads = cores;
        } else {
            numberOfThreads = a.length;
        }

        int workload = a.length / numberOfThreads;
        int extraWork = a.length % numberOfThreads;

        Thread[] threads = new Thread[numberOfThreads];
        int section_start = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            if(i == 0){
                threads[i] = new Thread(new MultiplyThread(i, "normal", a, b, product, section_start, (workload + extraWork)));
                section_start += workload + extraWork; 
                continue;
            }
            threads[i] = new Thread(new MultiplyThread(i, "normal", a, b, product, section_start, workload));
            section_start += workload; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try { threads[i].join(); } 
            catch (Exception e) {}
        }
        return product;
    }

    public static double[][] multiplyTransposedAThread(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        double[][] trans_a = transpose(a);

        int cores = Runtime.getRuntime().availableProcessors();
        int numberOfThreads = 0;
        if (a.length > cores) {
            numberOfThreads = cores;
        } else {
            numberOfThreads = a.length;
        }

        int workload = a.length / numberOfThreads;
        int extraWork = a.length % numberOfThreads;

        Thread[] threads = new Thread[numberOfThreads];
        int section_start = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            if(i == 0){
                threads[i] = new Thread(new MultiplyThread(i, "trans_a", trans_a, b, product, section_start, (workload + extraWork)));
                section_start += workload + extraWork; 
                continue;
            }
            threads[i] = new Thread(new MultiplyThread(i, "trans_a", trans_a, b, product, section_start, workload));
            section_start += workload; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try { threads[i].join(); } 
            catch (Exception e) {}
        }
        return product;
    }

    public static double[][] multiplyTransposedBThread(double[][] a, double[][] b){
        double[][] product = new double[a.length][a.length];
        double[][] trans_b = transpose(b);

        int cores = Runtime.getRuntime().availableProcessors();
        int numberOfThreads = 0;
        if (a.length > cores) {
            numberOfThreads = cores;
        } else {
            numberOfThreads = a.length;
        }

        int workload = a.length / numberOfThreads;
        int extraWork = a.length % numberOfThreads;

        Thread[] threads = new Thread[numberOfThreads];
        int section_start = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            if(i == 0){
                threads[i] = new Thread(new MultiplyThread(i, "trans_b", trans_b, b, product, section_start, (workload + extraWork)));
                section_start += workload + extraWork; 
                continue;
            }
            threads[i] = new Thread(new MultiplyThread(i, "trans_b", trans_b, b, product, section_start, workload));
            section_start += workload; 
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try { threads[i].join(); } 
            catch (Exception e) {}
        }
        return product;
    }


}