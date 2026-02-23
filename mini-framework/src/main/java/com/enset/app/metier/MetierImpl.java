package com.enset.app.metier;

import com.enset.app.dao.IDao;

public class MetierImpl implements IMetier {
    private IDao dao;

    /*
     * public MetierImpl(IDao dao) {
     * this.dao = dao;
     * }
     */

    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}
