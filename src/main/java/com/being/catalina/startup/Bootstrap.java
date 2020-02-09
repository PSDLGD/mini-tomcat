package com.being.catalina.startup;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Tomcat 启动类
 */
public final class Bootstrap {

    static {
        // Will always be non-null
        String userDir = System.getProperty("user.dir");
        // Home First
        String home = System.getProperty("mini-tomcat.base");
        File homeFile = null;
        if (home != null) {
            File f = new File(home);
            try {
                homeFile = f.getCanonicalFile();
            } catch (IOException ioe) {
                homeFile = f.getAbsoluteFile();
            }
        }
        if (homeFile == null) {
            // Fall-back. Use current directory
            File f = new File(userDir);
            try {
                homeFile = f.getCanonicalFile();
            } catch (IOException ioe) {
                homeFile = f.getAbsoluteFile();
            }
        }
        System.setProperty("mini-tomcat.base", homeFile.getPath());

    }

    private Object catalinaDaemon = null;
    // 公用类加载器，其路径为common.loader，默认指向$CATALINA_HOME/lib下的包
    ClassLoader commonLoader = null;
    // 用于加载Tomcat应用服务器的类加载器，其路径为server.loader，默认为空。此时Tomcat使用common类加载器加载应用服务器。
    ClassLoader catalinaLoader = null;
    // 是所有Web应用的父加载器，其路径为shared.loader，默认为空。此时Tomcat使用common类加载其作为Web应用的父加载器
    ClassLoader sharedLoader = null;

    public void start() throws Exception {
        if (catalinaDaemon == null) {
            init();
        }
        Method method = catalinaDaemon.getClass().getMethod("start", (Class[]) null);
        method.invoke(catalinaDaemon, (Object[]) null);


    }

    public void init() throws Exception {
        initClassLoaders();
        Thread.currentThread().setContextClassLoader(catalinaLoader);

        Class<?> startupClass = catalinaLoader.loadClass("com.being.catalina.startup.Catalina");
        Object startupInstance = startupClass.getConstructor().newInstance();
        String methodName = "setParentClassLoader";
        Class<?> paramTypes[] = new Class[1];
        paramTypes[0] = Class.forName("java.lang.ClassLoader");
        Object paramValues[] = new Object[1];
        paramValues[0] = sharedLoader;
        Method method = startupInstance.getClass().getMethod(methodName, paramTypes);
        method.invoke(startupInstance, paramValues);
        catalinaDaemon = startupInstance;
    }

    private void initClassLoaders() {
        try {
            commonLoader = createClassLoader("common", null);
            if (commonLoader == null) {
                commonLoader = this.getClass().getClassLoader();
            }
            catalinaLoader = createClassLoader("server", commonLoader);
            sharedLoader = createClassLoader("shared", commonLoader);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private ClassLoader createClassLoader(String name, ClassLoader parent) throws Exception {
        String value = CatalinaProperties.getProperty(name + ".loader");
        if (value == null || value.equals("")) {
            return parent;
        }
        Set<URL> set = new LinkedHashSet<>();
        File libDir = new File(System.getProperty("mini-tomcat.base"), "lib");
        if (libDir.exists()) {
            set.add(new URL(libDir.toURI().toString()));
            String[] fileNames = libDir.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    String fineName = fileName.toLowerCase(Locale.ENGLISH);
                    if (fineName.endsWith(".jar")) {
                        File file = new File(libDir, fileName);
                        file = file.getCanonicalFile();
                        URL url = new URL(file.toURI().toString());
                        set.add(url);
                    }
                }
            }
        }
        URL[] urls = new URL[set.size()];
        if (parent != null) {
            return new URLClassLoader(set.toArray(urls), parent);
        } else {
            return new URLClassLoader(set.toArray(urls));
        }
    }

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start();
    }


}
