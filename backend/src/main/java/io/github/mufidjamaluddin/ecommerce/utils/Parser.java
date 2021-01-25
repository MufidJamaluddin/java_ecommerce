package io.github.mufidjamaluddin.ecommerce.utils;

public class Parser {

    public static int ParseIntOrDefault(String num, int defaultNum) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return defaultNum;
        }
    }
}
