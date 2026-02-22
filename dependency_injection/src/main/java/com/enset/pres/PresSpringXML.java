package com.enset.pres;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.*;

import com.enset.metier.IMetier;

public class PresSpringXML {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IMetier metier = context.getBean("metier", IMetier.class);
        System.out.println(metier.calcul());
    }
}
