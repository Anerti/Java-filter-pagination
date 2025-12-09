package org.example;

public class Category {
    private final String name;
    private final int id;
    public Category(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("{id: %d, category: %s}", id, name);
    }
}
