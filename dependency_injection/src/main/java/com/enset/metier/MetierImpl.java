package com.enset.metier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enset.dao.IDao;

@Component
public class MetierImpl implements IMetier {

    @Autowired
    private IDao dao;

    public double calcul() {
        double data = dao.getData();
        return data * 2;
    }

    public IDao getDao() {
        return dao;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }

}
