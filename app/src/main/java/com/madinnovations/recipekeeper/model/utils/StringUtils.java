package com.madinnovations.recipekeeper.model.utils;

/**
 * Created by madanle on 5/5/16.
 */
public final class StringUtils {
    /**
     * Compares two strings for equality. If both are null they are considered equal.
     *
     * @param value1  a string to compare
     * @param value2  another string to compare
     * @return true if the strings are the same, otherwise false.
     */
    public static boolean equals(String value1, String value2) {
        if(value1 == null) {
            return (value2 == null);
        }
        return value1.equals(value2);
    }
}
