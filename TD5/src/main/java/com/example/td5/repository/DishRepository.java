package com.example.td5.repository;


import com.example.td5.entity.Dish;
import com.example.td5.entity.Ingredient;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class dishrepository {

    private DataSource dataSource = null;

    public dishrepository(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }


        Map<Long, Dish> dishMap = new LinkedHashMap<>();
        String dishSql = "SELECT id, name, selling_price FROM dish";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(dishSql)) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                dishMap.put(id, new Dish(id, rs.getString("name"),
                        rs.getDouble("selling_price"), new ArrayList<>()));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching dishes", e);
        }


        if (!dishMap.isEmpty()) {
            String ingsSql = """
                SELECT di.dish_id, i.id, i.name, i.category, i.price
                FROM dish_ingredient di
                JOIN ingredient i ON i.id = di.ingredient_id
                WHERE di.dish_id = ANY(?)
                """;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(ingsSql)) {
                Array arr = conn.createArrayOf("bigint",
                        dishMap.keySet().toArray());
                ps.setArray(1, arr);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Long dishId = rs.getLong("dish_id");
                        Ingredient ing = new Ingredient(
                                rs.getLong("id"), rs.getString("name"),
                                rs.getString("category"), rs.getDouble("price")
                        );
                        dishMap.get(dishId).getIngredients().add(ing);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error fetching dish ingredients", e);
            }
        }

        return new ArrayList<>(dishMap.values());
    }

    public Optional<Dish> findById(Long id) {
        String sql = "SELECT id, name, selling_price FROM dish WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish(rs.getLong("id"), rs.getString("name"),
                            rs.getDouble("selling_price"), new ArrayList<>());
                    loadIngredients(conn, dish);
                    return Optional.of(dish);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching dish", e);
        }
        return Optional.empty();
    }

   
    public void updateIngredients(Long dishId, List<Ingredient> requested) {

        List<Long> requestedIds = requested.stream()
                .map(Ingredient::getId).toList();

        String checkSql = "SELECT id FROM ingredient WHERE id = ANY(?)";
        List<Long> validIds = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                Array arr = conn.createArrayOf("bigint", requestedIds.toArray());
                ps.setArray(1, arr);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) validIds.add(rs.getLong("id"));
                }
            }

            try (PreparedStatement del = conn.prepareStatement(
                    "DELETE FROM dish_ingredient WHERE dish_id = ?")) {
                del.setLong(1, dishId);
                del.executeUpdate();
            }

            if (!validIds.isEmpty()) {
                String ins = "INSERT INTO dish_ingredient(dish_id, ingredient_id) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(ins)) {
                    for (Long ingId : validIds) {
                        ps.setLong(1, dishId);
                        ps.setLong(2, ingId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating dish ingredients", e);
        }
    }

    private void loadIngredients(Connection conn, Dish dish) throws SQLException {
        String sql = """
            SELECT i.id, i.name, i.category, i.price
            FROM dish_ingredient di
            JOIN ingredient i ON i.id = di.ingredient_id
            WHERE di.dish_id = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, dish.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dish.getIngredients().add(new Ingredient(
                            rs.getLong("id"), rs.getString("name"),
                            rs.getString("category"), rs.getDouble("price")
                    ));
                }
            }
        }
    }
}
