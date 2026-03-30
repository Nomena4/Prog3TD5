package com.example.td5.entity;



import java.util.List;

public class Dish {
    private Long id;
    private String name;
    private Double sellingPrice;
    private List<Ingredient> ingredients;

    public Dish() {}
    public Dish(Long id, String name, Double sellingPrice, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.ingredients = ingredients;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(Double sellingPrice) { this.sellingPrice = sellingPrice; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
}
