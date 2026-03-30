package com.example.td5.exception;



public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(Long id) {
        super("Dish.id=" + id + " is not found");
    }
}
