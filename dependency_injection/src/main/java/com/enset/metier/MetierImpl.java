package com.enset.metier;

import com.enset.dao.IDao;

public class MetierImpl implements IMetier {
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
