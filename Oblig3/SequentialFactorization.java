public class SequentialFactorization {

    private SieveOfEratosthenes sieve;
    private Oblig3Precode writer;
    private int n;
    private int[] primes;
    private long end;

    SequentialFactorization(int n){
        this.n = n;
        this.end = (long)n * n;
        this.writer = new Oblig3Precode(n);
        this.sieve = new SieveOfEratosthenes(n);
        this.primes = this.sieve.getPrimes();
    }

    public Oblig3Precode factorize(){
        end -= 100;
        for (long i = end; i < end + 100; i++) {
            long temp = i;
            for (int j = 0; j < primes.length; j++) {
                if (temp % primes[j] == 0) {
                    writer.addFactor(i, (long)primes[j]);
                    temp /= (long)primes[j];
                    j --;
                }
            }
            if (temp != 1) writer.addFactor(i, temp);
        }
        return this.writer;
    }
}
