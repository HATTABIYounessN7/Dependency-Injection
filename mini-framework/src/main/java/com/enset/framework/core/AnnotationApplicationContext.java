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

    /**
     * Initialise le contexte d'application en analysant le package spécifié
     * et en injectant les dépendances dans les beans découverts.
     * 
     * @param basePackage le package racine à analyser
     */
    public AnnotationApplicationContext(String basePackage) {
        try {
            scanPackage(basePackage);
            injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère un bean enregistré par son nom.
     * 
     * @param name le nom du bean à récupérer
     * @return l'objet bean correspondant, ou null s'il n'existe pas
     */
    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }

    /**
     * Analyse le package spécifié pour découvrir les classes annotées
     * avec @Component.
     * 
     * @param basePackage le package racine à analyser
     * @throws Exception si le package n'existe pas ou en cas d'erreur de chargement
     */
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

    /**
     * Analyse récursivement les répertoires pour découvrir et enregistrer les
     * classes
     * annotées avec @Component comme beans dans le contexte.
     * 
     * @param directory   le répertoire à analyser
     * @param packageName le nom du package correspondant
     * @throws Exception en cas d'erreur lors du chargement des classes
     */
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

    /**
     * Crée une instance de la classe spécifiée en utilisant un constructeur
     * annoté @Inject
     * ou le constructeur par défaut s'il existe.
     * 
     * @param targetClass la classe pour laquelle créer une instance
     * @return l'instance créée avec dépendances injectées
     * @throws Exception en cas d'erreur lors de l'instantiation
     */
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

    /**
     * Injecte les dépendances dans tous les beans enregistrés en analysant les
     * champs
     * et méthodes annotés avec @Inject.
     * 
     * @throws Exception en cas d'erreur lors de l'injection des dépendances
     */
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

    /**
     * Résout une dépendance en cherchant un bean du type spécifié parmi les beans
     * enregistrés.
     * 
     * @param type le type de la dépendance à résoudre
     * @return le bean correspondant au type
     * @throws RuntimeException si aucun bean ne correspond au type spécifié
     */
    private Object resolveDependency(Class<?> type) {
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }

        throw new RuntimeException("No bean for type: " + type);
    }

    /**
     * Résout un tableau de dépendances en cherchant des beans correspondant aux
     * types spécifiés.
     * 
     * @param types les types des dépendances à résoudre
     * @return un tableau des beans correspondant aux types fournis
     */
    private Object[] resolveDependencies(Class<?>[] types) {
        Object[] dependencies = new Object[types.length];

        for (int i = 0; i < types.length; i++) {
            dependencies[i] = resolveDependency(types[i]);
        }

        return dependencies;
    }
}
