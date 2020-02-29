package com.tema1.players;

import com.tema1.common.Comparator;
import com.tema1.common.Constants;
import com.tema1.goods.Inventory;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

import java.util.stream.Stream;

public final class BribedPlayer extends BasicPlayer {
    public BribedPlayer(final int id) {
        super(id);
    }

    @Override
    public PlayerStrategy getStrategy() {
        return PlayerStrategy.BRIBED;
    }

    @Override
    public void prepareInspectionBag(final int roundID) {
        // se ordoneaza cartile dupa criteriul bribed
        Inventory<Goods> orderedInv = getInventory().sort(
                new Comparator.Goods.LegalFirst().reversed()
                .thenComparing(new Comparator.Goods.EmptyFirst().reversed())
                .thenComparing(new Comparator.Goods.ByProfit().reversed())
                .thenComparing(new Comparator.Goods.ByID().reversed()));

        Inventory.Entry<Goods> firstEntry = orderedInv.get(0);
        if (firstEntry.getItem().getType() == GoodsType.Legal
                || getMoney() < Constants.MIN_MONEY_FOR_BRIBED_STRATEGY) {

            // nu se poate juca strategia bribed
            // se face fallback la basic
            super.prepareInspectionBag(roundID);
            return;
        }


        int possiblePenalty = 0;
        int illegalItemsCount = 0;
        int bribe;
        int maxIllegalItems;

        // numarul maxim de carti ilegale ce pot fi luate (in functie de banii pentru mita)
        if (getMoney() >= Constants.BRIBE_MONEY_HIGH) {
            maxIllegalItems = Constants.MAX_GOODS_IN_BAG;
        } else {
            maxIllegalItems = Constants.MAX_ILLEGAL_GOODS_FOR_LOW_BRIBE;
        }

        for (Inventory.Entry<Goods> entry : orderedInv) {
            // numarul de carti luate este minimul dintre:
            int itemsToAdd = Stream.of(
                    (getMoney() - possiblePenalty - 1) / entry.getItem().getPenalty(),
                    entry.getQuantity(),
                    Constants.MAX_GOODS_IN_BAG - getInspectionBag().getTotalItemCount()
            ).min(java.util.Comparator.naturalOrder()).get();

            if (entry.getItem().getType() == GoodsType.Illegal) {
                itemsToAdd = Math.min(itemsToAdd, maxIllegalItems);
                maxIllegalItems -= itemsToAdd;
                illegalItemsCount += itemsToAdd;
            }

            possiblePenalty += entry.getItem().getPenalty() * itemsToAdd;
            getInspectionBag().increaseItemAmount(entry.getItem(), itemsToAdd);
        }

        if (illegalItemsCount > Constants.MAX_ILLEGAL_GOODS_FOR_LOW_BRIBE) {
            bribe = Constants.BRIBE_MONEY_HIGH;
        } else {
            bribe = Constants.BRIBE_MONEY_LOW;
        }
        getInspectionBag().setDeclaredGoods(GoodsFactory.getInstance()
                .getGoodsById(GoodsFactory.LegalGoodsIds.APPLE));
        getInspectionBag().setBribe(bribe);
        decreaseMoney(bribe);
    }

    @Override
    public void inspectPlayers() {
        IPlayer ltPlayer = PlayerFactory.getInstance().getPlayers()
                .get(Math.floorMod(getId() - 1, PlayerFactory.getInstance().getPlayers().size()));
        IPlayer rtPlayer = PlayerFactory.getInstance().getPlayers()
                .get(Math.floorMod(getId() + 1, PlayerFactory.getInstance().getPlayers().size()));

        inspectPlayer(ltPlayer);
        if (ltPlayer != rtPlayer) {
            inspectPlayer(rtPlayer);
        }

        for (IPlayer player : PlayerFactory.getInstance().getPlayers()) {
            if (player != ltPlayer && player != rtPlayer && player != this) {
                increaseMoney(player.getInspectionBag().getBribe());
                player.getInspectionBag().setBribe(0);
            }
        }
    }
}
