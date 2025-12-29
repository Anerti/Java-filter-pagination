package org.example.service;

import org.example.db.DBConnection;
import org.example.model.Category;
import org.example.model.Product;

import java.time.Instant;
import java.util.List;

public class DataRetriever {
    private DBConnection connection = new DBConnection();

    List<Category> getAllCategories(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    List<Product> getProductList (int page, int size){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}