package groovy;


import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.syntax.Types;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @Author:lmq
 * @Date: 2022/4/13
 * @Desc:
 **/
public class TestGroovyBase {

    private String name;

    private Map<String, String> data = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("a.b".contains("."));
        test1();
        System.out.println("========test1 over================");
        test2();
        testGroovyLoader2();
        System.out.println("========test2 over================");
        testJSR223();
        System.out.println("========testjsr223 over================");
        testInvokeJavaMethod();
        System.out.println("========testInvokeJavaMethod over================");
        testGroovyLoader();//注意metadata溢出
    }

    private static void testReadGroovyFiles() throws IOException {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(128);
        final String path = "classpath*:*.groovy_template";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Arrays.stream(resolver.getResources(path))
                .parallel()
                .forEach(resource -> {
                    try {
                        String fileName = resource.getFilename();
                        InputStream input = resource.getInputStream();
                        InputStreamReader reader = new InputStreamReader(input);
                        BufferedReader br = new BufferedReader(reader);
                        StringBuilder template = new StringBuilder();
                        for (String line; (line = br.readLine()) != null; ) {
                            template.append(line).append("\n");
                        }
                        concurrentHashMap.put(fileName, template.toString());
                    } catch (Exception e) {
                    }
                });
        String scriptBuilder = concurrentHashMap.get("ScriptTemplate.groovy_template");
        String scriptClassName = "testGroovy";
//这一部分String的获取逻辑进行可配置化
        String StrategyLogicUnit = "if(context.amount>=20000){\n" +
                "            context.nextScenario='A'\n" +
                "            return true\n" +
                "        }\n" +
                "        ";
        String fullScript = String.format(scriptBuilder, scriptClassName, StrategyLogicUnit);
    }

    private static void testJSR223() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");// 每次生成一个engine实例
        Bindings binding = engine.createBindings();
        TestGroovyBase value = new TestGroovyBase();
        value.setName("mainSet");
        binding.put("date", new Date()); // 入参
        binding.put("bindingV", value); // 入参
        engine.eval("def getTime(){return date.getTime();}", binding);// 如果script文本来自文件,请首先获取文件内容
        engine.eval("def sayHello(name,age){return 'Hello,I am ' + name + ',age' + age;}");
        Long time = (Long) ((Invocable) engine).invokeFunction("getTime", null);// 反射到方法
        System.out.println(time);
        String message = (String) ((Invocable) engine).invokeFunction("sayHello", "zhangsan", 12);
        System.out.println(message);
        System.out.println(value.getName());
    }

    private static void testInvokeJavaMethod() {
        final String script = "Runtime.getRuntime().availableProcessors()";
        Binding intBinding = new Binding();
        GroovyShell shell = new GroovyShell(intBinding);
        final Object eval = shell.evaluate(script);
        System.out.println(eval);
    }


    private static void testGroovyLoader() throws InstantiationException, IllegalAccessException {
        //防止用户调用System.exit或Runtime等方法导致系统宕机，以及自定义的Groovy片段代码执行死循环或调用资源超时等问题，Groovy提供了SecureASTCustomizer安全管理者和SandboxTransformer沙盒环境。
        final SecureASTCustomizer secure = new SecureASTCustomizer();// 创建SecureASTCustomizer
        secure.setClosuresAllowed(true);// 禁止使用闭包
        List<Integer> tokensBlacklist = new ArrayList<>();
        tokensBlacklist.add(Types.KEYWORD_WHILE);// 添加关键字黑名单 while和goto
        tokensBlacklist.add(Types.KEYWORD_GOTO);
        secure.setTokensBlacklist(tokensBlacklist);
        secure.setIndirectImportCheckEnabled(true);// 设置直接导入检查
        List<String> list = new ArrayList<>();// 添加导入黑名单，用户不能导入JSONObject
        list.add("com.alibaba.fastjson.JSONObject");
        secure.setImportsBlacklist(list);
        List<Class<? extends Statement>> statementBlacklist = new ArrayList<>();// statement 黑名单，不能使用while循环块
        statementBlacklist.add(WhileStatement.class);
        secure.setStatementsBlacklist(statementBlacklist);
        final CompilerConfiguration config = new CompilerConfiguration();// 自定义CompilerConfiguration，设置AST
        config.addCompilationCustomizers(secure);
        GroovyClassLoader groovyClassLoader2 = new GroovyClassLoader(TestGroovyBase.class.getClassLoader(), config);

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        String helloScript = "package com.vivo.groovy.util;" +  // 可以是纯Java代码
                "class Hello {" +
                "String say(String name) {" +
                "System.out.println(\"hello, \" + name);" +
                " return name;}" +
                "}";
        Class helloClass = groovyClassLoader.parseClass(helloScript);
        GroovyObject object = (GroovyObject) helloClass.newInstance();
        Object ret = object.invokeMethod("say", "vivo"); // 控制台输出"hello, vivo"
        System.out.println(ret.toString()); // 打印vivo
    }


    private static void test1() {

        Binding groovyBinding = new Binding();
        GroovyShell groovyShell = new GroovyShell(groovyBinding);

        String scriptContent = "import groovy.TestGroovyBase;def query = new TestGroovyBase().testQuery(100L);\n query";
        Script script = groovyShell.parse(scriptContent);
        Object run = script.run();
        System.out.println(run);

        Binding groovyBinding2 = new Binding();
        TestGroovyBase value = new TestGroovyBase();
        value.setName("mainSet");
        groovyBinding2.setVariable("TestGroovyBase", value);
        GroovyShell groovyShell2 = new GroovyShell(groovyBinding2);
        String scriptContent2 = "def query = TestGroovyBase.testQuery(200L);\n query";
        Script script2 = groovyShell2.parse(scriptContent2);
        Object run2 = script2.run();
        System.out.println(run2);
    }

    private static void test2() {
        Binding groovyBinding = new Binding();
        TestGroovyBase value = new TestGroovyBase();
        value.setName("mainSet");
        groovyBinding.setVariable("TestGroovyBase", value);
        GroovyShell groovyShell = new GroovyShell(groovyBinding);
        String scriptContent =
                "println('script:'+TestGroovyBase.getName());\ndef query = TestGroovyBase.testQuery(2L);\nTestGroovyBase.setName('innerName')\n"
                        + "query";
        Script script = groovyShell.parse(scriptContent);
        System.out.println(script.run());
        //groovy脚本执行可以改变java对象的信息
        System.out.println(value.getName());
        System.out.println("----------inner test2 one done--------------");
        TestGroovyBase value2 = new TestGroovyBase();
        value.setName("mainSet222222222");
        Binding groovyBinding2 = new Binding();
        groovyBinding2.setVariable("TestGroovyBase", value2);
        script.setBinding(groovyBinding2);
        System.out.println(script.run());
        System.out.println(value2.getName());

    }

    private static void testGroovyLoader2() throws InstantiationException, IllegalAccessException {
        try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader()) {
            String helloScript = "package com.test.groovy.util;import groovy.TestGroovyBase;" +  // 可以是纯Java代码
                    "class Hello {" +
                    "String say(String name,TestGroovyBase tb,String newName,Map v) {" +
                    "System.out.println(\"hello, \" + name);" +
                    "System.out.println(tb.getName()+ \":hello, \" + name);" +
                    "tb.setName(newName);" +
                    "System.out.println(tb.getData()['key']+ \":hello, \" + name);" +
                    " return name;}" +
                    "}";
            Class helloClass = groovyClassLoader.parseClass(helloScript);
            TestGroovyBase value = new TestGroovyBase();
            value.setName("mainSet");
            value.getData().put("key", "value");
            GroovyObject object = (GroovyObject) helloClass.newInstance();
            Object ret = object.invokeMethod("say",
                    new Object[]{"vivo", value, "newName", value.getData()}); // 控制台输出"hello, vivo"
            System.out.println(ret.toString()); // 打印vivo
            System.out.println(value.getName());
            Object ret2 = object.invokeMethod("say",
                    new Object[]{"vivo", value, "newName222222", value.getData()}); // 控制台输出"hello, vivo"
            System.out.println(ret2.toString());
            System.out.println(value.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String testQuery(long id) {
        return "Test query success, id is " + id;
    }

}
