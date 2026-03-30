package com.example.td5.controller;


import com.example.td5.entity.Ingredient;
import com.example.td5.entity.StockValue;
import com.example.td5.repository.IngredientRepository;
import com.example.td5.repository.StockMovementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;
    private final StockMovementRepository stockMovementRepository;

    public IngredientController(IngredientRepository ingredientRepository,
                                StockMovementRepository stockMovementRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Long id) {
        Optional<Ingredient> found = ingredientRepository.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }
        return ResponseEntity.ok(found.get());
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStock(
            @PathVariable Long id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit) {


        if (at == null || unit == null) {
            return ResponseEntity.status(400)
                    .body("Either mandatory query parameter `at` or `unit` is not provided.");
        }


        Optional<Ingredient> found = ingredientRepository.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }

        LocalDateTime atDate;
        try {
            atDate = LocalDateTime.parse(at);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(400)
                    .body("Invalid date format for `at`. Expected ISO-8601 (e.g. 2025-01-15T10:00:00)");
        }

        StockValue stock = stockMovementRepository.getStockAt(id, atDate, unit);
        return ResponseEntity.ok(stock);
    }
}
