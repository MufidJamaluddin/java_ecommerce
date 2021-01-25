package io.github.mufidjamaluddin.ecommerce.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigInteger;

@Data
@Builder
public class ProductViewModel {

    @NotBlank(message = "code must be filled")
    @Max(value = 10, message = "code should not be greater than 50")
    @JsonProperty("code")
    private String code;

    @NotBlank(message = "product name must be filled")
    @Max(value = 50, message = "product name should not be greater than 50")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "price must be filled")
    @PositiveOrZero(message = "price must be valid")
    @JsonProperty("price")
    private BigInteger price;

    @NotBlank(message = "store name must be filled")
    @Max(value = 50, message = "store name should not be greater than 50")
    @JsonProperty("storeName")
    private String storeName;

    @NotBlank(message = "brand name must be filled")
    @Max(value = 50, message = "brand name should not be greater than 50")
    @JsonProperty("brandName")
    private String brandName;

    @NotBlank(message = "category name must be filled")
    @Max(value = 50, message = "category name should not be greater than 50")
    @JsonProperty("categoryName")
    private String categoryName;

    @NotBlank(message = "product description must be filled")
    @JsonProperty("desc")
    private String desc;
}
