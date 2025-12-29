package org.example;

import org.example.db.DBConnection;
import org.example.service.DataRetriever;

public class Main {
    public static void main(String[] args) {
        DBConnection connection = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(connection);

        //System.out.println(dataRetriever.getAllCategories());

        //System.out.println(dataRetriever.getProductList(1, 10));
        //System.out.println(dataRetriever.getProductList(1, 5));
        //System.out.println(dataRetriever.getProductList(1, 3));
        System.out.println(dataRetriever.getProductList(2, 2));
    }
}