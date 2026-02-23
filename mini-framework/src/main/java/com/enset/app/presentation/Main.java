package com.enset.app.presentation;

import com.enset.app.metier.IMetier;
import com.enset.framework.core.ApplicationContext;
import com.enset.framework.core.XmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new XmlApplicationContext("mini-framework/src/main/resources/config.xml");
        IMetier metier = (IMetier) context.getBean("metier");
        System.out.println(metier.calcul());
    }
}
