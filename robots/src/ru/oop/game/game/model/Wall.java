package ru.oop.game.game.model;

public class Wall extends GameObject {
    private final double width;
    private final double height;

    public Wall(double x, double y, double width, double height, ModelContext context) {
        super(x, y, context);
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(double duration) {
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}