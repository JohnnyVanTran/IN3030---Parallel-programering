import java.util.Arrays;

class TransposeThread implements Runnable{
    public int id;
    public double[][] matrix;
    public double[][] destination;
    public int sectionStart;
    public int sectionLength;

    TransposeThread(int id, double[][] matrix, double[][] destination, int sectionStart, int sectionLength){
        this.id = id;
        this.matrix = matrix;
        this.destination = destination;
        this.sectionStart = sectionStart;
        this.sectionLength = sectionLength;
    }

    @Override
    public void run() {
        for (int i = sectionStart; i < sectionStart + sectionLength; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.destination[i][j] = this.matrix[j][i];
            }
        }
    }
}