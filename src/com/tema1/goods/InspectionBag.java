package com.tema1.goods;

public final class InspectionBag<T> extends Inventory<T> {
    private int bribe;
    private Goods declaredGoods;

    public Goods getDeclaredGoods() {
        return declaredGoods;
    }
    public void setDeclaredGoods(final Goods declaredGoods) {
        this.declaredGoods = declaredGoods;
    }

    public int getBribe() {
        return bribe;
    }
    public void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    @Override
    public void clear() {
        super.clear();
        setBribe(0);
        setDeclaredGoods(null);
    }
}
