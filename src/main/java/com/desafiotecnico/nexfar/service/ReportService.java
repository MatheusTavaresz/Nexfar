package com.desafiotecnico.nexfar.service;

import com.desafiotecnico.nexfar.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {


    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Order> findOrdersWithDynamicFilters(Map<String, Object> filters) {
        Query query = new Query();

        filters.forEach((key, value) -> {
            switch (key) {
                case "cnpj":
                    query.addCriteria(Criteria.where("client.cnpj").is(value));
                    break;
                case "createdAt":
                    Map<String, String> dates = (Map<String, String>) value;
                    query.addCriteria(Criteria.where("createdAt").gte(dates.get("from")).lte(dates.get("to")));
                    break;
                case "status":
                    query.addCriteria(Criteria.where("status").is(value));
                    break;
                case "netTotal":
                    Map<String, Double> total = (Map<String, Double>) value;
                    query.addCriteria(Criteria.where("netTotal").gte(total.get("min")).lte(total.get("max")));
                    break;
            }
        });

        return mongoTemplate.find(query, Order.class);
    }
}
