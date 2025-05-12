/*package ru.oop.game.game.model;

public class Robot {
    private double positionX;
    private double positionY;
    private double direction;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public Robot(double startX, double startY) {
        this.positionX = startX;
        this.positionY = startY;
        this.direction = 0;
    }

    public void moveToTarget(int targetX, int targetY, double duration) {
        double distance = distanceTo(targetX, targetY);
        if (distance < 0.5) {
            return;
        }

        double velocity = maxVelocity;
        double angleToTarget = angleTo(targetX, targetY);
        double angularVelocity = calculateAngularVelocity(angleToTarget);

        move(velocity, angularVelocity, duration);
    }

    private double calculateAngularVelocity(double angleToTarget) {
        if (angleToTarget > direction) {
            return maxAngularVelocity;
        }
        if (angleToTarget < direction) {
            return -maxAngularVelocity;
        }
        return 0;
    }

    private void move(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newX = positionX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) - Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = positionX + velocity * duration * Math.cos(direction);
        }

        double newY = positionY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) - Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = positionY + velocity * duration * Math.sin(direction);
        }

        positionX = newX;
        positionY = newY;
        direction = asNormalizedRadians(direction + angularVelocity * duration);
    }

    public double distanceTo(int targetX, int targetY) {
        double diffX = targetX - positionX;
        double diffY = targetY - positionY;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double angleTo(int targetX, int targetY) {
        double diffX = targetX - positionX;
        double diffY = targetY - positionY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private double applyLimits(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    private double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2*Math.PI;
        while (angle >= 2*Math.PI) angle -= 2*Math.PI;
        return angle;
    }

    // Геттеры
    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public double getDirection() { return direction; }
}*/




/*package ru.oop.game.game.model;

public class Robot extends GameObject {
    private double direction;
    private int targetPositionX;
    private int targetPositionY;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public Robot(double startX, double startY) {
        super(startX, startY);
        this.direction = 0;
        this.targetPositionX = (int)startX;
        this.targetPositionY = (int)startY;
    }

    @Override
    public void update(double duration) {
        moveToTarget(targetPositionX, targetPositionY, duration);
    }

    public void moveToTarget(int targetX, int targetY, double duration) {
        double distance = distanceTo(targetX, targetY);
        if (distance < 0.5) {
            return;
        }

        double velocity = maxVelocity;
        double angleToTarget = angleTo(targetX, targetY);
        double angularVelocity = calculateAngularVelocity(angleToTarget);

        move(velocity, angularVelocity, duration);
    }

    private double calculateAngularVelocity(double angleToTarget) {
        double angleDifference = asNormalizedRadians(angleToTarget - direction);

        if (angleDifference > Math.PI) {
            angleDifference -= 2 * Math.PI;
        }

        return applyLimits(angleDifference, -maxAngularVelocity, maxAngularVelocity);
    }

    private void move(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newDirection = asNormalizedRadians(direction + angularVelocity * duration);

        // Если угловая скорость очень мала, двигаемся прямо
        if (Math.abs(angularVelocity) < 1e-6) {
            positionX += velocity * duration * Math.cos(direction);
            positionY += velocity * duration * Math.sin(direction);
        } else {
            // Движение по дуге
            double radius = velocity / angularVelocity;
            positionX += radius * (Math.sin(newDirection) - Math.sin(direction));
            positionY += -radius * (Math.cos(newDirection) - Math.cos(direction));
        }

        direction = newDirection;
    }

    public double distanceTo(int targetX, int targetY) {
        double diffX = targetX - positionX;
        double diffY = targetY - positionY;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double angleTo(int targetX, int targetY) {
        double diffX = targetX - positionX;
        double diffY = targetY - positionY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private double applyLimits(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    private double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2*Math.PI;
        while (angle >= 2*Math.PI) angle -= 2*Math.PI;
        return angle;
    }

    public void setTargetPosition(int x, int y) {
        this.targetPositionX = x;
        this.targetPositionY = y;
    }

    // Геттеры
    public double getDirection() { return direction; }
}*/


