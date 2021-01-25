package io.github.mufidjamaluddin.ecommerce.member.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.sql.Date;

@Data
@Builder
public class MemberViewModel {

    @JsonProperty("id")
    private String id;

    @NotBlank(message = "mobile number must be filled")
    @Size(min = 11, max = 13, message
            = "mobile phone must be valid (11-13 digits)")
    @JsonProperty("mobile_number")
    private String mobileNumber;

    @NotBlank(message = "email must be filled")
    @Email(message = "email should be valid")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "mobile number must be filled")
    @Max(value = 50, message = "first name should not be greater than 50")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "last name must be filled")
    @Max(value = 50, message = "last name should not be greater than 50")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "gender must be filled")
    @JsonProperty("gender")
    private String gender;

    @NotNull(message = "date of birth must be filled")
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("roles")
    private String roles;
}
