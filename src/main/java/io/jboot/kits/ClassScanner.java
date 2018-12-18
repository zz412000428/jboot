/**
 * Copyright (c) 2015-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.kits;

import io.jboot.JbootConsts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 */
public class ClassScanner {

    private static final Set<Class> appClasses = new HashSet<>();


    public static final Set<String> includeJars = new HashSet<>();
    public static void addScanJarPrefix(String prefix){
        includeJars.add(prefix);
    }
    static {
        includeJars.add("jboot-");
    }



    public static final Set<String> excludeJars = new HashSet<>();
    public static void addUnscanJarPrefix(String prefix){
        excludeJars.add(prefix);
    }
    static {
        excludeJars.add("jfinal-");
        excludeJars.add("cos-2017.5.jar");
        excludeJars.add("cglib-");
        excludeJars.add("undertow-");
        excludeJars.add("xnio-");
        excludeJars.add("javax.");
        excludeJars.add("HikariCP-");
        excludeJars.add("druid-");
        excludeJars.add("mysql-");
        excludeJars.add("junit-");
        excludeJars.add("hamcrest-");
        excludeJars.add("jboss-");
        excludeJars.add("motan-");
        excludeJars.add("commons-pool");
        excludeJars.add("commons-beanutils");
        excludeJars.add("commons-codec");
        excludeJars.add("commons-collections");
        excludeJars.add("commons-configuration");
        excludeJars.add("commons-lang");
        excludeJars.add("commons-logging");
        excludeJars.add("commons-pool");
        excludeJars.add("commons-io");
        excludeJars.add("commons-httpclient");
        excludeJars.add("commons-fileupload");
        excludeJars.add("commons-configuration");
        excludeJars.add("commons-validator");
        excludeJars.add("commons-email");
        excludeJars.add("hessian-");
        excludeJars.add("metrics-");
        excludeJars.add("javapoet-");
        excludeJars.add("netty-");
        excludeJars.add("consul-");
        excludeJars.add("gson-");
        excludeJars.add("httpcore-");
        excludeJars.add("zookeeper-");
        excludeJars.add("slf4j-");
        excludeJars.add("fastjson-");
        excludeJars.add("guava-");
        excludeJars.add("failureaccess-");
        excludeJars.add("listenablefuture-");
        excludeJars.add("jsr305-");
        excludeJars.add("checker-qual-");
        excludeJars.add("error_prone_annotations-");
        excludeJars.add("j2objc-");
        excludeJars.add("animal-sniffer-");
        excludeJars.add("cron4j-");
        excludeJars.add("jedis-");
        excludeJars.add("lettuce-");
        excludeJars.add("reactor-");
        excludeJars.add("fst-");
        excludeJars.add("kryo-");
        excludeJars.add("jackson-");
        excludeJars.add("javassist-");
        excludeJars.add("objenesis-");
        excludeJars.add("reflectasm-");
        excludeJars.add("asm-");
        excludeJars.add("minlog-");
        excludeJars.add("jsoup-");
        excludeJars.add("ons-client-");
        excludeJars.add("amqp-client-");
        excludeJars.add("ehcache-");
        excludeJars.add("sharding-");
        excludeJars.add("snakeyaml-");
        excludeJars.add("groovy-");
        excludeJars.add("profiler-");
        excludeJars.add("joda-time-");
        excludeJars.add("shiro-");
        excludeJars.add("dubbo-");
        excludeJars.add("curator-");
        excludeJars.add("resteasy-");
        excludeJars.add("reactive-");
        excludeJars.add("validation-");
        excludeJars.add("httpclient-");
        excludeJars.add("jcip-");
        excludeJars.add("jcl-");
        excludeJars.add("microprofile-");
        excludeJars.add("org.osgi");
        excludeJars.add("zkclient-");
        excludeJars.add("jjwt-");
        excludeJars.add("okhttp-");
        excludeJars.add("okio-");
        excludeJars.add("zbus-");
        excludeJars.add("swagger-");
        excludeJars.add("j2cache-");
        excludeJars.add("caffeine-");
        excludeJars.add("jline-");
        excludeJars.add("qpid-");
        excludeJars.add("geronimo-");
        excludeJars.add("activation-");
        excludeJars.add("idea_rt");
        excludeJars.add("MRJToolkit");
    }

    public static <T> List<Class<T>> scanSubClass(Class<T> pclazz) {
        return scanSubClass(pclazz, false);
    }


    public static <T> List<Class<T>> scanSubClass(Class<T> pclazz, boolean mustCanNewInstance) {
        initIfNecessary();
        List<Class<T>> classes = new ArrayList<>();
        findClassesByParent(classes, pclazz, mustCanNewInstance);
        return classes;
    }

    public static List<Class> scanClass() {
        return scanClass(false);
    }

