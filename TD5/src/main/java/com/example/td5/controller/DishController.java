package com.example.td5.controller;

import com.example.td5.exception.DishNotFoundException;
import com.example.td5.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    // GET /dishes
    @GetMapping
    public ResponseEntity<?> getAllDishes() {
        try {
            return ResponseEntity.ok(dishService.getAllDishes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> addIngredientToDish(
            @PathVariable Long id,
            @RequestParam Long ingredientId) {
        try {
            dishService.addIngredientToDish(id, ingredientId);
            return ResponseEntity.ok("Ingredient added successfully");
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<?> getDishIngredients(
            @PathVariable Long id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround) {
        try {
            return ResponseEntity.ok(
                    dishService.getDishIngredients(id, ingredientName, ingredientPriceAround)
            );
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}