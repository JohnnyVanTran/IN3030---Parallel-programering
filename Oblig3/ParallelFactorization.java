import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Parallel
 */
public class ParallelFactorization {
    public int thread_count;
    public ParaSieve paraSieve;
    public Oblig3Precode writer;
    public int[] primes;
    public long[] to_factor = new long[100];

    ParallelFactorization(long n, int thread_count){
        this.thread_count = thread_count;
        this.writer = new Oblig3Precode((int)n);
        
        this.paraSieve = new ParaSieve((int)n, thread_count);
        this.primes = paraSieve.get_primes();
        // System.out.println(Arrays.toString(primes));
        long end = (long) n*n;
        for (int i = 0; i < 100; i++) {
            this.to_factor[i] = end - i - 1;
        }
    }

    public Oblig3Precode factorize(){
        TreeMap<Long, LinkedList<Long>> result_bucket = new TreeMap<>();
        for (int i = 0; i < this.to_factor.length; i++) {
            result_bucket.put(this.to_factor[i], new LinkedList<Long>());
        }
        FactorMonitor monitor = new FactorMonitor(this.to_factor, result_bucket);
        // System.out.println(Arrays.toString(monitor.to_factor));
        int table_size = this.primes.length / this.thread_count + 1;
        int[][] all_the_tables = new int[thread_count][table_size];
        int primes_index = 0;
        for (int i = 0; i < table_size; i++) {
            for (int j = 0; j < thread_count; j++) {
                if(primes_index == this.primes.length){
                    all_the_tables[j][i] = 0;
                }
                else{
                    int prime_number = this.primes[primes_index];
                    all_the_tables[j][i] = prime_number;
                    primes_index ++;
                }
            }
        }
        // System.out.println(Arrays.deepToString(all_the_tables));
        Thread[] threads = new Thread[thread_count];
        for (int i = 0; i < this.thread_count; i++) {
            int[] work = all_the_tables[i];
            threads[i] = new Thread(new FactorWorker(i, work, monitor));
        }

        for (int i = 0; i < thread_count; i++) {
            threads[i].start();
        }
        // System.out.println("All threads started");

        for (int i = 0; i < thread_count; i++) {
            try { threads[i].join();} catch (Exception e) {}
        }
        // System.out.println("All threads done");
    
        writer.factors = monitor.result_bucket;

        return this.writer;
    }
    
}