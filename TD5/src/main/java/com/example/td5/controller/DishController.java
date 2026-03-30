package com.example.td5.controller;

import com.example.td5.entity.Dish;
import com.example.td5.entity.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredients(
            @PathVariable Long id,
            @RequestBody(required = false) List<Ingredient> ingredients) {

        if (ingredients == null) {
            return ResponseEntity.status(400)
                    .body("Request body is required and must contain a list of ingredients.");
        }

        try {
            Dish updated = dishService.updateIngredients(id, ingredients);
            return ResponseEntity.ok(updated);
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}