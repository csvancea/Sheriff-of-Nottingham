package com.tema1.players;

import java.util.LinkedList;
import java.util.List;

public final class PlayerFactory {
    private final List<IPlayer> playerList;
    private static PlayerFactory instance = null;

    private PlayerFactory() {
        playerList = new LinkedList<IPlayer>();
    }

    public static PlayerFactory getInstance() {
        if (instance == null) {
            instance = new PlayerFactory();
        }
        return instance;
    }

    public void initPlayers(final List<String> playerNames) {
        for (int i = 0; i != playerNames.size(); ++i) {
            IPlayer player;
            switch (playerNames.get(i)) {
                case "basic":
                    player = new BasicPlayer(i);
                    break;
                case "bribed":
                    player = new BribedPlayer(i);
                    break;
                case "greedy":
                    player = new GreedyPlayer(i);
                    break;
                default:
                    System.out.println("invalid player strategy");
                    player = null;
            }
            playerList.add(player);
        }
    }

    public List<IPlayer> getPlayers() {
        return playerList;
    }
}
