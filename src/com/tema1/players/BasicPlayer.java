package com.tema1.players;

import com.tema1.common.Comparator;
import com.tema1.common.Constants;
import com.tema1.goods.Inventory;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.GoodsType;
import com.tema1.goods.InspectionBag;
import com.tema1.main.GameCards;

import java.util.Map;

public class BasicPlayer implements IPlayer {
    private int money;
    private final Inventory<Goods> inventory;
    private final Inventory<Goods> savedInventory;
    private final InspectionBag<Goods> inspectionBag;
    private final int id;

    public BasicPlayer(final int id) {
        this.id = id;
        this.inventory = new Inventory<Goods>();
        this.savedInventory = new Inventory<Goods>();
        this.inspectionBag = new InspectionBag<Goods>();
        setMoney(Constants.START_MONEY);
    }

    /**
     * Getter pentru id-ul jucatorului.
     * @return id-ul jucatorului
     */
    @Override
    public final int getId() {
        return id;
    }

    /**
     * Getter pentru strategia jucatorului.
     * @return strategia jucatorului
     */
    @Override
    public PlayerStrategy getStrategy() {
        return PlayerStrategy.BASIC;
    }

    /**
     * Getter pentru inventarul cartilor trase ultima data.
     * @return inventar
     */
    @Override
    public final Inventory<Goods> getInventory() {
        return inventory;
    }

    /**
     * Getter pentru inventarul cartilor care au trecut de inspectie.
     * @return inventar
     */
    @Override
    public final Inventory<Goods> getSavedInventory() {
        return savedInventory;
    }

    /**
     * Getter pentru sacul dat spre inspectie.
     * @return sacul dat spre inspectie
     */
    @Override
    public final InspectionBag<Goods> getInspectionBag() {
        return inspectionBag;
    }

    /**
     * Getter pentru bani.
     * @return suma de bani detinuta de jucator
     */
    @Override
    public final int getMoney() {
        return money;
    }

    /**
     * Setter pentru bani.
     */
    @Override
    public final void setMoney(final int money) {
        this.money = money;
    }

    /**
     * Trage un nou set de carti.
     */
    @Override
    public final void drawCards() {
        for (Goods goods: GameCards.getInstance().drawCards()) {
            inventory.increaseItemAmount(goods);
        }
    }

    /**
     * Arde cartile ramase nefolosite.
     */
    @Override
    public final void throwCards() {
        inventory.clear();
    }

    /**
     * Ordoneaza cartile trase dupa criteriul basic.
     * @return un nou inventar sortat
     */
    protected final Inventory<Goods> getBasicOrderedInventory() {
        Inventory<Goods> orderedInv = getInventory().sort(new Comparator.Goods.LegalFirst()
                        .thenComparing(new Comparator.Goods.EmptyFirst().reversed())
                        .thenComparing(new Comparator.Goods.ByQuantityForLegals().reversed())
                        .thenComparing(new Comparator.Goods.ByProfit().reversed())
                        .thenComparing(new Comparator.Goods.ByID().reversed()));

        return orderedInv;
    }

    /**
     * Scaneaza cartile din inventar si le selecteaza doar pe cele relevante strategiei jucatorului.
     * @param roundID runda curenta (incepe de la 0)
     */
    @Override
    public void prepareInspectionBag(final int roundID) {
        Inventory<Goods> orderedInv = getBasicOrderedInventory();
        Inventory.Entry<Goods> firstEntry = orderedInv.get(0);

        int amount;
        getInspectionBag().setBribe(0);
        if (firstEntry.getItem().getType() == GoodsType.Legal) {
            amount = Math.min(firstEntry.getQuantity(), Constants.MAX_GOODS_IN_BAG);
            getInspectionBag().setDeclaredGoods(firstEntry.getItem());
        } else {
            amount = 1;
            getInspectionBag().setDeclaredGoods(GoodsFactory.getInstance()
                    .getGoodsById(GoodsFactory.LegalGoodsIds.APPLE));
        }
        getInspectionBag().setAmount(firstEntry.getItem(), amount);
        getInventory().decreaseItemAmount(firstEntry.getItem(), amount);
    }

    /**
     * Salveaza bunurile care au trecut de inspectie.
     * Adauga bonusul pentru cartile ilegale (daca este cazul).
     * Recupereaza mita (daca este cazul).
     */
    @Override
    public final void retrieveItemsFromInspectionBag() {
        for (Inventory.Entry<Goods> entry : getInspectionBag()) {
            // se salveaza bunul de acest tip
            getSavedInventory().increaseItemAmount(entry.getItem(), entry.getQuantity());

            // adauga bonusurile cartilor ilegale
            if (entry.getItem().getType() == GoodsType.Illegal) {
                for (Map.Entry<Goods, Integer> illegalEntry
                        : ((IllegalGoods) entry.getItem()).getIllegalBonus().entrySet()) {
                    getSavedInventory().increaseItemAmount(illegalEntry.getKey(),
                            illegalEntry.getValue() * entry.getQuantity());
                }
            }
        }

        // recupereaza mita (daca este cazul)
        increaseMoney(getInspectionBag().getBribe());
        getInspectionBag().clear();
    }

    /**
     * Calculeaza profitul acumulat in urma cartilor salvate.
     * @return profitul calculat
     */
    @Override
    public final int computeCardsProfit() {
        int profit = 0;
        for (Inventory.Entry<Goods> entry : getSavedInventory()) {
            profit += entry.getItem().getProfit() * entry.getQuantity();
        }
        return profit;
    }

    /**
     * Inspecteaza un jucator conform strategiei jucatorului.
     * @param inspected jucatorul inspectat
     */
    protected void inspectPlayer(final IPlayer inspected) {
        boolean penaltyApplied = false;

        if (getMoney() < Constants.MIN_MONEY_FOR_INSPECTION) {
            return;
        }

        for (Inventory.Entry<Goods> entry : inspected.getInspectionBag()) {
            if (!entry.getItem().equals(inspected.getInspectionBag().getDeclaredGoods())) {
                // se aplica penalty pentru bun nedeclarat
                int penalty = entry.getItem().getPenalty() * entry.getQuantity();

                inspected.decreaseMoney(penalty);
                increaseMoney(penalty);
                penaltyApplied = true;
                entry.setQuantity(0);

                GameCards.getInstance().pushCard(entry.getItem());
            }
        }

        if (!penaltyApplied) {
            // se aplica penalty pentru inspectie nefondata
            int penalty = inspected.getInspectionBag().getTotalItemCount()
                    * inspected.getInspectionBag().getDeclaredGoods().getPenalty();
            inspected.increaseMoney(penalty);
            decreaseMoney(penalty);
        }
    }

    /**
     * Aplica criteriul de selectie al jucatorilor care urmeaza a fi inspectati conform strategiei.
     */
    @Override
    public void inspectPlayers() {
        for (IPlayer player : PlayerFactory.getInstance().getPlayers()) {
            if (this != player) {
                inspectPlayer(player);
            }
        }
    }

    @Override
    public final String toString() {
        return getId() + " " + getStrategy() + " " + getMoney();
    }
}
