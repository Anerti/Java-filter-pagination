package org.example;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    public static Instant formater(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse("2024-02-01", formatter);
        ZonedDateTime output = localDate.atStartOfDay(ZoneOffset.UTC);
        return output.toInstant();
    }

    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        System.out.println(dataRetriever.getAllCategories());

        System.out.println(dataRetriever.getProductList(1, 10));
        System.out.println(dataRetriever.getProductList(1, 5));
        System.out.println(dataRetriever.getProductList(1, 3));
        System.out.println(dataRetriever.getProductList(2, 2));

        System.out.println(dataRetriever.getProductByCriteria("Dell", null, null, null));
        System.out.println(dataRetriever.getProductByCriteria(null, "info", null, null));
        System.out.println(dataRetriever.getProductByCriteria("iPhone", "mobile", null, null));
        System.out.println(dataRetriever.getProductByCriteria("iPhone", "mobile", null, null));
        System.out.println(dataRetriever.getProductByCriteria(null, null, formater("2024-02-01"), formater("2024-03-01")));
        System.out.println(dataRetriever.getProductByCriteria("Samsung", "bureau", null, null));
        System.out.println(dataRetriever.getProductByCriteria("Sony", "informatique", null, null));
        System.out.println(dataRetriever.getProductByCriteria(null, "audio", formater("2024-01-01"), formater("2024-12-01")));
        System.out.println(dataRetriever.getProductByCriteria(null, null, null, null));

        System.out.println(dataRetriever.getProductByCriteria(null, null, null, null, 1, 10));
        System.out.println(dataRetriever.getProductByCriteria("Dell", null, null, null, 1, 5));
        System.out.println(dataRetriever.getProductByCriteria(null, "informatique", null, null, 1, 10));
    }
}