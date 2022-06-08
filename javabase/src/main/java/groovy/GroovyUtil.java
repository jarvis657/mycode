package groovy;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import io.netty.util.internal.ObjectUtil;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author:lmq
 * @Date: 2022/4/13
 * @Desc:
 **/
public class GroovyUtil {

    private static Map<String, Script> scriptMap = new ConcurrentHashMap<>();

//    @EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
    public static Object engine(String filePath, String fileName, Map<String, Object> variable) {
//        GroovyUtil o = (GroovyUtil) AopContext.currentProxy();
        Binding binding = new Binding();
        if (CollectionUtils.isNotEmpty(variable.keySet())) {
            variable.entrySet().stream()
                    .filter(entry -> StringUtils.isNotBlank(entry.getKey()) && !Objects.isNull(entry.getValue()))
                    .forEach(entry -> binding.setVariable(entry.getKey(), entry.getValue()));
        }
        Script script = getScriptInstance(filePath, fileName);
        script.setBinding(binding);
        return script.run();

    }

    private static Script getScriptInstance(String filePath, String fileName) {
        File file = new File(filePath + File.separator + fileName);
//        String md5Hex = DigestUtil.md5Hex(file);
        String md5Hex = "xxxxxxxxxxx";
        if (scriptMap.containsKey(md5Hex)) {
            return scriptMap.get(md5Hex);
        } else {
            Script script = null;
            try {
                GroovyScriptEngine engine = new GroovyScriptEngine(filePath);
                script = engine.createScript(fileName, new Binding());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件不存在");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("生成script文件失败");
            }
            scriptMap.put(md5Hex, script);
            return script;
        }
    }

    public void testGroovyScriptEngine() throws Exception {
        String url = "...(文件地址)";
        GroovyScriptEngine engine = new GroovyScriptEngine(url);
        for (int i = 0; i < 5; i++) {
            Binding binding = new Binding();
            binding.setVariable("index", i);
            // 每一次执行获取缓存Class,创建新的Script对象
            Object run = engine.run("HelloWorld.groovy", binding);
            System.out.println(run);
        }
    }

    private static void exec() throws Exception {
        /*
        GroovyScriptEngine
         */
        long start2 = System.currentTimeMillis();
        Class script = new GroovyScriptEngine("src/Groovy/com/baosight/groovy/").loadScriptByName("CycleDemo.groovy");
        try {
            Script instance = (Script) script.newInstance();
            instance.invokeMethod("cycle", new Object[]{1});
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end2 = System.currentTimeMillis() - start2;
        System.out.println(" GroovyScriptEngine时间：" + end2);
    }
}
