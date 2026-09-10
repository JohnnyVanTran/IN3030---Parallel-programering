import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class WaitAndSwap {
    final static Semaphore mutex = new Semaphore(1, true);
    final static Semaphore releaseSemaphore = new Semaphore(0, true);
    final static Semaphore waitSemaphore = new Semaphore(0, true);
    final static Semaphore writeSemaphore = new Semaphore(1, true);
    static int count = 0;
    static double variableSpeedRate = 0.0; // threads sleep for a random time between 0 and this rate in milliseconds
    static int extraSlowThreads = 0; // number of threads that sleep 10x variableSpeedRate
    static boolean variableSpeedThreads = true;
    static String filename = "result_oblig5";
    static File reportFile = new File(filename);

    public WaitAndSwap(){
        if (reportFile.exists()) reportFile.delete();
    }

    public static void waitAndSwap(int i) throws InterruptedException {
        mutex.acquire();
        count++;
        mutex.release();

        variSpeed(i, count);
        if (count % 2 != 0){ // for 1., 3., 5., ... call
            if (count % 4 == 1){
                releaseSemaphore.release();
                debugPrintln(i, count, String.format("waiting until %d releases me..", i+2));
                variSpeed(i, count);
                waitSemaphore.acquire();
            } else {
                debugPrintln(i, count, String.format("releasing %d", i-2));
                waitSemaphore.release();
                variSpeed(i, count);
                releaseSemaphore.release();
                variSpeed(i, count);
                waitSemaphore.acquire();
            }
        } else{ // for 2., 4., 6., ... call
            debugPrintln(i, count, "just passing through!");
            releaseSemaphore.acquire();
            variSpeed(i, count);
            if (count % 4 == 0) { 
                waitSemaphore.release();
            }
        }
        
        writeSemaphore.acquire();
        try (FileWriter writer = new FileWriter(reportFile, true)) {
            writer.append(String.format("released thread %d\n", i));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeSemaphore.release();
    }

    static class SwapThread implements Runnable{
        int i;

        public SwapThread(int i){
            this.i = i;
        }

        public void run(){
            try {
                TimeUnit.MILLISECONDS.sleep((long) (i-1)*1000); // let them start in order.
                waitAndSwap(i);
                
            } catch (InterruptedException e) {
                System.err.println("Thread:" + i +  " interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }

    // Used the two next functions from the WaitNextC program:

    public static void variSpeed(int id, int iteration) { // let the calling thread sleep a random time
        long myWait = (long) (Math.random() * variableSpeedRate);
        if (variableSpeedRate == 0.0) return;
        if (id < extraSlowThreads) myWait = (long) (variableSpeedRate * 10.0);
        // make the first <extraSlowThreads> always wait 10xvariableSpeedRate
        debugPrintln(id, iteration, "variSpeed delay: " + myWait + " ms");
        if (variableSpeedThreads)
            try {
                TimeUnit.MILLISECONDS.sleep(myWait);
            } catch (Exception e) { return;};
        debugPrintln(id, iteration, "resuming after variSpeed delay");
    }

    public static void debugPrintln(int id, int iteration, String msg) {
        System.out.println("Thread " + id + ", count " + iteration + ", " + msg);
    }

    public static void main(String[] args) {
        int iteration = 1;
        
        if (args.length < 1) {
            System.out.println("use: java WaitAndSwap <num iterations> create as many threads as there are iterations");
            System.exit(0);
        }
        if (args.length >= 1) {
            iteration = Integer.parseInt(args[0]);
            System.out.println("   threads: " + iteration);
        }
    
        // Creating and starting threads
        for (int i = 1; i <= iteration; i++) {
            Thread thread = new Thread(new SwapThread(i));
            thread.start();
        }
    }
}