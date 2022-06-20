package com.sma.backend.utils;

import java.security.SecureRandom;

/**
 * common class for random value.
 *
 * @author Mak Sophea
 * @since Date: 08/07/2020
 */
public final class SecureRandomDataUtils {

    private static final char[] DEFAULT_RANDOM_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] DEFAULT_RANDOM_NUMBER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private SecureRandomDataUtils() {
    }

    public static String getString(int minLength, int maxLength) {
        return getString(minLength, maxLength, DEFAULT_RANDOM_CHARS);
    }

    public static String getString(int minLength, int maxLength, char... characters) {
        if (minLength > maxLength) {
            throw new IllegalArgumentException("The minimum length can't be bigger than the maximum length");
        } else {
            int length = minLength + (int) (SECURE_RANDOM.nextDouble() * (double) (maxLength - minLength));
            return getString(length, characters);
        }
    }

    public static String getString(int length) {
        return getString(length, DEFAULT_RANDOM_CHARS);
    }

    public static String getString(int length, char... characters) {
        if (length < 0) {
            throw new IllegalArgumentException("Can't generate a string with negative length");
        } else {
            final StringBuilder sb = new StringBuilder(length);

            for (int i = 0; i < length; ++i) {
                int numberOfAvailableCharacters = characters.length;
                int characterIndex = (int) (SECURE_RANDOM.nextDouble() * (double) numberOfAvailableCharacters);
                sb.append(characters[characterIndex]);
            }

            return sb.toString();
        }
    }

    public static String getRandomNumber(int length) {
        return getString(length, DEFAULT_RANDOM_NUMBER);
    }
}
