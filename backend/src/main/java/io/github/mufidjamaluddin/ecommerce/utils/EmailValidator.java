package io.github.mufidjamaluddin.ecommerce.utils;

import java.util.regex.Pattern;

public class EmailValidator {

    /*
     Thread Safe (https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html)
     */
    private static final Pattern emailPatternValidator;

    static {
        emailPatternValidator = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        );
    }

    public static boolean isEmail(String email) {
        var matcher = emailPatternValidator.matcher(email);
        return matcher.matches();
    }

}
