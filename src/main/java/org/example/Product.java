package org.example;

import java.time.Instant;

public class Product {
    private final int id;
    private final String name;
    private final Instant creationDateTime;
    private final Category category;
    private final double price;

    public Product(int id, String name, Instant creationDateTime, double price, Category category) {
        this.id = id;
        this.name = name;
        this.creationDateTime = creationDateTime;
        this.category = category;
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Instant getCreationDateTime() {
        return creationDateTime;
    }
    public Category getCategory() {
        return category;
    }
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("{id: %d, name: %s, price: %.2f, categoryId: %d, category: %s, creationDateTime: %s}",
                id, name, price, category.getId(), category.getName(), creationDateTime);
    }
}
