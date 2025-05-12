package ru.oop.game.game.model;

public abstract class GameObject {
    protected double positionX;
    protected double positionY;
    protected ModelContext modelContext;

    public GameObject(double x, double y, ModelContext context) {
        this.positionX = x;
        this.positionY = y;
        this.modelContext = context;
    }

    public abstract void update(double duration);

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void setModelContext(ModelContext context) {
        this.modelContext = context;
    }
}