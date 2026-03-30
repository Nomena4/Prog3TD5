package com.example.td5.repository;



import com.example.td5.entity.Ingredient;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepository {

    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT id, name, category, price FROM ingredient";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ingredients", e);
        }
        return list;
    }

    public Optional<Ingredient> findById(Long id) {
        String sql = "SELECT id, name, category, price FROM ingredient WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ingredient by id", e);
        }
        return Optional.empty();
    }

    private Ingredient mapRow(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price")
        );
    }
}
