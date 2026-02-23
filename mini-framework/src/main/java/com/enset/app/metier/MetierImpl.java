package com.enset.app.metier;

import com.enset.app.dao.IDao;
import com.enset.framework.annotations.Component;
import com.enset.framework.annotations.Inject;

@Component("metier")
public class MetierImpl implements IMetier {
    private IDao dao;

    @Inject
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}
