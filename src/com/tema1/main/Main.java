package com.tema1.main;

import com.tema1.players.PlayerFactory;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        GameLogic gameLogic = new GameLogic(gameInput);

        GameCards.getInstance().initCards(gameInput.getAssetIds());
        PlayerFactory.getInstance().initPlayers(gameInput.getPlayerNames());

        gameLogic.play();
        gameLogic.printStandings();
    }
}
