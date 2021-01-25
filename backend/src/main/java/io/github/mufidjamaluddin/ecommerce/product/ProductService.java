package io.github.mufidjamaluddin.ecommerce.product;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Mono<Product> getProductByCode(String code) {
        return this.productRepository.findById(code);
    }

    public Mono<Product> createProduct(Product product) {
        return this.productRepository.save(product);
    }
}
