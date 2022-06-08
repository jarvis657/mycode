package regression.libs;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

interface CurveFitting {

    public List<Object> getParams();

    public Object run(List<Object> params) throws Exception;

    public void printResult(Object result);
}

public class TimeCostCalculator {

    public static void main(String[] args) throws Exception {
        TimeCostCalculator tcc = new TimeCostCalculator();
        double timeCost = tcc.calcTimeCost(new CalcCurveFitting());
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("time cost is: " + timeCost + "s");
        System.out.println("--------------------------------------------------------------------------");
    }

    /**
     * 计算指定对象的运行时间开销。
     *
     * @param curveFitting 指定被测对象。
     * @return 返回sub.run的时间开销，单位为s。
     * @throws Exception
     */
    public double calcTimeCost(CurveFitting curveFitting) throws Exception {
        List<Object> params = curveFitting.getParams();
        long startTime = System.nanoTime();
        Object result = curveFitting.run(params);
        long stopTime = System.nanoTime();
        curveFitting.printResult(result);
        System.out.println("start: " + startTime + " / stop: " + stopTime);
        return 1.0e-9 * (stopTime - startTime);
    }

}

class CalcCurveFitting implements CurveFitting {

    private final int degree = 5;    // 阶数
    private WeightedObservedPoints points;

    public CalcCurveFitting() {
        int arrayLength = 200000;
        System.out.println(String.format("本算例用于计算多项式曲线拟合。正在初始化计算数据（%s点，%s阶......", arrayLength, degree));
        double[] inputDataX = new double[arrayLength];
        //      inputDataX = new double[] {1, 2, 3, 4, 5, 6, 7};
        double[] inputDataY = new double[inputDataX.length];
        double[] factor = new double[degree + 1];    // N阶多项式会有N+1个系数，其中之一为常数项
        for (int index = 0; index < factor.length; index++) {
            factor[index] = index + 1;
        }
        for (int index = 0; index < inputDataY.length; index++) {
            inputDataX[index] = index * 0.00001;
            inputDataY[index] = calcPoly(inputDataX[index], factor);    // y = sum(x[n) * fact[n])
            // System.out.print(inputDataY[index] + ", ");
        }
        points = new WeightedObservedPoints();
        for (int index = 0; index < inputDataX.length; index++) {
            points.add(inputDataX[index], inputDataY[index]);
        }
        System.out.println("init completely");
    }

    @Override
    public List<Object> getParams() {
        List<Object> params = new ArrayList<Object>();
        params.add(points);
        return params;
    }

    @Override
    public Object run(List<Object> params) throws Exception {
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        WeightedObservedPoints points = (WeightedObservedPoints) params.get(0);
        return fitter.fit(points.toList());
    }

    @Override
    public void printResult(Object result) {
        for (double data : (double[]) result) {
            System.out.println(data);
        }
    }

    private double calcPoly(double x, double[] factor) {
        double y = 0;
        for (int deg = 0; deg < factor.length; deg++) {
            y += Math.pow(x, deg) * factor[deg];
        }
        return y;
    }

}
