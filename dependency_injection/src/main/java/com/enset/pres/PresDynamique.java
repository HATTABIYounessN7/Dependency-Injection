package com.enset.pres;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

import com.enset.dao.IDao;
import com.enset.metier.IMetier;

public class PresDynamique {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(new File("config.txt"))) {

            String daoClassName = scanner.nextLine();
            String metierClassname = scanner.nextLine();

            Class<?> cDao = Class.forName(daoClassName);
            Class<?> cMetier = Class.forName(metierClassname);

            IDao dao = (IDao) cDao.getDeclaredConstructor().newInstance();
            IMetier metier = (IMetier) cMetier.getDeclaredConstructor().newInstance();

            Method method = cMetier.getMethod("setDao", IDao.class);
            method.invoke(metier, dao);

            System.out.println(metier.calcul());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
