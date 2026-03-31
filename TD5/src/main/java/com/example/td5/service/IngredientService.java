package com.example.td5.service;


import com.example.td5.entity.Ingredient;
import com.example.td5.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() throws SQLException {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getIngredientById(Long id) throws SQLException {
        return ingredientRepository.findById(id);
    }

    public Optional<Integer> getStockById(Long id) throws SQLException {
        return ingredientRepository.findStockById(id);
    }
}