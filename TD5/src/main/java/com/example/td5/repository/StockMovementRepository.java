package com.example.td5.repository;

import com.example.td5.entity.StockValue;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class StockMovementRepository {

    private final DataSource dataSource;

    public StockMovementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public StockValue getStockAt(Long ingredientId, LocalDateTime at, String unit) {
        String sql = """
            SELECT COALESCE(SUM(quantity), 0) AS stock
            FROM stock_movement
            WHERE ingredient_id = ?
              AND unit = ?
              AND movement_date <= ?
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ingredientId);
            ps.setString(2, unit);
            ps.setTimestamp(3, Timestamp.valueOf(at));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StockValue(unit, rs.getDouble("stock"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating stock", e);
        }
        return new StockValue(unit, 0.0);
    }
}