package ru.oop.game.game.model;

import ru.oop.game.game.model.GameObject;
import ru.oop.game.game.model.ModelContext;

import java.awt.geom.Rectangle2D;

public class Robot extends GameObject {
    private double direction;
    private int targetPositionX;
    private int targetPositionY;
    private boolean isStuck = false;
    private boolean targetVisible = true;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private static final double RADIUS = 1;

    public Robot(double startX, double startY, ModelContext context) {
        super(startX, startY, context);
        this.direction = 0;
        this.targetPositionX = (int)startX;
        this.targetPositionY = (int)startY;
    }

    public double getRadius() {
        return RADIUS;
    }

    public Rectangle2D getBoundingBox(double x, double y) {
        return new Rectangle2D.Double(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
    }

    @Override
    public void update(double duration) {
        moveToTarget(targetPositionX, targetPositionY, duration);
    }

    public void setTargetPosition(int x, int y) {
        this.targetPositionX = x;
        this.targetPositionY = y;
        this.targetVisible = true;
        this.isStuck = false; // Сбрасываем флаг застревания при новой цели
    }

    public boolean isTargetVisible() {
        return targetVisible;
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    /*private void moveToTarget(int targetX, int targetY, double duration) {
        if (isStuck) return;

        double distance = distanceTo(targetX, targetY);
        if (distance < 0.5) {
            return;
        }

        double angleToTarget = angleTo(targetX, targetY);
        double angularVelocity = calculateAngularVelocity(angleToTarget);

        direction = angleToTarget;

        double newX = positionX + maxVelocity * duration * Math.cos(direction);
        double newY = positionY + maxVelocity * duration * Math.sin(direction);

        //if (!modelContext.tryMove(this, newX, newY)) {
            //isStuck = true; // Если не можем двигаться - отмечаем застревание
        //}

        if (!modelContext.tryMove(this, newX, newY)) {
            // Проверяем реальное расстояние до цели
            if (distanceTo(targetPositionX, targetPositionY) < RADIUS * 2) {
                isStuck = true;
            }
        }
    }*/

    private void moveToTarget(int targetX, int targetY, double duration) {
        if (isStuck) return;

        double step = maxVelocity * duration;
        double dx = targetX - positionX;
        double dy = targetY - positionY;
        double distance = Math.hypot(dx, dy);

        if (distance < 1) return;

        // Двигаемся постепенно с проверкой коллизий на каждом шаге
        double ratio = Math.min(step, distance) / distance;
        double newX = positionX + dx * ratio;
        double newY = positionY + dy * ratio;

        // Постепенное движение с промежуточными проверками
        while (Math.abs(positionX - newX) > 0.1 || Math.abs(positionY - newY) > 0.1) {
            double testX = positionX + (newX - positionX) * 0.5;
            double testY = positionY + (newY - positionY) * 0.5;

            if (modelContext.tryMove(this, testX, testY)) {
                positionX = testX;
                positionY = testY;
            } else {
                isStuck = true;
                return;
            }
        }

        if (!modelContext.tryMove(this, newX, newY)) {
            isStuck = true;
            return;
        }

        positionX = newX;
        positionY = newY;
        isStuck = false;
    }

    public double distanceTo(int targetX, int targetY) {
        double diffX = targetX - positionX;
        double diffY = targetY - positionY;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double angleTo(int targetX, int targetY) {
        double diffX = targetX - positionX;
        double diffY = targetY - positionY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private double applyLimits(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    private double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2*Math.PI;
        while (angle >= 2*Math.PI) angle -= 2*Math.PI;
        return angle;
    }

    private double calculateAngularVelocity(double angleToTarget) {
        double angleDifference = asNormalizedRadians(angleToTarget - direction);

        if (angleDifference > Math.PI) {
            angleDifference -= 2 * Math.PI;
        }

        return applyLimits(angleDifference, -maxAngularVelocity, maxAngularVelocity);
    }

    public boolean isStuck() {
        return isStuck;
    }

    // Геттеры
    public double getDirection() { return direction; }
}