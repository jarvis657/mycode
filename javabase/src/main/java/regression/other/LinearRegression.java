package regression.other;

/**
 * @Author:lmq
 * @Date: 2022/2/23
 * @Desc:
 **/
/******************************************************************************
 *  Compilation:  javac LinearRegression.java
 *  Execution:    java  LinearRegression
 *  Dependencies: none
 *
 *  Compute least squares solution to y = beta * x + alpha.
 *  Simple linear regression.
 *
 ******************************************************************************/

import com.google.gson.Gson;
import java.util.ArrayList;

/**
 * The {@code LinearRegression} class performs a simple linear regression
 * on an set of <em>n</em> data points (<em>y<sub>i</sub></em>, <em>x<sub>i</sub></em>).
 * That is, it fits a straight line <em>y</em> = &alpha; + &beta; <em>x</em>,
 * (where <em>y</em> is the response variable, <em>x</em> is the predictor variable,
 * &alpha; is the <em>y-intercept</em>, and &beta; is the <em>slope</em>)
 * that minimizes the sum of squared residuals of the linear regression model.
 * It also computes associated statistics, including the coefficient of
 * determination <em>R</em><sup>2</sup> and the standard deviation of the
 * estimates for the slope and <em>y</em>-intercept.
 */
public class LinearRegression {

    static class XX extends ArrayList<Integer> {

        private String detail;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    private final double intercept, slope;
    private final double r2;
    private final double svar0, svar1;

    public static void main(String[] args) {
        XX x = new XX();
        x.add(1);
        x.add(2);
        x.add(3);
        x.setDetail("detail");
        Gson gson = new Gson();
        String s1 = gson.toJson(x);
//        String s1 = Jacksons.transObjectToString(x);
        System.out.println(s1);

        LinearRegression linearRegression = new LinearRegression(
//                new double[]{1, 2, 3, 4, 5},
                new double[]{1, 2, 3, 4, 5, 6},
//                new double[]{1, 2, 3, 4, 5, 6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26},
//                new double[]{0.1, 0.5, 0.7, 0.8, 0.7, 0.6, 0.5, 0.4});
//         new double[]{0.1, 0.5, 0.7, 0.8, 0.7, 0.6, 0.5, 0.4});//多项式回归-0.04 x^2 + 0.39 x - 0.17   (R^2 = 0.887)  线性回归  y=0.02x + 0.45  (R^2 = 0.051)
//         new double[]{0.89, 0.86, 0.87, 0.84, 0.86, 0.75});//多项式回归 -0.01 x^2 + 0.02 x + 0.86  (R^2 = 0.754) 线性回归  y=-0.02x + 0.92  (R^2 = 0.627)
//         new double[]{43.24, 91.28, 65.30, 67.5, 72.72, 50,100});//多项式回归 0.38 x^2 + 0.34 x + 61.01 (R^2 = 0.134) 线性回归  y=3.40x + 56.41  (R^2 = 0.129)
//         new double[]{89.91, 87.47, 93.13, 86.89, 91.22, 92.21});//多项式回归 0.21 x^2 - 1.01 x + 90.46 (R^2 = 0.173) 线性回归 y=0.47x + 88.49  (R^2 = 0.121)
//         new double[]{79.91, 80.47, 88.13, 89.89, 90.22, 92.21});//多项式回归 -0.40 x^2 + 5.41 x + 73.86  (R^2 = 0.916) 线性回归 y=2.64x + 77.55  (R^2 = 0.874)
//                new double[]{76.77, 71.74, 71.22, 67.27, 63.83, 57.23});//多项式回归-0.35 x^2 - 1.14 x + 77.29(R^2 = 0.974) 线性回归 y=-3.58x + 80.55  (R^2 = 0.955)
//                new double[]{38.75, 59.77, 64.91, 66.66,68.08});//多项式回归-3.04 x^2 + 24.81 x + 18.67   (R^2 = 0.956)   线性回归  y=6.55x + 39.97  (R^2 = 0.735)
//                new double[]{0, 83.33, 66.66, 72.72,0});//多项式回归-20.67 x^2 + 122.95 x - 96.96   (R^2 = 0.887)        线性回归     y=-1.06x + 47.73  (R^2 = 0.002)
//                new double[]{48.07, 86.75, 66.66, 63.15, 50, 50, 100});//多项式回归1.64 x^2 - 10.78 x + 76.68   (R^2 = 0.156)  线性回归 y=2.34x + 57.00  (R^2 = 0.063)
//                new double[]{89.31,86.85,87.07,84.75,86.70,75.66});//多项式回归-0.64 x^2 + 2.47 x + 86.16   (R^2 = 0.751)  线性回归  y=-2.03x + 92.16  (R^2 = 0.619)
//                new double[]{89.18, 88.69, 89.03, 88.95, 87.83, 86.82, 88.55, 86.14, 87.23, 87.81, 87.38, 87.4, 86.73, 88.8, 86, 84.29, 84.75, 84.81, 86.43, 87.2, 85.79, 82.5, 80.05, 77.92, 77.17, 75.66});//多项式回归-0.03 x^2 + 0.39 x + 87.26 (R^2 = 0.823) 线性回归  y=-0.41x + 90.98  (R^2 = 0.665)
                new double[]{75,76,77,78,79,80});//   y=1.00x + 74.00  (R^2 = 1.000)
        String s = linearRegression.toString();
        double predict = linearRegression.predict(27);
        System.out.println(s);
        System.out.println(predict);
    }

