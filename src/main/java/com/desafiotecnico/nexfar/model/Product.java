package com.desafiotecnico.nexfar.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Product {
    @Field("sku")
    private String sku;

    @Field("name")
    private String name;
}
