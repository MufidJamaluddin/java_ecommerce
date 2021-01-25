package io.github.mufidjamaluddin.ecommerce.member.exceptions;

public class AccountExistException extends Exception {
    public AccountExistException(String account) {
        super(String.format("account %s is already available!", account));
    }
}
