class Sequential extends ConvexHull{

    public Sequential(int n, int seed){
        super(n,seed, new NPunkter17(n, seed));
    }
    public IntList calcHull(){
        this.hull  =  new IntList();
        IntList nums = points.lagIntList();
        hull.add(MIN_X);
        recursive_function(MIN_X, MAX_X, nums);
        hull.add(MAX_X);
        recursive_function(MAX_X, MIN_X, nums);
        return hull;
    }

    private void recursive_function(int start, int stop, IntList validpoints){

        int a = y[start] - y[stop];
        int b = x[stop] - x[start];
        int c = (y[stop]*x[start]) - (y[start]*x[stop]);
        IntList possible_points = new IntList();

        int d = 0;
        boolean found_min = false;
        int d_min = 1;
        int d_idx = 0;
        //list when all values are ether 0 or higher.
        IntList zero_dist_idx  =  new IntList();

        for (int i = 0; i < validpoints.len; i++){
            d = a*x[validpoints.get(i)] + b*y[validpoints.get(i)] + c;
            //finding all points d <= 0
            if (d<0){
                //If we havent found the lowest yet
                if (!found_min){
                    d_min = d;
                    found_min = true;
                    d_idx = validpoints.get(i);
                }else if (d < d_min){
                    d_min = d;
                    //Adding the last lowest
                    possible_points.add(d_idx);
                    d_idx = validpoints.get(i);
                }else{
                    possible_points.add(validpoints.get(i));
                }
            }else if(d == 0){
                zero_dist_idx.add(validpoints.get(i));
            }
        }
        //recursive in front
        if (found_min){
            recursive_function(start,d_idx,possible_points);
            hull.add(d_idx); //add midpoint
            recursive_function(d_idx,stop,possible_points);
        } 
        //If only 0 is found, we have to sort them in the right order
        else {	
            int min = 0;
            d = 0;
            int d_x;
            int d_y;
            IntList distance_zero = new IntList(zero_dist_idx.len);
            for (int i = 0; i < zero_dist_idx.len; i++){
                d_x = x[start] - x[zero_dist_idx.get(i)];
                d_y = y[start] - y[zero_dist_idx.get(i)];
                distance_zero.add((d_x*d_x) + (d_y*d_y));
            }
            this.sort(zero_dist_idx, distance_zero);
            hull.append(zero_dist_idx);
        }
    }
}
