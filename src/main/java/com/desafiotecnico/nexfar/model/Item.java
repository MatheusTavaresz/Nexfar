package com.desafiotecnico.nexfar.model;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
public class Item {
    @Field("product")
    private Product product;

    @Field("quantity")
    private Integer quantity;

    @Field("price")
    private Double price;

    @Field("finalPrice")
    private Double finalPrice;
}