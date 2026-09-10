class Parallel extends ConvexHull{
    int threads;
    public Parallel(int n, int seed, int threads){
        super(n,seed, new NPunkter17(n, seed));
        this.threads = threads;
        if (threads<1){
            this.threads = Runtime.getRuntime().availableProcessors();
        }
    }
    public IntList calcHull(){
        this.hull = new IntList();
        IntList nums = points.lagIntList();
        hull.add(MIN_X);
        IntList monitor = new IntList();	//hull for thread to add things to.
        if (threads > 1){ //we have multiple threads, lets go!
            Thread t0 = new Thread(new ConvexThread(this, (threads/2) + (threads%2), MIN_X, MAX_X, nums, hull));
            t0.start();
            Thread t1 = new Thread(new ConvexThread(this, threads/2, MAX_X, MIN_X, nums, monitor));
            t1.start();
            try{
                t0.join();
                hull.add(MAX_X);
                t1.join();
                hull.append(monitor);
            }catch (Exception e){
                System.out.println(e);
            }
        } else { //BRRR ONLY 1 thread, like wtf dude? give me atleast 2
            Thread t = new Thread(new ConvexThread(this,0,MIN_X,MAX_X,nums,this.hull));
            t.start();
            try{
                t.join();
            }catch (Exception e){
                System.out.println(e);
            }
            hull.add(MAX_X);
            t = new Thread(new ConvexThread(this,0,MAX_X,MIN_X,nums,this.hull));
            t.start();
            try{
                t.join();
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return hull;
    }
}