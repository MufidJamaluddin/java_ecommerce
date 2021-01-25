package io.github.mufidjamaluddin.ecommerce.member.exceptions;

public class AccountIsNotExistException extends Exception {
    public AccountIsNotExistException(String account) {
        super(String.format("account %s is not exist!", account));
    }
}
