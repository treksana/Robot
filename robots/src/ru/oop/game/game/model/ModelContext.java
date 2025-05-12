/*package ru.oop.game.game.model;

import java.util.List;

public class ModelContext {
    private final GameModel model;

    public ModelContext(GameModel model) {
        this.model = model;
    }

    public boolean tryMove(GameObject movingObject, double newX, double newY) {
        // Проверяем коллизии со всеми объектами
        for (GameObject obj : model.getGameObjects()) {
            if (obj != movingObject && checkCollision(movingObject, newX, newY, obj)) {
                return false; // Коллизия обнаружена
            }
        }
        // Если коллизий нет - перемещаем объект
        movingObject.setPosition(newX, newY);
        return true;
    }

    private boolean checkCollision(GameObject obj1, double x1, double y1, GameObject obj2) {
        // Для робота и стенки
        if (obj1 instanceof Robot && obj2 instanceof Wall) {
            Robot robot = (Robot)obj1;
            Wall wall = (Wall)obj2;

            // Проверяем пересечение круга (робота) с прямоугольником (стенкой)
            double robotRadius = 15; // Половина диаметра робота
            double closestX = clamp(x1, wall.getPositionX(), wall.getPositionX() + wall.getWidth());
            double closestY = clamp(y1, wall.getPositionY(), wall.getPositionY() + wall.getHeight());

            double distanceX = x1 - closestX;
            double distanceY = y1 - closestY;

            return (distanceX * distanceX + distanceY * distanceY) < (robotRadius * robotRadius);
        }
        return false;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}*/




