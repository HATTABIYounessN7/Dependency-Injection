package com.enset.pres;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.enset.metier.IMetier;

public class PresSpringAnnotation {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.enset");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println(metier.calcul());
    }
}
