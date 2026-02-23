package com.enset.framework.core;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.enset.framework.annotations.Component;
import com.enset.framework.annotations.Inject;

public class AnnotationApplicationContext implements ApplicationContext {
    private Map<String, Object> beans = new HashMap<>();

    public AnnotationApplicationContext(String basePackage) {
        try {
            scanPackage(basePackage);
            injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }

    private void scanPackage(String basePackage) throws Exception {
        String path = basePackage.replace(".", "/");
        System.out.println("Base Package : " + basePackage + " & Path: " + path);
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        System.out.println("Resource: " + resource);
        if (resource == null) {
            throw new RuntimeException("Package not found: " + basePackage);
        }

        File directory = new File(resource.toURI());
        System.out.println("Directory: " + directory);

        scanDirectory(directory, basePackage);
    }

    private void scanDirectory(File directory, String packageName) throws Exception {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> targetClass = Class.forName(className);
                if (targetClass.isAnnotationPresent(Component.class)) {
                    Component component = targetClass.getAnnotation(Component.class);
                    String beanName = component.value().isEmpty() ? targetClass.getSimpleName() : component.value();
                    Object instance = createInstance(targetClass);
                    beans.put(beanName, instance);
                }
            }

        }
    }

    private Object createInstance(Class<?> targetClass) throws Exception {
        for (Constructor<?> constructor : targetClass.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = resolveDependencies(parameterTypes);
                return constructor.newInstance(dependencies);
            }
        }

        return targetClass.getDeclaredConstructor().newInstance();
    }

    private void injectDependencies() throws Exception {
        for (Object bean : beans.values()) {
            Class<?> targetClass = bean.getClass();

            for (Field field : targetClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Object dependency = resolveDependency(field.getType());
                    field.setAccessible(true);
                    field.set(bean, dependency);
                }
            }

            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Inject.class)) {
                    Object dependency = resolveDependency(method.getParameterTypes()[0]);
                    method.invoke(bean, dependency);
                }
            }
        }
    }

    private Object resolveDependency(Class<?> type) {
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }

        throw new RuntimeException("No bean for type: " + type);
    }

    private Object[] resolveDependencies(Class<?>[] types) {
        Object[] dependencies = new Object[types.length];

        for (int i = 0; i < types.length; i++) {
            dependencies[i] = resolveDependency(types[i]);
        }

        return dependencies;
    }
}
