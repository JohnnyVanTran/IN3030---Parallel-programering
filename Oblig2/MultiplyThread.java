class MultiplyThread implements Runnable{
    public int id;
    public String type;
    public double[][] a;
    public double[][] b;
    public double[][] destination;
    public int sectionStart;
    public int sectionLength;

    MultiplyThread(int id, String type, double[][] a, double[][] b, double[][] destination,
                    int sectionStart, int sectionLength){
        this.id = id;
        this.type = type;
        this.a = a;
        this.b = b;
        this.destination = destination;
        this.sectionStart = sectionStart;
        this.sectionLength = sectionLength;
    }

    @Override
    public void run() {
        if(this.type.equals("normal")){
            for (int i = sectionStart; i < sectionStart + sectionLength; i++) {
                for (int j = 0; j < a.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        this.destination[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
        }
        else if(this.type.equals("trans_a")){
            for (int i = sectionStart; i < sectionStart + sectionLength; i++) {
                for (int j = 0; j < a.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        this.destination[i][j] += a[k][i] * b[k][j];
                    }
                }
            }    
        }
        else if(this.type.equals("trans_b")){
            for (int i = sectionStart; i < sectionStart + sectionLength; i++) {
                for (int j = 0; j < a.length; j++) {
                    for (int k = 0; k < a.length; k++) {
                        this.destination[i][j] += a[i][k] * b[j][k];
                    }
                }
            } 
        }
    }
}