package com.demo.manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovementRepository extends MongoRepository<MovementRepository, String> {
}
