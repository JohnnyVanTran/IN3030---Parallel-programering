import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ParaSieve {

    public int n, k;
    public SieveMonitor monitor;

    public ParaSieve(int n, int threadCount) {
        this.n = n;
        this.k = threadCount;      
        this.monitor = new SieveMonitor(this.n);
    }
    
    private void start() {
        Thread[] threads = new Thread[k];
        int value_Start = 3;
        int value_Length = n / this.k;
        int rest = ((value_Length * n) % 16);

        for (int i = 0; i < this.k - 1; i++) {
            int value_end = value_Start + value_Length;
            if (value_end > n) {
                value_end = n;
            }
            threads[i] = new Thread(
                new SieveWorker(i, this.monitor, value_Start, value_end + rest, this.monitor.initial_primes));
            value_Start = value_end + 1;
        }
        //Thread with the remaining aswell
        threads[this.k - 1] = new Thread(new SieveWorker(this.k - 1, this.monitor, value_Start + rest, n, this.monitor.initial_primes));
        
        for (int i = 0; i < this.k; i++) {
            threads[i].start();
        }

        for (int i = 0; i < this.k; i++) {
            try { threads[i].join(); } catch (InterruptedException e) {}
        }
    }

    public int[] get_primes() {
        this.start();
        return monitor.get_primes();
    }

}
