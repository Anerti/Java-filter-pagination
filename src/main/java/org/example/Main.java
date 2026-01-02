package org.example;

import org.example.db.DBConnection;
import org.example.service.DataRetriever;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static Instant formater(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        ZonedDateTime output = localDate.atStartOfDay(ZoneOffset.UTC);
        return output.toInstant();
    }

    public static void main(String[] args) throws SQLException {
        DBConnection connection = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(connection);

        //System.out.println(dataRetriever.getAllCategories());

        //System.out.println(dataRetriever.getProductList(1, 10));
        //System.out.println(dataRetriever.getProductList(1, 5));
        //System.out.println(dataRetriever.getProductList(1, 3));
        //System.out.println(dataRetriever.getProductList(2, 2));

        //System.out.println(dataRetriever.getProductsByCriteria("Dell", null, null, null));
        //System.out.println(dataRetriever.getProductsByCriteria(null, "info", null, null));
        //System.out.println(dataRetriever.getProductsByCriteria("iPhone", "mobile", null, null));
        //System.out.println(dataRetriever.getProductsByCriteria(null, null, formater("2024-02-01"), formater("2024-03-01")));
        //System.out.println(dataRetriever.getProductsByCriteria("Samsung", "bureau", null, null));
        //System.out.println(dataRetriever.getProductsByCriteria("Sony", "informatique", null, null));
        //System.out.println(dataRetriever.getProductsByCriteria(null, "audio", formater("2024-02-01"), formater("2024-12-01")));
        //System.out.println(dataRetriever.getProductsByCriteria(null, null, null, null));

        //System.out.println(dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10));
        //System.out.println(dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 10));
        //System.out.println(dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10));
    }
}