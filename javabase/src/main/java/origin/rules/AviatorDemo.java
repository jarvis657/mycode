package origin.rules;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Feature;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.script.AviatorScriptEngine;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @Author:lmq
 * @Date: 2022/4/13
 * @Desc:
 **/
public class AviatorDemo {

    public static void main(String[] args) throws IOException, ScriptException {
        test1();
        test2();
        test3();
        test4();
        test5();
    }


    public static void test1() {
        String name = "测试";
        Map<String, Object> env = new HashMap<>(1);
        env.put("name", name);
        String result = (String) AviatorEvaluator.execute(" 'hello ' + name ", env);
        System.out.println(result);
    }


    private static void test2() throws IOException {
        //注册函数
        AviatorEvaluator.addFunction(new AddFunction());
        System.out.println(AviatorEvaluator.execute("add(1, 2)"));           // 3.0
        System.out.println(AviatorEvaluator.execute("add(add(1, 2), 100)")); // 103.0
    }


    private static void test3() {
        String expression = "a-(b-c) > 100";
        Expression compiledExp = AviatorEvaluator.compile(expression);
        // Execute with injected variables.
        Boolean result = (Boolean) compiledExp.execute(compiledExp.newEnv("a", 100.3, "b", 45, "c", -199.100));
        System.out.println(result);
        // Compile a script
        Expression script = AviatorEvaluator.getInstance().compile("println('Hello, AviatorScript!');");
        script.execute();
    }

    /**
     * ## examples/statements.av
     * let a = 1;
     * let b = 2;
     * c = a + b;
     *
     * @throws IOException
     */
    private static void test4() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statements.av");
        Object result = exp.execute();
        System.out.println(result);
    }


    private static void test5() throws ScriptException {
        final ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("AviatorScript");
        AviatorEvaluatorInstance instance = ((AviatorScriptEngine) engine).getEngine();
        // Use compatible feature set
        instance.setOption(Options.FEATURE_SET, Feature.getCompatibleFeatures());
        // Doesn't support if in compatible feature set mode.
        engine.eval("if(true) { println('support if'); }");
    }

    /**
     * 自定义函数
     */
    static class AddFunction extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorDouble(left.doubleValue() + right.doubleValue());
        }

        @Override
        public String getName() {
            return "add";
        }
    }


}
