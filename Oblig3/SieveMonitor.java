import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SieveMonitor{
    public byte[] byteArray;
    public int n;
    public int[] initial_primes;


    public int currentPrime_index = 1;
    public final Lock lock = new ReentrantLock();
    public AtomicInteger prime_count = new AtomicInteger(0);
    public final Condition wait_for_all_finish = lock.newCondition();
    
    public int total_threads = 0;
    public int num_threads_done_phase_1 = 0;


    public SieveMonitor(int n){
        this.n = n;
        int cells = n / 16 + 1;
        this.byteArray = new byte[cells];

        //Finding primes up to squareroot of n + 1
        SieveOfEratosthenes seqSieve = new SieveOfEratosthenes((int) Math.sqrt(n) + 1);
        this.initial_primes = seqSieve.getPrimes();
    }

    public void done_phase_one(){
        lock.lock();
        try{
            this.num_threads_done_phase_1 ++;
            if(num_threads_done_phase_1 == total_threads){
                wait_for_all_finish.signalAll();
            }
            else{
                try { wait_for_all_finish.await();} catch (Exception e) {}
            }
        }
        finally{
            lock.unlock();
        }
    }

    public int[] get_primes(){
        int num_primes = this.prime_count.get() + 1;

        int[] primes = new int[num_primes];
        primes[0] = 2;
        int currentPrime = 3;
        for (int i = 1; i < num_primes; i++) {
            primes[i] = currentPrime;
            currentPrime = findNextPrime(currentPrime + 2);
        }
        return primes;
    }
    

    private int findNextPrime(int startAt) {
        for (int i = startAt; i < n; i += 2) {
            if(isPrime(i)) {
                return i;
            }
        }
        return 0;
    }
    
    public boolean isPrime(int i){
        if((i % 2) == 0){
            return false;
        }
        int byteCell = i / 16;
        int bit = (i / 2) % 8;
        return (byteArray[byteCell] & (1 << bit)) == 0;
    }
}