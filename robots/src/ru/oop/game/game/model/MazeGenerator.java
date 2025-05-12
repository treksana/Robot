/*package ru.oop.game.game.model;

import java.util.*;

public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final int cellSize;
    private final boolean[][] horizontalWalls; // Горизонтальные стенки (между рядами)
    private final boolean[][] verticalWalls;   // Вертикальные стенки (между колонками)
    private final Random random = new Random();

    public MazeGenerator(int rows, int cols, int cellSize) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.horizontalWalls = new boolean[rows + 1][cols]; // +1 для нижней границы
        this.verticalWalls = new boolean[rows][cols + 1];   // +1 для правой границы

        // Инициализируем все стенки как существующие
        for (int i = 0; i <= rows; i++) {
            Arrays.fill(horizontalWalls[i], true);
        }
        for (int i = 0; i < rows; i++) {
            Arrays.fill(verticalWalls[i], true);
        }
    }

    public List<Wall> generateMaze() {
        // Используем алгоритм поиска в глубину для генерации лабиринта
        boolean[][] visited = new boolean[rows][cols];
        dfs(0, 0, visited);

        // Преобразуем стенки в объекты Wall
        List<Wall> walls = new ArrayList<>();

        // Добавляем границы лабиринта (толщина 2px)
        walls.add(new Wall(0, 0, cols * cellSize, 2, null)); // Верхняя граница
        walls.add(new Wall(0, (rows) * cellSize, cols * cellSize, 2, null)); // Нижняя граница
        walls.add(new Wall(0, 0, 2, rows * cellSize, null)); // Левая граница
        walls.add(new Wall((cols) * cellSize - 5, 0, 2, rows * cellSize, null)); // Правая граница

        // Добавляем внутренние горизонтальные стенки
        for (int row = 0; row <= rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (horizontalWalls[row][col]) {
                    walls.add(new Wall(col * cellSize, row * cellSize, cellSize, 2, null));
                }
            }
        }

        // Добавляем внутренние вертикальные стенки
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols; col++) {
                if (verticalWalls[row][col]) {
                    walls.add(new Wall(col * cellSize, row * cellSize, 2, cellSize, null));
                }
            }
        }

        return walls;
    }

    private void dfs(int row, int col, boolean[][] visited) {
        visited[row][col] = true;

        // Соседние клетки в случайном порядке
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        shuffleDirections(directions);

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValid(newRow, newCol) && !visited[newRow][newCol]) {
                // Удаляем стенку между текущей и новой клеткой
                if (dir[0] == 0) { // Горизонтальное перемещение
                    int wallCol = dir[1] > 0 ? col + 1 : col;
                    verticalWalls[row][wallCol] = false;
                } else { // Вертикальное перемещение
                    int wallRow = dir[0] > 0 ? row + 1 : row;
                    horizontalWalls[wallRow][col] = false;
                }

                dfs(newRow, newCol, visited);
            }
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private void shuffleDirections(int[][] directions) {
        for (int i = directions.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = directions[index];
            directions[index] = directions[i];
            directions[i] = temp;
        }
    }
}*/

package ru.oop.game.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final int cellSize;
    private final boolean[][] horizontalWalls;
    private final boolean[][] verticalWalls;
    private final Random random = new Random();

    public MazeGenerator(int rows, int cols, int cellSize) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;

        // +1 для нижней и правой границ
        this.horizontalWalls = new boolean[rows + 1][cols];
        this.verticalWalls = new boolean[rows][cols + 1];

        // Инициализация всех стенок
        for (boolean[] row : horizontalWalls) Arrays.fill(row, true);
        for (boolean[] row : verticalWalls) Arrays.fill(row, true);
    }

    public List<Wall> generateMaze() {
        boolean[][] visited = new boolean[rows][cols];
        dfs(0, 0, visited);

        List<Wall> walls = new ArrayList<>();
        final int WALL_THICKNESS = 2;

        // Границы лабиринта (корректировка координат)
        // Верхняя граница
        walls.add(new Wall(0, 0, cols * cellSize, WALL_THICKNESS, null));
        // Левая граница
        walls.add(new Wall(0, 0, WALL_THICKNESS, rows * cellSize, null));
        // Правая граница (корректировка X-координаты)
        walls.add(new Wall(cols * cellSize - WALL_THICKNESS, 0,
                WALL_THICKNESS, rows * cellSize, null));
        // Нижняя граница (корректировка Y-координаты)
        walls.add(new Wall(0, rows * cellSize - WALL_THICKNESS,
                cols * cellSize, WALL_THICKNESS, null));

        // Внутренние горизонтальные стенки
        for (int row = 0; row <= rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (horizontalWalls[row][col]) {
                    int y = row * cellSize - (row == 0 ? 0 : WALL_THICKNESS);
                    walls.add(new Wall(
                            col * cellSize,
                            y,
                            cellSize,
                            WALL_THICKNESS,
                            null
                    ));
                }
            }
        }

        // Внутренние вертикальные стенки
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols; col++) {
                if (verticalWalls[row][col]) {
                    int x = col * cellSize - (col == 0 ? 0 : WALL_THICKNESS);
                    walls.add(new Wall(
                            x,
                            row * cellSize,
                            WALL_THICKNESS,
                            cellSize,
                            null
                    ));
                }
            }
        }

        return walls;
    }

    private void dfs(int row, int col, boolean[][] visited) {
        visited[row][col] = true;
        int[][] directions = {{-1,0}, {0,1}, {1,0}, {0,-1}};
        shuffleDirections(directions);

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValid(newRow, newCol) && !visited[newRow][newCol]) {
                // Удаляем стенку между клетками
                if (dir[0] != 0) { // Вертикальное движение
                    int wallRow = row + (dir[0] > 0 ? 1 : 0);
                    horizontalWalls[wallRow][col] = false;
                } else { // Горизонтальное движение
                    int wallCol = col + (dir[1] > 0 ? 1 : 0);
                    verticalWalls[row][wallCol] = false;
                }
                dfs(newRow, newCol, visited);
            }
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private void shuffleDirections(int[][] directions) {
        for (int i = directions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }
    }
}