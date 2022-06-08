package origin.spi;

import java.io.File;
import java.util.ArrayList;
import java.util.ServiceLoader;
import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import org.apache.tomcat.util.scan.JarFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 插件实例加载工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 18:36
 */
public class SpiLoader {

    private Logger log = LoggerFactory.getLogger(getClass());
    private final static String JAR_FILE_SUFFIX = ".jar";
    //    private InjectResource injectResource;
    private URLClassLoader urlClassLoader;

    public SpiLoader(String jarFilePath) {
        File file = new File(jarFilePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("jar file does not exist, path=" + jarFilePath);
        }
        final URL[] urLs = getURLs(jarFilePath);
        if (urLs.length == 0) {
            throw new IllegalArgumentException("does not have any available jar in path:" + jarFilePath);
        }
//        this.injectResource = new DefaultInjectResource();
        this.urlClassLoader = new URLClassLoader(urLs, this.getClass().getClassLoader());
    }

    /**
     * 允许自定义
     *
     * @param injectResource
     */
//    public void setInjectResource(InjectResource injectResource) {
//        this.injectResource = injectResource;
//    }

    /**
     * 获取对应的插件模块
     *
     * @return
     */
//    public List<Components> getComponents() {
//        return loadObjectList(Components.class);
//    }
//
//    public void loadComponents() {
//        loadObjectList(Components.class);
//    }

    /**
     * 加载对应的实例对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> loadObjectList(Class<T> clazz) {
        List<T> objList = new ArrayList<>();
        // 基于SPI查找
        final ServiceLoader<T> moduleServiceLoader = ServiceLoader.load(clazz, this.urlClassLoader);

        final Iterator<T> moduleIt = moduleServiceLoader.iterator();
        while (moduleIt.hasNext()) {

            final T module;
            try {
                module = moduleIt.next();
            } catch (Throwable cause) {
                log.error("error load jar", cause);
                continue;
            }

            final Class<?> classOfModule = module.getClass();

            // 如果有注入对象
//            if (injectResource != null) {
//                for (final Field resourceField : FieldUtils.getFieldsWithAnnotation(classOfModule, Resource.class)) {
//                    final Class<?> fieldType = resourceField.getType();
//                    Object fieldObject = injectResource.getFieldValue(fieldType);
//                    if (fieldObject != null) {
//                        try {
//                            FieldUtils.writeField(
//                                    resourceField,
//                                    module,
//                                    fieldObject,
//                                    true
//                            );
//                        } catch (Exception e) {
//                            log.warn(" set Value error : " + e.getMessage());
//                        }
//                    }
//                }
//                injectResource.afterProcess(module);
//            }
            objList.add(module);
        }
        return objList;
    }

    /**
     * 获取模块jar的urls
     *
     * @param jarFilePath 插件路径
     * @return 插件URL列表
     */
    private URL[] getURLs(String jarFilePath) {
        File file = new File(jarFilePath);
        List<URL> jarPaths = Lists.newArrayList();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return jarPaths.toArray(new URL[0]);
            }
            for (File jarFile : files) {
                if (isJar(jarFile)) {
                    try {
                        File tempFile = File.createTempFile("manager_plugin", ".jar");
                        tempFile.deleteOnExit();
                        FileUtils.copyFile(jarFile, tempFile);
                        jarPaths.add(new URL("file:" + tempFile.getPath()));
                    } catch (IOException e) {
                        log.error("error occurred when get jar file", e);
                    }
                } else {
                    jarPaths.addAll(Arrays.asList(getURLs(jarFile.getAbsolutePath())));
                }
            }
        } else if (isJar(file)) {
            try {
                File tempFile = File.createTempFile("manager_plugin", ".jar");
                FileUtils.copyFile(file, tempFile);
                jarPaths.add(new URL("file:" + tempFile.getPath()));
            } catch (IOException e) {
                log.error("error occurred when get jar file", e);
            }
            return jarPaths.toArray(new URL[0]);
        } else {
            log.error("plugins jar path has no available jar, use empty url, path={}", jarFilePath);
        }
        return jarPaths.toArray(new URL[0]);
    }

    /**
     * @param file
     * @return
     */
    private boolean isJar(File file) {
        return file.isFile() && file.getName().endsWith(JAR_FILE_SUFFIX);
    }

    public static void main(String[] args) {
        String jarFile = "E:\\study\\sandbox\\sandbox-module\\";

//        JarFactory factory = new JarFactory(jarFile);
//        List<Components> components = factory.getComponents();
//
//        System.out.println(components);

    }
}
