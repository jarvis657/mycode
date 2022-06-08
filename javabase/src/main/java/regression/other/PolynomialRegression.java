package regression.other;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PolynomialRegression} class performs a polynomial regression
 * on an set of <em>N</em> data points (<em>y<sub>i</sub></em>, <em>x<sub>i</sub></em>).
 * That is, it fits a polynomial
 * <em>y</em> = &beta;<sub>0</sub> +  &beta;<sub>1</sub> <em>x</em> +
 * &beta;<sub>2</sub> <em>x</em><sup>2</sup> + ... +
 * &beta;<sub><em>d</em></sub> <em>x</em><sup><em>d</em></sup>
 * (where <em>y</em> is the response variable, <em>x</em> is the predictor variable,
 * and the &beta;<sub><em>i</em></sub> are the regression coefficients)
 * that minimizes the sum of squared residuals of the multiple regression model.
 * It also computes associated the coefficient of determination <em>R</em><sup>2</sup>.
 * <p>
 * This implementation performs a QR-decomposition of the underlying
 * Vandermonde matrix, so it is neither the fastest nor the most numerically
 * stable way to perform the polynomial regression.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class PolynomialRegression implements Comparable<PolynomialRegression> {

    private final String variableName;  // name of the predictor variable
    private int degree;                 // degree of the polynomial regression
    private Matrix beta;                // the polynomial regression coefficients
    private double sse;                 // sum of squares due to error
    private double sst;                 // total sum of squares


    /**
     * Performs a polynomial reggression on the data points {@code (y[i], x[i])}.
     * Uses n as the name of the predictor variable.
     *
     * @param x the values of the predictor variable
     * @param y the corresponding values of the response variable
     * @param degree the degree of the polynomial to fit
     * @throws IllegalArgumentException if the lengths of the two arrays are not equal
     */
    public PolynomialRegression(double[] x, double[] y, int degree) {
        this(x, y, degree, "x");
    }

    /**
     * Performs a polynomial reggression on the data points {@code (y[i], x[i])}.
     *
     * @param x the values of the predictor variable
     * @param y the corresponding values of the response variable
     * @param degree the degree of the polynomial to fit
     * @param variableName the name of the predictor variable
     * @throws IllegalArgumentException if the lengths of the two arrays are not equal
     */
    public PolynomialRegression(double[] x, double[] y, int degree, String variableName) {
        this.degree = degree;
        this.variableName = variableName;
        int n = x.length;
        QRDecomposition qr = null;
        Matrix matrixX = null;
        // in case Vandermonde matrix does not have full rank, reduce degree until it does
        while (true) {
            // build Vandermonde matrix
            double[][] vandermonde = new double[n][this.degree + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= this.degree; j++) {
                    vandermonde[i][j] = Math.pow(x[i], j);
                }
            }
            matrixX = new Matrix(vandermonde);
            // find least squares solution
            qr = new QRDecomposition(matrixX);
            if (qr.isFullRank()) {
                break;
            }
            // decrease degree and try again
            this.degree--;
        }
        // create matrix from vector
        Matrix matrixY = new Matrix(y, n);
        // linear regression coefficients
        beta = qr.solve(matrixY);
        // mean of y[] values
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += y[i];
        }
        double mean = sum / n;
        // total variation to be accounted for
        for (int i = 0; i < n; i++) {
            double dev = y[i] - mean;
            sst += dev * dev;
        }
        // variation not accounted for
        Matrix residuals = matrixX.times(beta).minus(matrixY);
        sse = residuals.norm2() * residuals.norm2();
    }

    /**
     * Returns the {@code j}th regression coefficient.
     *
     * @param j the index
     * @return the {@code j}th regression coefficient
     */
    public double beta(int j) {
        // to make -0.0 print as 0.0
        if (Math.abs(beta.get(j, 0)) < 1E-4) {
            return 0.0;
        }
        return beta.get(j, 0);
    }

    /**
     * Returns the degree of the polynomial to fit.
     *
     * @return the degree of the polynomial to fit
     */
    public int degree() {
        return degree;
    }

    /**
     * Returns the coefficient of determination <em>R</em><sup>2</sup>.
     *
     * @return the coefficient of determination <em>R</em><sup>2</sup>,
     *         which is a real number between 0 and 1
     */
    public double R2() {
        if (sst == 0.0) {
            return 1.0;   // constant function
        }
        return 1.0 - sse / sst;
    }

    /**
     * Returns the expected response {@code y} given the value of the predictor
     * variable {@code x}.
     *
     * @param x the value of the predictor variable
     * @return the expected response {@code y} given the value of the predictor
     *         variable {@code x}
     */
    public double predict(double x) {
        // horner's method
        double y = 0.0;
        for (int j = degree; j >= 0; j--) {
            y = beta(j) + (x * y);
        }
        return y;
    }

    /**
     * Returns a string representation of the polynomial regression model.
     *
     * @return a string representation of the polynomial regression model,
     *         including the best-fit polynomial and the coefficient of
     *         determination <em>R</em><sup>2</sup>
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int j = degree;
        // ignoring leading zero coefficients
        while (j >= 0 && Math.abs(beta(j)) < 1E-5) {
            j--;
        }
        // create remaining terms
        while (j >= 0) {
            if (j == 0) {
                s.append(String.format("%.2f", beta(j)));
            } else if (j == 1) {
                s.append(String.format("%.2f%s + ", beta(j), variableName));
            } else {
                s.append(String.format("%.2f%s^%d + ", beta(j), variableName, j));
            }
            j--;
        }
        s = s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
        // replace "+ -2n" with "- 2n"
        return s.toString().replace("+ -", "- ");
    }

    public List<Double> getWeight() {
        List<Double> s = new ArrayList<>();
        int j = degree;
        // ignoring leading zero coefficients
        while (j >= 0 && Math.abs(beta(j)) < 1E-5) {
            j--;
        }
        // create remaining terms
        while (j >= 0) {
            if (j == 0) {
//                s.add(Double.valueOf(String.format("%.2f ", beta(j))));
            } else if (j == 1) {
                s.add(Double.valueOf(String.format("%.2f  ", beta(j))));
            } else {
                s.add(Double.valueOf(String.format("%.2f  ", beta(j))));
            }
            j--;
        }
        s.add(Double.valueOf(String.format("%.3f", R2())));
        // replace "+ -2n" with "- 2n"
        return s;
    }


    /**
     * Compare lexicographically.
     */
    public int compareTo(PolynomialRegression that) {
        double EPSILON = 1E-5;
        int maxDegree = Math.max(this.degree(), that.degree());
        for (int j = maxDegree; j >= 0; j--) {
            double term1 = 0.0;
            double term2 = 0.0;
            if (this.degree() >= j) {
                term1 = this.beta(j);
            }
            if (that.degree() >= j) {
                term2 = that.beta(j);
            }
            if (Math.abs(term1) < EPSILON) {
                term1 = 0.0;
            }
            if (Math.abs(term2) < EPSILON) {
                term2 = 0.0;
            }
            if (term1 < term2) {
                return -1;
            } else if (term1 > term2) {
                return +1;
            }
        }
        return 0;
    }

    /**
     * Unit tests the {@code PolynomialRegression} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
//        double[] x = {10, 20, 40, 80, 160, 200};
//        double[] y = {100, 350, 1500, 6700, 20160, 40000};
        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7,8};
//        double[] y = new double[]{15, 14, 18, 12, 23, 17, 10, 300};
//        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
//        double[] x = new double[]{1, 2, 3, 4, 5, 6};
        double[] y = new double[]{0.1, 0.5, 0.7, 0.8, 0.7, 0.6, 0.5, 0.4}; //-0.04 x^2 + 0.39 x - 0.17   (R^2 = 0.887)
//        double[] y = new double[]{0.89, 0.86, 0.87, 0.84, 0.86, 0.75};// -0.01 x^2 + 0.02 x + 0.86   (R^2 = 0.754)
//        double[] y = new double[]{43.24, 91.28, 65.30, 67.5, 72.72, 50, 100};// 0.38 x^2 + 0.34 x + 61.01   (R^2 = 0.134)
//        double[] y = new double[]{89.91, 87.47, 93.13, 86.89, 91.22, 92.21};// 0.21 x^2 - 1.01 x + 90.46   (R^2 = 0.173)
//        double[] y = new double[]{79.91, 80.47, 88.13, 89.89, 90.22, 92.21};// -0.40 x^2 + 5.41 x + 73.86   (R^2 = 0.916)
//        double[] y = new double[]{76.77, 71.74, 71.22, 67.27, 63.83, 57.23}; -0.35 x^2 - 1.14 x + 77.29   (R^2 = 0.974)
//        double[] y = new double[]{38.75, 59.77, 64.91, 66.66,68.08};//-3.04 x^2 + 24.81 x + 18.67   (R^2 = 0.956)
//        double[] y = new double[]{0, 83.33, 66.66, 72.72,0};//-20.67 x^2 + 122.95 x - 96.96   (R^2 = 0.887)
//        double[] y = new double[]{48.07, 86.75, 66.66, 63.15, 50, 50, 100};//1.64 x^2 - 10.78 x + 76.68   (R^2 = 0.156)
//        double[] y = new double[]{89.31,86.85,87.07,84.75,86.70,75.66};//-0.64 x^2 + 2.47 x + 86.16   (R^2 = 0.751)
//        double[] y = new double[]{89.18, 88.69, 89.03, 88.95, 87.83, 86.82, 88.55, 86.14, 87.23, 87.81, 87.38, 87.4, 86.73, 88.8, 86, 84.29, 84.75, 84.81, 86.43, 87.2, 85.79, 82.5, 80.05, 77.92, 77.17, 75.66};//-0.03 x^2 + 0.39 x + 87.26   (R^2 = 0.823)
//        double[] y= new double[]{75,76,77,78,79,80}; //1.00 x + 74.00   (R^2 = 1.000)

        int degree = 2;
        PolynomialRegression regression = new PolynomialRegression(x, y, degree);
        while (regression.R2() < 0.8 && degree < 10) {
            regression = new PolynomialRegression(x, y, degree++);
        }
        // Use System.out.println() so that it works with either stdlib.jar or algs4.jar version
        System.out.println(regression.predict(3));
        System.out.println(regression);
        System.out.println(regression.getWeight());
    }
}

