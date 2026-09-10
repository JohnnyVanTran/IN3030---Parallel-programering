import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Arrays;

class Main {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);

        A2 monitor = new A2(n);
        A2 monitor2 = new A2(n);

        //Starter klokke for Arrays.sort(int[] arr)
        //Sekventsiell
        /* long startTime2 = System.nanoTime();

        //Bruker ikke array i monitor
        int[] arr = new int[k];
        int length = monitor2.allNumbers.length;
        Arrays.sort(monitor2.allNumbers);

        for (int i = 0; i < k; i++) {
            arr[i] = monitor2.allNumbers[length - 1 - i];
        }
        long endTime2 = System.nanoTime();
        long totalTime2 = endTime2 - startTime2;
        System.out.println("Total time Sequence used was: " + totalTime2);
        System.out.println("Array : " + Arrays.toString(arr)); */


        //Starts making threads
        int cores = Runtime.getRuntime().availableProcessors();
        int size = n / cores;     //deler antall cores på n for å dele
        Thread[] threads = new Thread[cores];

        //Klokke
        long startTime = System.nanoTime();              
        for (int i = 0; i < cores; i++) {
            Thread t = new Thread(new InsertSortThread(monitor, i * size, ((i + 1) * size), k));
            threads[i] = t;
            t.start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (Exception e){
                System.out.println("Thread runtime exception");
            }
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total time Threads used was:  " + totalTime);
        System.out.println("Array : " + Arrays.toString(monitor.numbers));
    }
}

class A2 {

    private Lock lock = new ReentrantLock();
    private Condition con = lock.newCondition();
    int[] allNumbers;
    int[] numbers;
    Random r;

    // Oppretter liste med tilfeldig tall og numbers
    // som er listen med det K-største tallene
    public A2(int n) {
        r = new Random(1234);
        numbers = null;
        allNumbers = r.ints(n).toArray();
    }

    public void insertSort(int[] arr, int element) {

        if (element > arr[arr.length - 1]) {
            arr[arr.length - 1] = element;
        } else {
            return;
        }
        for (int i = arr.length - 1; i > 0; i--) {
            if (arr[i] > arr[i - 1]) {
                int temp = arr[i - 1];
                arr[i - 1] = arr[i];
                arr[i] = temp;
            } else {
                break;
            }
        }
    }

    public void insertReversedFilled(int[] arr, int startFrom, int element) {
        int temp = 0;
        arr[startFrom] = element;

        for (int i = startFrom; i > 0; i--) {
            if (arr[i] > arr[i - 1]) {
                temp = arr[i - 1];
                arr[i - 1] = arr[i];
                arr[i] = temp;
            } else {
                break;
            }
        }
    }

    public void finishSort(int[] arr) {
        lock.lock();
        try {
            if (numbers == null) {
                numbers = arr;
                return;
            } else {
                for (int i = 0; i < numbers.length - 1; i++) {
                    insertSort(numbers, arr[i]);
                }
            }
        } finally {
            lock.unlock();
        }
        
    }
}

class InsertSortThread implements Runnable {

    A2 monitor;
    int start;    //hvor man starter på arrayet som inneholder n-elementer til monitor
    int finish;   //hvor man avslutter på arrayet som inneholder n-elementer til monitor
    int[] storage;

    public InsertSortThread(A2 monitor, int start, int finish, int size) {
        this.monitor = monitor;
        this.start = start;
        this.finish = finish;
        storage = new int[size];
    }

    @Override
    public void run() {
        storage[0] = monitor.allNumbers[start];
        int counter = 1;

        for (int i = start + 1; i < storage.length; i++) {
            monitor.insertReversedFilled(storage, counter, monitor.allNumbers[i]);
        }
        for (int i = start + storage.length - 1; i < finish; i++) {
            monitor.insertSort(storage, i);
        }   
        monitor.finishSort(storage);
    }
}