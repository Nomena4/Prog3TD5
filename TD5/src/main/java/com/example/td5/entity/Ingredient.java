package com.example.td5.entity;

public class Ingredient {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;

    public Ingredient() {}
    public Ingredient(Long id, String name, String category, Double price, Integer stock) {
        this.id       = id;
        this.name     = name;
        this.category = category;
        this.price    = price;
        this.stock    = stock;
    }

    public Long    getId()       { return id; }
    public void    setId(Long id) { this.id = id; }
    public String  getName()     { return name; }
    public void    setName(String name) { this.name = name; }
    public String  getCategory() { return category; }
    public void    setCategory(String category) { this.category = category; }
    public Double  getPrice()    { return price; }
    public void    setPrice(Double price) { this.price = price; }
    public Integer getStock()    { return stock; }
    public void    setStock(Integer stock) { this.stock = stock; }
}