    /**
     * Performs a linear regression on the data points {@code (y[i], x[i])}.
     *
     * @param x the values of the predictor variable
     * @param y the corresponding values of the response variable
     * @throws IllegalArgumentException if the lengths of the two arrays are not equal
     */
    public LinearRegression(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.length;

        // first pass
        double sumx = 0.0, sumy = 0.0;
        for (int i = 0; i < n; i++) {
            sumx += x[i];
            sumy += y[i];
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope = xybar / xxbar;
        intercept = ybar - slope * xbar;

        // more statistical analysis
        double rss = 0.0;//多项式回归 residual sum of squares
        double ssr = 0.0;//多项式回归 regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = slope * x[i] + intercept;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = n - 2;
        r2 = ssr / yybar;
        double svar = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar / n + xbar * xbar * svar1;
    }

    /**
     * Returns the <em>y</em>-intercept &alpha; of the best of the best-fit line <em>y</em> = &alpha; + &beta;
     * <em>x</em>.
     *
     * @return the <em>y</em>-intercept &alpha; of the best-fit line <em>y = &alpha; + &beta; x</em>
     */
    public double intercept() {
        return intercept;
    }

    /**
     * Returns the slope &beta; of the best of the best-fit line <em>y</em> = &alpha; + &beta; <em>x</em>.
     *
     * @return the slope &beta; of the best-fit line <em>y</em> = &alpha; + &beta; <em>x</em>
     */
    public double slope() {
        return slope;
    }

    /**
     * Returns the coefficient of determination <em>R</em><sup>2</sup>.
     *
     * @return the coefficient of determination <em>R</em><sup>2</sup>,
     *         which is a real number between 0 and 1
     */
    public double R2() {
        return r2;
    }

    /**
     * Returns the standard error of the estimate for the intercept.
     *
     * @return the standard error of the estimate for the intercept
     */
    public double interceptStdErr() {
        return Math.sqrt(svar0);
    }

    /**
     * Returns the standard error of the estimate for the slope.
     *
     * @return the standard error of the estimate for the slope
     */
    public double slopeStdErr() {
        return Math.sqrt(svar1);
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
        return slope * x + intercept;
    }

    /**
     * Returns a string representation of the simple linear regression model.
     *
     * @return a string representation of the simple linear regression model,
     *         including the best-fit line and the coefficient of determination
     *         <em>R</em><sup>2</sup>
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("y=%.2fx + %.2f", slope(), intercept()));
        s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
        s.append(String.format("interceptStdErr:%.2f,slopeStdErr:%.2f", interceptStdErr(), slopeStdErr()));
        return s.toString();
    }

}
