package com.desafiotecnico.nexfar.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Client {
    @Field("cnpj")
    private String cnpj;

    @Field("name")
    private String name;
}