package com.enset.dao;

import org.springframework.stereotype.Component;

@Component
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        double temp = Math.random() * 40.;
        return temp;
    }
}
