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

    public List<Category> getAllCategories() throws SQLException {
        final String query = "SELECT id AS category_id, name AS category_name FROM product_management_app.product_category";

        List<Category> categories = new ArrayList<>();
        Connection c = connection.getConnection();
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

        ps.close();
        rs.close();
        connection.closeConnection(c);
        return categories;
    }

    private void getProduct(List<Product> products, PreparedStatement ps) throws SQLException {
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
        ps.close();
        rs.close();
    }

    public List<Product> getProductList (int page, int size) throws SQLException {
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
        Connection c = connection.getConnection();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1,size);
        ps.setInt(2,(page - 1) * size);
        getProduct(products, ps);

        connection.closeConnection(c);
        return products;
    }

    private StringBuilder getProductsCriteriaQueryHandler(String productName, String categoryName, Instant creationMin, Instant creationMax, Integer page, Integer size){
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

        if (productName != null && !productName.isEmpty()) query.append(" AND product.name ILIKE ?");

        if (categoryName != null && !categoryName.isEmpty()) query.append(" AND product_category.name ILIKE ?");

        if (creationMin != null) query.append(" AND creation_datetime >= ?");

        if (creationMax != null) query.append(" AND creation_datetime <= ?");

        return (size != null && page != null) ? query.append(" LIMIT ? OFFSET ?;") : query.append(";");
    }

    private List<Product> getProductsByCriteriaRequestHandler(String productName, String categoryName, Instant creationMin, Instant creationMax, StringBuilder query, Integer page, Integer size) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection c = connection.getConnection();
        PreparedStatement ps = c.prepareStatement(query.toString());
        int paramIndex = 1;
        if (productName != null && !productName.isEmpty()) ps.setString(paramIndex++, String.format("%%%s%%", productName));

        if (categoryName != null && !categoryName.isEmpty()) ps.setString(paramIndex++, String.format("%%%s%%", categoryName));

        if (creationMin != null) ps.setTimestamp(paramIndex++, Timestamp.from(creationMin));

        if (creationMax != null) ps.setTimestamp(paramIndex++, Timestamp.from(creationMax));

        if (size != null && page != null) {
            ps.setInt(paramIndex++, size);
            ps.setInt(paramIndex, (page - 1) * size);
        }

        getProduct(products, ps);
        connection.closeConnection(c);
        return products;
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) throws SQLException {
        final StringBuilder query = getProductsCriteriaQueryHandler(productName, categoryName, creationMin, creationMax, null, null);

        return getProductsByCriteriaRequestHandler(productName, categoryName, creationMin, creationMax, query, null, null);
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) throws SQLException {
        final StringBuilder query = getProductsCriteriaQueryHandler(productName, categoryName, creationMin, creationMax, page, size);

        return getProductsByCriteriaRequestHandler(productName, categoryName, creationMin, creationMax, query, page, size);
    }
}