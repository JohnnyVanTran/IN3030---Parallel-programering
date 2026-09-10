import java.util.Arrays;

public class Sequential {

    //Return the transposed version of a given matrix
    public static double[][] transpose( double[][] m ) {
        double[][] t = new double[m.length][m.length];
        
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m.length; j++) {
                t[i][j] = m[j][i];
            }
        }
        return t;
    }

    //Tripple for-loop action 
    public static double[][] classicAlgo( double[][] m1, double[][] m2 ) {

        double[][] m3 = new double[m1.length][m1.length];

        for(int i = 0; i < m1.length; i++) {
            for(int j = 0; j < m1.length; j++) {
                for(int k = 0; k < m1.length; k++) {
                    m3[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return m3;
    }

    public static double[][] multiplyTransposedA( double[][] m1, double[][] m2 ) {
        m1 = transpose(m1);
        double[][] m3 = new double[m1.length][m1.length];

        for(int i = 0; i < m1.length; i++) {
            for(int j = 0; j < m1.length; j++) {
                for(int k = 0; k < m1.length; k++) {
                    m3[i][j] += m1[k][i] * m2[k][j];
                }
            }
        }
        return m3;
    }
    public static double[][] multiplyTransposedB( double[][] m1, double[][] m2 ) {
        m2 = transpose(m2);
        double[][] m3 = new double[m1.length][m1.length];

        for(int i = 0; i < m1.length; i++) {
            for(int j = 0; j < m1.length; j++) {
                for(int k = 0; k < m1.length; k++) {
                    m3[i][j] += m1[i][k] * m2[j][k];
                }
            }
        }
        return m3;
    }
}