    public static List<Class> scanClass(boolean mustCanNewInstance) {

        initIfNecessary();

        if (!mustCanNewInstance) {
            return new ArrayList<>(appClasses);
        }

        List<Class> list = new ArrayList<>();
        for (Class clazz : appClasses) {
            if (canNewInstance(clazz)) {
                list.add(clazz);
            }
        }

        return list;
    }


    private static boolean canNewInstance(Class clazz) {
        return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }


    public static List<Class> scanClassByAnnotation(Class annotationClass, boolean mustCanNewInstance) {
        initIfNecessary();

        List<Class> list = new ArrayList<>();
        for (Class clazz : appClasses) {
            Annotation annotation = clazz.getAnnotation(annotationClass);
            if (annotation == null) {
                continue;
            }

            if (mustCanNewInstance && !canNewInstance(clazz)) {
                continue;
            }

            list.add(clazz);
        }

        return list;
    }

    private static void initIfNecessary() {
        if (appClasses.isEmpty()) {
            addClassesFromFilePath(getRootClassPath());
            addClassesFromJars();
        }
    }


    private static <T> void findClassesByParent(List<Class<T>> classes, Class<T> pclazz, boolean mustCanNewInstance) {
        for (Class clazz : appClasses) {

            if (!pclazz.isAssignableFrom(clazz)) {
                continue;
            }

            if (mustCanNewInstance && !canNewInstance(clazz)) {
                continue;
            }

            classes.add(clazz);
        }
    }


    private static void addClassesFromJars() {

        Set<String> jars = new HashSet<>();

        findJars(jars, ClassScanner.class.getClassLoader());

        for (String path : jars) {

            JarFile jarFile = null;
            try {
                jarFile = new JarFile(path);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entryName = jarEntry.getName();
                    if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                        String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                        addClass(classForName(className));
                    }
                }
            } catch (IOException e1) {
            } finally {
                if (jarFile != null)
                    try {
                        jarFile.close();
                    } catch (IOException e) {
                    }
            }

        }
    }


    private static void addClassesFromFilePath(String filePath) {
        List<File> classFileList = new ArrayList<>();
        scanClassFile(classFileList, filePath);

        for (File file : classFileList) {

            int start = filePath.length();
            int end = file.toString().length() - ".class".length();

            String classFile = file.toString().substring(start + 1, end);
            String className = classFile.replace(File.separator, ".");

            addClass(classForName(className));
        }
    }

    private static void addClass(Class clazz) {
        if (clazz != null) appClasses.add(clazz);
    }


    private static void findJars(Set<String> jarPaths, ClassLoader classLoader) {
        try {
            if (classLoader instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
                URL[] urLs = urlClassLoader.getURLs();
                for (URL url : urLs) {
                    String path = url.getPath();
                    path = URLDecoder.decode(path, "UTF-8");

                    // path : /d:/xxx
                    if (path.startsWith("/") && path.indexOf(":") == 2) {
                        path = path.substring(1);
                    }

                    if (!path.toLowerCase().endsWith(".jar")) {
                        addClassesFromFilePath(new File(path).getCanonicalPath());
                    }

                    if (isIncludeJar(path)) {
                        if (JbootConsts.MODE.isDevMode()){
                            System.out.println("jboot scan jar : " + path);
                        }
                        jarPaths.add(path);
                    }
                }
            }
            ClassLoader parent = classLoader.getParent();
            if (parent != null) {
                findJars(jarPaths, parent);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isIncludeJar(String path) {

        String jarName = new File(path).getName();

        for (String exclude : includeJars) {
            if (jarName.startsWith(exclude)) {
                return true;
            }
        }

        for (String exclude : excludeJars) {
            if (jarName.startsWith(exclude)) {
                return false;
            }
        }

        if (path.startsWith(getJavaHome())) {
            return false;
        }

        return true;
    }


    @SuppressWarnings("unchecked")
    private static Class classForName(String className) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            return Class.forName(className, false, cl);
        } catch (Throwable ex) {
            //ignore
        }
        return null;
    }


    private static void scanClassFile(List<File> fileList, String path) {
        File files[] = new File(path).listFiles();
        if (null == files || files.length == 0)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                scanClassFile(fileList, file.getAbsolutePath());
            } else if (file.getName().endsWith(".class")) {
                fileList.add(file);
            }
        }
    }


    private static String javaHome;

    private static String getJavaHome() {
        if (javaHome == null) {
            try {
                javaHome = new File(System.getProperty("java.home"), "..").getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return javaHome;
    }

    private static String rootClassPath;

    private static String getRootClassPath() {
        if (rootClassPath == null) {
            try {
                String path = getClassLoader().getResource("").toURI().getPath();
                rootClassPath = new File(path).getAbsolutePath();
            } catch (Exception e) {
                try {
                    String path = ClassScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                    path = java.net.URLDecoder.decode(path, "UTF-8");
                    if (path.endsWith(File.separator)) {
                        path = path.substring(0, path.length() - 1);
                    }
                    rootClassPath = path;
                } catch (UnsupportedEncodingException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
        return rootClassPath;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : ClassScanner.class.getClassLoader();
    }


}