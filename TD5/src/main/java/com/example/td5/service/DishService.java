package com.example.td5.service;

import com.example.td5.entity.Dish;
import com.example.td5.entity.Ingredient;
import com.example.td5.exception.DishNotFoundException;
import com.example.td5.repository.DishRepository;
import com.example.td5.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class DishService {

    private final DishRepository       dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishService(DishRepository dishRepository,
                       IngredientRepository ingredientRepository) {
        this.dishRepository       = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Dish> getAllDishes() throws SQLException {
        return dishRepository.findAll();
    }

    public List<Ingredient> getDishIngredients(
            Long dishId,
            String ingredientName,
            Double ingredientPriceAround) throws SQLException {

        dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException(dishId));

        return ingredientRepository.findByDishIdWithFilters(
                dishId, ingredientName, ingredientPriceAround
        );
    }

    public void addIngredientToDish(Long dishId, Long ingredientId) throws SQLException {
        dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException(dishId));
        dishRepository.addIngredientToDish(dishId, ingredientId);
    }
}