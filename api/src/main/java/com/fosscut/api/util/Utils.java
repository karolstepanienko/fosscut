package com.fosscut.api.util;

import java.util.Random;

public class Utils {
    public static String generateRandomLowerCaseAlphanumericString(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 97)) // remove special characters and uppercase letters
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
        return generatedString;
    }
}
