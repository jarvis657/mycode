package groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/27
 * @Desc:
 **/
@Component
public class GroovyComponent {

    private static final Logger log = LoggerFactory.getLogger(GroovyComponent.class);
    private static final String PATH = "classpath:groovy/**/*.groovy";
    private final Map<String, Class<?>> fileName2ScriptClassMap = new ConcurrentHashMap<>();
    private final Map<String, String> fileName2ScriptMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void readGroovyFiles() throws IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Arrays.stream(resolver.getResources(PATH)).parallel().forEach(resource -> {
            String fileName = resource.getFilename();
            if (fileName.contains(".")) {
                fileName = fileName.split("\\.")[0];
            }
            try (InputStream inputStream = resource.getInputStream(); InputStreamReader streamReader = new InputStreamReader(
                    inputStream); BufferedReader reader = new BufferedReader(streamReader)) {
                StringBuilder template = new StringBuilder();
                reader.lines().forEach(line -> {
                    template.append(line).append("\n");
                });
                fileName2ScriptMap.put(fileName, template.toString());
            } catch (Exception e) {
                log.error("GroovyComponent.readGroovyFiles [processGroovyFile] fileName:{}", fileName, e);
            }
        });
    }

    public Object runScriptClass(String fileName, String method, Object... args) throws Exception {
        Class<?> escapeClass = fileName2ScriptClassMap.computeIfAbsent(fileName, (d) -> {
            try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader()) {
                return groovyClassLoader.parseClass(fileName2ScriptMap.get(fileName));
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        });
        System.out.println("#############################");
        System.out.println(escapeClass.toString());
        GroovyObject object = (GroovyObject) escapeClass.newInstance();
        return object.invokeMethod(method, args);
    }

    public void updateScript(String fileName, String content) {
        fileName2ScriptMap.put(fileName, content);
        fileName2ScriptClassMap.computeIfPresent(fileName, (key, value) -> {
            try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader()) {
                return groovyClassLoader.parseClass(fileName2ScriptMap.get(fileName));
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        });
        System.out.println("=======================================");
        System.out.println(fileName2ScriptClassMap.get(fileName).toString());
    }
}
