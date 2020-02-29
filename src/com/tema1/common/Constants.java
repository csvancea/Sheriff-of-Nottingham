package com.tema1.common;

public final class Constants {
    private Constants() { }

    /**
     * Numarul de carti trase.
     */
    public static final int CARDS_TO_DRAW = 10;

    /**
     * Numarul maxim de carti ce pot fi date spre inspectie.
     */
    public static final int MAX_GOODS_IN_BAG = 8;

    /**
     * Suma de bani de inceput.
     */
    public static final int START_MONEY = 80;

    /**
     * Suma minima de bani necesara pentru a putea inspecta alt jucator.
     */
    public static final int MIN_MONEY_FOR_INSPECTION = 16;

    /**
     * Suma minima de bani necesara pentru a putea aplica strategia bribed.
     */
    public static final int MIN_MONEY_FOR_BRIBED_STRATEGY = 6;

    /**
     * Suma de bani reprezentand "mita mica".
     */
    public static final int BRIBE_MONEY_LOW = 5;

    /**
     * Suma de bani reprezentand "mita mare".
     */
    public static final int BRIBE_MONEY_HIGH = 10;

    /**
     * Numarul maxim de bunuri ilegale ce pot fi luate dand "mita mica".
     */
    public static final int MAX_ILLEGAL_GOODS_FOR_LOW_BRIBE = 2;
}
