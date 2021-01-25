package io.github.mufidjamaluddin.ecommerce.product;

import io.github.mufidjamaluddin.ecommerce.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductHandler {

    private final ProductService productService;
    private final Validator validator;

    public ProductHandler(ProductService productService, Validator validator) {
        this.productService = productService;
        this.validator = validator;
    }

    @GetMapping("/")
    public Flux<ProductViewModel> getAllProducts() {
        return this.productService
                .getAllProducts()
                .map(Mapper::fromServiceModel);
    }

    @GetMapping("/{code}")
    public Mono<ProductViewModel> getProductByCode(
            @PathVariable("code") String code) {
        return this.productService
                .getProductByCode(code)
                .map(Mapper::fromServiceModel);
    }

    @PostMapping("/")
    public Mono<ResponseEntity<Object>> createProduct(
            ServerWebExchange serverWebExchange,
            @RequestBody ProductViewModel product) {

        var violations = this.validator.validate(product);
        if(!violations.isEmpty()) {
            return Mono.create(e -> ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList())
                )
            );
        }

        return SecurityUtils.getUserFromRequest(serverWebExchange)
                .switchIfEmpty(
                    Mono.create(e -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("{\"status\":\"unauthorized\"}"))
                )
                .map(user -> {
                    var data = Mapper.toServiceModel(product);

                    data.setCreatedAt(new Date());
                    data.setCreatedBy(user);

                    return this.productService
                        .createProduct(data)
                        .map(Mapper::fromServiceModel);
                })
                .flatMap(m -> m)
                .map(ResponseEntity::ok);
    }

    private static class Mapper {

        private static ProductViewModel fromServiceModel(Product product) {
            return ProductViewModel.builder()
                    .code(product.getCode())
                    .name(product.getName())
                    .price(product.getPrice())
                    .storeName(product.getStoreName())
                    .brandName(product.getBrandName())
                    .categoryName(product.getCategoryName())
                    .desc(product.getDesc())
                    .build();
        }

        private static Product toServiceModel(ProductViewModel productVm) {
            return Product.builder()
                    .code(productVm.getCode())
                    .name(productVm.getName())
                    .price(productVm.getPrice())
                    .storeName(productVm.getStoreName())
                    .brandName(productVm.getBrandName())
                    .categoryName(productVm.getCategoryName())
                    .desc(productVm.getDesc())
                    .build();
        }

    }

}
