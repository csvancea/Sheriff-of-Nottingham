package com.tema1.common;

import com.tema1.goods.GoodsType;
import com.tema1.goods.Inventory;
import com.tema1.players.IPlayer;

public final class Comparator {
    public static final class Player {
        public static final class ByMoney implements java.util.Comparator<IPlayer> {
            @Override
            public int compare(final IPlayer lhs, final IPlayer rhs) {
                return lhs.getMoney() - rhs.getMoney();
            }
        }

        public static final class ByID implements java.util.Comparator<IPlayer> {
            @Override
            public int compare(final IPlayer lhs, final IPlayer rhs) {
                return lhs.getId() - rhs.getId();
            }
        }
    }

    public static final class Goods {
        public static final class LegalFirst
                implements java.util.Comparator<Inventory.Entry<com.tema1.goods.Goods>> {
            @Override
            public int compare(final Inventory.Entry<com.tema1.goods.Goods> lhs,
                               final Inventory.Entry<com.tema1.goods.Goods> rhs) {
                if (lhs.getItem().getType() == GoodsType.Legal
                        && rhs.getItem().getType() == GoodsType.Illegal) {
                    return -1;
                }
                if (rhs.getItem().getType() == GoodsType.Legal
                        && lhs.getItem().getType() == GoodsType.Illegal) {
                    return 1;
                }
                return 0;
            }
        }

        public static final class EmptyFirst
                implements java.util.Comparator<Inventory.Entry<com.tema1.goods.Goods>> {
            @Override
            public int compare(final Inventory.Entry<com.tema1.goods.Goods> lhs,
                               final Inventory.Entry<com.tema1.goods.Goods> rhs) {
                if (lhs.getQuantity() > 0 && rhs.getQuantity() <= 0) {
                    return 1;
                }
                if (rhs.getQuantity() > 0 && lhs.getQuantity() < 0) {
                    return 0;
                }
                return 0;
            }
        }

        public static final class ByQuantityForLegals
                implements java.util.Comparator<Inventory.Entry<com.tema1.goods.Goods>> {
            @Override
            public int compare(final Inventory.Entry<com.tema1.goods.Goods> lhs,
                               final Inventory.Entry<com.tema1.goods.Goods> rhs) {
                if (lhs.getItem().getType() == rhs.getItem().getType()
                        && lhs.getItem().getType() == GoodsType.Legal) {
                    return new ByQuantity().compare(lhs, rhs);
                }
                return 0;
            }
        }

        public static final class ByQuantity
                implements java.util.Comparator<Inventory.Entry<com.tema1.goods.Goods>> {
            @Override
            public int compare(final Inventory.Entry<com.tema1.goods.Goods> lhs,
                               final Inventory.Entry<com.tema1.goods.Goods> rhs) {
                return lhs.getQuantity() - rhs.getQuantity();
            }
        }

        public static final class ByProfit
                implements java.util.Comparator<Inventory.Entry<com.tema1.goods.Goods>> {
            @Override
            public int compare(final Inventory.Entry<com.tema1.goods.Goods> lhs,
                               final Inventory.Entry<com.tema1.goods.Goods> rhs) {
                return lhs.getItem().getProfit() - rhs.getItem().getProfit();
            }
        }

        public static final class ByID
                implements java.util.Comparator<Inventory.Entry<com.tema1.goods.Goods>> {
            @Override
            public int compare(final Inventory.Entry<com.tema1.goods.Goods> lhs,
                               final Inventory.Entry<com.tema1.goods.Goods> rhs) {
                return lhs.getItem().getId() - rhs.getItem().getId();
            }
        }
    }
}
