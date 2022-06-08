package groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import javax.annotation.PostConstruct;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:lmq
 * @Date: 2022/4/13
 * @Desc:
 **/
@RestController
@RequestMapping("/groovy/script")
public class GroovyScriptController {

    @Autowired
    private Binding groovyBinding;

    private GroovyShell groovyShell;

    @PostConstruct
    public void init() {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(this.getClass().getClassLoader());
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding("utf-8");
        compilerConfiguration.setScriptBaseClass(TestGroovyBase.class.getName());
        groovyShell = new GroovyShell(groovyClassLoader, groovyBinding, compilerConfiguration);
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public String execute(@RequestBody String scriptContent) {
        Script script = groovyShell.parse(scriptContent);
        return String.valueOf(script.run());
    }
}
