package com.tema1.main;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class GameCards {
    private static GameCards instance = null;
    private final Queue<Goods> cards;

    public static GameCards getInstance() {
        if (instance == null) {
            instance = new GameCards();
        }
        return instance;
    }

    private GameCards() {
        cards = new LinkedList<Goods>();
    }

    public void initCards(final List<Integer> goodsList) {
        for (int id : goodsList) {
            Goods goods = GoodsFactory.getInstance().getGoodsById(id);
            cards.add(goods);
        }
    }

    public Goods drawCard() {
        return cards.remove();
    }
    public List<Goods> drawCards() {
        List<Goods> ret = new LinkedList<Goods>();
        for (int i = 0; i != Constants.CARDS_TO_DRAW; ++i) {
            ret.add(drawCard());
        }
        return ret;
    }

    public void pushCard(final Goods card) {
        cards.add(card);
    }
    public void pushCards(final List<Goods> cardsList) {
        cards.addAll(cardsList);
    }
}
