package com.example.td5.service;


import com.example.td5.entity.Dish;
import com.example.td5.entity.Ingredient;
import com.example.td5.exception.DishNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepo;
    public DishService(DishRepository dishRepo) { this.dishRepo = dishRepo; }

    public List<Dish> getAll() throws SQLException {
        return dishRepo.findAll();
    }

    public Dish updateIngredients(Long id, List<Ingredient> ingredients) throws SQLException {
        dishRepo.findById(id).orElseThrow(() -> new DishNotFoundException(id));
        dishRepo.updateIngredients(id, ingredients);
        return dishRepo.findById(id).get();
    }
}
