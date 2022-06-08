package regression;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * @Author:lmq
 * @Date: 2022/3/25
 * @Desc:
 **/
public class MathTest {

//    public static void main(String[] args) {
//        double[] epsilon = new double[]{1.0e-20, 5.0e-14, 2.0e-13, 3.0e-13, 2.0e-13, 1.0e-20};
//        PolynomialFunction poly = new PolynomialFunction(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0});
//        for (int maxOrder = 0; maxOrder < 6; ++maxOrder) {
//            PolynomialFunction[] p = new PolynomialFunction[maxOrder + 1];
//            p[0] = poly;
//            for (int i = 1; i <= maxOrder; ++i) {
//                p[i] = p[i - 1].polynomialDerivative();
//            }
//            for (double x = 0.1; x < 1.2; x += 0.001) {
//                DerivativeStructure dsX = new DerivativeStructure(1, maxOrder, 0, x);
//                DerivativeStructure dsY1 = dsX.getField().getZero();
//                for (int i = poly.degree(); i >= 0; --i) {
//                    dsY1 = dsY1.multiply(dsX).add(poly.getCoefficients()[i]);
//                }
//                double[] f = new double[maxOrder + 1];
//                for (int i = 0; i < f.length; ++i) {
//                    f[i] = p[i].value(x);
//                }
//                DerivativeStructure dsY2 = dsX.compose(f);
//                DerivativeStructure zero = dsY1.subtract(dsY2);
//                for (int n = 0; n <= maxOrder; ++n) {
//                    System.out.print( zero.getPartialDerivative(n) );;
//                    System.out.print(":");
//                    System.out.println(epsilon[n]);
//                }
//            }
//        }
//    }
    public static void main(String[] args) {
        WeightedObservedPoints obs = new WeightedObservedPoints();
        obs.add(2D, 2.9D);
        obs.add(6D, 3.0D);
        obs.add(8D, 4.8D);
        obs.add(3D, 1.8D);
        obs.add(2D, 2.9D);
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(4);
        List<WeightedObservedPoint> obs2List = obs.toList();
//        double[] best = fitter.fit(obs2List);
//        double a = best[0];
//        double b = best[1];
//        double c = best[2];
//        double d = best[3];
//        // f(x) = d + ((a - d) / (1 + Math.pow(x / c, b)))
//        StringBuilder func = new StringBuilder();
//        func.append("f(x) =");
//        func.append(d > 0 ? " " : " - ");
//        func.append(Math.abs(d));
//        func.append(" ((");
//        func.append(a > 0 ? "" : "-");
//        func.append(Math.abs(a));
//        func.append(d > 0 ? " - " : " + ");
//        func.append(Math.abs(d));
//        func.append(" / (1 + ");
//        func.append("(x / ");
//        func.append(c > 0 ? "" : " - ");
//        func.append(Math.abs(c));
//        func.append(") ^ ");
//        func.append(b > 0 ? " " : " - ");
//        func.append(Math.abs(b));
        //y =-0.008983686067019405*a^4 + 0.1248567019400353*b^3 + -0.23481040564373887*c^2 + -1.7142857142857142*d + 6.412698412698411
        final double[] coeff = fitter.fit(obs2List);
        for (int i = 0; i < coeff.length; i++) {
            System.out.println(coeff[i]);
        }
        double[] x = new double[]{1,2,3,4,5};
        double[] y = new double[]{1,2,3,4,5};


        linearFit(x,y);
    }

    public static void py(String[] args) {


    }


    public static void linearFit(double[] x, double[] y) {
        double[][] data = new double[x.length][y.length];
        for (int i = 0; i < x.length; i++) {
            data[i][0] = x[i];
            data[i][1] = y[i];
        }
        List<double[]> fitData = new ArrayList<>();
        SimpleRegression regression = new SimpleRegression();
        regression.addData(data); // 数据集
        /*
         * RegressionResults 中是拟合的结果
         * 其中重要的几个参数如下：
         *   parameters:
         *      0: b
         *      1: k
         *   globalFitInfo
         *      0: 平方误差之和, SSE
         *      1: 平方和, SST
         *      2: R 平方, RSQ
         *      3: 均方误差, MSE
         *      4: 调整后的 R 平方, adjRSQ
         *
         * */
        RegressionResults results = regression.regress();
        double b = results.getParameterEstimate(0);
        double k = results.getParameterEstimate(1);
        double r2 = results.getRSquared();
        // 重新计算生成拟合曲线
        for (double[] datum : data) {
            double[] xy = {datum[0], k * datum[0] + b};
            fitData.add(xy);
        }
        StringBuilder func = new StringBuilder();
        func.append("f(x) =");
        func.append(b >= 0 ? " " : " - ");
        func.append(Math.abs(b));
        func.append(k > 0 ? " + " : " - ");
        func.append(Math.abs(k));
        func.append("x");
        func.append(" r2=").append(r2);
        double[][] doubles = fitData.stream().toArray(double[][]::new);
        String s = func.toString();
        System.out.println(s);
        double predict = regression.predict(111);
        System.out.println(predict);
    }

}
