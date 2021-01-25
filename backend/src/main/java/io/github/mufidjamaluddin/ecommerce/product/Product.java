package io.github.mufidjamaluddin.ecommerce.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Member")
public class Product {

    @Id
    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("price")
    private BigInteger price;

    @Column("store_name")
    private String storeName;

    @Column("brand_name")
    private String brandName;

    @Column("category_name")
    private String categoryName;

    @Column("desc")
    private String desc;

    @Column("created_by")
    private String createdBy;

    @Column("created_at")
    private Date createdAt;
}
