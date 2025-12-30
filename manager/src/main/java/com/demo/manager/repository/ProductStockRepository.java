package com.demo.manager.repository;
/*
*  This repository can use for atomic operation. Using this the user can't
*  create racecondition o boilerplate
* */


import com.demo.manager.model.Product;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;


@Repository
public class ProductStockRepository {
    private final MongoTemplate mongoTemplate;

    public ProductStockRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public Integer applyStockDeltaIfPossible(String productId, int delta) {
        // Condition: exist the product, and quantity >= -delta when delta is negative
        Query query = new Query(Criteria.where("_id").is(productId));
        if (delta < 0) {
            query.addCriteria(Criteria.where("quantity").gte(Math.abs(delta)));
        }

        Update update = new Update().inc("quantity", delta);
        UpdateResult res = mongoTemplate.updateFirst(query, update, Product.class);

        if (res.getModifiedCount() == 1) {
            // retrieve the updated quantity
            Product updated = mongoTemplate.findById(productId, Product.class);
            return updated != null ? updated.getQuantity() : null;
        }
        return null; // condition not satisfating
    }

}
