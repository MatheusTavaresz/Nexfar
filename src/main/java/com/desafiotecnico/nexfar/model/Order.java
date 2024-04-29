package com.desafiotecnico.nexfar.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "order")
public class Order {
        @Id
        private String id;

        @Field("orderId")
        private String orderId;

        @Field("client")
        private Client client;

        @Field("items")
        private List<Item> items;

        @Field("createdAt")
        private Date createdAt;

        @Field("status")
        private String status;

        @Field("netTotal")
        private Double netTotal;

        @Field("totalWithTaxes")
        private Double totalWithTaxes;


}

