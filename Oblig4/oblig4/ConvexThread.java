public class ConvexThread implements Runnable{
    private IntList hull;
    public ConvexHull p;
    private IntList v_points;
    private int start_idx;
    private int stop_idx;
    private int thread_amount;

    public ConvexThread(ConvexHull parent_p, int thread_amount, int start, int stop, IntList validpoints, IntList hullpoints){
        this.p = parent_p;
        this.hull = hullpoints;
        this.stop_idx = stop;
        this.start_idx = start;
        this.v_points = validpoints;
        this.thread_amount = thread_amount;
    }

    public void run(){
        recursive_function(start_idx, stop_idx, v_points);
    }

    private void recursive_function(int start, int stop, IntList validpoints){
        int a = p.y[start] - p.y[stop] ;
        int b = p.x[stop] - p.x[start];
        int c = (p.y[stop]*p.x[start]) - (p.y[start]*p.x[stop]);
        IntList possible_points = new IntList();	//list of possible points

        int d = 0;
        boolean found_min = false;
        int d_min = 1;
        int d_idx = 0;
        IntList zero_dist_idx = new IntList();

        for (int i=0; i<validpoints.len; i++){
            d = a*p.x[validpoints.get(i)] + b*p.y[validpoints.get(i)] + c;
            if (d < 0){ //find all possible points (0 or lower).
                if (!found_min){ //if we have found none yet.
                    d_min = d ;
                    found_min = true;
                    d_idx = validpoints.get(i);
                }else if (d<d_min){
                    d_min = d ;
                    possible_points.add(d_idx);
                    d_idx = validpoints.get(i);
                }else{
                    possible_points.add(validpoints.get(i));
                }
            }else if(d == 0){
                zero_dist_idx.add(validpoints.get(i));
            }
        }
        if (found_min && thread_amount<=1){ //recursive in front
            recursive_function(start,d_idx,possible_points);
            hull.add(d_idx); //add midpoint
            recursive_function(d_idx,stop,possible_points);
        }else if(found_min){//start threads
            //new thread
            IntList l1 = new IntList();
            Thread t = new Thread(new ConvexThread(p,thread_amount/2, d_idx,stop,possible_points,l1));
            t.start();

            //this thread
            this.thread_amount = thread_amount-(thread_amount/2);
            recursive_function(start,d_idx,possible_points);
            this.hull.add(d_idx);
            //join the new thread and hope it used almost the same amount of time :)
            try{
                t.join();
                hull.append(l1);
            }catch(Exception e){
                System.out.println(e);
            }
        } else{	//we are at the edge and need to add the corners.
            int min = 0;
            d = 0;
            int d_x;
            int d_y;
            IntList distance_zero = new IntList(zero_dist_idx.len);
            for (int i = 0; i<zero_dist_idx.len; i++){ //hopefully a low number
                d_x = p.x[start]-p.x[zero_dist_idx.get(i)];
                d_y = p.y[start]-p.y[zero_dist_idx.get(i)];
                distance_zero.add((d_x*d_x) + (d_y*d_y)); //dist square from start
            }
            p.sort(zero_dist_idx,distance_zero);
            hull.append(zero_dist_idx);
        }
    }
}