package ru.oop.game.game.model;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class ModelContext {
    private final GameModel model;

    public ModelContext(GameModel model) {
        this.model = model;
    }

    /*public boolean tryMove(GameObject movingObject, double newX, double newY) {
        if (newX < 0 || newY < 0 || newX > 800 || newY > 600) {
            return false;
        }

        for (GameObject obj : model.getGameObjects()) {
            if (obj != movingObject && checkCollision(movingObject, newX, newY, obj)) {
                return false;
            }
        }

        movingObject.setPosition(newX, newY);
        return true;
    }*/

    public boolean tryMove(GameObject movingObject, double newX, double newY) {
        if (!(movingObject instanceof Robot)) {
            movingObject.setPosition(newX, newY);
            return true;
        }

        Robot robot = (Robot)movingObject;
        double radius = robot.getRadius();

        // Проверка границ игрового поля
        if (newX - radius < 0 || newY - radius < 0 ||
                newX + radius > 800 || newY + radius > 600) {
            return false;
        }

        // Проверка коллизий со стенками
        /*for (GameObject obj : model.getGameObjects()) {
            if (obj != robot && obj instanceof Wall &&
                    checkRobotWallCollision(robot, newX, newY, (Wall)obj)) {
                return false;
            }
        }*/

        for (GameObject obj : model.getGameObjects()) {
            if (obj != robot && obj instanceof Wall &&
                    checkCircleRectangleCollision(newX, newY, radius, (Wall)obj)) {
                return false;
            }
        }


        robot.setPosition(newX, newY);
        return true;
    }

    /*private boolean checkCollision(GameObject checkingObj, double x1, double y1, GameObject withObj) {
        if (checkingObj instanceof Robot && withObj instanceof Wall) {
            return checkRobotWallCollision((Robot)checkingObj, x1, y1, (Wall)withObj);
        }
        return false;
    }*/

    /*private boolean checkRobotWallCollision(Robot robot, double newX, double newY, Wall wall) {
        double robotRadius = 15;
        double wallLeft = wall.getPositionX();
        double wallRight = wallLeft + wall.getWidth();
        double wallTop = wall.getPositionY();
        double wallBottom = wallTop + wall.getHeight();

        // Находим ближайшую точку на стенке к роботу
        double closestX = clamp(newX, wallLeft, wallRight);
        double closestY = clamp(newY, wallTop, wallBottom);

        // Проверяем расстояние до ближайшей точки
        double distanceX = newX - closestX;
        double distanceY = newY - closestY;
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        return distanceSquared < (robotRadius * robotRadius);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }*/

    /*private boolean checkRobotWallCollision(Robot robot, double newX, double newY, Wall wall) {
        double robotRadius = 15;

        // Координаты стенки
        double wallLeft = wall.getPositionX();
        double wallRight = wallLeft + wall.getWidth();
        double wallTop = wall.getPositionY();
        double wallBottom = wallTop + wall.getHeight();

        // Центр робота
        double robotCenterX = newX;
        double robotCenterY = newY;

        // Находим ближайшую точку на стенке к центру робота
        double closestX = clamp(robotCenterX, wallLeft, wallRight);
        double closestY = clamp(robotCenterY, wallTop, wallBottom);

        // Расстояние от центра робота до ближайшей точки на стенке
        double distanceX = robotCenterX - closestX;
        double distanceY = robotCenterY - closestY;

        // Проверяем пересечение
        return Math.abs(distanceX) < robotRadius && Math.abs(distanceY) < robotRadius;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }*/

    /*private boolean checkRobotWallCollision(Robot robot, double newX, double newY, Wall wall) {
        // Получаем bounding box робота в новой позиции
        Rectangle2D robotRect = robot.getBoundingBox(newX, newY);

        // Получаем прямоугольник стенки
        Rectangle2D wallRect = new Rectangle2D.Double(
                wall.getPositionX(),
                wall.getPositionY(),
                wall.getWidth(),
                wall.getHeight()
        );

        // Проверяем пересечение прямоугольников
        return robotRect.intersects(wallRect);
    }*/

    /*private boolean checkRobotWallCollision(Robot robot, double newX, double newY, Wall wall) {
        // Уменьшаем размеры стенки на 1px с каждой стороны для более точного определения коллизий
        double wallPadding = 1;
        double wallLeft = wall.getPositionX() + wallPadding;
        double wallTop = wall.getPositionY() + wallPadding;
        double wallRight = wallLeft + wall.getWidth() - 2 * wallPadding;
        double wallBottom = wallTop + wall.getHeight() - 2 * wallPadding;

        // Получаем bounding box робота с учетом его радиуса
        double robotLeft = newX - robot.getRadius();
        double robotTop = newY - robot.getRadius();
        double robotRight = newX + robot.getRadius();
        double robotBottom = newY + robot.getRadius();

        // Проверяем пересечение
        return robotRight > wallLeft &&
                robotLeft < wallRight &&
                robotBottom > wallTop &&
                robotTop < wallBottom;
    }*/


    /*private boolean checkCircleRectangleCollision(double circleX, double circleY, double radius, Wall wall) {
        // Координаты стенки
        double wallLeft = wall.getPositionX();
        double wallRight = wallLeft + wall.getWidth();
        double wallTop = wall.getPositionY();
        double wallBottom = wallTop + wall.getHeight();

        // Находим ближайшую точку на стенке к центру круга
        double closestX = clamp(circleX, wallLeft, wallRight);
        double closestY = clamp(circleY, wallTop, wallBottom);

        // Расстояние от центра круга до ближайшей точки
        double distanceX = circleX - closestX;
        double distanceY = circleY - closestY;

        // Проверяем пересечение
        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }*/

    private boolean checkCircleRectangleCollision(double circleX, double circleY, double radius, Wall wall) {
        Rectangle2D wallRect = new Rectangle2D.Double(
                wall.getPositionX(),
                wall.getPositionY(),
                wall.getWidth(),
                wall.getHeight()
        );

        // Расширенная проверка для тонких стен
        double buffer = 1.0; // Буфер для компенсации погрешностей
        return wallRect.intersects(
                circleX - radius - buffer,
                circleY - radius - buffer,
                2 * (radius + buffer),
                2 * (radius + buffer)
        );
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}