package com.example.td5.controller;

import com.example.td5.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<?> getAllIngredients() {
        try {
            return ResponseEntity.ok(ingredientService.getAllIngredients());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Long id) {
        try {
            return ingredientService.getIngredientById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStockById(@PathVariable Long id) {
        try {
            Optional<Integer> stock = ingredientService.getStockById(id);
            if (stock.isPresent()) {
                return ResponseEntity.ok(stock.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}