package com.example.td5.service;

import com.example.td5.entity.Ingredient;
import com.example.td5.entity.StockValue;
import com.example.td5.exception.IngredientNotFoundException;
import com.example.td5.repository.IngredientRepository;
import com.example.td5.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepo;
    private final StockMovementRepository stockRepo;

    public IngredientService(IngredientRepository ingredientRepo, StockMovementRepository stockRepo) {
        this.ingredientRepo = ingredientRepo; this.stockRepo = stockRepo;
    }

    public List<Ingredient> getAll() throws SQLException {
        return ingredientRepo.findAll();
    }

    public Ingredient getById(Long id) throws SQLException {
        return ingredientRepo.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public StockValue getStock(Long id, LocalDateTime at, String unit) throws SQLException {
        getById(id); // vérifie existence
        return stockRepo.getStockAt(id, at, unit);
    }
}
