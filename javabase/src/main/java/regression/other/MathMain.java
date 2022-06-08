package regression.other;

import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateDifferentiableSolver;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import origin.utils.Jacksons;

/**
 * @Author:lmq
 * @Date: 2022/3/18
 * @Desc:
 **/
public class MathMain {
//
//    private static PolynomialFunction getPolynomialFitter(List<List<Double>> pointlist) {
//        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
//        final WeightedObservedPoints obs = new WeightedObservedPoints();
//        for (List<Double> point : pointlist) {
//            obs.add(point.get(0), point.get(1));
//        }
//        double[] fit = fitter.fit(obs.toList());
//        System.out.printf("\nCoefficient %f, %f, %f", fit[0], fit[1], fit[2]);
//        final PolynomialFunction fitted = new PolynomialFunction(fit);
//        return fitted;
//    }
//    private static double getRSquare(PolynomialFunction fitter, List<List<Double>> pointList) {
//        final double[] coefficients = fitter.getCoefficients();
//        double[] predictedValues = new double[pointList.size()];
//        double residualSumOfSquares = 0;
//        final DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
//        for (int i=0; i< pointList.size(); i++) {
//            predictedValues[i] = predict(coefficients, pointList.get(i).get(0));
//            double actualVal = pointList.get(i).get(1);
//            double t = Math.pow((predictedValues[i] - actualVal), 2);
//            residualSumOfSquares  += t;
//            descriptiveStatistics.addValue(actualVal);
//        }
//        final double avgActualValues = descriptiveStatistics.getMean();
//        double totalSumOfSquares = 0;
//        for (int i=0; i<pointList.size(); i++) {
//            totalSumOfSquares += Math.pow( (predictedValues[i] - avgActualValues),2);
//        }
//        return 1.0 - (residualSumOfSquares/totalSumOfSquares);
//    }


    /**
     * 解方程
     *
     * @param args
     */
    public static void main(String[] args) {
//        double[] d = new double[]{6.0, -5.0, 1.0};
//        [-1.63*3, 15.43*2]
        System.out.println("-0.089348x^2 + 1.196650x + 14.494061");
        double[] d = new double[]{-0.089348, 1.196650, 14.494061};
//        double[] d = new double[]{-35, 30};
        solveDerivativeEquation(d);


        System.out.println("--------------------------------------------");
        double[] ddd = new double[d.length - 1];
        for (int i = 0; i < d.length - 1; i++) {
            ddd[i] = d[i] * (d.length - i - 1);
        }
        System.out.println(Jacksons.transObjectToString(ddd));
        double[] r = new double[ddd.length];
        for (int i = ddd.length - 1; i >= 0; i--) {
            r[i] = ddd[ddd.length - i - 1];
        }
        System.out.println(Jacksons.transObjectToString(r));
        UnivariateDifferentiableFunction function = new PolynomialFunction(r);
//        System.out.printf("\nPolynimailCurveFitter R-Square %f", getRSquare(function, trainData));
        System.out.println(function);
        UnivariateDifferentiableSolver solver = new NewtonRaphsonSolver();

        Set<String> res = new HashSet<>();
        int i = 0;
        double solusion = 0;
        try {
            while (i < 100) {
                solusion = solver.solve(100, function, i);
                res.add(String.format("%.6f", solusion));
                i++;
            }
        } catch (Exception e) {
            //无解
        }
        System.out.println(res);
    }

    public static void solveDerivativeEquation(double[] weight) {
//        System.out.println("-1.63x^3 + 15.43x^2 - 35.11x + 21.7");
//        double[] d = new double[]{-1.63, 15.43, -35.11, 21.7};
        double[] derivativeWeight = new double[weight.length - 1];
        for (int i = 0; i < weight.length - 1; i++) {
            derivativeWeight[i] = weight[i] * (weight.length - i - 1);
        }
        double[] r = new double[derivativeWeight.length];
        for (int i = derivativeWeight.length - 1; i >= 0; i--) {
            r[i] = derivativeWeight[derivativeWeight.length - i - 1];
        }
        UnivariateDifferentiableFunction function = new PolynomialFunction(r);
        System.out.println(function);
        UnivariateDifferentiableSolver solver = new NewtonRaphsonSolver();

        Set<String> res = new HashSet<>();
        int i = 0;
        double solusion = 0;
        try {
            while (i < 1000) {
                solusion = solver.solve(i + 1000, function, i);
                res.add(String.format("%.6f", solusion));
                i++;
            }
        } catch (Exception e) {
            //无解
        }
        System.out.println(res);
    }
//    public static void main(String[] args) {
//// ... 创建并初始化输入数据：
//        double[] x = new double[]{1, 2, 3, 4, 5, 6, 7};
//        double[] y = new double[]{2.1, 3.3, 5.2, 1.2, 1.4, 6.6, 2.2};
////        将原始的x - y数据序列合成带权重的观察点数据序列：
//        WeightedObservedPoints points = new WeightedObservedPoints();
//// 将x-y数据元素调用points.add(x[i], y[i])加入到观察点序列中
//        for (int i = 0; i < x.length; i++) {
//            points.add(x[i], y[i]);
//        }
//        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);   // degree 指定多项式阶数
//        double[] result = fitter.fit(points.toList());   // 曲线拟合，结果保存于双精度数组中，由常数项至最高次幂系数排列
//        System.out.println(Jacksons.transObjectToString(result));
//    }
}
