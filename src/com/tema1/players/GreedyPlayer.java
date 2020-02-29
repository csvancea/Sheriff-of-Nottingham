package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.Inventory;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

public final class GreedyPlayer extends BasicPlayer {
    public GreedyPlayer(final int id) {
        super(id);
    }

    @Override
    public PlayerStrategy getStrategy() {
        return PlayerStrategy.GREEDY;
    }

    @Override
    public void prepareInspectionBag(final int roundID) {
        // se joaca strategia de baza
        super.prepareInspectionBag(roundID);

        if (roundID % 2 == 1
                && getInspectionBag().getTotalItemCount() < Constants.MAX_GOODS_IN_BAG) {

            // se incearca adaugarea unui bun ilegal in rundele pare
            Inventory<Goods> orderedInv = super.getBasicOrderedInventory();
            for (Inventory.Entry<Goods> entry : orderedInv) {
                if (entry.getItem().getType() == GoodsType.Illegal && entry.getQuantity() > 0) {
                    getInspectionBag().increaseItemAmount(entry.getItem());
                    getInventory().decreaseItemAmount(entry.getItem());
                    break;
                }
            }
        }
    }

    @Override
    protected void inspectPlayer(final IPlayer inspected) {
        if (inspected.getInspectionBag().getBribe() > 0) {
            increaseMoney(inspected.getInspectionBag().getBribe());
            inspected.getInspectionBag().setBribe(0);
        } else {
            super.inspectPlayer(inspected);
        }
    }
}
