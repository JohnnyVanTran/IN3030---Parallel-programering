import java.util.Arrays;

public class Main{
    public static void main(String[] args) {
        int n = 0;
        int seed = 0;
        int threads = 0;
        try{
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
            threads = Integer.parseInt(args[2]);
        }catch(Exception e){
            System.out.println("write in this format: java Main n(number of points) seed threads(number of threads)");
            System.out.println("Example: java Testprog 1000 420 2");
            return;
        }
        //Sequential
        Sequential s = new Sequential(n,seed);
        float [] timeSeq = new float[7];
        IntList res = null;
        for (int i = 0; i<7;i++){
            timeSeq[i] = System.nanoTime();
            res = s.calcHull();
            timeSeq[i] = (System.nanoTime()-timeSeq[i]) / 1000000; //1000000
            System.out.println(timeSeq[i]);
        }
        Arrays.sort(timeSeq);
        //Median
        System.out.println("Seq: time " + timeSeq[3] + "ms");
        System.out.println(timeSeq[0]);


        //Paralell TEST
        Parallel p = new Parallel(n,seed,threads);
        float [] timesPara = new float[7];
        IntList resp = null;
        for (int i = 0; i<7;i++){
            timesPara[i] = System.nanoTime();
            resp = p.calcHull();
            timesPara[i] = (System.nanoTime()-timesPara[i]) / 1000000;
            System.out.println(timesPara[i]);
        }
        Arrays.sort(timesPara);
        System.out.println("par: time " + timesPara[3] + "ms");
        System.out.println("Speedup: " + (timeSeq[3]/timesPara[3]));

        for (int i = 0; i< res.len; i++){
            if (res.get(i) != resp.get(i)){
                System.out.println("ERROR!, " + res.get(i) + "not equal " +resp.get(i));
            }
        }
        /* System.out.println("DONE");
        Oblig4Precode precode  =  new Oblig4Precode(s,res);
        precode.writeHullPoints();
        System.out.println("Hull points:");
        precode.theCoHull.print();
        precode.drawGraph(); */
    }
}