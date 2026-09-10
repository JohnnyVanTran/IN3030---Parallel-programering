import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Arrays;
import java.text.DecimalFormat;

class Main2 {

    public static void main(String[] args) {
        
        int n = 1000;
        System.out.println("N" + ";" + "K" + ";"  + "Parallel" + ";" + "Synch");

        for (int i = n; i < 100_000_001; i = i * 10) {
            System.out.print(i + ";" + 100 + ";");
            Main2 m = new Main2(i, 100);
            m.makeThreads();
            m.makeSequential();
        }
        
        System.out.print("\n");

        for (int i = n; i < 100_000_001; i = i * 10) {
            System.out.print(i + ";" + 20 + ";");
            Main2 m = new Main2(i, 20);
            m.makeThreads();
            m.makeSequential();
        }

        System.out.print("\n");
        
        for (int i = n; i < 100_000_001; i = i * 10) {
            System.out.print(i + ";" + 20 + ";");
            Main2 m = new Main2(i, 20);
            m.makeThreads();
            m.makeMainInsert();
        }
    }

    int[] allNumbers;    //all numbers
    int[][] endPoint;    //where all the biggest number end
    int[] sortedNumbers; //final array with the k-biggest numbers
    int size;            //size of the arrays that is supposed to contain the biggest numbers
    Random r;
    DecimalFormat d = new DecimalFormat("# ##0.000");
    
    public Main2(int n, int k) {
        r = new Random(1234);
        allNumbers = r.ints(n).toArray();
        size = k;
        sortedNumbers = null;
    }

    void makeMainInsert() {
        int[] arr = new int[size];
        int currentSize = 0;

        long startTime = System.nanoTime();
        while (currentSize < size) {
            arr[currentSize] = allNumbers[currentSize];
            insertSort(arr, currentSize);
            currentSize++;
        }
        for (int i = currentSize; i < allNumbers.length; i++) {
            if (arr[currentSize - 1] < allNumbers[i]) {
                arr[currentSize - 1] = allNumbers[i];
                insertSort(arr, currentSize - 1);
            }
        }
        long endTime = System.nanoTime();  
        double totalTime = (endTime - startTime) * 1e-6;
        System.out.println(";" + d.format(totalTime));            
    }


    void makeSequential() {
         //Starter klokke for Arrays.sort(int[] arr)
        //Sekventsiell
        int[] arr = allNumbers;
        int[] sortedArr = new int[size];
        long startTime = System.nanoTime();

        //Bruker ikke array i monitor
        Arrays.sort(arr);
        for (int i = 0; i < size; i++) {
            sortedArr[i] = arr[arr.length - 1 - i];
        }
        long endTime = System.nanoTime();
        double totalTime = (endTime - startTime) * 1e-6;
        System.out.println(";" + d.format(totalTime));
        //System.out.println("Array : " + Arrays.toString(sortedArr));
    }

    void makeThreads() {
        int cores = Runtime.getRuntime().availableProcessors();
        int checkSize = allNumbers.length / cores;     //deler antall cores på n for å dele
        Thread[] threads = new Thread[cores];
        endPoint = new int[cores][size];
        //Klokke
        long startTime = System.nanoTime();              
        for (int i = 0; i < cores; i++) {
            Thread t = new Thread(new threadSort(i * checkSize,    //start
                                                ((i + 1) * checkSize),  //finish
                                                i));    //index where the numbers are supposed to be stored
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
        for (int[] threadList : endPoint) {
            if (sortedNumbers == null) {
                sortedNumbers = threadList;
            } else {
                for (int i = 0; i < size; i++) {
                    if (threadList[i] > sortedNumbers[size - 1]) {
                        sortedNumbers[size - 1] = threadList[i];
                        insertSort(sortedNumbers, size - 1);
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        double totalTime = (endTime - startTime) * 1e-6;
        System.out.print(d.format(totalTime));
        //System.out.println("Array : " + Arrays.toString(sortedNumbers));
    }

    void insertSort(int[] arr, int end) {
            for (int i = end; i > 0; i--) {
                if (arr[i] > arr[i - 1]) {
                    int t = arr[i];
                    arr[i] = arr[i - 1];
                    arr[i - 1] = t;
                } else {
                    break;
                }
            }
        }

    class threadSort implements Runnable {

        int[] storage;  //where i store numbers
        int start;      //start of section
        int finish;     //end of section
        int index;      //index where storage is supposed to be stored

        public threadSort(int s, int f, int index) {
            start = s;
            finish = f;
            storage = new int[size];
            this.index = index;
        }

        @Override
        public void run() {
            int currentSize = 0;

            while (currentSize < size) {
                storage[currentSize] = allNumbers[start + currentSize];
                insertSort(storage, currentSize);
                currentSize++;
            }
            for (int i = start + currentSize; i < finish; i++) {
                if (storage[currentSize - 1] < allNumbers[i]) {
                    storage[currentSize - 1] = allNumbers[i];
                    insertSort(storage, currentSize - 1);
                }
            }
            endPoint[index] = storage;
        }
    }
}