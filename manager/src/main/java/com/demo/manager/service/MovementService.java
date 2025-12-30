package com.demo.manager.service;

import com.demo.manager.repository.MovementRepository;
import org.springframework.stereotype.Service;

@Service
public class MovementService {
    private final MovementRepository movementRepository;

    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }


}
