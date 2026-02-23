package com.enset.app.dao;

import com.enset.framework.annotations.Component;

@Component("dao")
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        return Math.random() * 100;
    }
}
