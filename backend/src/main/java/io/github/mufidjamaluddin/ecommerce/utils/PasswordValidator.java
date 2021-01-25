package io.github.mufidjamaluddin.ecommerce.utils;

import io.github.mufidjamaluddin.ecommerce.shared.IRegisterViewModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordValidator
        implements ConstraintValidator<ValidPassword, IRegisterViewModel> {

    @Override
    public boolean isValid(IRegisterViewModel data,
                           ConstraintValidatorContext context) {

        return Objects.equals(data.getPassword(), data.getRetypePassword());
    }
}
