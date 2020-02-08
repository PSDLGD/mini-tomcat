package com.being.tomcat.catalina.startup;

import java.lang.reflect.Method;

/**
 * Tomcat 启动类
 */
public final class Bootstrap {

    private Object catalinaDaemon = null;
    // 公用类加载器，其路径为common.loader，默认指向$CATALINA_HOME/lib下的包
    ClassLoader commonLoader = null;
    // 用于加载Tomcat应用服务器的类加载器，其路径为server.loader，默认为空。此时Tomcat使用common类加载器加载应用服务器。
    ClassLoader catalinaLoader = null;
    //
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

        Class<?> startupClass = catalinaLoader.loadClass("com.cream.tomcat.catalina.startup.Catalina");
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

    }


}
