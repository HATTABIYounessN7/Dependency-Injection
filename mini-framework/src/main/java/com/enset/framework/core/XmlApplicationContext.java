package com.enset.framework.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.enset.framework.xml.BeanConfig;
import com.enset.framework.xml.Beans;
import com.enset.framework.xml.Property;

import jakarta.xml.bind.JAXBContext;

public class XmlApplicationContext implements ApplicationContext {

    private Map<String, Object> beans = new HashMap<>();

    public XmlApplicationContext(String configFile) {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Beans config = (Beans) context.createUnmarshaller().unmarshal(new File(configFile));

            for (BeanConfig beanConfig : config.getBeans()) {
                Class<?> beanConfigClass = Class.forName(beanConfig.getClassName());
                Object instance = beanConfigClass.getDeclaredConstructor().newInstance();
                beans.put(beanConfig.getId(), instance);
            }

            for (BeanConfig beanConfig : config.getBeans()) {
                Object targetBean = beans.get(beanConfig.getId());
                if (beanConfig.getProperties() != null) {
                    for (Property property : beanConfig.getProperties()) {
                        Object dependency = beans.get(property.getRef());
                        String setterName = "set" + property.getName().substring(0, 1).toUpperCase() +
                                property.getName().substring(1);
                        Method setter = targetBean.getClass()
                                .getMethod(setterName, dependency.getClass().getInterfaces()[0]);
                        setter.invoke(targetBean, dependency);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }
}
