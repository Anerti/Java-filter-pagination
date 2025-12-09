package org.example;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataRetriever {
    private final DBConnection dbConnection = new DBConnection();

    List<Category> getAllCategories(){
        List<Category> categories = new ArrayList<>();

        final String q = "SELECT id, name FROM product_management_app.product_category";
        try (Connection c = dbConnection.getConnection()) {
            PreparedStatement statement = c.prepareStatement(q);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                categories.add(new Category(name, id));
            }
            rs.close();
            statement.close();
            return List.copyOf(categories);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<Product> getProductList(int page, int size){
        List<Product> products = new ArrayList<>();

        String q = ("SELECT " +
                "product.id, " +
                "product.name, " +
                "product.price, " +
                "product.creation_datetime, " +
                "product_category.id AS category_id, " +
                "product_category.name AS category " +
                "FROM product_management_app.product " +
                "INNER JOIN product_management_app.product_category " +
                "ON product.id = product_category.product_id " +
                "LIMIT ? OFFSET ?;");

        try (Connection c = dbConnection.getConnection()){
            PreparedStatement statement = c.prepareStatement(q);
            statement.setInt(1, size);
            statement.setInt(2, page);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final double price = rs.getDouble("price");
                final int categoryId = rs.getInt("category_id");
                final String categoryNames = rs.getString("category");
                final Timestamp creationDatetime = rs.getTimestamp("creation_datetime");

                products.add(new Product(id, name, creationDatetime.toInstant(), price, new Category(categoryNames, categoryId)));
            }
            rs.close();
            statement.close();
            return List.copyOf(products);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    List<Product> getProductByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax){
        List<Product> products = new ArrayList<>();

        StringBuilder q = new StringBuilder("SELECT " +
                "product.id, " +
                "product.name, " +
                "product.price, " +
                "product.creation_datetime, " +
                "product_category.id AS category_id, " +
                "product_category.name AS category " +
                "FROM product_management_app.product " +
                "INNER JOIN product_management_app.product_category " +
                "ON product.id = product_category.product_id");

        if (productName != null) q.append(" WHERE product.name ILIKE ?");

        if (categoryName != null) q.append(q.indexOf("WHERE") != -1 ? " AND product_category.name ILIKE ?"
                    : " WHERE product_category.name ILIKE ?");

        if (creationMin != null) q.append(q.indexOf("WHERE") != -1 ? " AND product.creation_datetime >= ?"
                    : " WHERE product.creation_datetime >= ?");

        if (creationMax != null) q.append(q.indexOf("WHERE") != -1 ? " AND product.creation_datetime <= ?"
                    : " WHERE product.creation_datetime <= ?");
        q.append(";");

        try (Connection c = dbConnection.getConnection()){
            PreparedStatement statement = c.prepareStatement(String.valueOf(q));
            int parametersCount = 1;
            if (productName != null) {
                statement.setString(parametersCount, String.format("%%%s%%", productName));
                parametersCount++;
            }
            if (categoryName != null) {
                statement.setString(parametersCount, String.format("%%%s%%", categoryName));
                parametersCount++;
            }
            if (creationMin != null) {
                statement.setTimestamp(parametersCount, Timestamp.from(creationMin));
                parametersCount++;
            }

            if (creationMax != null) statement.setTimestamp(parametersCount, Timestamp.from(creationMax));
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int categoryId = rs.getInt("category_id");
                String categoryNames = rs.getString("category");
                Timestamp creationDatetime = rs.getTimestamp("creation_datetime");

                products.add(new Product(id, name, creationDatetime.toInstant(), price, new Category(categoryNames, categoryId)));
            }

            rs.close();
            statement.close();
            return List.copyOf(products);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    List<Product> getProductByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size){
        List<Product> products = new ArrayList<>();

        final StringBuilder q = new StringBuilder("SELECT " +
                "product.id, " +
                "product.name, " +
                "product.price, " +
                "product.creation_datetime, " +
                "product_category.id AS category_id, " +
                "product_category.name AS category " +
                "FROM product_management_app.product " +
                "INNER JOIN product_management_app.product_category " +
                "ON product.id = product_category.product_id");

        if (productName != null) q.append(" WHERE product.name ILIKE ?");
        if (categoryName != null) q.append(q.indexOf("WHERE") != -1 ? " AND product_category.name ILIKE ?" :
                " WHERE product_category.name ILIKE ?");

        if (creationMin != null) q.append(q.indexOf("WHERE") != -1 ? " AND creation_datetime >= ?" :
                " WHERE creation_datetime >= ?");

        if (creationMax != null) q.append((q.indexOf("WHERE") != -1) ? " AND creation_datetime <= ?" :
                " WHERE creation_datetime <= ?");

        q.append(" LIMIT ? OFFSET ?;");

        try (Connection c = dbConnection.getConnection()){
            PreparedStatement  statement = c.prepareStatement(String.valueOf(q));
            int parametersCount = 1;

            if (productName != null) {
                statement.setString(parametersCount, String.format("%%%s%%", productName));
                parametersCount++;
            }
            if (categoryName != null) {
                statement.setString(parametersCount, String.format("%%%s%%", categoryName));
                parametersCount++;
            }
            if (creationMin != null) {
                statement.setTimestamp(parametersCount, Timestamp.from(creationMin));
                parametersCount++;
            }
            if (creationMax != null) {
                statement.setTimestamp(parametersCount, Timestamp.from(creationMax));
                parametersCount++;
            }

            if (size <= 0) size = 10;

            statement.setInt(parametersCount, size);
            parametersCount++;

            if (page <= 0) page = 1;

            statement.setInt(parametersCount, page);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final double price = rs.getDouble("price");
                final int categoryId = rs.getInt("category_id");
                final String categoryNames = rs.getString("category");
                Timestamp creationDatetime = rs.getTimestamp("creation_datetime");

                products.add(new Product(id, name, creationDatetime.toInstant(), price, new Category(categoryNames, categoryId)));
            }

            rs.close();
            statement.close();
            return List.copyOf(products);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
