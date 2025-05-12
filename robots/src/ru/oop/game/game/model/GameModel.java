/*package ru.oop.game.game.model;

public class GameModel {
    private final Robot robot;
    private int targetPositionX = 150;
    private int targetPositionY = 100; //лучше перенести в робот

    public GameModel() {
        this.robot = new Robot(100, 100); // Начальная позиция робота
    }

    public void update(double duration) {
        robot.moveToTarget(targetPositionX, targetPositionY, duration);
    }
    //gameobject добавить метод update, робот будетдвигать себя к цели
    //добавить метод setmouseClick, найти робота среди списка объекта и сет ему позицию
    // здесь код для работы любых Object на поле. view тоже дробим

    // Геттеры и сеттеры
    public double getRobotPositionX() { return robot.getPositionX(); }
    public double getRobotPositionY() { return robot.getPositionY(); }
    public double getRobotDirection() { return robot.getDirection(); }
    public int getTargetPositionX() { return targetPositionX; }
    public int getTargetPositionY() { return targetPositionY; }

    //также поменять робота на коллекцию GameObject
    public void setTargetPosition(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
    }
}*/


/*package ru.oop.game.game.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final List<GameObject> gameObjects = new ArrayList<>();

    public GameModel() {
        // Добавляем робота в список объектов
        gameObjects.add(new Robot(100, 100));
    }

    public void update(double duration) {
        for (GameObject obj : gameObjects) {
            obj.update(duration);
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setTargetPosition(int x, int y) {
        // Находим робота и устанавливаем ему цель
        for (GameObject obj : gameObjects) {
            if (obj instanceof Robot) {
                ((Robot)obj).setTargetPosition(x, y);
                break;
            }
        }
    }
}*/




package ru.oop.game.game.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final ModelContext modelContext;

    public GameModel() {
        this.modelContext = new ModelContext(this);

        // Генерируем лабиринт
        MazeGenerator mazeGenerator = new MazeGenerator(10, 15, 50); // 10x15 клеток, размер клетки 50px
        List<Wall> mazeWalls = mazeGenerator.generateMaze();

        // Добавляем все стенки лабиринта
        for (Wall wall : mazeWalls) {
            wall.setModelContext(modelContext);
            gameObjects.add(wall);
        }

        // Добавляем робота в начало лабиринта
        gameObjects.add(new Robot(25, 25, modelContext));


        /*this.modelContext = new ModelContext(this);
        gameObjects.add(new Robot(100, 100, modelContext));
        gameObjects.add(new Wall(50, 50, 200, 10, modelContext));
        gameObjects.add(new Wall(250, 50, 10, 200, modelContext));*/
    }

    public void update(double duration) {
        for (GameObject obj : gameObjects) {
            obj.update(duration);
        }
    }

    public void handleMouseClick(int x, int y) {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Robot) {
                ((Robot)obj).setTargetPosition(x, y);
                break;
            }
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}