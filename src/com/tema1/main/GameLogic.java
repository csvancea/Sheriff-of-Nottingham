package com.tema1.main;

import com.tema1.common.Comparator;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.LegalGoods;
import com.tema1.players.IPlayer;
import com.tema1.players.PlayerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class GameLogic {
    private final GameInput gameInput;

    public GameLogic(final GameInput gameInput) {
        this.gameInput = gameInput;
    }

    private void giveKingQueenBonuses() {
        for (Map.Entry<Integer, Goods> goods
                : GoodsFactory.getInstance().getAllGoods().entrySet()) {

            if (goods.getValue().getType() == GoodsType.Legal) {
                LegalGoods item = (LegalGoods) goods.getValue();

                // se sorteaza jucatorii descrescator dupa numarul de bunuri tip `item`
                // si crescator dupa id
                // deci primii 2 jucatori vor fi king, respectiv queen

                List<IPlayer> sortedList = PlayerFactory.getInstance().getPlayers().stream()
                        .sorted((lhs, rhs) -> {
                            int diff = rhs.getSavedInventory().getAmount(item)
                                    - lhs.getSavedInventory().getAmount(item);
                            if (diff != 0) {
                                return diff;
                            }
                            return new Comparator.Player.ByID().compare(lhs, rhs);
                        })
                        .collect(Collectors.toList());

                IPlayer king = sortedList.get(0);
                IPlayer queen = sortedList.get(1);

                if (king.getSavedInventory().getAmount(item) > 0) {
                    king.increaseMoney(item.getKingBonus());
                }
                if (queen.getSavedInventory().getAmount(item) > 0) {
                    queen.increaseMoney(item.getQueenBonus());
                }
            }
        }
    }

    private void giveGoodsProfit() {
        for (IPlayer player : PlayerFactory.getInstance().getPlayers()) {
            player.increaseMoney(player.computeCardsProfit());
        }
    }

    private void playSubRound(final int roundID, final IPlayer sheriff) {
        for (IPlayer player : PlayerFactory.getInstance().getPlayers()) {
            if (sheriff != player) {
                player.drawCards();
                player.prepareInspectionBag(roundID);
            }
        }

        sheriff.inspectPlayers();

        for (IPlayer player : PlayerFactory.getInstance().getPlayers()) {
            if (sheriff != player) {
                player.retrieveItemsFromInspectionBag();
                player.throwCards();
            }
        }
    }

    private void playRound(final int roundID) {
        for (IPlayer sheriff : PlayerFactory.getInstance().getPlayers()) {
            playSubRound(roundID, sheriff);
        }
    }

    public void play() {
        for (int i = 0; i != gameInput.getRounds(); ++i) {
            playRound(i);
        }

        giveGoodsProfit();
        giveKingQueenBonuses();
    }

    public void printStandings() {
        List<IPlayer> sortedList = PlayerFactory.getInstance().getPlayers().stream()
                .sorted(new Comparator.Player.ByMoney().reversed()
                        .thenComparing(new Comparator.Player.ByID()))
                .collect(Collectors.toList());

        sortedList.forEach((player) -> System.out.println(player.toString()));
    }
}
