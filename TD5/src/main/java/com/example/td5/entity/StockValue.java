package com.example.td5.entity;



public class StockValue {
    private String unit;   // PCS, KG, L
    private Double value;

    public StockValue() {}
    public StockValue(String unit, Double value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}
