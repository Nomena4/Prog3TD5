package com.example.td5.exception;



public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(Long id) {
        super("Ingredient.id=" + id + " is not found");
    }
}
