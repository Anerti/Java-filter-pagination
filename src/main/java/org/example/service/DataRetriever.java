package org.example.service;

import org.example.db.DBConnection;
import org.example.model.Category;
import org.example.model.Product;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection connection;

    public DataRetriever(DBConnection connection) {
        this.connection = connection;
    }

    public List<Category> getAllCategories(){
        final String query = "SELECT id AS category_id, name AS category_name FROM product_management_app.product_category";

        List<Category> categories = new ArrayList<>();
        try (Connection c = connection.getConnection()) {
            PreparedStatement ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(
                        new Category(
                                rs.getInt("category_id"),
                                rs.getString("category_name")
                        )
                );
            }
            return categories;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProductList (int page, int size){
        final String query =
                """
                    SELECT
                        product.id AS product_id,
                        product.name AS product_name,
                        product_category.id AS category_id,
                        product_category.name AS category,
                        product.creation_datetime AS product_creation_date
                    FROM
                        product_management_app.product
                    INNER JOIN
                        product_management_app.product_category
                    ON
                        product.id = product_category.product_id
                    LIMIT ?
                    OFFSET ?;
                """;
        List<Product> products = new ArrayList<>();
        try (Connection c = connection.getConnection()){
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1,size);
            ps.setInt(2,page - 1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(
                        new Product(
                                rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getTimestamp("product_creation_date").toInstant(),
                            new Category(
                                    rs.getInt("category_id"),
                                    rs.getString("category")
                            )
                        )
                );
            }
            return products;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax){
        final StringBuilder query = new StringBuilder
                ("""
                    SELECT
                        product.id AS product_id,
                        product.name AS product_name,
                        product_category.id AS category_id,
                        product_category.name AS category,
                        product.creation_datetime AS product_creation_date
                    FROM
                        product_management_app.product
                    INNER JOIN
                        product_management_app.product_category
                    ON
                        product.id = product_category.product_id
                    WHERE 1 = 1
                """);
        List<Product> products = new ArrayList<>();

        if (productName != null && !productName.isEmpty()) query.append(" AND product.name ILIKE ?");

        if (categoryName != null && !categoryName.isEmpty()) query.append(" AND product_category.name ILIKE ?");

        if (creationMin != null) query.append(" AND creation_datetime >= ?");

        if (creationMax != null) query.append(" AND creation_datetime <= ?");

        query.append(";");

        try (Connection c = connection.getConnection()){
            PreparedStatement ps = c.prepareStatement(query.toString());
            int paramIndex = 1;
            if (productName != null && !productName.isEmpty()) ps.setString(paramIndex++, String.format("%%%s%%", productName));

            if (categoryName != null && !categoryName.isEmpty()) ps.setString(paramIndex++, String.format("%%%s%%", categoryName));

            if (creationMin != null) ps.setTimestamp(paramIndex++, Timestamp.from(creationMin));

            if (creationMax != null) ps.setTimestamp(paramIndex, Timestamp.from(creationMax));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(
                        new Product(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getTimestamp("product_creation_date").toInstant(),
                                new Category(
                                        rs.getInt("category_id"),
                                        rs.getString("category")
                                )
                        )
                );
            }
            return products;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}