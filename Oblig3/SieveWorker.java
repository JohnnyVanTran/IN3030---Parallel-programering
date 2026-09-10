import java.util.Arrays;

public class SieveWorker implements Runnable{
    public int id;
    public SieveMonitor m;
    public byte[] byteArray;
    public int[] work;
    public int value_start;
    public int value_end;
    public int num_primes;

    public SieveWorker(int id, SieveMonitor m, int value_start, int value_end, int[] work){
        this.id = id;
        this.m = m;
        m.total_threads ++;
        this.byteArray = m.byteArray;
        this.work = work;
        this.value_start = value_start;
        this.value_end = value_end;
        // System.out.printf("ID: %d val_s: %d  val_e: %d  work: %s\n", id, value_start, value_end, Arrays.toString(work));
        // System.out.println(Arrays.toString(work));
    }

    @Override
    public void run(){
        for (int i = 1; i < work.length; i++) {
            int prime = work[i];
            traverse(prime);
        }
        m.done_phase_one();
        for(int i = this.value_start; i <= this.value_end; i++){
            if(m.isPrime(i)){
                this.num_primes ++;
            }
        }
        m.prime_count.getAndAdd(num_primes);
    }

    private void mark(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;
        byteArray[byteIndex] |= (1 << bitIndex);
    }

    private void traverse(int prime) {
        for (int i = prime * prime; i <= value_end; i += prime * 2) mark(i);
    }
}