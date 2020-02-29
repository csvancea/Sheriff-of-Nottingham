package com.tema1.players;

import com.tema1.goods.Inventory;
import com.tema1.goods.Goods;
import com.tema1.goods.InspectionBag;

public interface IPlayer {
    PlayerStrategy getStrategy();

    void drawCards();
    void throwCards();

    Inventory<Goods> getInventory();
    Inventory<Goods> getSavedInventory();
    InspectionBag<Goods> getInspectionBag();
    int getId();

    int getMoney();
    void setMoney(int money);
    default void increaseMoney(final int money) {
        setMoney(getMoney() + money);
    }
    default void decreaseMoney(final int money) {
        setMoney(getMoney() - money);
    }

    int computeCardsProfit();

    void retrieveItemsFromInspectionBag();
    void prepareInspectionBag(int roundID);
    void inspectPlayers();
}
