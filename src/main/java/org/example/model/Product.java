package org.example.model;

import java.time.Instant;
import java.util.Objects;

public class Product{
    private final int id;
    private final String name;
    private final Instant creationDatetime;
    private final Category category;

    public Product(int id, String name, Instant creationDatetime, Category category) {
        this.id = id;
        this.name = name;
        this.creationDatetime = creationDatetime;
        this.category = category;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Instant getCreationDatetime() {
        return creationDatetime;
    }
    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", category=" + category +
                '}';
    }
}