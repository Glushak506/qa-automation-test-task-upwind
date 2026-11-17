package com.example.utils;

public final class PriceUtils {

    private PriceUtils() {
    }

    //for one method utils package maybe too much, but for perspective is correct vector i think
    public static double toNumber(String price) {

        return Double.parseDouble(price.replace("$", ""));
    }
}
