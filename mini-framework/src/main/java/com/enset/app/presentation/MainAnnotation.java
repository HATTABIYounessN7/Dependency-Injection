package com.enset.app.presentation;

import com.enset.app.metier.IMetier;
import com.enset.framework.core.AnnotationApplicationContext;
import com.enset.framework.core.ApplicationContext;

public class MainAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationApplicationContext("com.enset.app");
        IMetier metier = (IMetier) context.getBean("metier");
        System.out.println(metier.calcul());
    }
}
