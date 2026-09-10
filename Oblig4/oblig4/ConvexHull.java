public class ConvexHull {
    int n, seed, MAX_X, MAX_Y, MIN_X, MIN_Y;
    int x[], y[];

    //points
    NPunkter17 points;
    IntList hull;

    ConvexHull(final int n, final int seed, final NPunkter17 nPunkter17) {
        this.n = n;
        this.seed = seed;
        this.x = new int[n];
        this.y = new int[n];
        
        points = nPunkter17;

        points.fyllArrayer(x, y);
        for (int i = 0; i < n; i++) {
            if (x[i] > x[MAX_X])
                MAX_X = i;
            else if (x[i] < x[MIN_X])
                MIN_X = i;
            if (y[i] > y[MAX_Y])
                MAX_Y = i;
        }
    }

    /* public static void main(String[] args) {
        final int n = 10;
        final int seed = 42;
        NPunkter17 nPunkter17 = new NPunkter17(n, seed);
        ConvexHull ch = new ConvexHull(n, seed, new NPunkter17(n, seed));
        IntList coHull = nPunkter17.lagIntList();
        Oblig4Precode precode = new Oblig4Precode(ch, coHull);
        precode.drawGraph();
    } */
    
    public void sort(IntList idx, IntList length){
            this.internal_sort(idx, length, idx.len);
	}
    private void internal_sort(IntList idx, IntList length, int n){
        if (n <= 1){	//done
            return;
        }
        internal_sort( idx, length, n - 1 );        //one element sorted, sort rest
        int last_idx = idx.get(n - 1);              //last element of the array
        int last_dist = length.get(n - 1);
        int j = n - 2;                               
        while (j >= 0 && length.get(j) > last_dist){        //find the correct index of the last element
            length.add(length.get(j),j+1);         //shift section of sorted elements up by one element if correct index is not found
            idx.add(idx.get(j),j+1);
            j--;
        }
        length.add(last_dist,j+1);   //setting the last element
        idx.add(last_idx,j+1);
    }
}