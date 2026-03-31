package com.example.td5.repository;


import com.example.td5.entity.Dish;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class DishRepository {

    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Dish map(ResultSet rs) throws SQLException {
        return new Dish(rs.getLong("id"), rs.getString("name"));
    }

    public List<Dish> findAll() throws SQLException {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT * FROM dish";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Optional<Dish> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM dish WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public void addIngredientToDish(Long dishId, Long ingredientId) throws SQLException {
        String sql = "INSERT INTO dish_ingredient (dish_id, ingredient_id) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, dishId);
            ps.setLong(2, ingredientId);
            ps.executeUpdate();
        }
    }
}