package com.example.td5.repository;

import com.example.td5.entity.Ingredient;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class IngredientRepository {

    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Ingredient map(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getInt("stock")
        );
    }                           // ✅ une seule accolade ici — pas deux !

    public List<Ingredient> findAll() throws SQLException {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredient";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Optional<Ingredient> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM ingredient WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Integer> findStockById(Long id) throws SQLException {
        String sql = "SELECT stock FROM ingredient WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rs.getInt("stock"));
            }
        }
        return Optional.empty();
    }

    public List<Ingredient> findByDishIdWithFilters(
            Long dishId,
            String ingredientName,
            Double ingredientPriceAround) throws SQLException {

        List<Ingredient> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT i.id, i.name, i.category, i.price, i.stock
            FROM ingredient i
            JOIN dish_ingredient di ON di.ingredient_id = i.id
            WHERE di.dish_id = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(dishId);

        if (ingredientName != null && !ingredientName.isBlank()) {
            sql.append(" AND i.name ILIKE ?");
            params.add("%" + ingredientName + "%");
        }

        if (ingredientPriceAround != null) {
            sql.append(" AND i.price BETWEEN ? AND ?");
            params.add(ingredientPriceAround - 50);
            params.add(ingredientPriceAround + 50);
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }
}