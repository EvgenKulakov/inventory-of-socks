package com.demo.inventory.of.socks.dao;

import com.demo.inventory.of.socks.model.Socks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SocksDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SocksDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void increaseSocksStock(String color, int cottonPercentage, int quantity) {
        String sql =
                "INSERT INTO socks (color, cotton_percentage, quantity) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (color, cotton_percentage) DO UPDATE SET quantity = socks.quantity + ?";
        jdbcTemplate.update(sql, color, cottonPercentage, quantity, quantity);
    }

    public boolean decreaseSocksStock(String color, int cottonPercentage, int quantity) {
        boolean enoughQuantity = false;
        String sqlCheck = "SELECT quantity FROM socks WHERE color = ? AND cotton_percentage = ?";
        Integer currentQuantity = jdbcTemplate.query(
                sqlCheck,
                (ResultSet rs) -> rs.next() ? rs.getInt("quantity") : 0,
                color, cottonPercentage
        );

        if (currentQuantity >= quantity) {
            String sqlUpdate = "UPDATE socks SET quantity = quantity - ? WHERE color = ? AND cotton_percentage = ?";
            jdbcTemplate.update(sqlUpdate, quantity, color, cottonPercentage);
            enoughQuantity = true;
        }
        return enoughQuantity;
    }

    public int getSocksQuantity(String color, String operator, int cottonPercentage) {
        String sql;
        switch (operator) {
            case "moreThan":
                sql = "SELECT COALESCE(SUM(quantity), 0) as quantity FROM socks WHERE color = ? AND cotton_percentage > ?";
                break;
            case "lessThan":
                sql = "SELECT COALESCE(SUM(quantity), 0) as quantity FROM socks WHERE color = ? AND cotton_percentage < ?";
                break;
            case "equal":
                sql = "SELECT quantity FROM socks WHERE color = ? AND cotton_percentage = ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        };

        return jdbcTemplate.query(
                sql,
                (ResultSet rs) -> rs.next() ? rs.getInt("quantity") : 0,
                color, cottonPercentage
        );
    }

    public List<Socks> findSocksByRangeWithSorting(int cottonPercentageStart, int cottonPercentageEnd,
                                                   String sortBy, String order) {
        String sql = "SELECT * FROM socks WHERE cotton_percentage BETWEEN ? AND ? ORDER BY " + sortBy + " " + order;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Socks.class), cottonPercentageStart, cottonPercentageEnd);
    }

    public List<Socks> findSocksByRangeWithSorting(int cottonPercentageStart, int cottonPercentageEnd) {
        String sql = "SELECT * FROM socks WHERE cotton_percentage BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Socks.class), cottonPercentageStart, cottonPercentageEnd);
    }

    public void updateSock(int id, String color, int cottonPercentage, int quantity) {
        try {
            String sql = "UPDATE socks SET color = ?, cotton_percentage = ?, quantity = ? WHERE id = ?";
            jdbcTemplate.update(sql, color, cottonPercentage, quantity, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("A socks with the color and cotton percentage already exists.");
        }
    }

    public void batchInsertSocks(List<Socks> socksBatch) {
        String sql =
                "INSERT INTO socks (color, cotton_percentage, quantity) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (color, cotton_percentage) DO UPDATE SET quantity = socks.quantity + ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Socks socks = socksBatch.get(i);
                ps.setString(1, socks.getColor());
                ps.setInt(2, socks.getCottonPercentage());
                ps.setInt(3, socks.getQuantity());
                ps.setInt(4, socks.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return socksBatch.size();
            }
        });
    }
}
